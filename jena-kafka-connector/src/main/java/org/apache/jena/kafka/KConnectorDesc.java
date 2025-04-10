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

import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.atlas.logging.Log;

/**
 * Details of a connector to Kafka.
 * <p>
 * The machinery is in {@link DeserializerActionFK} which reads from kafka, and
 * creates a {@link RequestFK}.
 * <p>
 * For Fuseki, the {@link RequestFK} is handled by {@code FKRequestProcessor} which
 * dispatches the request to the main Fuseki execution path (includes Fuseki logging).
 */
public class KConnectorDesc {

    // Source
    private final String topic;
    private final String bootstrapServers;

    // Destination - URI path in this Fuseki server
    private final String fusekiDispatchPath;
    // URL to replay the request to.
    private final String remoteEndpoint;

    private final boolean syncTopic;
    private final boolean replayTopic;

    // State tracking.
    private final String stateFile;

    // Kafka consumer setup.
    private final Properties kafkaConsumerProps;

    public KConnectorDesc(KConnectorDescConfig config) {
        this.topic = Objects.requireNonNull(config.topic(), "topic");
        this.bootstrapServers = config.bootstrapServers();
        this.fusekiDispatchPath = config.fusekiDispatchName();
        this.remoteEndpoint = config.remoteEndpoint();
        this.syncTopic = config.syncTopic();
        this.replayTopic = config.replayTopic();
        this.stateFile = config.stateFile();
        this.kafkaConsumerProps = config.kafkaConsumerProps();

        boolean hasLocalFusekiService = StringUtils.isEmpty(config.fusekiDispatchName());
        boolean hasRemoteEndpoint = StringUtils.isEmpty(config.remoteEndpoint());

        if ( hasRemoteEndpoint && hasLocalFusekiService  )
            Log.warn(this, "ConnectorFK built with both a local dispatch path and remote endpoint URL");
        if ( ! hasRemoteEndpoint && ! hasLocalFusekiService )
            Log.warn(this, "ConnectorFK built with no local dispatch path nor remote endpoint URL");
    }

    public String getTopic() {
        return topic;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    /**
     * The destination of events on the Kafka topic.
     * <p>
     * Either they are dispatched to Fuseki, in the same JVM, and the connector
     * destination is given by {@link #getLocalDispatchPath} or replayed on to a remote
     * endpoint URL given by {@link #getRemoteEndpoint}.
     *
     * @return HTTP path for local dispatch.
     */
    public String getLocalDispatchPath() {
        return fusekiDispatchPath;
    }

    public boolean dispatchLocal() {
        return ! StringUtils.isEmpty(fusekiDispatchPath);
    }

    /**
     * The destination of events on the Kafka topic if remote.
     *
     * @return HTTP URL for remote dispatch.
     */
    public String getRemoteEndpoint() {
        return remoteEndpoint;
    }

    public boolean getSyncTopic() {
        return syncTopic;
    }

    public boolean getReplayTopic() {
        return replayTopic;
    }

    public String getStateFile() {
        return stateFile;
    }

    public Properties getKafkaConsumerProps() {
        return kafkaConsumerProps;
    }

    @Override
    public String toString() {
        return "ConnectorFK [topic=" + topic + ", fusekiDispatchName=" + fusekiDispatchPath + ", remoteEndpoint=" + remoteEndpoint + ", syncTopic="
               + syncTopic + ", replayTopic=" + replayTopic + ", stateFile=" + stateFile
               + "]";
    }
}
