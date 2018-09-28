package com.example.administrator.myapplication.http;

import com.beidousat.mobile.minik.common.EventBusID;
import com.beidousat.mobile.minik.common.MessageEvent;
import com.beidousat.mobile.minik.common.TConst;
import com.beidousat.mobile.minik.common.UserInfo;
import com.beidousat.mobile.minik.tools.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: Hanson
 * date:   2016/9/8
 * describe:
 */
public class TokenAuthenticator implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //拦截请求，添加认证信息
        request = request.newBuilder()
                .addHeader("access_token", UserInfo.getInstance().getUser().token)
                .addHeader("user_id", UserInfo.getInstance().getUser().id)
                .build();

        Response response = chain.proceed(request);
        if (response.code() == TConst.HttpStatus.NOT_AUTHORIZATION ||
                response.code() == TConst.HttpStatus.NOT_AUTHENTICATION) {
            SharePreferenceUtil util = SharePreferenceUtil.createConfigFile();
            util.setTokenExpire(true);
            EventBus.getDefault().post(new MessageEvent(EventBusID.AUTHORIZATION, "登录超时，请重新登录"));
        }

        return response;
    }
}
