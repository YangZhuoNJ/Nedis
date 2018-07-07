package com.nj.nedis.store;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 2018/7/7.
 */
public class Store {

    private static Map<String, String> store = new ConcurrentHashMap<String, String>(1024 * 1024);

    public static Set<String> keys() {
        return store.keySet();
    }

    public static  void set(String key, String value) {
        store.put(key, value);
    }


    public static String get(String key) {
        return store.get(key);
    }
}
