//storage for data (HashMap<key,value>)
package com.codebyisaac.cache;
import java.util.Map;
import java.util.HashMap;

public class CacheEngine {
    private final Map<String,String> memoryMap = new HashMap<>();

    public void set(String key, String value){
        memoryMap.put(key,value);
    }
    public String get(String key) {
        return memoryMap.get(key);
    }
    public void delete(String key){
        memoryMap.remove(key);
    }
}
