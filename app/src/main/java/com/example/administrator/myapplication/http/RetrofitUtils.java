package com.example.administrator.myapplication.http;


import com.beidousat.mobile.minik.common.TConst;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retofit网络请求工具类
 * Created by HDL on 2016/7/29.
 */
public class RetrofitUtils {
    private static final int READ_TIMEOUT = 5;//读取超时时间,单位  秒
    private static final int CONN_TIMEOUT = 10;//连接超时时间,单位  秒

    private static Retrofit mRetrofit;

    private RetrofitUtils() {

    }

    public static Retrofit newInstence() {
        mRetrofit = null;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addNetworkInterceptor(new StethoInterceptor())
//                .addNetworkInterceptor(new TokenAuthenticator())
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)//添加一个client,不然retrofit会自己默认添加一个
                .baseUrl(TConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit;
    }

    public static Retrofit newInstence(Converter.Factory convertFactory) {
        mRetrofit = null;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addNetworkInterceptor(new StethoInterceptor())
//                .addNetworkInterceptor(new TokenAuthenticator())
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)//添加一个client,不然retrofit会自己默认添加一个
                .baseUrl(TConst.BASE_URL)
                .addConverterFactory(convertFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit;
    }

    public static Retrofit getVersionInstance() {
        mRetrofit = null;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addNetworkInterceptor(new StethoInterceptor())
//                .addNetworkInterceptor(new TokenAuthenticator())
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)//添加一个client,不然retrofit会自己默认添加一个
                .baseUrl(TConst.VERSION_UPDATE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit;
    }
}
