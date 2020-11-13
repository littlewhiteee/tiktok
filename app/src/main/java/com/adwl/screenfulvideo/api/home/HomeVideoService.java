package com.adwl.screenfulvideo.api.home;


import com.adwl.screenfulvideo.api.entity.base.Result;
import com.adwl.screenfulvideo.api.entity.home.Joke;
import com.adwl.screenfulvideo.api.entity.mine.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by wmm on 2020/10/24
 */
public interface HomeVideoService {

    /**
     * 首页 视频列表
     *
     */
    @GET("v2/index/index")
    Observable<Result<User>>videoList();
}
