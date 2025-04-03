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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.atlas.json.JSON;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.kafka.JenaKafkaException;
import org.apache.jena.kafka.refs.RefBytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Track the state of data ingested from Kafka.  */
public class DataState {
    static Logger LOG = LoggerFactory.getLogger(DataState.class);

    // Rename as "localDispatch" and "remoteEndpoint"
    private static String fDataset = "dataset";
    private static String fEndpoint = "endpoint";
    private static String fTopic = "topic";
    private static String fOffset = "offset";

    private final String dispatchPath;
    private final String remoteEndpoint;
    private final String topic;
    private final RefBytes state;
    // This is the offset of the next state to write.
    // -1 : for uninitialized.
    //  0 : first to be written
    //  X : Last Kafka offset read is X-1.
    private long offset;

    /** Minimal dummy DataState */
    public static DataState createEphemeral(String topic) {
        PersistentState state = PersistentState.createEphemeral();
        return new DataState(state, "", "", topic);
    }

    public static DataState create(PersistentState state) {
        return fromJson(state);
    }

    private DataState(RefBytes state, String localDispatchPath, String remoteEndpoint, String topic) {
        this.state = state;
        this.dispatchPath = localDispatchPath;
        this.remoteEndpoint = (remoteEndpoint != null) ? remoteEndpoint : "" ;
        this.topic = topic;
        this.offset = -1;
    }

    public static DataState restoreOrCreate(RefBytes state, String localDispatchPath, String remoteEndpoint, String topic) {
        Objects.requireNonNull(state);
        Objects.requireNonNull(topic);
        Objects.requireNonNull(localDispatchPath);

        if ( state.getBytes().length == 0 ) {
            DataState dataState = new DataState(state, localDispatchPath, remoteEndpoint, topic);
            return dataState;
        }

        // Existing.
        InputStream bout = new ByteArrayInputStream(state.getBytes());
        DataState dataState = fromJson(state);
        checkExpectedSettings(dataState, localDispatchPath, remoteEndpoint, topic);
        return dataState;
    }

    private static void checkExpectedSettings(DataState dataState, String localDispatchPath, String remoteEndpoint, String topic) {
        if ( ! dataState.dispatchPath.equals(localDispatchPath) )
            throw new JenaKafkaException("Dataset name does not match: loaded="+dataState.dispatchPath+ " / expected dataset=" +localDispatchPath);
        if ( ! Objects.equals(dataState.remoteEndpoint, remoteEndpoint) )
            throw new JenaKafkaException("Endpoint name does not match: loaded="+dataState.remoteEndpoint+ " / expected endpoint=" +remoteEndpoint);
        if ( ! dataState.topic.equals(topic) )
            throw new JenaKafkaException("Topic does not match: loaded="+dataState.dispatchPath+ " / expected topic=" +topic);
    }

    private void writeState() {
        if ( state == null )
            return;
        // Via JSON.
        JsonObject obj = asJson();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try ( IndentedWriter b = new IndentedWriter(output) ) {
            JSON.write(b, obj);
            b.println();
        }

        state.setBytes(output.toByteArray());
    }

    @Override
    public String toString() {
        JsonObject obj = asJson();
        return JSON.toStringFlat(obj);
    }

    private JsonObject asJson() {
        return JSON.buildObject(builder-> {
            if ( dispatchPath != null )
                builder.pair(fDataset, dispatchPath);
            if ( remoteEndpoint != null )
                builder.pair(fEndpoint, remoteEndpoint);
            builder.pair(fTopic,   topic);
            builder.pair(fOffset,  offset);
            });
    }

    private static DataState fromJson(RefBytes state) {
        InputStream bout = new ByteArrayInputStream(state.getBytes());
        JsonObject obj = JSON.parse(bout);

        String datasetName = obj.getString(fDataset);
        if ( datasetName == null )
            throw new JenaKafkaException("No dataset name: "+JSON.toStringFlat(obj));
        String endpoint = obj.hasKey(fEndpoint) ? obj.getString(fEndpoint) : null;
        String topicName  = obj.getString(fTopic);
        if ( topicName == null )
            throw new JenaKafkaException("No topic name: "+JSON.toStringFlat(obj));
        Number offsetNum = obj.getNumber(fOffset);
        if ( offsetNum == null )
            throw new JenaKafkaException("No offset: "+JSON.toStringFlat(obj));

        DataState dataState = new DataState(state, datasetName, endpoint, topicName);
        dataState.offset = offsetNum.longValue();
        return dataState;
    }

    /**
     * Last offset seen.
     * <p>
     * Kafka works in terms of "next offset" - strictly, the next possible offset.
     * It usually starts at zero for the first in a topic.
     * <p>
     * {@code DataState} records last offset seen.
     */
    public long getLastOffset() {
        return offset;
    }

    /**
     * Last offset seen.
     * <p>
     * Kafka works in terms of "next offset" - strictly, the next possible offset.
     * It usually starts at zero for the first in a topic.
     * <p>
     * {@code DataState} records last offset seen.
     */
    public void setLastOffset(long offset) {
        this.offset = offset;
        writeState();
    }

    public String getDatasetName() {
        return dispatchPath;
    }

    public String getTopic() {
        return topic;
    }
}
