package com.adwl.screenfulvideo.browser;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.adwl.screenfulvideo.R;
import com.adwl.screenfulvideo.base.BaseActivity;
import com.adwl.screenfulvideo.manager.BaseNavigator;
import com.adwl.screenfulvideo.utils.EmptyUtils;
import com.adwl.screenfulvideo.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by wmm on 2020/9/9
 */
public abstract class BaseBrowserActivity extends BaseActivity {
    public static final String K_BROWSER_URL = "browser_url";
    public static final String K_BROWSER_TITLE = "browser_title";
    public static final String K_ENABLE_OFFLINE = "enable_offline"; // 允许离线

    private static final String DEFAULT_TITLE = "详情";

    protected LinearLayout rootLayout;

    protected RelativeLayout headerLayout;

    protected TextView headerTitle;

    protected ImageView backBtn;



    protected ProgressBar progressBar;

    View refreshBtn;

    IWebView baseWebView;

    protected String urlStr;
    protected String titleStr;
    protected boolean enableOffline;

    protected List<String> titleList;
    protected Map<String, String> shareStrMap;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_base_browser);
        ButterKnife.bind(this);
        titleList = new ArrayList<>();
        shareStrMap = new HashMap<>();
        findViews();
        addHeaderExpandView();
        retrieveParams();
        setupHeader();
        addViewOnTopOfWebView();
        addWebView();
        setupWebView();
        setUa();
        loadUrl(urlStr);
        checkInternet();
    }

    /**
     * 扩展header的函数
     */
    protected void addHeaderExpandView() {
    }

    private void findViews() {
        rootLayout = findViewById(R.id.root_layout);
        headerLayout = findViewById(R.id.topbar_header_layout);
        headerTitle = findViewById(R.id.title_text);
        backBtn = findViewById(R.id.back_btn);
        progressBar = findViewById(R.id.progress_bar);
        refreshBtn = findViewById(R.id.refresh_btn);
    }


    @OnClick(R.id.refresh_btn)
    void toRefresh() {
        baseWebView._reload();
    }

    // 返回
    @OnClick(R.id.back_btn)
    void toBack() {
        if (baseWebView._canGoBack()) {
            baseWebView._goBack();
        } else {
            finish();
        }
    }



    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (baseWebView != null) {
            baseWebView._loadUrl(url);
        }
    }

    private void setUa() {
        String originUa = baseWebView._getUserAgentString();
        String appUa = customUserAgent();
        if (EmptyUtils.isEmpty(appUa)) {
            return;
        }

        if (EmptyUtils.isEmpty(originUa)) {
            baseWebView._setUserAgentString(appUa);
        } else if (!originUa.contains(appUa)) {
            baseWebView._setUserAgentString(originUa + " " + appUa);
        }
    }

    // 自定义UserAgent
    protected String customUserAgent() {
        return "";
    }

    // 得到传递的参数
    protected void retrieveParams() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            urlStr = getIntent().getExtras().getString(K_BROWSER_URL);
            titleStr = getIntent().getExtras().getString(K_BROWSER_TITLE);
            enableOffline = getIntent().getExtras().getBoolean(K_ENABLE_OFFLINE);
        }
    }

    @CallSuper
    protected void setupHeader() {
        titleList.clear();
        if (EmptyUtils.isEmpty(titleStr)) {
            setTopTitle(DEFAULT_TITLE);
            titleList.add(DEFAULT_TITLE);
        } else {
            setTopTitle(titleStr);
            titleList.add(titleStr);
        }
    }

    protected void addViewOnTopOfWebView() {

    }

    private void addWebView() {
        baseWebView = getWebView();
        if (!(baseWebView instanceof View)) {
            throw new RuntimeException("getWebView() must return a view");
        }
        View view = (View) baseWebView;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootLayout.addView(view, params);
    }


    /**
     * SystemWebView, TBSWebView
     *
     * @return IWebView
     */
    protected abstract IWebView getWebView();

    /**
     * 是否显示share按钮
     *
     * @return true: 显示 false: 不显示
     */
    protected boolean showShare() {
        return true;
    }

    protected void setupWebView() {
        checkInternet();

        baseWebView._addJavascriptInterface(new ShareStrInterface() {
            @JavascriptInterface
            @Override
            public void shareStrCallback(final String shareStr) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(shareStr)) {
                            shareStrMap.put(baseWebView._getUrl(), shareStr);
                        }
                    }
                });
            }
        }, "ShareInterface");

        baseWebView._setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (TextUtils.isEmpty(url)) {
                    return;
                }

                BaseNavigator.toBrowser(BaseBrowserActivity.this, url);
            }
        });
    }

    // 检查网络
    private void checkInternet() {
        if (!enableOffline && !NetworkUtils.isNetworkConnected(this)) {
            baseWebView._setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            refreshBtn.setVisibility(View.VISIBLE);
        } else {
            baseWebView._setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            refreshBtn.setVisibility(View.GONE);
            baseWebView._loadUrl(urlStr);
        }
    }

    /* ======================== WebViewClient Start  ========================*/
    protected boolean webViewShouldOverrideUrlLoading(IWebView view, String url) {
        if (BaseNavigator.toMail(this, url) ||
                BaseNavigator.toCallDial(this, url) ||
                BaseNavigator.toMarket(this, url) ||
                BaseNavigator.toAppQQ(this, url)
        ) {
            return true;
        }
        return false;
    }

    // onPageStarted
    protected void onWebViewPageStarted(IWebView view, String url, Bitmap favicon) {

    }

    protected void onWebViewPageFinished(IWebView view, String url) {

    }

    protected void onWebViewReceivedError() {
//        ToastManager.showToastForShort(this, "网页加载失败");
    }
    /* ======================== WebViewClient End ========================*/


    /* ======================== WebChromeClient Start ========================*/
    protected void receivedTitle(IWebView webView, String title) {
        if (webView._canGoBack()) {
            //添加标题
            if (!TextUtils.isEmpty(title)) {
                setTopTitle(title);
                titleList.add(title);
            } else {
                if (EmptyUtils.isEmpty(titleStr)) {
                    title = titleStr;
                } else {
                    title = DEFAULT_TITLE;
                }
                setTopTitle(title);
                titleList.add(title);
            }
        } else {
            String realTitle;
            if (EmptyUtils.isEmpty(titleStr)) {
                if (EmptyUtils.isEmpty(title)) {
                    realTitle = DEFAULT_TITLE;
                } else {
                    realTitle = title;
                }
            } else {
                realTitle = titleStr;
            }

            if (titleList == null) {
                titleList = new ArrayList<>();
            }

            setTopTitle(realTitle);
            if (EmptyUtils.isEmpty(titleList)) {
                titleList.add(realTitle);
            } else {
                titleList.set(0, realTitle);
            }
        }
    }

    protected void progress(IWebView view, int newProgress) {
        if (newProgress == 0 || newProgress == 100) {
            progressBar.setVisibility(View.GONE);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(newProgress);
    }
    /* ======================== WebChromeClient End ========================*/


    @Override   //默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && baseWebView._canGoBack()) {
            baseWebView._goBack();
            if (titleList.size() >= 1) {
                titleList.remove(titleList.size() - 1);
            }
            if (titleList.size() >= 1) {
                setTopTitle(titleList.get(titleList.size() - 1));
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void setTopTitle(String title) {
        headerTitle.setText(title);
    }

    // 关闭
    private void toClose() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (baseWebView != null) {
            baseWebView._destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseWebView._onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baseWebView._onPause();
    }

    // 获取网页中分享数据接口
    private interface ShareStrInterface {
        void shareStrCallback(String shareStr);

    }


    protected void setTopBackgroundColor(@ColorRes int colorId) {
        if (colorId == 0) {
            return;
        }

        headerLayout.setBackgroundColor(ContextCompat.getColor(this, colorId));
    }

    protected void setTopTitleColor(@ColorRes int colorId) {
        if (colorId == 0) {
            return;
        }

        headerTitle.setTextColor(ContextCompat.getColor(this, colorId));
    }

    protected void setBackDrawable(@DrawableRes int drawableId) {
        if (drawableId == 0) {
            return;
        }
        backBtn.setImageDrawable(ContextCompat.getDrawable(this, drawableId));
    }


    protected void setProgressDrawable(@DrawableRes int drawableId) {
        if (drawableId == 0) {
            return;
        }
        progressBar.setProgressDrawable(ContextCompat.getDrawable(this, drawableId));
    }
}
