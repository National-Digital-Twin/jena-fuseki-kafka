package org.apache.jena.kafka;

import java.util.Properties;

public record KConnectorDescConfig(String topic, String bootstrapServers, String fusekiDispatchName,
                                   String remoteEndpoint, String stateFile, boolean syncTopic, boolean replayTopic,
                                   Properties kafkaConsumerProps) {
}