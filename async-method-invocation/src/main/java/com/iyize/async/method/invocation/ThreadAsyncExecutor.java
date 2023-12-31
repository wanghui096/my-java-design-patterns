package com.iyize.async.method.invocation;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangHui
 * @version V1.0
 * @Package com.iyize.async.method.invocation
 * @date 2023/8/31 10:54
 */
public class ThreadAsyncExecutor implements AsyncExecutor {

    private final AtomicInteger idx = new AtomicInteger(0);
    @Override
    public <T> AsyncResult<T> startProcess(Callable<T> task) {
        return startProcess(task, null);
    }

    @Override
    public <T> AsyncResult<T> startProcess(Callable<T> task, AsyncCallback<T> callback) {
        CompletableResult<T> completableResult = new CompletableResult<>(callback);
        new Thread(() -> {
            try {
                completableResult.setValue(task.call());
            } catch (Exception e) {
                completableResult.setException(e);
            }
        }, "executor-" + idx.incrementAndGet()).start();
        return completableResult;
    }

    @Override
    public <T> T endProcess(AsyncResult<T> asyncResult) throws ExecutionException, InterruptedException {
        if (!asyncResult.isCompleted()) {
            asyncResult.await();
        }
        return asyncResult.getValue();
    }

    private static class CompletableResult<T> implements AsyncResult<T> {
        static final int RUNNING = 1;
        static final int FAILED = 2;
        static final int COMPLETED = 3;

        final Object lock;
        final Optional<AsyncCallback<T>> callback;
        volatile int state = RUNNING;
        T value;
        Exception exception;

        CompletableResult(AsyncCallback<T> callback) {
            this.lock = new Object();
            this.callback = Optional.ofNullable(callback);
        }

        void setValue(T value) {
            this.value= value;
            this.state = COMPLETED;
            this.callback.ifPresent(ac -> ac.onComplete(value, Optional.empty()));
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        void setException(Exception exception) {
            this.exception = exception;
            this.state = FAILED;
            this.callback.ifPresent(ac -> ac.onComplete(null, Optional.of(exception)));
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        @Override
        public boolean isCompleted() {
            return state > RUNNING;
        }

        @Override
        public T getValue() throws ExecutionException {
            if (state == COMPLETED) {
                return value;
            } else if (state == FAILED) {
                throw new ExecutionException(exception);
            }
            throw new IllegalStateException("Execution not completed yet");
        }

        @Override
        public void await() throws InterruptedException {
            synchronized (lock) {
                if (!isCompleted()) {
                    lock.wait();
                }
            }
        }
    }
}
