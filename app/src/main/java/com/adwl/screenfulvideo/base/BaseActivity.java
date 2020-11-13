package com.adwl.screenfulvideo.base;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.adwl.screenfulvideo.R;
import com.adwl.screenfulvideo.utils.StatusBarUtils;


/**
 * created by wmm on 2020/10/9
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private boolean isDestroyed;
    private boolean isResume;

    public boolean isDestroyed2() {
        return isDestroyed;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupStatusBar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setupStatusBar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setupStatusBar();
    }

    private void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setupStatusBar(this, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }

    public boolean isResume() {
        return isResume;
    }


    /**
     * activity theme primary color
     */
    public int getPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * dialog theme primary color
     */
    public int getDialogPrimaryColor() {
        return getPrimaryColor();
    }
}
