package com.adwl.screenfulvideo.api.entity.base;

/**
 * created by wmm on 2020/9/8
 */
public class Result<T> {

    /*
    {
        "code": "SUCCESS",
        "message": "成功",
        "data": {

        }
    }*/
    public String message;
    public int code;
    public T data;


    public boolean isSuccessful() {
        return code == 200;
    }
}
