package com.adwl.screenfulvideo.manager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.adwl.screenfulvideo.utils.CommonUtils;


/**
 * created by wmm on 2020/9/8
 */
public class BaseNavigator {

    //系统设置页面
    public static void toSystemSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_SETTINGS);
        toImplicitActivity(activity, intent);
    }

    // 打开浏览器
    public static boolean toBrowser(Activity activity, String url) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return false;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        if (!CommonUtils.resolveActivity(activity, intent)) {
            return false;
        }
        toImplicitActivity(activity, intent);
        return true;
    }

    // 打开邮件
    public static boolean toMail(Activity activity, String url) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return false;
        }
        if (!url.startsWith("mailto:")) {
            return false;
        }

        url = url.replace("mailto:", "");
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("application/octet-stream");
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{url.split("[\\u003F]")[0]}); // ? ascii 3F
        if (!CommonUtils.resolveActivity(activity, mailIntent)) {
            return false;
        }
        toImplicitActivity(activity, mailIntent);
        return true;
    }

    // 打开拨号盘
    public static boolean toCallDial(Activity activity, String url) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return false;
        }

        if (!url.startsWith("tel:")) {
            return false;
        }

        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        if (!CommonUtils.resolveActivity(activity, dialIntent)) {
            return false;
        }
        toImplicitActivity(activity, dialIntent);
        return true;
    }

    // 打开拨号盘
    public static boolean toCallDialWithPhoneNum(Activity activity, String phoneNum) {
        if (activity == null || TextUtils.isEmpty(phoneNum)) {
            return false;
        }
        String url = "tel:" + phoneNum;

        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
        if (!CommonUtils.resolveActivity(activity, dialIntent)) {
            return false;
        }
        toImplicitActivity(activity, dialIntent);
        return true;
    }

    // 去应用市场
    public static boolean toMarket(Activity activity, String url) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        if (!uri.getScheme().equals("market")) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!CommonUtils.resolveActivity(activity, intent)) {
            return false;
        }
        toImplicitActivity(activity, intent);
        return true;
    }

    // 应用宝
    public static boolean toAppQQ(Activity activity, String url) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return false;
        }

        Uri uri = Uri.parse(url);
        String host = uri.getHost();
        return ("a.app.qq.com".equals(host) || "android.myapp.com".equals(host)) && toBrowser(activity, url);
    }


    public static void toImplicitActivity(Activity activity, Intent intent) {
        if (activity == null) {
            return;
        }
        activity.startActivity(intent);
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
