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

package org.apache.jena.fuseki.kafka;

import org.apache.jena.kafka.JenaKafkaException;

@SuppressWarnings("java:S110") // SonarQube rule: This class has 6 parents which is greater than 5 authorized.
public class FusekiKafkaException extends JenaKafkaException {
    public FusekiKafkaException(String msg) { super(msg); }
    public FusekiKafkaException(String msg, Throwable cause) { super(msg, cause); }
}
