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

The Blockchain can also be seen as a vertical stack, where the top of the stack is the most recent block and the bottom is the genesis block.

A blockchain makes also use of other components:
- Miner. Miners add a new block made of transactions to the blockchain. It requires a lot of computation to calculate the
hash of the previous block so to put it in the header of the new block. Hash method use is SHA256.  
  

- Peer. A peer is a node that is connected to the blockchain network. They host ledgers and smart contracts (IT protocols 
which create an agreement between two parties). These two are used to encapsulate information inside the blockchain.   


## Frameworks

## Interactions

## Implementation Roadmap

## Code


