//storage for data (ConcurrentHashMap<key,value>)
package com.codebyisaac.cache;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;

public class CacheStorage {
    private final Map<String,CacheEntry> memoryMap = new ConcurrentHashMap<>();

    //standard set no expiry (-1)
    public void set(String key, String value){
        memoryMap.put(key,new CacheEntry(value, -1));
    }

    //overloaded SET that accepts TTL in milliseconds
    public void set(String key, String value, long ttlMs){
        long expireAt = System.currentTimeMillis() + ttlMs;
        memoryMap.put(key, new CacheEntry(value, expireAt));
    }

    public String get(String key) {
        CacheEntry entry = memoryMap.get(key);
        if (entry == null) return null;

        //check expiration status on read
        if (entry.isExpired()){
            System.out.println("Key " + key + " expired. Purging.");
            memoryMap.remove(key);
            return null;
        }
        return entry.getValue();
    }

    public void delete(String key){
        memoryMap.remove(key);
    }

    //for active background cleaner
    public Set<String> getAllKeys(){
        return memoryMap.keySet();
    }
}
