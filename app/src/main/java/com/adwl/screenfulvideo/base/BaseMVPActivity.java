package com.adwl.screenfulvideo.base;

import android.os.Bundle;

import com.adwl.screenfulvideo.base.contract.presenter.BasePresenter;


/**
 * created by wmm on 2020/9/7
 */
public abstract class BaseMVPActivity<T extends BasePresenter> extends TopbarBaseActivity {
    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    /**
     * 创建Presenter
     *
     * @return Presenter
     */
    public abstract T createPresenter();

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter = null;
        }
        super.onDestroy();
    }
}
