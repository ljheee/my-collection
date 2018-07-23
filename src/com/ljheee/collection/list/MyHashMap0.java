package com.ljheee.collection.list;

/**
 * Created by lijianhua04 on 2018/7/21.
 */
public class MyHashMap0 {

    static class MyEntry{
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
    private MyEntry[] entries;

    public MyHashMap0(int initCapacity) {

        entries = new MyEntry[initCapacity];
    }

    public  MyHashMap0(){
        this(16);
    }


    public void put(Object key, Object value){

        // todo 扩容
        for (int i = 0; i < size; i++){
            if(entries[i].key.equals(key)){
                entries[i].value = value;// 检查重复,覆盖
                return;
            }
        }

        entries[size++] = new MyEntry(key, value);
    }

    public MyEntry get(Object key) {

        MyEntry entry = null;
        for (int i = 0; i < size; i++){
            if(entries[i].key.equals(key)){
                entry = entries[i];
                break;
            }
        }
        return entry;
    }

    public boolean containsKey(Object  key){
        for (int i = 0; i < size; i++){
            if(entries[i].key.equals(key)){
                return true;
            }
        }
        return false;
    }








}
