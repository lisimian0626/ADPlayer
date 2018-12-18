package com.example.administrator.myapplication.evenbus;

public class PlayerEvent {

    private  int id;
    private  Object data;

    public static final int TYPE_DOWNLOADCOMPLITE = 0;
    protected PlayerEvent(int id) {
        this.id = id;
    }

    protected PlayerEvent(int id, Object data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
