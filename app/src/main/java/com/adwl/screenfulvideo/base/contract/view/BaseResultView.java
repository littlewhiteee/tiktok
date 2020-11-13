package com.adwl.screenfulvideo.base.contract.view;

/**
 * created by wmm on 2020/9/7
 */
public interface BaseResultView<RESULT> {
    void dataSuccess(RESULT data);

    void dataFail(Throwable e);
}
