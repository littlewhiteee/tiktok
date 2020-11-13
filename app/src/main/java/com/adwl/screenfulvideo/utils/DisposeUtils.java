package com.adwl.screenfulvideo.utils;

import io.reactivex.disposables.Disposable;

public class DisposeUtils {
    public static void dispose(Disposable... disposables) {
        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
