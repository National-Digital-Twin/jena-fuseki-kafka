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

import java.util.Properties;

import org.apache.jena.atlas.lib.Version;
import org.apache.kafka.clients.consumer.ConsumerConfig;

public class SysJenaKafka {

    private SysJenaKafka() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PATH         = "org.apache.jena.kafka";

    /** The product name */
    public static final String NAME         = "Apache Jena Kafka Connector";

    /** Software version taken from the jar file. */
    public static final String VERSION      = Version.versionForClass(FusekiKafka.class).orElse("<development>");

    /**
     * Size in bytes per consumer.poll in a system.
     * <p>
     * This sets {@link ConsumerConfig#MAX_PARTITION_FETCH_BYTES_CONFIG}
     * ({@code max.partition.fetch.bytes}) which has a Kafka default of 1Mb.
     * <p>
     * To replicate data, we need Fuseki or user application to see all the data
     * in-order which forces the choice of one partition. See also
     * {@link ConsumerConfig#FETCH_MAX_BYTES_CONFIG} which has a Kafka default of
     * 50Mb.
     */
    public static int KAFKA_FETCH_BYTE_SIZE = 50 * 1024 * 1024;

    /**
     * Size in messages per consumer.poll in a system.
     * <p>
     * This sets {@link ConsumerConfig#MAX_POLL_RECORDS_CONFIG} ({@code max.poll.records})
     * which has a Kafka default of 500.
     */
    public static int KAFKA_FETCH_POLL_SIZE = 5000;

    /**
     * Kafka consumer properties.
     */
    public static Properties consumerProperties(String server) {
        Properties props = new Properties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, KAFKA_FETCH_BYTE_SIZE);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KAFKA_FETCH_POLL_SIZE);

        props.put("bootstrap.servers", server);
        return props;
    }
}
