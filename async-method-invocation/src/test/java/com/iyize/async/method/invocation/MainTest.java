package com.iyize.async.method.invocation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wangHui
 * @version V1.0
 * @Package com.iyize.async.method.invocation
 * @date 2023/8/31 13:27
 */
class MainTest {

    @Test
    void shouldExecuteApplicationWithoutException() {
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }

}