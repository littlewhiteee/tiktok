package com.adwl.screenfulvideo.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.adwl.screenfulvideo.R;

public class VideoMenuView extends RelativeLayout {

    public VideoMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VideoMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.video_menu, this, true);
    }
}
