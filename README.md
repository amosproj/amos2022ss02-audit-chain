# Audit Chain (AMOS SS 2022)

Welcome to the Audit Chain Repository. This project is a middleware based on blockchain data structures to guarantee the unchanged, compliant, in sequence, fault tolerant and buffered data flow between any kind of producers and consumers.

# Build and Run

### Clone the repository from from GitHub: 
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

	**Open the project in your favorite IDE. We use IntellJ for demos**
 - Get the middleware i.e rabbitmq running:
   ```
   docker compose up
    ```
   https://user-images.githubusercontent.com/104498986/171229907-b865ec51-0488-4892-b9a9-155b550f6f70.mp4
- To run the producer-dummy module
  ```
  mvn clean compile exec:java@ProducerDummy
  ```
- To run the consumer-dummy module
  ```
  mvn clean compile exec:java@ConsumerDummy
  ```
  https://user-images.githubusercontent.com/104498986/171228527-b9953e5c-cffa-414a-bd9a-7ca58bb34c0b.mp4
- To run the blockchain module
  ```
      mvn clean compile exec:java@BlockChain
  ```

- Generate JAVADOC (then you can find it at /target/site/apidocs/index.html).

  ```
  mvn javadoc:javadoc
  ```


# Audit Chain (AMOS SS 2022)
=======



Generate JAVADOC (then you can find it at /target/site/apidocs/index.html).

```
mvn javadoc:javadoc
```

To Run the classes:

```
mvn clean compile exec:java@ProducerDummy

mvn clean compile exec:java@ConsumerDummy

mvn clean compile exec:java@Blockchain
```

link to docker for installation + command
build and run
producer dummy
rabbitmq (docker)
consumer dummy
pom.xml => lifecycle + use plug ins -> something like mvn exec:java

Welcome to the Audit Chain Repository. This project is a middleware based on blockchain data structures to guarantee the unchanged, compliant, in sequence, fault tolerant and buffered data flow between any kind of producers and consumers.
AMOS Project
The project is created as a work as part of a student project, carried out by Prof. Riehle at the Friedrich-Alexander University of Erlangen-Nuremberg.
General
The project is developed following a dependency structure, typical of the audit chain module.

The main idea is that events of any kind, like IoT, file systems and measurement loggers should be transmitted securely via the network.
There is a central event queue, which records these events and uses them for further steps that would involve third parties, so consumers.
There is a central event queue, which records events and uses them for further actions. Consumers can approach the queue, in order to get the events and work with them accordingly. Events are recorded serially and the central event queue is transmitted via the network queue. In order to define terms, we could define three main characters:
the producer: who is the creator and sender of the events
the queue: the middle ground where events are stored and safely shared
the consumer: who takes events from the queue in order to perform further actions with them (that are not of interest in our application).
An example of the interaction described above could be the following:

In our case, producer dummy (which would be represented by the FS-Filter and the IoT digital thermometer in the picture)  would create the events that are then stored in the central queue and from this one, our consumer dummy (so the data scanner and the audit protocol), would use them for further processing, like, also storing.
Listed, are the next steps that we are further developing:
Offers the possibility for producers and consumers to register consumers
Uses the events from the queue to trigger further actions based on them.
**TODO**: UML diagram here!!!!!!!
We have used the component diagram to show different parts of our code solution. Our code is basically composed of 3 major components:
Producer dummy (client): which operates through the dependencies among its 3 sub-components database, data generator, and persistence mechanism (storage buffer).
When the producer dummy is triggered, the data generator catches data events (message) from the database and then forwards it to the client session and persistence mechanism. The persistence mechanism is a contingent component that stores the last data event and gets triggered in case of any failure.
rabbit MQ (Event Queue): After the producer dummy successfully generates an event, it goes to rabbit MQ (event queue). Rabbit MQ is a 3rd party program that we are using as a component for queuing events.
Consumer dummy: Lastly, after rabbit MQ queues events successfully, the data event moves to the consumer dummy.
The folder structure is as follows:
/producer dummy/:
/consumer dummy/:
/middleware/:
/blockchain/: data structure chosen for the message chain: work in progress.

Technology Stack:
In the following list you can see the projects underlying technology stack. To be more accurate, it displays the programming language chosen, front- and back-end frameworks, software applications and the chosen version control system.

Programming Languages:
Java (Backend): Java is an object-oriented programming language which consists of a development tool for creating code and a runtime environment to run code
Version Control Software:
Git: Git is a distributed version control system with the aim to help our group to develop the software together.
GitHub: GitHub is the place where our repository lays. It is a provider for hosting software development and version control using Git.

Other development tools:
Docker Engine: Docker is for isolation of an application using containers.
Docker CLI: It is the Command Line Interface using Docker.

RabbitMQ:
An open source message broker software.

Build & Deployment Documentation
Prerequisites:
Git
Java SDK 16
Docker

Build

Clone the repository from from GitHub:
Clone via HTTPS
git clone https://github.com/amosproj/amos2022ss02-audit-chain.git

Clone via SSH
git clone git@github.com:amosproj/amos2022ss02-audit-chain.git

Or download the latest release and unzip the package from git.


Docker:
docker-compose

maven
 
