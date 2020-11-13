package com.adwl.screenfulvideo.home.listener;

import android.widget.TextView;

public interface VideoMenuListener {
    void like(int position, TextView view);

    void comment(int position);

    void forward(int position);
}
