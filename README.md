# README  

**Repository:** `jena-fuseki-kafka`   
**Description:** `This repository provides an Apache Jena Fuseki extension module for receiving data over Apache Kafka topics. The module is used to read knowledge from a Kafka message stream typically as the final stage in a pipeline of data processing for ingest.`

<!-- SPDX-License-Identifier: Apache-2.0 AND OGL-UK-3.0 -->

## Overview  
This repository contributes to the development of **secure, scalable, and interoperable data-sharing infrastructure**. It supports NDTP’s mission to enable **trusted, federated, and decentralised** data-sharing across organisations.  

This repository is one of several open-source components that underpin NDTP’s **Integration Architecture (IA)**—a framework designed to allow organisations to manage and exchange data securely while maintaining control over their own information. The IA is actively deployed and tested across multiple sectors, ensuring its adaptability and alignment with real-world needs. 

For a complete overview of the Integration Architecture (IA) project, please see the [Integration Architecture Documentation](https://github.com/National-Digital-Twin/integration-architecture-documentation).

## Prerequisites  
Before using this repository, ensure you have the following dependencies installed:  
- **Required Tooling:** 
    - Java 17
    - Github PAT token set to allow retrieval of maven packages from Github Packages
- **Pipeline Requirements:** 
    - Cloud platform credentials
- **Supported Kubernetes Versions:** N/A
- **System Requirements:** 
    - Java 17
    - Docker
    - Kafka (or connectivity to)

## Quick Start  
Follow these steps to get started quickly with this repository. For detailed installation, configuration, and deployment, refer to the relevant MD files.  

### 1. Download and Build  
```sh  
git clone https://github.com/National-Digital-Twin/jena-fuseki-kafka.git
cd jena-fuseki-kafka
```
### 2. Run Build Version  
```sh  
mvn clean package

```

This creates a jar file `jena-fmod-kafka-VER.jar` in `jena-fmod-kafka/target/`.

Move this jar to 'lib/' in the directory you wish to run Fuseki with the Fuseki-Kafka connector.

Copy the bash script `fuseki-main` to the same directory.

_This includes running Apache Kafka via docker containers from `testcontainers.io`. There is a large, one time, download._

### 3. Usage

#### Standalone

In the directory where you wish to run Fuseki:

Get a copy of Fuseki Main:

```sh
wget https://repo1.maven.org/maven2/org/apache/jena/jena-fuseki-server/4.7.0/jena-fuseki-server-4.7.0.jar
```
and place in the current directory.

Get a copy of the script [fuseki-main](https://github.com/National-Digital-Twin/jena-fuseki-kafka/blob/main/fuseki-main)
then run 

```sh
fuseki-main jena-fuseki-server-4.7.0.jar --conf config.ttl
```

where `config.ttl` is the configuration file for the server including the
connector setup.

_Windows uses can run `fuseki-main.bat` which may need adjusting for the correct
version number of Fuseki._

#### Maven Project
To use the library directly in your project:
```xml
<dependency>
    <groupId>uk.gov.dbt.ndtp.jena</groupId>
    <artifactId>jena-fuseki-kafka-module</artifactId>
    <version>VERSION</version>
</dependency>
```

_populate the `artifactId` above as appropriate._

## Features  
- **Key functionality**  
    - Provides an Apache Jena Fuseki extension module for receiving and processing data over Apache Kafka topics.  
    - Enables ingestion of knowledge from Kafka message streams as part of a data processing pipeline.  

- **Key integrations**  
    - Seamlessly integrates with Apache Kafka for data streaming.  

- **Modularity**  
    - Compatible with Java-based projects through Maven dependency management.  
    - Designed to work within the National Digital Twin Programme’s Integration Architecture.  

## Testing Guide

### Unit Tests
Unit testing is a software testing method where individual components or modules of an application are tested in isolation to verify their correctness. These components are often the smallest testable parts of the application, such as functions/methods, or class functions.

To perform unit tests, we use SonarQube. This platform allows us to see coverage (and other useful metrics) inside an easy to use yet powerful web interface hosted locally.

### Starting SonarQube Locally
1. *The following command spins up a docker container for a community edition of SonarQube locally (at time of writing **25.5.0.107428** is the latest version).*
   ```shell
   docker run -p 9000:9000 -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true --name sonarqube sonarqube:25.5.0.107428-community
   ```
  > [!NOTE]
  > The **SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true** environment flag. This is to bypass Elasticsearch bootstrap checks as they, are not necessary for a local setup and can sometimes cause issues during setup.

2. Go to your web browser and navigate to http://localhost:9000. This will bring up the login portal for SonarQube. When logging in for the first time use admin for both the username and password. It will ask you to set a new password for which you can choose anything of preference. 
  > [!NOTE]  
  > If you get an **Unexpected application error**, refresh the page (it seems to be to do with an overlay not rendering properly).

3. Select **Create a local project** and give it a name (this should ideally be the exact same name as the repository).

4. On the next page, select the **Use the global setting** option and progress to the next page by clicking **Create project**.

5. To create the token required to analyse the repository with SonarQube, scroll towards the bottom of the **Analysis Method** page and click on Locally.

6. You will be prompted to generate a project token or to use an existing token. For the first time setup select **Generate a project token**, The **Expires in** dropdown will default to 30 days, and then click the **Generate** button.

7. On the next page, click **Continue** and then under the **Run analysis on your project** section, select the option that says "Maven".

8. On selecting, the option that best describes your project, follow the steps shown to run a SonarQube analysis (the command provided must be run at the root of the project folder).

> [!WARNING]
> If you get an error while running this command, then check that your Java version matches the expected Java version of the repository. The error message should inform you which version is expected.

## Configuration Details

Documentation detailing the relevant configuration and endpoints is provided [here](docs/configuration-jena-fuseki-kafka.md ). 


## Public Funding Acknowledgment  
This repository has been developed with public funding as part of the National Digital Twin Programme (NDTP), a UK Government initiative. NDTP, alongside its partners, has invested in this work to advance open, secure, and reusable digital twin technologies for any organisation, whether from the public or private sector, irrespective of size.  

## License  
This repository contains both source code and documentation, which are covered by different licenses:  
- **Code:** Originally developed by Telicent UK Ltd, now maintained by National Digital Twin Programme. Licensed under the [Apache License 2.0](LICENSE.md).  
- **Documentation:** Licensed under the [Open Government Licence (OGL) v3.0](OGL_LICENSE.md).  

By contributing to this repository, you agree that your contributions will be licensed under these terms.

See [LICENSE.md](LICENSE.md), [OGL_LICENSE.md](OGL_LICENSE.md), and [NOTICE.md](NOTICE.md) for details.  

## Security and Responsible Disclosure  
We take security seriously. If you believe you have found a security vulnerability in this repository, please follow our responsible disclosure process outlined in [SECURITY.md](SECURITY.md).  

## Contributing  
We welcome contributions that align with the Programme’s objectives. Please read our [Contributing](CONTRIBUTING.md) guidelines before submitting pull requests.  

## Acknowledgements  
This repository has benefited from collaboration with various organisations. For a list of acknowledgments, see [ACKNOWLEDGEMENTS.md](ACKNOWLEDGEMENTS.md).  

## Support and Contact  
For questions or support, check our Issues or contact the NDTP team on ndtp@businessandtrade.gov.uk.

**Maintained by the National Digital Twin Programme (NDTP).**  

© Crown Copyright 2025. This work has been developed by the National Digital Twin Programme and is legally attributed to the Department for Business and Trade (UK) as the governing entity.
