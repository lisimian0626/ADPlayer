package com.example.administrator.myapplication.constract;


import com.example.administrator.myapplication.base.BasePresenter;
import com.example.administrator.myapplication.base.BaseView;
import com.example.administrator.myapplication.http.HttpResult;
import com.example.administrator.myapplication.model.ADModel;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * author: Hanson
 * date:   2016/8/31
 * describe:
 */
public interface MainConstract {
    interface View extends BaseView<Presenter> {
        void updateProfile(ADModel adModel);
    }

    interface Presenter extends BasePresenter {
        void getProfile(String secret);
        void getRoomCount();
        void getShops();
    }

    interface Model {
        Observable<HttpResult<ADModel>> getProfile(String secret);

    }
}
