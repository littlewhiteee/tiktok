package com.adwl.screenfulvideo.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.adwl.screenfulvideo.base.contract.presenter.BasePresenter;

import org.jetbrains.annotations.NotNull;

/**
 * created by wmm on 2020/9/7
 */
public abstract class BaseMVPFragment<T extends BasePresenter> extends BaseLoadFragment {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mPresenter = createPresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 创建Presenter
     *
     * @return Presenter
     */
    public abstract T createPresenter();

    @Override
    public void onDestroyView() {
//        if (mPresenter != null) {
//            mPresenter.detach();
//            mPresenter = null;
//        }
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
            mPresenter = null;
        }
        super.onDestroy();
    }
}
