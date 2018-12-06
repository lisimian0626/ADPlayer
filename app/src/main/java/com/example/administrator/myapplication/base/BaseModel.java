package com.example.administrator.myapplication.base;

public class BaseModel<T> {
    private T data;

    public BaseModel(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
