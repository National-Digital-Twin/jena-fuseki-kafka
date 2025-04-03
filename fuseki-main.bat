@echo off
@rem SPDX-License-Identifier: Apache-2.0
@rem
@rem  Copyright (c) Telicent Ltd.
@rem
@rem  Licensed under the Apache License, Version 2.0 (the "License");
@rem  you may not use this file except in compliance with the License.
@rem  You may obtain a copy of the License at
@rem
@rem      http://www.apache.org/licenses/LICENSE-2.0
@rem
@rem  Unless required by applicable law or agreed to in writing, software
@rem  distributed under the License is distributed on an "AS IS" BASIS,
@rem  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem  See the License for the specific language governing permissions and
@rem  limitations under the License.
@rem
@rem  This file is unmodified from its original version developed by Telicent Ltd.,
@rem  and is now included as part of a repository maintained by the National Digital Twin Programme.
@rem  All support, maintenance and further development of this code is now the responsibility
@rem  of the National Digital Twin Programme.

java -cp jena-fuseki-server-4.7.0.jar:lib/* org.apache.jena.fuseki.main.cmds.FusekiMainCmd %*
exit /B
