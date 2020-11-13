package com.adwl.screenfulvideo.home.contract;


import com.adwl.screenfulvideo.api.entity.home.Joke;
import com.adwl.screenfulvideo.base.contract.presenter.BasePresenter;
import com.adwl.screenfulvideo.base.contract.view.BaseLoadingView;
import com.adwl.screenfulvideo.base.contract.view.BaseToastView;


/**
 * created by wmm on 2020/9/11
 */
public interface HomeFragmentContract {
    interface View extends BaseLoadingView, BaseToastView {
        void getVideoListSuccess();
    }


    interface Presenter extends BasePresenter {
        void getVideoList();
    }
}
