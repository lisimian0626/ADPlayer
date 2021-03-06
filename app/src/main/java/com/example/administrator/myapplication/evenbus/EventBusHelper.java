package com.example.administrator.myapplication.evenbus;

import com.example.administrator.myapplication.model.PlanListInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * author: Hanson
 * date:   2018/2/27
 * describe:
 * @author admin
 */

public class EventBusHelper {

    public static void register(Object target) {
        EventBus.getDefault().register(target);
    }

    public static void unregister(Object target) {
        EventBus.getDefault().unregister(target);
    }

    public static void sendEvent(BusEvent even) {
        EventBus.getDefault().post(even);
    }
//    public static final void sendLoginEvent(LoginEvent event) {
//        EventBus.getDefault().post(event);
//    }
//
//    public static void sendThirdPartyLogin(ThirdPartyLoginEvent event) {
//        EventBus.getDefault().post(event);
//    }

    public static void sendDownComplite(String str_plan) {
        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.TYPE_DOWNLOADCOMPLITE, str_plan));
    }


}
