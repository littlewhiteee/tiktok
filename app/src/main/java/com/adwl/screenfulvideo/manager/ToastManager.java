package com.adwl.screenfulvideo.manager;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adwl.screenfulvideo.R;


/**
 * ToastManager
 */
public class ToastManager {

    private static Toast toast;

    /**
     * 显示 toast
     */
    public static void showToast(Context context, String text, boolean forLong) {
        if (context == null) {
            return;
        }
        View view;
        if (toast == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.toast_layout, null);
            TextView textView = view.findViewById(R.id.title_text);
            textView.setText(text);
        } else {

            view = toast.getView();
            if (view.getParent() != null && view.getParent() instanceof ViewGroup) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            TextView textView = view.findViewById(R.id.title_text);
            textView.setText(text);
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if (forLong) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.setView(view);
        toast.show();
    }

    /**
     * 显示 toast
     */
    public static void showToastNoOverlap(Context context, String text, boolean forLong) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.toast_layout, null);
            TextView textView = view.findViewById(R.id.title_text);
            textView.setText(text);
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(view);
        } else {
            TextView textView = toast.getView().findViewById(R.id.title_text);
            textView.setText(text);
        }
        if (forLong) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showToastForLong(Context context, String text) {
        showToast(context, text, true);
    }

    public static void showToastForShort(Context context, String text) {
        showToast(context, text, false);
    }

}
