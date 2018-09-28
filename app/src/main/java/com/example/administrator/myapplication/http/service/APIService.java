package com.example.administrator.myapplication.http.service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface APIService {
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
