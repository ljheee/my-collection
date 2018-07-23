package com.ljheee.collection.list;

import java.util.HashMap;

/**
 * Created by lijianhua04 on 2018/7/23.
 */
public class MyHashSet {


    private HashMap map = null;
    private final Object PRESENT = new Object();

    public MyHashSet() {
        map = new HashMap();
    }

    public int size(){
        return map.size();
    }

    public void add(Object value){
         map.put(value,PRESENT);
    }




}
