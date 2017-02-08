package com.example.zoway.stopcarapp.api.lmpl;

import rx.Subscription;

/**
 * Created by Administrator on 2017/1/18.
 */
public interface ParkingOrderInteractor {

    //订阅动作
    Subscription parkingOrderInfo(String url, String body, BaseSubscriber<String> subscriber);


}
