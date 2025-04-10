// SPDX-License-Identifier: Apache-2.0
// Originally developed by Telicent Ltd.; subsequently adapted, enhanced, and maintained by the National Digital Twin Programme.

/*
 *  Copyright (c) Telicent Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 *  Modifications made by the National Digital Twin Programme (NDTP)
 *  © Crown Copyright 2025. This work has been developed by the National Digital Twin Programme
 *  and is legally attributed to the Department for Business and Trade (UK) as the governing entity.
 */


package org.apache.jena.kafka;

import java.io.PrintStream;
import java.util.Map;
import java.util.function.Function;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.riot.WebContent;
import org.apache.jena.riot.web.HttpNames;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * Deserialize to an internal "request object"
 */
public class DeserializerActionFK implements Deserializer<RequestFK> {

    private static final String DEFAULT_CONTENT_TYPE = WebContent.contentTypeNQuads;
    // Verbose mode - dumps incoming Kafka event to an output stream.
    // The purpose is to be able to capture events
    private final Function<Integer, PrintStream> dumpOutput;
    private boolean verbose = false;

    /**
     * New DeserializerActionFK
     * @param verbose
     *      Dump events.
     * @param dumpOutput
     *      Supply a {@code PrintStream} for dump output.
     *      The output is thread-safe.
     *      The output is flushed after each event.
     */
    public DeserializerActionFK(boolean verbose, Function<Integer, PrintStream> dumpOutput) {
        this.verbose = verbose;
        this.dumpOutput = dumpOutput;
    }

    public DeserializerActionFK() {
        this(false, null);
    }

    private int counter = 0;

    @Override
    public RequestFK deserialize(String topic, Headers headers, byte[] data) {
        Map<String, String> requestHeaders = JK.headerToMap(headers);

        if ( verbose && dumpOutput != null ) {
            synchronized(this) {
                counter++;
                try (PrintStream out = dumpOutput.apply(counter)) {
                    out.printf("## %d ##%n", counter);
                    headers.forEach(h -> out.println(h.key() + ": " + StrUtils.fromUTF8bytes(h.value())));
                    out.println();
                    String x = StrUtils.fromUTF8bytes(data);
                    out.print(x);
                    if (!x.endsWith("\n"))
                        out.println();
                    out.flush();
                }
            }
        }

        // Default Content-Type to NQuads
        requestHeaders.putIfAbsent(HttpNames.hContentType, DEFAULT_CONTENT_TYPE);

        // Content-Length
        requestHeaders.putIfAbsent(HttpNames.hContentLength, Integer.toString(data.length));

        return new RequestFK(topic, requestHeaders, data);
    }

    @Override
    public RequestFK deserialize(String topic, byte[] data) {
        return deserialize(topic, null, data);
    }
}
