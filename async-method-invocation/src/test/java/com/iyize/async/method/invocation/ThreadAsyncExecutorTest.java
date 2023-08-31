package com.iyize.async.method.invocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author wangHui
 * @version V1.0
 * @Package com.iyize.async.method.invocation
 * @date 2023/8/31 13:44
 */
class ThreadAsyncExecutorTest {


    @Mock
    private Callable<Object> task;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulTaskWithoutCallback() throws Exception {
        assertTimeout(Duration.ofMillis(3000), () -> {
            ThreadAsyncExecutor executor = new ThreadAsyncExecutor();
            Object result = new Object();
            when(task.call()).thenReturn(result);

            AsyncResult<Object> asyncResult = executor.startProcess(task);
            assertNotNull(asyncResult);
            asyncResult.await();
            assertTrue(asyncResult.isCompleted());

            verify(task, times(1)).call();

            assertSame(result, asyncResult.getValue());


        });
    }
}