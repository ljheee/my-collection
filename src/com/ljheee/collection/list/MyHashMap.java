package com.ljheee.collection.list;

import java.util.LinkedList;

/**
 * Created by ljheee on 2018/7/21.
 */
public class MyHashMap {

    static class MyEntry {
        Object key;
        Object value;

        public MyEntry() {
        }

        public MyEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }


    private int size;
    private int capacity;

    private LinkedList linkedLists[];

    public MyHashMap(int capacity) {
        this.capacity = capacity;
        linkedLists = new LinkedList[capacity];
    }

    public MyHashMap() {
        this(16);
    }

    public void put(Object key, Object value) {

        int hash = key.hashCode();
        hash = hash < 0 ? -hash : hash;
        int n = hash % capacity;
        if (linkedLists[n] == null) {
            linkedLists[n] = new LinkedList();
        }

        linkedLists[n].add(new MyEntry(key, value));
    }


    public MyEntry get(Object key) {

        int hash = key.hashCode();
        hash = hash < 0 ? -hash : hash;
        int n = hash % capacity;

        if (linkedLists[n] == null) {
            return null;
        }

        for (Object e : linkedLists[n]) {
            if (((MyEntry) e).key.equals(key)) {
                return (MyEntry) e;
            }
        }
        return null;
    }


}
