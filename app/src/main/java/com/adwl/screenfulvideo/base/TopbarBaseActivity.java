package com.adwl.screenfulvideo.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.adwl.screenfulvideo.R;
import com.adwl.screenfulvideo.widget.GlobalStatusView;

import butterknife.ButterKnife;

/**
 * created by wmm on 2020/9/7
 */
public abstract class TopbarBaseActivity extends BaseActivity {
    protected FrameLayout rootLayout;
    protected RelativeLayout topbarLayout;
    protected TextView topbarTitleView;
    private TextView backView;
    protected LayoutInflater layoutInflater;
    protected View contentLayout;
    private View topbarLine;

    private GlobalStatusView statusView;
    protected int headerLayoutHeight;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutInflater = LayoutInflater.from(this);
        rootLayout = (FrameLayout) layoutInflater.inflate(R.layout.activity_topbar_base, null);
        topbarLayout = rootLayout.findViewById(R.id.topbar_header_layout);
        topbarLine = rootLayout.findViewById(R.id.header_bottom_line);
        topbarTitleView = topbarLayout.findViewById(R.id.topbar_header_title);
        addBackView();
        showTopbarLine(false);
        headerLayoutHeight = getResources().getDimensionPixelOffset(R.dimen.topbar_height);

        if (getContentLayout() != 0) {
            contentLayout = layoutInflater.inflate(getContentLayout(), rootLayout, false);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentLayout.getLayoutParams();
            params.topMargin = getResources().getDimensionPixelOffset(R.dimen.topbar_height);
            rootLayout.addView(contentLayout, params);
        }

        if (enableStatusView()) {
            addStatusView();
            statusView.setBuild(createStatusViewBuild());
        }
        setContentView(rootLayout);
        ButterKnife.bind(this);

        if (isClearWindowBg()) {
            clearWindowBg();
        }
    }

    @LayoutRes
    protected abstract int getContentLayout();

    private void addStatusView() {
        statusView = new GlobalStatusView(this);
        rootLayout.addView(statusView);
        statusView.setStatusViewListener(this::onRetry);
    }

    /**
     * 显示Loading
     */
    public void displayLoading() {
        if (statusView != null) {
            statusView.setStatus(GlobalStatusView.STATUS_LOADING);
        }
    }

    public void displayEmpty() {
        if (statusView != null) {
            statusView.setStatus(GlobalStatusView.STATUS_EMPTY_DATA);
        }
    }

    public void displayError() {
        if (statusView != null) {
            statusView.setStatus(GlobalStatusView.STATUS_LOAD_FAILED);
        }
    }

    public void displaySuccess() {
        if (statusView != null) {
            statusView.setStatus(GlobalStatusView.STATUS_LOAD_SUCCESS);
        }
    }

    public boolean enableStatusView() {
        return false;
    }

    //重试
    public void onRetry() {
    }

    //空页面，loading页面，错误页面配置
    public GlobalStatusView.Build createStatusViewBuild() {
        return null;
    }

    public void showTopbarLine(boolean show) {
        if (show) {
            if (topbarLine.getVisibility() != View.VISIBLE) {
                topbarLine.setVisibility(View.VISIBLE);
            }
        } else {
            if (topbarLine.getVisibility() == View.VISIBLE) {
                topbarLine.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 隐藏头部
     */
    public void hideTopbar() {
        if (topbarLayout != null) {
            topbarLayout.setVisibility(View.GONE);
        }

        if (contentLayout != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentLayout.getLayoutParams();
            params.topMargin = 0;
            contentLayout.requestLayout();
        }
    }

    public boolean isTopbarShow() {
        return topbarLayout != null && topbarLayout.getVisibility() == View.VISIBLE;
    }

    private void addBackView() {
        backView = new TextView(this);
        backView.setTextColor(Color.WHITE);
        backView.setGravity(Gravity.CENTER_VERTICAL);
        backView.setPadding(getResources().getDimensionPixelOffset(R.dimen.topbar_item_margin), 0, getResources().getDimensionPixelOffset(R.dimen.topbar_item_margin_half), 0);
        setBackIcon(R.drawable.selector_topbar_back);
        setBackText("");


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        topbarLayout.addView(backView, params);

        backView.setOnClickListener(v -> onBackClick());
        showTopbarBack(false);
    }


    public void addRightView(View rightView) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        topbarLayout.addView(rightView, params);
    }

    /**
     * 显示后退按钮
     */
    public void showTopbarBack(boolean show) {
        backView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 定义点击后退按钮后的行为(默认关闭activity)
     */
    protected void onBackClick() {
        finish();
    }

    /**
     * 设置后退按钮右边的文字
     */
    public void setBackText(@StringRes int titleRes) {
        backView.setText(titleRes);
    }

    public void setBackText(CharSequence backTitle) {
        backTitle = backTitle == null ? "" : backTitle;
        backView.setText(backTitle);
    }

    public void setBackIcon(@DrawableRes int backIconRes) {
        Drawable backDrawable = ContextCompat.getDrawable(this, backIconRes);
        setBackIcon(backDrawable);
    }

    public void setBackIcon(Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            backView.setCompoundDrawables(drawable, null, null, null);//画在左边
        }
    }

    /**
     * 为标题设置字体大小
     *
     * @param titleSize 字体大小(单位px)
     */
    public void setTopbarTitleSize(float titleSize) {
        topbarTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
    }


    public void setTopbarTitle(@StringRes int titleRes) {
        setTopbarTitle(getString(titleRes));
    }

    public void setTopbarTitle(CharSequence titleText) {
        if (titleText != null) {
            topbarTitleView.setText(titleText);
        }
    }

    public void setTopbarTitleColor(@ColorRes int colorRes) {
        topbarTitleView.setTextColor(ContextCompat.getColor(this, colorRes));
    }

    /**
     * @param colorRes 颜色资源
     */
    public void setTopbarBackground(@ColorRes int colorRes) {
        int color = ContextCompat.getColor(this, colorRes);
        setTopbarBackgroundColor(color);
    }

    /**
     * @param color 颜色
     */
    public void setTopbarBackgroundColor(@ColorInt int color) {
        topbarLayout.setBackgroundColor(color);
    }


    /**
     * @param drawable drawable
     */
    public void setTopbarBackground(Drawable drawable) {
        if (drawable != null && topbarLayout != null) {
            topbarLayout.setBackground(drawable);

        }
    }

    //是否去掉系统自带的背景色
    public boolean isClearWindowBg() {
        return false;
    }

    public void clearWindowBg() {
        getWindow().setBackgroundDrawable(null);
    }
}
