package com.iyize.async.method.invocation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author wangHui
 * @version V1.0
 * @Package com.iyize.async.method.invocation
 * @date 2023/8/30 17:58
 */
public interface AsyncExecutor {


    <T> AsyncResult<T> startProcess(Callable<T> task);

    <T> AsyncResult<T> startProcess(Callable<T> task, AsyncCallback<T> callback);

    <T> T endProcess(AsyncResult<T> asyncResult) throws ExecutionException, InterruptedException;
}
