package com.ljheee.concurrent.future;

import java.util.concurrent.Future;

/**
 * Created by ljheee on 2019/5/23.
 */
public interface BaseFuture<T> extends Future<T> {
    default BaseFuture addListener(FutureListener<? extends Future<? super T>> var1){
        return this;
    }

    default BaseFuture removeListener(FutureListener<? extends Future<? super T>> var1){
        return this;
    }

//    BaseFuture addListeners(FutureListener... var1);
//
//    BaseFuture removeListeners(FutureListener... var1);

}
