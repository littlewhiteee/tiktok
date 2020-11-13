package com.adwl.screenfulvideo.browser;

/**
 * created by wmm on 2020/9/9
 */
public interface DownloadListener {
    void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength);
}
