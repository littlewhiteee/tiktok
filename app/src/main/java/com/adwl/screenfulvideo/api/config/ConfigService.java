package com.adwl.screenfulvideo.api.config;


import com.adwl.screenfulvideo.api.entity.base.Result;
import com.adwl.screenfulvideo.api.entity.config.Config;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * created by wmm on 2020/10/15
 */
public interface ConfigService {
    @GET("v2/index/config")
    Observable<Result<Config>> config();

}
