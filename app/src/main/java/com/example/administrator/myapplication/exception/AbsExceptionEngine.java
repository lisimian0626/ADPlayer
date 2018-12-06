package com.example.administrator.myapplication.exception;

import io.reactivex.functions.Consumer;

/**
 * author: Hanson
 * date:   2018/1/17
 * describe:
 */

public abstract class AbsExceptionEngine implements Consumer<Throwable> {
    /**
     * 处理出错信息
     * @param message 出错信息
     */
    public abstract void handMessage(String message);

    @Override
    public void accept(Throwable throwable) throws Exception {
        handMessage(ExceptionHandler.handle(throwable));
    }
}
