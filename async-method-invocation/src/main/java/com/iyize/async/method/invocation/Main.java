package com.iyize.async.method.invocation;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author wangHui
 * @version V1.0
 * @Package org.example
 * @date 2023/8/30 17:44
 */
@Slf4j
public class Main {
    private static final String ROCKET_LAUNCH_LOG_PATTERN = "Space rocket <%s> launched successfully";

    public static void main(String[] args) throws Exception{
        ThreadAsyncExecutor executor = new ThreadAsyncExecutor();

        AsyncResult<Integer> asyncResult1 = executor.startProcess(lazyval(10, 500));
        AsyncResult<String> asyncResult2 = executor.startProcess(lazyval("test", 300));
        AsyncResult<Long> asyncResult3 = executor.startProcess(lazyval(50L, 700));
        AsyncResult<Integer> asyncResult4 = executor.startProcess(lazyval(20, 400), callback("Deploying lunar rover"));
        AsyncResult<String> asyncResult5 = executor.startProcess(lazyval("callback", 600), callback("Deploying lunar rover"));

        Thread.sleep(350);
        log("Mission command is sipping coffee");

        Integer integer = executor.endProcess(asyncResult1);
        String s = executor.endProcess(asyncResult2);
        Long aLong = executor.endProcess(asyncResult3);
        asyncResult4.await();
        asyncResult5.await();

        log(String.format(ROCKET_LAUNCH_LOG_PATTERN, integer));
        log(String.format(ROCKET_LAUNCH_LOG_PATTERN, s));
        log(String.format(ROCKET_LAUNCH_LOG_PATTERN, aLong));
    }


    private static <T> Callable<T> lazyval(T value, long delayMillis) {
        return () -> {
            Thread.sleep(delayMillis);
            log(String.format(ROCKET_LAUNCH_LOG_PATTERN, value));
            return value;
        };
    }

    private static <T> AsyncCallback<T> callback(String name) {

        return (value, ex) -> {
            if (ex.isPresent()) {
                log(String.format("%s failed: %s", name, ex.get().getMessage()));
            } else {
                log(String.format(ROCKET_LAUNCH_LOG_PATTERN, name));
            }
        };
    }

    private static void log(String msg) {
        LOGGER.info(msg);
    }
}