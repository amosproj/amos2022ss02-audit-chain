# BLOCKCHAIN DOCUMENTATION

This document is meant to give a first insight overview in the blockchain part of the project.

## Table of Contents
[Blockchain Explanation](#Blockchain Explanation)  
[Frameworks](#Frameworks)  
[Interactions](#Interactions)  
[Implementation Roadmap](#Implementation Roadmap)  
[Code](#Code)  


## Blockchain Explanation
A Blockchain is a data structure and it is characterized by being an immutable, ordered, "back-linked list" of blocks of 
blocks of transactions. It is not only a simple back linked list, in fact each block contains a hash of the previous block. 
This hash is used to link the blocks together and allows the properties of immutability to be maintained. It is not 
possible to edit the blockchain by adding or removing blocks in the middle or changing them, because this would cause 
a change in the hash of the block. This change will be propagated to the next block since its hash would also change, and so on. 

A blockchain is a distributed database shared among computer network nodes. A blockchain, like a database, saves information electronically in digital format. Blockchains are best recognized for preserving a secure and decentralized record of transactions in cryptocurrency systems like Bitcoin. A blockchain's novelty is that it ensures the authenticity and security of a data record while also generating confidence without the requirement for a trusted third party. 

The way data is structured differs significantly between a traditional database and a blockchain. A blockchain is a digital ledger that stores data in groupings called blocks. When a block is full, it is closed and linked to the preceding block, producing a data chain known as the blockchain. All additional information added after that newly added block is compiled into a newly formed block, which is subsequently added to the chain once it is complete.

A database organizes data into tables, whereas a blockchain organizes data into chunks (blocks) that are strung together, as the name suggests. When implemented in a decentralized manner, this data structure creates an irreversible data chronology. When a block is filled, it becomes permanent and part of the chronology. When each block is added to the chain, it is given a specific time stamp.

The Blockchain can also be seen as a vertical stack, where the top of the stack is the most recent block and the bottom is the genesis block.

A blockchain makes also use of other components:
- Miner. Miners add a new block made of transactions to the blockchain. It requires a lot of computation to calculate the
hash of the previous block so to put it in the header of the new block. Hash method use is SHA256.  
  

- Peer. A peer is a node that is connected to the blockchain network. They host ledgers and smart contracts (IT protocols 
which create an agreement between two parties). These two are used to encapsulate information inside the blockchain.   

###### Benefits of Blockchain:
Duplicate record keeping and third-party validations waste a lot of time in operations. Fraud and cyberattacks can compromise record-keeping systems. Data verification might be slowed by lack of openness. Transaction volumes have exploded since the introduction of IoT. All of this slows operations, lowers profits, and signals the need for a better solution. Here comes blockchain.

- Greater assurance: 
As a member of a members-only network, you can trust that you will receive accurate and timely data from blockchain, and that your confidential blockchain records will be shared only with network members to whom you have specifically authorized access.

- Greater safety:
All network participants must agree on data accuracy, and all confirmed transactions are immutable because they are permanently recorded. A transaction cannot be deleted by anyone, including the system administrator. 

- Enhanced efficiencies:
Time-consuming record reconciliations are eliminated with a distributed ledger shared across network participants. A collection of rules called a smart contract can be placed on the blockchain and implemented automatically to speed up transactions.



## Frameworks

## Interactions

## Implementation Roadmap

## Code


