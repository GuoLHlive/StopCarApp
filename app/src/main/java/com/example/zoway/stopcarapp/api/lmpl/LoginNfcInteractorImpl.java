package com.example.zoway.stopcarapp.api.lmpl;

import android.util.Log;

import com.example.zoway.stopcarapp.api.LoginApi;


import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/14.
 */
public class LoginNfcInteractorImpl implements LoginNfcInteractor {

    private LoginApi loginApi;

    @Inject
    public LoginNfcInteractorImpl(LoginApi loginApi) {
        this.loginApi = loginApi;
    }

    @Override
    public Subscription loginFnc(String url, String body, BaseSubscriber<String> subscriber) {
//        try {
//            Log.i("RetrofitLog","loginFnc:"+body.string().toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Observable<String> login = loginApi.login(url, body);
        login.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscriber;
    }


    //上传
    @Override
    public Subscription loginFnc(String url, Map<String, RequestBody> params, BaseSubscriber<String> subscriber) {
        return null;
    }


}
