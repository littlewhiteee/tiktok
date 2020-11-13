package com.adwl.screenfulvideo.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by wmm on 2020/9/7
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;

    protected View rootView;

    private boolean isDestroyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentLayout() == 0) {
            throw new RuntimeException("getContentLayout() can not return 0");
        }
        isDestroyView = false;
        if (rootView == null || !isUseViewCache()) {
            rootView = inflater.inflate(getContentLayout(), container, false);
        }
        if (container != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @LayoutRes
    protected abstract int getContentLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyView = true;
//        unbinder.unbind();
    }

    public boolean isActivityDestroy() {
        return getActivity() == null || getActivity().isDestroyed();
    }

    public boolean isFragmentDestroy() {
        return isActivityDestroy() || isDestroyView;
    }

    /**
     * 如需改变请覆盖此方法
     * 是否使用RootView缓存 true
     */
    protected boolean isUseViewCache() {
        return true;
    }
}
