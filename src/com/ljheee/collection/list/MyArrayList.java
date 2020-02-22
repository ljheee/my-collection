package com.ljheee.collection.list;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by ljheee on 2018/7/21.
 */
public class MyArrayList {


    private Object[] elementData;
    private int size;


    public MyArrayList() {
    }


    public MyArrayList(int initCapacity) throws Exception {

        if(initCapacity < 0){
            throw new Exception("invalue capacity");
        }

        elementData = new Object[initCapacity];
    }

    public void add(Object item){

        //ensure capacity
        if(size  == elementData.length ){
            Object newElements[] = new Object[size * 2 +1];
            System.arraycopy(elementData,0,newElements,0, elementData.length);
            elementData = newElements;
        }

        elementData[size++] = item;
    }



    public boolean isEmpty(){
        return size==0;
    }

    public Object get(int index) throws Exception {
        if(index< 0 || index >= size){
            throw new Exception("");
        }
        return elementData[index];
    }


    public Object remove(int index) throws Exception {
        //a b c
        if(index< 0 || index >= size){
            throw new Exception("");
        }

        Object oldValue = elementData[index];

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = null; // clear to let GC do its work

        return oldValue;
    }


    public void remove(Object obj) throws Exception {
        for (int i=0;i < size;i++){
            if(get(i).equals(obj)){
                remove(i);
            }
        }
    }


    public Object set(int index, Object newValue){

        Object oldValue = elementData[index];
        elementData[index] = newValue;
        return oldValue;
    }



    public void add(int index, Object obj){


        System.arraycopy(elementData,index,elementData, index+1, size-index);
        elementData[index] = obj;
        size++;
    }

}
