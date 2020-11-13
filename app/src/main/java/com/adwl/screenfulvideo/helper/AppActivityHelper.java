package com.adwl.screenfulvideo.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.adwl.screenfulvideo.manager.CurrentActivityManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * created by wmm on 2020/9/8
 */
public class AppActivityHelper {

    private AppActivityHelper() {
    }

    private static class SingletonInstance {
        private static final AppActivityHelper INSTANCE = new AppActivityHelper();

    }

    public static AppActivityHelper getInstance() {
        return SingletonInstance.INSTANCE;
    }

    /**
     * 注册状态监听，仅在Application中使用
     */
    public void register(Application application) {
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public void unRegister(Application application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        //打开的Activity数量统计
        private int activityStartCount = 0;

        @Override
        public void onActivityCreated(@NotNull Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NotNull Activity activity) {
            activityStartCount++;
            //数值从0变到1说明是从后台切到前台
            if (activityStartCount == 1) {
                //从后台切到前台
                notifyListenerOnFront();
            }
        }

        @Override
        public void onActivityResumed(@NotNull Activity activity) {
            CurrentActivityManager.getInstance().setCurrentActivity(activity);
        }

        @Override
        public void onActivityPaused(@NotNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NotNull Activity activity) {
            activityStartCount--;
            //数值从1到0说明是从前台切到后台
            if (activityStartCount == 0) {
                //从前台切到后台
                notifyListenerOnBack();
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NotNull Activity activity) {
            if (activity == CurrentActivityManager.getInstance().getCurrentActivity()) {
                CurrentActivityManager.getInstance().setCurrentActivity(null);
            }
        }
    };


    private List<OnAppStatusListener> listeners = new ArrayList<>();

    private void notifyListenerOnBack() {
        for (OnAppStatusListener listener : listeners) {
            listener.onBack();
        }
    }

    private void notifyListenerOnFront() {
        for (OnAppStatusListener listener : listeners) {
            listener.onFront();
        }
    }

    /**
     * 注意适当时机调用 removeOnAppStatusListener
     */
    public void addOnAppStatusListener(OnAppStatusListener onAppStatusListener) {
        if (this.listeners.contains(onAppStatusListener)) {
            return;
        }
        this.listeners.add(onAppStatusListener);
    }

    public void removeOnAppStatusListener(OnAppStatusListener onAppStatusListener) {
        if (!this.listeners.contains(onAppStatusListener)) {
            return;
        }
        this.listeners.remove(onAppStatusListener);
    }

    public interface OnAppStatusListener {
        void onFront();

        void onBack();
    }

    public static class OnAppStatusListenerAdapter implements OnAppStatusListener {

        @Override
        public void onFront() {
        }

        @Override
        public void onBack() {
        }
    }
}
