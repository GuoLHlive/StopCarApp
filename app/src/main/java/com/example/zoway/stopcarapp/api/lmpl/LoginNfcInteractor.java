package com.example.zoway.stopcarapp.api.lmpl;



import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscription;

/**
 * Created by Administrator on 2016/12/14.
 */
public interface LoginNfcInteractor {

    //订阅动作
    Subscription loginFnc(String url, String body, BaseSubscriber<String> subscriber);
    //订阅动作
    Subscription loginFnc(String url, Map<String, RequestBody> params,BaseSubscriber<String> subscriber);

}
