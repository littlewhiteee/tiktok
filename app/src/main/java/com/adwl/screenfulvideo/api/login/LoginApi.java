package com.adwl.screenfulvideo.api.login;


import com.adwl.screenfulvideo.api.base.BaseApi;
import com.adwl.screenfulvideo.api.base.ResultHandler;
import com.adwl.screenfulvideo.api.base.RxHelper;
import com.adwl.screenfulvideo.api.entity.mine.User;

import io.reactivex.Observable;

/**
 * created by wmm on 2020/9/8
 */
public class LoginApi extends BaseApi {

    public static Observable<User> login(String phone, String code, String pwd) {
        return loginService()
                .login(phone, code, pwd)
                .compose(ResultHandler.handleResult())
                .compose(RxHelper.io_main());
    }

    private static LoginService loginService() {
        return retrofit().create(LoginService.class);
    }
}
