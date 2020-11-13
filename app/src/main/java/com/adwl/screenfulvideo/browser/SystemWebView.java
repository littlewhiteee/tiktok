package com.adwl.screenfulvideo.browser;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebView;

/**
 * SystemWebView
 */

public class SystemWebView extends WebView implements IWebView {
    public SystemWebView(Context context) {
        super(context);
    }

    public SystemWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SystemWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SystemWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SystemWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    @Override
    public void _loadUrl(String url) {
        this.loadUrl(url);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void _evaluateJavascript(String script, final ValueCallback<String> resultCallback) {
        this.evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (resultCallback != null) {
                    resultCallback.onReceiveValue(value);
                }
            }
        });
    }

    @Override
    public void _reload() {
        this.reload();
    }

    @Override
    public String _getUrl() {
        return this.getUrl();
    }

    @Override
    public String _getTitle() {
        return getTitle();
    }

    @Override
    public boolean _canGoBack() {
        return this.canGoBack();
    }

    @Override
    public void _goBack() {
        this.goBack();
    }

    @Override
    public void _onResume() {
        this.onResume();
    }

    @Override
    public void _onPause() {
        this.onPause();
    }

    @Override
    public void _destroy() {
        this.destroy();
    }

    @Override
    public void _setVisibility(int visibility) {
        this.setVisibility(visibility);
    }


    @SuppressLint("JavascriptInterface")
    @Override
    public void _addJavascriptInterface(Object object, String name) {
        this.addJavascriptInterface(object, name);
    }


    @Override
    public void _setDownloadListener(DownloadListener listener) {
        this.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            if (listener != null) {
                listener.onDownloadStart(url, userAgent, contentDisposition, mimetype, contentLength);
            }
        });
    }

    @Override
    public String _getUserAgentString() {
        if (this.getSettings() != null) {
            return this.getSettings().getUserAgentString();
        }
        return "";
    }

    @Override
    public void _setUserAgentString(String userAgent) {
        if (this.getSettings() != null) {
            this.getSettings().setUserAgentString(userAgent);
        }
    }
}
