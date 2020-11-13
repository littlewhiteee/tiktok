package com.adwl.screenfulvideo.api.base;

import android.app.Activity;

import com.adwl.screenfulvideo.manager.CurrentActivityManager;
import com.adwl.screenfulvideo.manager.ToastManager;
import com.adwl.screenfulvideo.utils.EmptyUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * created by wmm on 2020/9/8
 */
public abstract class ErrorHandler {

    public static void handleError(Throwable e) {
        String errorMsg = errorMsg(e);

        if (EmptyUtils.isEmpty(errorMsg)) {
            errorMsg = "请求失败";
        }
        showErrorMsg(errorMsg);
    }

    public static String errorMsg(Throwable e) {
        if (e == null) {
            return null;
        }

        if (e instanceof ApiException) {
            return ((ApiException) e).friendlyMessage();
        }

        if (e instanceof IOException) { // network exception
            if (e instanceof SocketTimeoutException) {
                // 超时
                return "连接超时";
            } else {
                // 连接错误
                return "请检查您的网络情况";
            }
        }
        return null;
    }


    /**
     * 显示错误提示
     *
     * @param msg msg
     */
    private static void showErrorMsg(String msg) {
        Activity context = CurrentActivityManager.getInstance().getCurrentActivity();
        if (context == null || EmptyUtils.isEmpty(msg)) {
            return;
        }
        ToastManager.showToastForShort(context, msg);

    }
}
