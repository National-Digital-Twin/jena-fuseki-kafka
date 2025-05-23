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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.gov.dbt.ndtp.secure.agent.sources.kafka.BasicKafkaTestCluster;
import org.apache.kafka.clients.admin.NewTopic;

/**
 * A mock Kafka cluster
 *
 * @deprecated Use {@link BasicKafkaTestCluster} or one of the other extensions of
 * {@link import uk.gov.dbt.ndtp.secure.agent.sources.kafka.KafkaTestCluster} instead as they provide more control over the Kafka
 * cluster.
 */
@Deprecated(since = "1.4.0", forRemoval = true)
public class MockKafka extends BasicKafkaTestCluster {

    /**
     * Gets the Kafka Server
     *
     * @return Server
     * @deprecated Use {@link #getBootstrapServers()} instead
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    public String getServer() {
        return this.getBootstrapServers();
    }

    /**
     * Creates a topic
     *
     * @param topic Topic Name
     * @deprecated Use {@link #resetTopic(String)} instead
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    public void createTopic(String topic) {
        NewTopic newTopic = new NewTopic(topic, 1, (short) 1);

        this.adminClient.createTopics(List.of(newTopic));
    }

    /**
     * Stops the cluster
     *
     * @deprecated Use {@link #teardown()} instead
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    public void stop() {
        this.teardown();
    }

    /**
     * Lists topics
     *
     * @return Use {@link #getAdminClient()} to access the Admin Client and use that to list topics directly
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    public Collection<String> listTopics() {
        try {
            return this.adminClient.listTopics().names().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
