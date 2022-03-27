package ITEM82;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Example {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        Map<String, String> map = new HashMap<>();

        map.putIfAbsent("K", "V");
        concurrentMap.get("test");

        Collections.synchronizedMap(map);
    }
}