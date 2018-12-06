package com.example.administrator.myapplication.base;

import com.example.administrator.myapplication.bussiness.ApiService;
import com.example.administrator.myapplication.net.RetrofitUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * author: Hanson
 * date:   2018/1/17
 * describe:通用Presenter，确定V-P一对一关系
 */

public abstract class CommonPresenter<T> implements BasePresenter {
    protected CompositeDisposable mCompositeDisposable;
    protected T mView;
    protected ApiService mApiService;

    public CommonPresenter(T view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mApiService= RetrofitUtils.newInstence().create(ApiService.class);
    }

    @Override
    public void cancel() {
        mCompositeDisposable.clear();
    }

    public void addDispose(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    /**
     * 获取调用该方法的方法名（例如：AAA()中调用getMethodName，返回 “AAA”）
     * @return
     */
    public String getMethodName() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        return stack[3].getMethodName();
    }
}
