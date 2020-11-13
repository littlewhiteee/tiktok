package com.adwl.screenfulvideo.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.adwl.screenfulvideo.BuildConfig;
import com.adwl.screenfulvideo.R;
import com.adwl.screenfulvideo.api.UserAgentManager;
import com.adwl.screenfulvideo.helper.AppActivityHelper;
import com.adwl.screenfulvideo.utils.CommonUtils;
import com.adwl.screenfulvideo.utils.Remember;
import com.dueeeke.videoplayer.exo.ExoMediaPlayerFactory;
import com.dueeeke.videoplayer.player.AndroidMediaPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

/**
 * created by wmm on 2020/10/9
 */
public class ScreenVideoApp extends Application {
    private static ScreenVideoApp mInstance;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new BallPulseFooter(context);
            }
        });
    }

    // 支持MultiDex
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        if (CommonUtils.isAppProcess(this)) {
            mInstance = this;
            initTimber();
            Remember.init(this, "joke_helper_pref");
            UserAgentManager.init(this);
            registerCallBacks();
//            //bugLy 初始化
            CrashReport.initCrashReport(getApplicationContext(), "ca9e283fd8", BuildConfig.DEBUG);
            initUMeng();
            initVideoConfig();
        }
    }

    private void initVideoConfig() {
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                //使用ExoPlayer解码
                .setPlayerFactory(ExoMediaPlayerFactory.create())
                .build());
    }

    //友盟初试化
    private void initUMeng() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
    }

    private void registerCallBacks() {
        AppActivityHelper.getInstance().register(this);
    }


    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }


    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NotNull String message, Throwable t) {

        }
    }


    public static ScreenVideoApp getInstance() {
        return mInstance;
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(android.R.color.transparent, android.R.color.black);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }
}
