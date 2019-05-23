package com.ljheee.concurrent.future;

import java.util.concurrent.Future;

/**
 *
 */
public interface FutureListener<F extends Future<?>>  {

    void onComplete(Future<?> future) throws Exception;
}
