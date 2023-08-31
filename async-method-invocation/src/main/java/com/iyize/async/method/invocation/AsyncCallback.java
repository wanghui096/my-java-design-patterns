package com.iyize.async.method.invocation;

import java.util.Optional;

/**
 * @author wangHui
 * @version V1.0
 * @Package com.iyize.async.method.invocation
 * @date 2023/8/30 17:56
 */
public interface AsyncCallback<T> {

    void onComplete(T value, Optional<Exception> e);

}
