package com.ljheee.collection.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe 使用示例
 * 直接操作内存(性能会有提高) netty源码常用该方式
 */
public class UnsafeDemo {

    static Unsafe unsafe;
    int value = 99;
    static long valueOffset;

    static {
//        unsafe = Unsafe.getUnsafe();

        try {
            Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            unsafe = (Unsafe) declaredField.get(null);

            // value 变量的位置地址偏移量
            valueOffset = unsafe.objectFieldOffset(UnsafeDemo.class.getDeclaredField("value"));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public void add() {
        int current;

        do {
            current = unsafe.getIntVolatile(this, valueOffset);//获取当前内存中的值
        } while (!unsafe.compareAndSwapInt(this, valueOffset, current, current + 1));// CAS 执行加1
    }

    public static void main(String[] args) {

        System.out.println(valueOffset);//12

        UnsafeDemo unsafeDemo = new UnsafeDemo();
        unsafeDemo.add();
        System.out.println(unsafeDemo.value);// 100

        unsafe.putIntVolatile(unsafeDemo, valueOffset, 33);
        System.out.println(unsafe.getIntVolatile(unsafeDemo, valueOffset));// 33
    }
}
