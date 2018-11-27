package com.example.administrator.myapplication.bussiness.persent;

import android.provider.ContactsContract;

import com.example.administrator.myapplication.bussiness.constract.ServerConstract;
import com.example.administrator.myapplication.bussiness.function.ServerFunction;
import com.example.administrator.myapplication.net.HttpResult;
import com.example.administrator.myapplication.utils.L;
import com.google.gson.JsonObject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * author: Hanson
 * date:   2016/9/19
 * describe:
 */
public class ServerPresenter implements ServerConstract.Presenter {
    ServerConstract.View mView;
    ServerConstract.Model mModel;
    CompositeSubscription mSubscriptions;

    public ServerPresenter(ServerConstract.View mView) {
        this.mView = mView;
        this.mSubscriptions = new CompositeSubscription();
        this.mModel = new ServerFunction();
        this.mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void heartbeat(String json) {
        Subscription subscription = mModel.heartbeat(json)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showProgress();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mSubscriptions.add(subscription);
    }
}
