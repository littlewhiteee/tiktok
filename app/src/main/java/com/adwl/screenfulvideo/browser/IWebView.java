package com.adwl.screenfulvideo.browser;

import android.webkit.DownloadListener;
import android.webkit.ValueCallback;

/**
 * created by wmm on 2020/9/9
 */
public interface IWebView {

    void _loadUrl(String url);

    void _evaluateJavascript(String script, ValueCallback<String> resultCallback);

    void _reload();

    String _getUrl();

    String _getTitle();

    boolean _canGoBack();

    void _goBack();

    void _onResume();

    void _onPause();

    void _destroy();

    void _setVisibility(int visibility);

    void _addJavascriptInterface(Object object, String name);

    void _setDownloadListener(DownloadListener listener);

    String _getUserAgentString();

    void _setUserAgentString(String userAgent);
}
