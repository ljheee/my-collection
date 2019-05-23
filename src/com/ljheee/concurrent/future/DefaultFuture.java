package com.ljheee.concurrent.future;

import java.util.concurrent.Future;

/**
 * Created by lijianhua04 on 2019/5/23.
 */
public abstract class DefaultFuture<T> implements BaseFuture<T>{

    FutureListener f;

    @Override
    public BaseFuture addListener(FutureListener<? extends Future<? super T>> f) {
        this.f = f;
        return this;
    }

    @Override
    public BaseFuture removeListener(FutureListener<? extends Future<? super T>> f) {
        this.f = null;
        return this;
    }


    public FutureListener getListener() {
        return f;
    }
}
