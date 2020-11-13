package com.adwl.screenfulvideo.api.home;


import com.adwl.screenfulvideo.api.base.BaseApi;
import com.adwl.screenfulvideo.api.base.ResultHandler;
import com.adwl.screenfulvideo.api.base.RxHelper;
import com.adwl.screenfulvideo.api.entity.home.Joke;
import com.adwl.screenfulvideo.api.entity.mine.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * created by wmm on 2020/10/24
 */
public class HomeVideoApi extends BaseApi {

        public static Observable<User> getVideoList() {
        return service().videoList()
                .compose(ResultHandler.handleResult())
                .compose(RxHelper.io_main());

    }

    private static HomeVideoService service() {
        return retrofit().create(HomeVideoService.class);
    }

}
