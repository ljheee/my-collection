package com.ljheee.collection.map;

/**
 *
 */
public class HashMap<K, V> {


    private static final int DEFAULT_SIZE = 1 << 4;
    private Entry<K, V>[] data; // table表
    private int capacity;// 2的整数次幂
    private int size = 0;// map存入了多少个


    public HashMap() {
        this(DEFAULT_SIZE);
    }

    public HashMap(int capacity) {
        data = new Entry[capacity];
        size = 0;
        this.capacity = capacity;
    }


    private class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;// entry 的next存放的是具有相同hash的entry
        int cap;//表示hash冲突的个数

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }


    private int hash(K key) {
        int h;
        h = (key == null) ? 0 : (h = key.hashCode()) ^ ((h >>> 16));
        return h % capacity;// 0~length-1
    }

    private void put(K key, V value) {
        int hash = hash(key);
        Entry<K, V> newE = new Entry<>(key, value, null);
        Entry<K, V> hasM = data[hash];
        while (hasM != null) {
            if (hasM.key.equals(key)) {
                hasM.value = value;// hash一样,equals也相等，就是相同的key：value覆盖原来的值
            }
            hasM = hasM.next;
        }

        newE.next = data[hash];
        data[hash] = newE;
        size++;
    }

    public V get(K key){
        int hash = hash(key);
        Entry<K, V> entry = data[hash];
        while(entry!=null){
            if(entry.key.equals(key)){
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    public static void main(String[] args) {
        HashMap<String,String> map = new HashMap<>();
        map.put("1","1");
        map.put("a","a");
        map.put("b","b");

        System.out.println(map.get("a"));
        System.out.println(map.get("1"));

    }
}
