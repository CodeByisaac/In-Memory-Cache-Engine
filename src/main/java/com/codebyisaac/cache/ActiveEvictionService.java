package com.codebyisaac.cache;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ActiveEvictionService implements Runnable{
    private final CacheStorage storage;
    private static final int SLEEP_TIME = 10000;
    private static final int SAMPLE_SIZE = 20; //samples a random subset to save cpu cycles

    public ActiveEvictionService(CacheStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run () {
        System.out.println("Active Eviction Background thread is alive");
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(SLEEP_TIME);
                executeEvictionLoop();
            } catch (InterruptedException e) {
                System.out.println("Eviction thread interruptted ");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void executeEvictionLoop() {
        Set<String> keys = storage.getAllKeys();
        if (keys.isEmpty()) return;

        List<String> keyList = new ArrayList<>(keys);
        Collections.shuffle(keyList);  //to get random subset

        int checked = 0;
        int expiredCount = 0;

        for(String key : keyList) {
            if (checked >= SAMPLE_SIZE) break;
            
            //.get() triggers passive eviction del key when time is expired
            if (storage.get(key) == null) {
                expiredCount++;
            }
            checked++;
        }

        if (expiredCount > 0) {
            System.out.println("[Active Eviction] Inspected " + checked + " random keys. Purged " + expiredCount + " expired entries.");
        }

    }
}
