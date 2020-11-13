package com.adwl.screenfulvideo.utils;

import android.text.TextUtils;

import java.util.Locale;

/**
 * created by wmm on 2020/10/26
 */
public class NumUtils {
    public static String formatNum(double num) {
        return removeEndZero(String.format(Locale.getDefault(), "%.1f", num));
    }

    public static String removeEndZero(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        if (str.contains(".")) {
            while (str.endsWith("0")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.endsWith(".")) {
                str = str.substring(0, str.length() - 1);
            }
        }

        return str;
    }

    public static String formatIccid(String iccid) {
        if (TextUtils.isEmpty(iccid)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < iccid.length(); i++) {
            builder.append(iccid.charAt(i));
            if (i % 5 == 4 && i != iccid.length() - 1) {
                builder.append(" ");
            }
        }
        return builder.toString().trim();
    }
}
