package com.adwl.screenfulvideo.home.presenter;

import com.adwl.screenfulvideo.api.base.BaseSubscriber;
import com.adwl.screenfulvideo.api.entity.mine.User;
import com.adwl.screenfulvideo.api.home.HomeVideoApi;
import com.adwl.screenfulvideo.app.ScreenVideoApp;
import com.adwl.screenfulvideo.base.contract.presenter.BasePresenterImpl;
import com.adwl.screenfulvideo.base.contract.view.BaseLoadingView;
import com.adwl.screenfulvideo.home.contract.HomeFragmentContract;
import com.adwl.screenfulvideo.manager.UserManager;
import com.adwl.screenfulvideo.utils.DisposeUtils;
import com.adwl.screenfulvideo.utils.NetworkUtils;


import io.reactivex.disposables.Disposable;

public class HomeFragmentPresenter extends BasePresenterImpl<HomeFragmentContract.View> implements HomeFragmentContract.Presenter {

    private Disposable videoListDisposable;

    public HomeFragmentPresenter(HomeFragmentContract.View view) {
        super(view);
    }

    @Override
    public void getVideoList() {
        if (mView == null) {
            return;
        }
        if (!NetworkUtils.isNetworkConnected(ScreenVideoApp.getInstance())) {
            mView.showToast("请检查您的网络情况");
            return;
        }

        DisposeUtils.dispose(videoListDisposable);
        mView.showLoading(BaseLoadingView.DIALOG_LOADING);

        videoListDisposable = HomeVideoApi.getVideoList()
                .subscribeWith(new BaseSubscriber<User>() {
                    @Override
                    public void onNext(User user) {
                        UserManager.get().updateUser(user);
                        if (mView != null) {
                            mView.dismissLoading(BaseLoadingView.DIALOG_LOADING);
                            mView.getVideoListSuccess();
                        }
                    }

                    @Override
                    public boolean _onError(Throwable e) {
                        if(mView !=null){
                            mView.dismissLoading(BaseLoadingView.DIALOG_LOADING);
                        }
                        return true;
                    }
                });
    }
}
