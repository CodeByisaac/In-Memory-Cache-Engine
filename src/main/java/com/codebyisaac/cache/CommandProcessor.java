package com.codebyisaac.cache;

import java.util.List;
public class CommandProcessor {
    private final CacheEngine cache;

    public CommandProcessor(CacheEngine cache) {
        this.cache = cache;
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
                String setKey = tokens.get(1);
                String setValue = tokens.get(2);
                cache.set(setKey, setValue);
                return ProtocolParser.toString("OK");

            case "GET":
                if (tokens.size() < 2) return ProtocolParser.toError("ERR wrong no of argument for 'get' command ");
                String getKey = tokens.get(1);
                String result = cache.get(getKey);
                if (result == null) return ProtocolParser.toNullBulkString();
                return ProtocolParser.toBulkString(result);

            case "DELETE":
                if (tokens.size() < 2) return ProtocolParser.toError("ERR wrong no of argument for 'delete' command");
                String delKey = tokens.get(1);
                cache.delete(delKey);
                return ProtocolParser.toString("OK");
        
            default:
                return ProtocolParser.toError("ERR unknown command '" + action + "'" );  
        }

    }
}
