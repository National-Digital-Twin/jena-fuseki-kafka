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


package org.apache.jena.fuseki.kafka.lib;

import java.io.InputStream;
import java.io.StringWriter;

import org.apache.jena.fuseki.kafka.FKProcessorBaseAction;
import org.apache.jena.kafka.RequestFK;
import org.apache.jena.rdfpatch.RDFPatch;
import org.apache.jena.rdfpatch.RDFPatchOps;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFLib;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class FKPrintRequest extends FKProcessorBaseAction {
    /** Print details of the request on stdout. */
    public static void parsePrint(RequestFK action) {
        new FKPrintRequest().process(action);
    }

    @Override
    public void startBatch(int batchSize, long startOffset) {
        // Placeholder method
    }

    @Override
    public void finishBatch(int processedCount, long finishOffset, long startOffset) {
        // Placeholder method
    }

    @Override
    protected void actionSparqlUpdate(String id, RequestFK request, InputStream data) {
        UpdateRequest up = UpdateFactory.read(data);
        String dataStr = up.toString();
        print(id, dataStr);
    }

    @Override
    protected void actionRDFPatch(String id, RequestFK request, InputStream data) {
        RDFPatch patch = RDFPatchOps.read(data);
        String dataStr = patch.toString();
        print(id, dataStr);
    }

    @Override
    protected void actionData(String id, RequestFK request, Lang lang, InputStream data) {
        StringWriter sw = new StringWriter();
        StreamRDF stream = StreamRDFLib.writer(sw);
        RDFParser.source(data).lang(lang).parse(stream);
        String dataStr = sw.toString();
        print(id, dataStr);
    }

    @SuppressWarnings("java:S106") // SonarQube rule: Replace this use of System.out by a logger.
    private void print(String id, String dataStr) {
        System.out.println("== Request: "+id);
        System.out.print(dataStr);
        if ( ! dataStr.endsWith("\n") )
            System.out.println();
        System.out.println("--");
    }
}
