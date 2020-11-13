package com.adwl.screenfulvideo.api.login;


import com.adwl.screenfulvideo.api.entity.base.Result;
import com.adwl.screenfulvideo.api.entity.mine.User;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * created by wmm on 2020/9/8
 */
public interface LoginService {

    @FormUrlEncoded
    @POST("v2/user/login")
    Observable<Result<User>> login(@Field("phone") String phone, @Field("code") String code, @Field("password") String pwd);


}
