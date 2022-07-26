# Audit Chain (AMOS SS 2022)

<img src="https://user-images.githubusercontent.com/48165495/174863628-06019f93-7c3a-4729-8d31-656b459be086.png" align="right" width="500px">

**Welcome** to the **Audit Chain Repository** ! This project is a middleware based on blockchain data structures to guarantee the unchanged, compliant, in sequence, fault tolerant and buffered data flow between any kind of producers and consumers.
<br clear="center"/>



## AMOS Project
The present project was created as part of a semester course by Master and Erasmus students and carried out by [Prof. Riehle](https://oss.cs.fau.de/person/riehle-dirk/) at the [Friedrich-Alexander-Universität
Erlangen-Nürnberg](https://www.fau.de).



## General
First of all, the project is developed following a dependency structure, typical of the audit chain module.

The main idea is that events of any kind, like IoT, file systems and measurement loggers should be transmitted securely via the network.
There is a central event queue, which records these events and uses them for further steps and actions that would involve third parties, like consumers. 
Consumers can approach the queue, in order to get the events and work with them accordingly. Events are recorded serially and are send to the central event queue.


The Project contains three main parts:
- *the producer*: who is the creator and sender of the events
- *the queue*: the middle ground where events are stored and safely shared. 
- *the consumer*: who takes events from the queue in order to perform further actions with them (that are not of interest in our application). <br />
  <br />
  A sample of the interaction described above could be the following: <br />
  <br />

![modules](https://user-images.githubusercontent.com/48165495/174449922-4773d975-2c8f-481a-bcdd-9f8192827f5d.png)
<br />

In our case, producer dummy (which would be represented by the FS-Filter and the IoT digital thermometer in the picture)  would create the events that are then stored in the central queue and from this one, our consumer dummy (so the data scanner and the audit protocol), would use them for further processing, like, also storing.
Listed, are the next steps that we are further developing:
- Offers the possibility for producers and consumers to register consumers
- Uses the events from the queue to trigger further actions based on them. <br />
  <br />

![uml](https://user-images.githubusercontent.com/105744782/180841646-d54eab87-b50d-40b1-bea9-ed547dfd55a6.png)

**We have used the component diagram to show different parts of our code solution. Our code is basically composed of 3 major components:**
- **Producer dummy** (client): which operates through the dependencies among its 3 sub-components RabbitMQChannel, data generator, and persistence mechanism.
  When the producer dummy is triggered, depending on the used DataGenerator it creates an "event" and assigns an unique incremental Number to the newly created "event". Furthermore the currently implemented Persistence Mechanism allows to either store always the newest event or every event into a File to prevent data loss if the Message was not yet send.
- **RabbitMQ** (Event Queue): After the producer dummy successfully generates an event, it goes to RabbitMQ (event queue). RabbitMQ is a 3rd party program that we are using as a component for queuing events.
- **Consumer dummy**: Lastly, after rabbitMQ queues events successfully, the data event moves to the consumer dummy.

**The folder structure is as follows:**
- **[producer dummy](https://github.com/amosproj/amos2022ss02-audit-chain/tree/main/src/main/java/ProducerDummy)**: is the component which produces and sends these events to the middleware.
- **[consumer dummy](https://github.com/amosproj/amos2022ss02-audit-chain/tree/main/src/main/java/ConsumerDummy)**: is the component which consumes messages from the middleware.
- **[middleware](https://github.com/amosproj/amos2022ss02-audit-chain/tree/main/Documentation/Middleware)**: the messaging broker, in our case RabbitMQ
- **[blockchain](https://github.com/amosproj/amos2022ss02-audit-chain/tree/main/src/main/java/BlockchainImplementation)**: data structure chosen for the message chain: work in progress.<br />
  <br />

# Technology Stack:
In the following list you can see the projects underlying technology stack. To be more accurate, it displays the programming language chosen, front- and back-end frameworks, software applications and the chosen version control system.

### 1. Programming Languages:
**Java** (Backend): Java is an object-oriented programming language which consists of a development tool for creating code and a runtime environment to run code.
<br />
**Python**:  It is a computer programming language often used to build scrips, automate tasks, and conduct data analysis. Its purpose here is to create in combination with PySide2 a GUI.
### 2. Version Control Software:
**Git**: Git is a distributed version control system with the aim to help our group to develop the software together. <br />
**GitHub**: GitHub is the place where our repository lays. It is a provider for hosting software development and version control using Git.
### 3.  RabbitMQ:
An open source message broker software.
### 4 . Other development tools:
**Docker Engine**: Docker is for isolation of an application using containers.
<br />
**Docker CLI**: It is the Command Line Interface using Docker.
<br />
**GUI**: It is a graphics-based operating system interface that uses icons, menus and a mouse to manage interaction with the system.
### 5 . JUnit Tests:
JUnit is a unit testing open-source framework for the Java programming language. We use this framework to write and execute automated tests. However, in Java, there are test cases that must be re-executed every time a new code is added. This is done to make sure that nothing in the code is broken. We have JUnit Tests for the following cases:
- JUnit Tests Producer Dummy
- JUnit Tests Consumer Dummy
- JUnit Tests Blockchain


# Build,Deployment Documentation & Testing
### Prerequisites:
- Git
- Java SDK 16
- Docker

### Clone the repository from GitHub:
- Clone via HTTPS:
  ```
  git clone https://github.com/amosproj/amos2022ss02-audit-chain.git
  ```
  https://user-images.githubusercontent.com/104498986/171228250-fe4c4e2e-93b7-4c99-be40-1b1585ffadc2.mp4


- Clone via SSH:
   ```
   git clone git@github.com:amosproj/amos2022ss02-audit-chain.git
   ```
- Or download the latest release and unzip the package from git.

  **Open the project in your favorite IDE. We suggest using IntellJ**

- Credentials used for rabbitmq in this project,
  ```
  username:admin
  password:admin
  ```
- To change the credential, update RABBITMQ_DEFAULT_USER, RABBITMQ_DEFAULT_PASS in docker-compose.yml file
- create a network by the name rabbitmq-cluster:
  ```
  docker network create rabbitmq-cluster 
  ```
- Get the middleware i.e rabbitmq-cluster running in daemon mode:
  ```
  docker-compose up -d
  ```
  
- Generate executable Jars (ProducerDummy, ConsumerDummy, Blockchain and Producer_Consumer).
  ```
  mvn clean package
  ```
- **By default, all the modules use respective config.properties file from /src/main/resources/module_name (or use the config.properties in the current path) to run and you can manipulate the configuration by changing the values in config.properties. Additionally, you can also run the projects using the jar files with the optional arguments described below.**

- All the modules can be run using jar files with 4 optional arguments, <br>
  ```
   -h,  --host <arg>          host ip of docker (127.0.0.1) <br>
   -p,  --port <arg>          port (5672) <br>
   -pw, --password <arg>      password of rabbitmq <br>
   -u,  --username <arg>      username of rabbitmq <br>
 
  ```
  
- To run the producer-dummy module
  ```
  java -jar target/AuditChain-ProducerDummy.jar --host 127.0.0.1 --port 5672 --username admin --password admin
  ```
  
- **Note: Remember to use username and password of rabbitmq as arguments or the edit the [config.properties](https://github.com/amosproj/amos2022ss02-audit-chain/blob/main/src/main/resources/ProducerDummy/config.properties) (or use the config.properties in the current path) of the ProducerDummy**  <br />
  <br />
   
- To run the consumer-dummy module

  ```
  java -jar target/AuditChain-ConsumerDummy.jar --host 127.0.0.1 --port 5672 --username admin --password admin
  ```
  
- **Note: Remember to use username and password of rabbitmq as arguments or the edit the [config.properties](https://github.com/amosproj/amos2022ss02-audit-chain/blob/main/src/main/resources/ConsumerDummy/config.properties) (or use the config.properties in the current path) of the ConsumerDummy**  <br />
  <br />

- Or you can run the Producerdummy and Consumerdummy modules together

  ```
  java -jar target/AuditChain-Producer_Consumer.jar --host 127.0.0.1 --port 5672 --username admin --password admin
  ```

- To run the blockchain module
  ```
  java -jar target/AuditChain-Blockchain.jar --host 127.0.0.1 --port 5672 --username admin --password admin
  ```
- **Note: Remember to use username and password of rabbitmq as arguments or the edit the [config.properties](https://github.com/amosproj/amos2022ss02-audit-chain/blob/main/src/main/resources/BlockchainImplementation/config.properties) (or use the config.properties in the current path) of the BlockChain module**  <br />

- Generate JAVADOC (then you can find it at /target/site/apidocs/index.html).

  ```
  mvn clean javadoc:javadoc
  ```


### Testing
- The applications of our project contains unittests that can be used to test the individual components. To run them locally just execute the following command:

  ```
  mvn clean compile test
  ```

# License
Distributed under the MIT License. For further information please see the [LICENSE](https://github.com/amosproj/amos2021ws05-fin-prod-port-quick-check/blob/main/LICENSE) file in the root directory.



