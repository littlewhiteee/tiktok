package com.adwl.screenfulvideo.browser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.adwl.screenfulvideo.utils.CommonUtils;

/**
 * created by wmm on 2020/9/9
 */
public abstract class SystemBrowserActivity extends BaseBrowserActivity{
    private SystemWebView webView;

    @Override
    public SystemWebView getWebView() {
        if (webView == null) {
            webView = new SystemWebView(this);
        }
        return webView;
    }

    // 打开浏览器
    public static boolean toBrowser(Activity activity, String url) {
        if (activity == null || TextUtils.isEmpty(url)) {
            return false;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        if (!CommonUtils.resolveActivity(activity, intent)) {
            return false;
        }
        activity.startActivity(intent);
        return true;
    }

    @Override
    protected void setupWebView() {
        super.setupWebView();
        webView.setBackgroundColor(Color.parseColor("#e0e0e0"));
        webView.getSettings().setJavaScriptEnabled(true); //设置使用够执行JS脚本
        webView.getSettings().setDomStorageEnabled(true); //设置使用localStorage if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // WebView 从Lollipop(5.0)开始, WebView 默认不允许混合模式，https当中不能加载http资源，如果要加载，需单独设置开启
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (TextUtils.isEmpty(url)) {
                    return;
                }

                toBrowser(SystemBrowserActivity.this, url);
            }
        });


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return webViewShouldOverrideUrlLoading((IWebView) view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                onWebViewPageStarted((IWebView) view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onWebViewPageFinished((IWebView) view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                onWebViewReceivedError();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                receivedTitle((IWebView) view, title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress((IWebView) view, newProgress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                geolocationPermissionsShowPrompt(origin, callback);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
    }

    /* ======================== WebChromeClient Start ========================*/
    // 网页定位
    protected void geolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {

    }
    /* ======================== WebChromeClient End ========================*/
}
