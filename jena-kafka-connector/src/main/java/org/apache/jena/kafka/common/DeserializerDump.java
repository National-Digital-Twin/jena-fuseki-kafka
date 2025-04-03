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


package org.apache.jena.kafka.common;

import static org.apache.jena.kafka.FusekiKafka.CONTENT_TYPE;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.jena.atlas.lib.Bytes;
import org.apache.jena.riot.*;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

/** Print out the events. */
public class DeserializerDump implements Deserializer<String> {

    @Override
    public String deserialize(String topic, Headers headers, byte[] data) {
        StringBuilder sbuff = new StringBuilder();
        AtomicBoolean a = new AtomicBoolean(false);
        headers.forEach(h->{
            String k = h.key();
            String v = Bytes.bytes2string(h.value());
            sbuff.append(String.format("%-10s %s%n", k+":", v));
            a.set(true);
        });

        if ( a.get() )
            sbuff.append("\n");

        try {
            // Library
            String contentType = Bytes.bytes2string(headers.lastHeader(CONTENT_TYPE).value());
            if ( WebContent.contentTypeSPARQLUpdate.equals(contentType) ) {
                bodyUpdate(sbuff, data);
                return sbuff.toString();
            } else {
                Lang lang = RDFLanguages.contentTypeToLang(contentType);
                if ( lang != null ) {
                    bodyLang(sbuff, lang, data);
                    return sbuff.toString();
                }
            }
        } catch (RuntimeException ex) {
            // Suppress exception
        }
        return Bytes.bytes2string(data);
    }

    private void bodyLang(StringBuilder sbuff, Lang lang, byte[] data) {
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
        DatasetGraph dsg = DatasetGraphFactory.createTxnMem();
        RDFDataMgr.read(dsg, bytesIn, lang);
        StringWriter sw = new StringWriter();
        RDFDataMgr.write(sw, dsg, RDFFormat.TRIG_BLOCKS);
        sbuff.append(sw.toString());
    }

    private void bodyUpdate(StringBuilder sbuff, byte[] data) {
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
        UpdateRequest req = UpdateFactory.read(bytesIn);
        sbuff.append(req.toString());
    }

    @Override
    public String deserialize(String topic, byte[] data) {
        return deserialize(topic, null, data);
    }
}
