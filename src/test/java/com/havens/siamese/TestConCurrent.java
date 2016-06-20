package com.havens.siamese;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by havens on 16-6-21.
 */
public class TestConCurrent {
    public static void main(String[] args){
        ConcurrentHashMap<String,Integer> storage = new ConcurrentHashMap<String,Integer>();
        storage.put("First", 1);
        storage.put("Second", 2);
        storage.put("Third",3);


        //Is this the proper way of removing a specific item from a tread-safe collection?


        for (Integer entry : storage.values()) {
            storage.remove("First");
            System.out.println(" " + entry);
        }
    }
}
