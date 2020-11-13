package com.adwl.screenfulvideo.api.constants;

/**
 * created by wmm on 2020/9/8
 */
public class ApiHost {
    // api
    private static final String API_HOST_DEV = "http://192.168.1.41:8084/";
    private static final String API_HOST_PROD = "http://app.pipidexia.com/";

    public static final String API_HOST;

    static {
        switch (Env.ENV) {
            case Env.ENV_DEV:
                API_HOST = API_HOST_DEV;
                break;
            case Env.ENV_PROD:
                API_HOST = API_HOST_PROD;
                break;
            default:
                API_HOST = API_HOST_PROD;
                break;
        }
    }
}
