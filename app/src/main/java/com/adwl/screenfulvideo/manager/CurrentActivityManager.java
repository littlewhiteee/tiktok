package com.adwl.screenfulvideo.manager;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * created by wmm on 2020/9/8
 */
public class CurrentActivityManager {

    private static CurrentActivityManager mInstance;
    private WeakReference<Activity> activityWF;
    private WeakReference<Activity> lastActivityWF;

    public static CurrentActivityManager getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentActivityManager();
        }
        return mInstance;
    }

    private CurrentActivityManager() {
    }

    public Activity getCurrentActivity() {
        if (activityWF == null) {
            return null;
        }
        return activityWF.get();
    }

    public Activity getLastActivity() {
        if (lastActivityWF == null) {
            return null;
        }

        return lastActivityWF.get();
    }

    public void setCurrentActivity(Activity activity) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            lastActivityWF = null;
        } else {
            lastActivityWF = new WeakReference<>(currentActivity);
        }

        if (activity == null) {
            activityWF = null;
            return;
        }
        activityWF = new WeakReference<>(activity);
    }

    public boolean isEqualActivity(Class<?> clazz) {
        return getCurrentActivity() != null && getCurrentActivity().getClass() == clazz;
    }
}
