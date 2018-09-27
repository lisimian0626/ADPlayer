package com.example.administrator.myapplication.evenbus;

import com.example.administrator.myapplication.model.ADModel;

public class PlayerEvent {

    public ADModel adModel;
    public static final int TYPE_UPDATE = 0;
    public static final int TYPE_UPDATE_PROGRESS = 1;
    public static final int TYPE_UPDATE_FAILURE = 2;

    public static PlayerEvent getEvent(ADModel adModel) {
        return new PlayerEvent(adModel);
    }

    protected PlayerEvent() {
    }

    protected PlayerEvent(ADModel adModel) {
        this.adModel = adModel;
    }

}
