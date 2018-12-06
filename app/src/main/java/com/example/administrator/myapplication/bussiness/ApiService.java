package com.example.administrator.myapplication.bussiness;

import com.example.administrator.myapplication.model.HeartbeatInfo;
import com.google.gson.JsonElement;

import org.json.JSONArray;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ApiService {
    @Streaming
    @GET("/")
    Observable<ResponseBody> download(@Url String url);

    @GET("/getHeartBreak/{json}")
    Observable<ResponseBody> heartbeat(@Path("json") String json);

}
