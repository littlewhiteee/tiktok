package com.adwl.screenfulvideo.api.base;


import com.adwl.screenfulvideo.utils.EmptyUtils;

/**
 * created by wmm on 2020/9/8
 */
public class ApiException extends RuntimeException {
    private int code;
    private String message;

    public ApiException(int code) {
        this.code = code;
    }

    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    private static final int UNKNOWN = 0; // ("未知错误"),

    public static final int CONTACT_NO_REGISTER = 100;
    public static final int USER_NO_LOGIN = 101;
    public static final int TOKEN_INVALID = 102;


    public String friendlyMessage() {
        switch (code()) {
            case CONTACT_NO_REGISTER:
                return "好友未注册";
            case USER_NO_LOGIN:
                return "用户未登录";
            case TOKEN_INVALID:
                return "token已失效，请重新登录";
            case UNKNOWN:
            default:
                if (EmptyUtils.isEmpty(message)) {
                    return "请求失败";
                }
                return message;
        }

    }

    public boolean isInvalidToken() {
        return code() == TOKEN_INVALID;
    }
}