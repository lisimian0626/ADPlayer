package com.example.administrator.myapplication.exception;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


/**
 * author: Hanson
 * date:   2018/1/17
 * describe:
 */

public class ExceptionHandler {
    public static String handle(Throwable throwable) {
        String msg = "";

        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            msg = processHttpError(httpException);
        } else if (throwable instanceof SocketTimeoutException) {
            msg = "网络超时，请重试...";
        } else if (throwable instanceof SocketException || throwable instanceof UnknownHostException) {
            msg = "未开启网络";
        } else {
            msg = "未知错误";
        }

        return msg;
    }

    private static String processHttpError(HttpException httpException) {
        String msg = "";
        switch (httpException.code()) {
            case 400:
            case 404:
                msg = "链接错误";
                break;
            case 401:
            case 402:
                msg = "用户未认证";
                break;
        }

        return msg;
    }
}
