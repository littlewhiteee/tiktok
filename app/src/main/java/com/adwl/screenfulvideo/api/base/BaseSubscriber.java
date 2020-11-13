package com.adwl.screenfulvideo.api.base;


import io.reactivex.observers.DisposableObserver;

/**
 * created by wmm on 2020/9/8
 */
public abstract class BaseSubscriber<T> extends DisposableObserver<T> {

    @Override
    public void onComplete() {

    }

    @Override
    public final void onError(Throwable e) {
        if (!_onError(e)) {
            ErrorHandler.handleError(e);
        }
    }

    protected boolean _onError(Throwable e) {
        return false;
    }
}
