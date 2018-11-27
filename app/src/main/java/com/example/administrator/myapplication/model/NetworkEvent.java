package com.example.administrator.myapplication.model;

/**
 * author: Hanson
 * date:   2018/1/12
 * describe:
 */

public class NetworkEvent {
    private String mMessage;
    private int mCode;
    private boolean isNetworkOk;

    public NetworkEvent(boolean isNetworkOk) {
        this.isNetworkOk = isNetworkOk;
    }

    public NetworkEvent(String message) {
        this.mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public boolean isNetworkOk() {
        return isNetworkOk;
    }

    public void setNetworkOk(boolean networkOk) {
        isNetworkOk = networkOk;
    }
}
