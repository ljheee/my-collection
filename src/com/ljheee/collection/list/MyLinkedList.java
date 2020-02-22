package com.ljheee.collection.list;

import java.util.LinkedList;

/**
 * Created by ljheee on 2018/7/21.
 */
public class MyLinkedList {

    private static class Node {
        Object item;
        Node next;
        Node prev;

        Node(Node prev, Object element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }


    private Node first;
    private Node last;

    private int size;

    public MyLinkedList() {
    }


    public void add(Object object){
        if(first == null){
            first = new Node(null, object, null);
            last = first;
        } else {
            Node node = new Node(last, object, null);
            last = node;
        }
        size++;
    }

    public int size(){
        return size;
    }


    public Object get(int index){

        if(first == null){
            return null;
        }

        Node n  = first;
        int i = 0;
        while (i != index){
            n = n.next;
            i++;
        }
        return n.item;
    }

    public Object remove(int index){
        if(first == null){
            return null;
        }
        Node n  = first;
        int i = 0;
        while (i != index){
            n = n.next;
            i++;
        }
        Object result = n.item;
        n.next.prev = n.prev;
        return result;
    }

    public void add(int index ,Object object){

        Node n  = first;
        int i = 0;
        while (i != index){
            n = n.next;
            i++;
        }
        Node newNode = new Node(n.prev, object, n);
        n.prev.next = newNode;
        n.prev = newNode;

    }



}
