package com.adwl.screenfulvideo.api.config;


import com.adwl.screenfulvideo.api.base.BaseApi;
import com.adwl.screenfulvideo.api.base.ResultHandler;
import com.adwl.screenfulvideo.api.base.RxHelper;
import com.adwl.screenfulvideo.api.entity.config.Config;

import io.reactivex.Observable;

/**
 * created by wmm on 2020/10/15
 */
public class ConfigApi extends BaseApi {
    /**
     * 配置
     */
    public static Observable<Config> config() {
        return configService()
                .config()
                .compose(ResultHandler.handleResult())
                .compose(RxHelper.io_main());
    }

    private static ConfigService configService() {
        return retrofit().create(ConfigService.class);
    }
}
