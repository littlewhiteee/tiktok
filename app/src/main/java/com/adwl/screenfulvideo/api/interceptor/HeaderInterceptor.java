package com.adwl.screenfulvideo.api.interceptor;


import com.adwl.screenfulvideo.manager.UserManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by wmm on 2020/9/8
 */
public class HeaderInterceptor implements Interceptor {
    public static final String K_UNIQUE_ID = "unique_id"; // 唯一id

    private String ua;

    public HeaderInterceptor(String ua) {
        this.ua = ua;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //封装headers
        Request request = chain.request().newBuilder()
                .addHeader("User-Agent", ua) //添加请求头信息
                .addHeader("Authorization", UserManager.get().getToken())
//                .addHeader("unique_id", Remember.getString(K_UNIQUE_ID, ""))
                .build();
        return chain.proceed(request);
    }
}