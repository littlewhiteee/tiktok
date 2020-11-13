package com.adwl.screenfulvideo.base.contract.view;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * created by wmm on 2020/9/7
 */
public interface BaseLoadingView {
    String PAGE_LOADING = "page_loading";//页面loading
    String DIALOG_LOADING = "dialog_loading";//弹窗loading

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({PAGE_LOADING, DIALOG_LOADING})
    @interface LoadingWay {

    }

    void showLoading(@LoadingWay String loadingWay);

    void dismissLoading(@LoadingWay String loadingWay);
}
