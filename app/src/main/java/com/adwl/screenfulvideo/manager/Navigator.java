package com.adwl.screenfulvideo.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.adwl.screenfulvideo.browser.BaseBrowserActivity;
import com.adwl.screenfulvideo.browser.BrowserActivity;

/**
 * created by wmm on 2020/11/11
 */
public class Navigator {
    // 跳转到WebViewActivity
    public static void toBrowserActivity(Activity activity, String url, String title) {
        toBrowserActivity(activity, url, title, false);
    }

    public static void toBrowserActivity(Activity activity, String url, String title, boolean enableOffline) {
        Bundle bundle = new Bundle();
        bundle.putString(BaseBrowserActivity.K_BROWSER_URL, url);
        bundle.putString(BaseBrowserActivity.K_BROWSER_TITLE, title);
        bundle.putBoolean(BaseBrowserActivity.K_ENABLE_OFFLINE, enableOffline);
        toActivityWithBundle(activity, BrowserActivity.class, bundle);
    }


    public static void toActivity(Activity activity, Class<? extends Activity> toActivityClazz) {
        toActivityWithBundle(activity, toActivityClazz, null);
    }

    public static void toActivityWithBundle(Activity activity, Class<? extends Activity> toActivityClazz, Bundle bundle) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, toActivityClazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    public static void toActivityForResult(Activity activity, Class<? extends Activity> toActivityClazz, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, toActivityClazz);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void toActivityForResult(Activity activity, Class<? extends Activity> toActivityClazz, int requestCode, Bundle bundle) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, toActivityClazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void toActivityForResult(Fragment fragment, Class<? extends Activity> toActivityClazz, int requestCode) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }
        Intent intent = new Intent(fragment.getActivity(), toActivityClazz);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void toActivityForResult(Fragment fragment, Class<? extends Activity> toActivityClazz, int requestCode, Bundle bundle) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }
        Intent intent = new Intent(fragment.getActivity(), toActivityClazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }
}
