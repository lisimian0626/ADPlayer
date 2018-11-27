package com.example.administrator.myapplication.bussiness.constract;

import com.example.administrator.myapplication.base.BasePresenter;
import com.example.administrator.myapplication.base.BaseView;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * author: Hanson
 * date:   2016/9/19
 * describe:
 */
public interface ServerConstract {
    interface View extends BaseView<Presenter> {
        void showProgress();
        void hideProgress();
    }

    interface Presenter extends BasePresenter {
        void heartbeat(String json);
    }

    interface Model {
        Observable<ResponseBody> heartbeat(String json);
    }
}
