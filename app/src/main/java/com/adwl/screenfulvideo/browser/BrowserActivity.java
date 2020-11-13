package com.adwl.screenfulvideo.browser;

import android.graphics.Color;
import android.os.Bundle;

/**
 * created by wmm on 2020/9/21
 */
public class BrowserActivity extends SystemBrowserActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWebView().setBackgroundColor(Color.WHITE);
    }
}
