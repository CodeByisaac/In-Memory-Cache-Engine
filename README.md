# In-Memory-Cache-Engine
High-performance TCP server that implements a subset of the Redis protocol (RESP) to store frequently accessed data directly in RAM for fast retrieval.

## Features
- Custom RESP Parser: Implements the Redis Serialization Protocol for client-server communication.
- TCP Server: Handles concurrent incoming client socket connections.
- Core Redis Commands: Supports SET, GET, PING, DELETE operations with args validation.
- Active TTL Expiration: Uses asynchronous background threads to auto evict expired keys w/o blocking active reads/writes.
- Thread-Safe Storage: Engineered on top of a concurrent data storage layer to ensure memory safety.

## Tech Stack
- Language: Java 17+
- Networking: Java Sockets (TCP/IP)
- Protocol: RESP
