package com.codebyisaac.cache;

public class CacheEntry {
    private final String value;
    private final long expireAt;

    public CacheEntry(String value, long expireAt){
        this.value = value;
        this.expireAt=expireAt;
    }

    public String getValue(){
        return value;
    }

    public boolean isExpired(){
        if (expireAt == -1) return false;  //-1 means key-value leaves forever
        return System.currentTimeMillis() > expireAt;
    }



    

    

    //check if expired

}
