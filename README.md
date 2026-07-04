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

## Run the Server
Compile and run the server using your preferred IDE, or execute commands below directly from your project root folder via terminal:
```bash
# compile the java classes
javac -d bin src/main/java/com/codebyisaac/cache/**/*.java

# start the cache server
java -cp bin com.codebyisaac.cache.CacheServer
```
*The server will boot up and begin listening for incoming traffic on port `6379`.*

## Connect via Redis-CLI
Because this engine is engineered entirely on top of the official RESP protocol, you can use the native Redis command-line utility to talk to it directly:
```bash
# connect to your custom local engine
redis-cli -p 6379
```

## Usage sample
Once conneced via `redis-cli` you can interact with the engine using standard syntax:
```text
# test connectivity
127.0.0.1:6379> PING
PONG

# store a basic key-value pair
127.0.0.1:6379> SET username "VictorIsaac"
OK

# retrieve the value
127.0.0.1:6379> GET username
"VictorIsaac"

# set a key with a 5-second Time-To-Live (TTL)
127.0.0.1:6379> SET token "xAha3mda41" PX 5000
OK
```