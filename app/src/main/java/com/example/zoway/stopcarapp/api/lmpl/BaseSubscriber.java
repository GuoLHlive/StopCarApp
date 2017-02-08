package com.example.zoway.stopcarapp.api.lmpl;

import android.util.Log;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/29.
 */

/*
*   订阅处理
* */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private static final String TAG = "AppError";

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.i(TAG, "onError:" + e.getMessage());









    }

    //处理
    protected abstract void onSuccess(T result);



}
