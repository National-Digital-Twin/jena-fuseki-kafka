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


package org.apache.jena.kafka.cmd;

import java.util.Arrays;

import org.apache.jena.atlas.lib.Version;
import org.apache.jena.kafka.SysJenaKafka;

@SuppressWarnings("java:S106") // SonarQube rule: Replace this use of System.out by a logger.
public class fk {

    public static void main(String[] args) {
        if ( args.length < 2 ) {
            System.err.println("No subcommand");
            System.exit(1);
        }

        if ( args.length == 0 ) {
            System.err.println("Usage: fk SUB ARGS...");
            System.exit(1);
        }

        String cmd = args[0];
        String[] argsSub = Arrays.copyOfRange(args, 1, args.length);
        String cmdExec = cmd;

        // Help
        switch (cmdExec) {
            case "help" :
            case "-h" :
            case "-help" :
            case "--help" :
                System.err.println("Commands: send, dump");
                return;
            case "version":
            case "--version":
            case "-version":
                version();
                System.exit(0);
                return;
            case "send":  FK_Send.main(argsSub); break;
            case "dump":  FK_DumpTopic.main(argsSub); break;
            default:
                System.err.println("Failed to find a command match for '"+cmd+"'");
        }
    }

    private static void version() {
        Version.printVersion(System.out, "FusekiKafka", Version.versionForClass(SysJenaKafka.class));
        System.exit(0) ;
    }
}
