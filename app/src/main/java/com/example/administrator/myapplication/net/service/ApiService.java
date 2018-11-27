package com.example.administrator.myapplication.net.service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiService {
    @Streaming
    @GET("/")
    Observable<ResponseBody> download(@Url String url);

    @GET("/getHeartBreak/{json}")
    Observable<ResponseBody> heartbeat(@Path("json") String json);

}
