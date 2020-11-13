package com.adwl.screenfulvideo.api.entity.config;

import android.content.Context;
import android.text.TextUtils;

import com.adwl.screenfulvideo.R;
import com.adwl.screenfulvideo.app.ScreenVideoApp;
import com.adwl.screenfulvideo.utils.DeviceUtils;

import java.util.Objects;

/**
 * created by wmm on 2020/10/15
 */
public class Config {

    public String shgy; //社区公约

    public VersionInfo versionInfo;
    public String yhxy; //用户协议
    public String ysxy; //隐私协议
    public String channel;

    public class VersionInfo {
        public String apk;
        public boolean updateForce;
        public String updateInfo;
        public String version;
    }


    public String getApkUrl() {
        if (versionInfo == null) {
            return "";
        }
        if (versionInfo.apk.contains("[app_name]")) {
            return versionInfo.apk.replaceAll("\\[app_name]", ScreenVideoApp.getInstance().getString(R.string.app_name));
        }
        return versionInfo.apk;
    }

    public boolean isUpdate(Context context) {
        return versionInfo != null &&
                versionInfo.version != null &&
                versionInfo.version.compareTo(Objects.requireNonNull(DeviceUtils.getVersionName(context))) > 0;
    }

    public boolean isForceUpdate(Context context) {
        return isUpdate(context) && versionInfo.updateForce;
    }

    public String getUpdateInfo() {
        if (versionInfo == null) {
            return "";
        }
        return versionInfo.updateInfo;
    }

    public boolean isContainChannelName(String channelName) {
        return !TextUtils.isEmpty(channel) && channel.contains(channelName);
    }

}
