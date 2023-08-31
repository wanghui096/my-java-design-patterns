package com.iyize.async.method.invocation;

import java.util.concurrent.ExecutionException;

/**
 * @author wangHui
 * @version V1.0
 * @Package com.iyize.async.method.invocation
 * @date 2023/8/30 17:55
 */
public interface AsyncResult<T> {
    boolean isCompleted();

    T getValue() throws ExecutionException;

    void await() throws InterruptedException;
}
