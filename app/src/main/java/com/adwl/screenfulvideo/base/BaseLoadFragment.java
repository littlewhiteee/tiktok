package com.adwl.screenfulvideo.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.adwl.screenfulvideo.widget.GlobalStatusView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * created by wmm on 2020/9/7
 */
public abstract class BaseLoadFragment extends BaseFragment {
    private FrameLayout rootLayout;

    private GlobalStatusView statusView;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(enableStatusView()){
            rootLayout = new FrameLayout(Objects.requireNonNull(getContext()));
            rootLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rootLayout.addView(rootView);
            addStatusView();
            statusView.setBuild(createStatusViewBuild());
            return rootLayout;
        }else{
            return rootView;
        }
    }

    public boolean enableStatusView(){
        return false;
    }

    private void addStatusView() {
        statusView = new GlobalStatusView(getContext());
        rootLayout.addView(statusView);
        statusView.setStatusViewListener(this::onRetry);
    }

    /**
     * 显示Loading
     */
    public void displayLoading() {
        if (statusView != null) {
            statusView.setStatus(GlobalStatusView.STATUS_LOADING);
        }
    }

    /**
     * 显示空页面
     */

    public void displayEmpty() {
        if (statusView != null) {
            statusView.setStatus(GlobalStatusView.STATUS_EMPTY_DATA);
        }
    }

    /**
     * 显示错误页面
     */

    public void displayError() {
        if (statusView != null) {
            statusView.setStatus(GlobalStatusView.STATUS_LOAD_FAILED);
        }
    }

    /**
     * loading成功页面
     */

    public void displaySuccess() {
        if (statusView != null) {
            statusView.setStatus(GlobalStatusView.STATUS_LOAD_SUCCESS);
        }
    }

    /**
     * 重试
     */
    public void onRetry() {

    }

    //空页面，loading页面，错误页面配置
    public  GlobalStatusView.Build createStatusViewBuild(){
        return null;
    }
}
