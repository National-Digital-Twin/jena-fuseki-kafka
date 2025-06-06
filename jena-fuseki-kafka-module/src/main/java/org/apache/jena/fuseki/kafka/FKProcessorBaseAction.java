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


package org.apache.jena.fuseki.kafka;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jena.atlas.logging.FmtLog;
import org.apache.jena.kafka.FusekiKafka;
import org.apache.jena.kafka.JenaKafkaException;
import org.apache.jena.kafka.RequestFK;
import org.apache.jena.kafka.ResponseFK;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.WebContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process incoming request as a SPARQL Update, RDF Patch or RDF data as appropriate.
 * <p>
 * This is the simplified version of what Fuseki would do for an operation sent to
 * the dataset URL. By looking at the {@code Content-Type}, it splits incoming Kafka messages into:
 * <ul>
 * <li>SPARQL Updates</li>
 * <li>RDF Patch</li>
 * <li>RDF Data</li>
 * <ul>
 */
public abstract class FKProcessorBaseAction implements FKProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FKProcessorBaseAction.class);

    protected FKProcessorBaseAction() {}

    @Override
    public abstract void startBatch(int batchSize, long offsetStart);

    @Override
    public abstract void finishBatch(int processedCount, long finishOffset, long startOffset);

    @Override
    public ResponseFK process(RequestFK request) {
        ResponseFK success =  ResponseFK.success(request.getTopic());
        try {
            String id = request.getTopic();
            processAction(id, request);
        } catch (Exception ignored) {
            return success;
        }

        return success;
    }

    private void processAction(String id, RequestFK request) {
        try {
            InputStream data = request.getInputStream();
            String contentType = request.getContentType();
            if ( contentType == null ) {
                actionFailed(id, request, "No content type. Message rejected.");
                return;
            }

            if ( WebContent.contentTypeSPARQLUpdate.equals(contentType) ) {
                actionSparqlUpdate(id, request, data);
                return;
            }
            if ( WebContent.contentTypePatch.equals(contentType) ) {
                actionRDFPatch(id, request, data);
                return;
            }

            Lang lang = RDFLanguages.contentTypeToLang(contentType);
            if ( lang != null ) {
                actionData(id, request, lang, data);
                return;
            }
            FmtLog.warn(FusekiKafka.LOG, "Failed to handle '%s'",  contentType);
        } catch (RuntimeException ex) {
            actionFailed(ex);
        }
    }

    protected void actionFailed(String id, RequestFK request, String message) {
        throw new JenaKafkaException(message);
    }

    protected void actionFailed(RuntimeException ex) {
        LOGGER.error(ex.getMessage(), ex);
    }

    protected abstract void actionSparqlUpdate(String id, RequestFK request, InputStream data);

    protected abstract void actionRDFPatch(String id, RequestFK request, InputStream data);

    protected abstract void actionData(String id, RequestFK requestd, Lang lang, InputStream data);

}
