package com.adwl.screenfulvideo.api.base;


import com.adwl.screenfulvideo.api.entity.base.Result;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * created by wmm on 2020/9/8
 */
public class ResultHandler {
    /**
     * 统一处理服务器返回
     */
    public static <T> ObservableTransformer<Result<T>, T> handleResult() {
        return upstream -> upstream.flatMap((Function<Result<T>, ObservableSource<T>>) result -> {
            if (result.isSuccessful()) {
                return createData(result.data);
            } else {
                return Observable.error(new ApiException(result.code, result.message));
            }
        });
    }

    private static <T> Observable<T> createData(final T t) {
        return Observable.create(emitter -> {
            try {
                emitter.onNext(t);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
