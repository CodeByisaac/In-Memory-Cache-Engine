package com.codebyisaac.cache;

import java.util.List;
public class CommandHandler {
    private final CacheStorage storage;

    public CommandHandler(CacheStorage storage) {
        this.storage = storage;
    }

    public String execute(List<String>tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return ProtocolParser.toError("ERR empty command");
        }

        String action = tokens.get(0).toUpperCase();

        switch(action) {
            case "PING" :
                return ProtocolParser.toString("PONG");
                
            case "SET":
                if (tokens.size() < 3) return ProtocolParser.toError("ERR wrong no of argumnents for 'set' command");
                String key = tokens.get(1);
                String val = tokens.get(2);
                
                //check if client passed : SET key value PX 5000
                if (tokens.size() >= 5 && tokens.get(3).equalsIgnoreCase("PX")){
                    long ttl = Long.parseLong(tokens.get(4));
                    storage.set(key, val, ttl);
                } else {
                    storage.set(key, val);
                }
                return ProtocolParser.toString("OK");

            case "GET":
                if (tokens.size() < 2) return ProtocolParser.toError("ERR wrong no of argument for 'get' command ");
                String getKey = tokens.get(1);
                String result = storage.get(getKey);
                if (result == null) return ProtocolParser.toNullBulkString();
                return ProtocolParser.toBulkString(result);

            case "DELETE":
                if (tokens.size() < 2) return ProtocolParser.toError("ERR wrong no of argument for 'delete' command");
                String delKey = tokens.get(1);
                storage.delete(delKey);
                return ProtocolParser.toString("OK");
        
            default:
                return ProtocolParser.toError("ERR unknown command '" + action + "'" );  
        }

    }
}
