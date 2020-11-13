package com.adwl.screenfulvideo.base.contract.presenter;


import com.adwl.screenfulvideo.utils.EmptyUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * created by wmm on 2020/10/9
 */
public class BasePresenterImpl<V> implements BasePresenter {

    protected CompositeDisposable mCompositeDisposable;

    protected V mView;

    public BasePresenterImpl(V view) {
        mView = view;
    }


    @Override
    public void start() {

    }

    /**
     * Disposable 放入容器集中处理
     *
     * @param disposable disposable
     */
    public void addDispose(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有 Disposable 放入容器集中处理
    }

    /**
     * 取消正在执行的订阅
     */
    public void removeDispose(Disposable disposable) {
        if (mCompositeDisposable != null && disposable != null) {
            mCompositeDisposable.remove(disposable);
        }
    }

    /**
     * 取消正在执行的订阅集合
     */
    public void removeDispose(Disposable... disposables) {
        if (mCompositeDisposable != null && !EmptyUtils.isEmpty(disposables)) {
            for (Disposable disposable : disposables) {
                mCompositeDisposable.remove(disposable);
            }

        }
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void removeDisposes() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//取消所有正在执行的订阅
        }
    }

    @Override
    public void detach() {
        mView = null;
        removeDisposes();
    }
}