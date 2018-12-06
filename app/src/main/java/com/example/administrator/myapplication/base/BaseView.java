package com.example.administrator.myapplication.base;

/**
 * author: Hanson
 * date:   2016/8/30
 * describe:
 */
public interface BaseView<T> {
    void onFeedBack(boolean success, String key, Object data);
}
