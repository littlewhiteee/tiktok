package com.adwl.screenfulvideo.api;

import android.content.Context;
import android.os.Build;

import com.adwl.screenfulvideo.utils.DeviceUtils;
import com.adwl.screenfulvideo.utils.Remember;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * UserAgentManager
 */
public class UserAgentManager {
    private static final String K_UA = "key_ua";

    public static String getUserAgent() {
        return Remember.getString(K_UA, "");
    }

    public static void init(Context context) {
        String ua = getUserAgent(context);
        Remember.putString(K_UA, ua);
    }

    /**
     *
     * 获得User-Agent
     *
     */
    private static String getUserAgent(Context context) {

        JsonObject jsonContainer =new JsonObject();
        //为当前的json对象添加键值对
        jsonContainer.addProperty("app_name", "screen_video");
        jsonContainer.addProperty("platform", "android");
        jsonContainer.addProperty("version_code", DeviceUtils.getVersionName(context));
        return jsonContainer.toString();

    }






    private static String getPhoneInfoStr() {
        StringBuilder phoneInfoStrBuilder = new StringBuilder();

        // 手机品牌, 手机型号, sdk, release
        String brand;
        String model;
        try {
            brand = URLEncoder.encode(Build.BRAND, "UTF-8");
            model = URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            brand = "";
            model = "";
        }
        phoneInfoStrBuilder.append(String.format(Locale.getDefault(), "brand: %s, model: %s, sdk: %d, release: %s",
                brand, model, Build.VERSION.SDK_INT, Build.VERSION.RELEASE));
        return phoneInfoStrBuilder.toString();

    }

}
