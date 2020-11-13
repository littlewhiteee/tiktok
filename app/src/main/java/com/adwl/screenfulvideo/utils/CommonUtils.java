package com.adwl.screenfulvideo.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Process;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * created by wmm on 2020/10/9
 */
public class CommonUtils {

    /**
     * 查看有没有能相应此Intent的组件(Component)
     *
     * @param intent intent
     * @return true:有 false:没有
     */
    public static boolean resolveActivity(Context context, Intent intent) {
        if (context == null || intent == null) {
            return false;
        }

        PackageManager pm = context.getPackageManager();
        return intent.resolveActivity(pm) != null;
    }

    public static boolean checkPackage(Context context, String packageName) {
        if (EmptyUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    public static void copyText(Context context, String copyText) {
        if (context == null) {
            return;
        }
        ClipboardManager c = (ClipboardManager) (context.getSystemService(Context.CLIPBOARD_SERVICE));
        //设置Clipboard 的内容
        c.setPrimaryClip(ClipData.newPlainText(null, copyText));
    }

    /**
     * 是否是 app 主进程
     */
    public static boolean isAppProcess(Context context) {
        return context.getApplicationInfo().packageName.equals(CommonUtils.getCurrentProcessName(context));

    }

    /**
     * 获得当前进程的名字
     *
     * @return 进程名
     */
    public static String getCurrentProcessName(Context context) {

        int pid = Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    /**
     * 获得当前进程的名字(Bugly推荐用这种方式)
     *
     * @return 进程名
     */
    public static String getCurrentProcessName() {
        return getProcessName(Process.myPid());
    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static String randomMacAddress() {
        Random rand = new Random();
        byte[] macAddr = new byte[6];
        rand.nextBytes(macAddr);

        macAddr[0] = (byte) (macAddr[0] & (byte) 254);  //zeroing last 2 bytes to make it unicast and locally adminstrated

        StringBuilder sb = new StringBuilder(18);
        for (byte b : macAddr) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
