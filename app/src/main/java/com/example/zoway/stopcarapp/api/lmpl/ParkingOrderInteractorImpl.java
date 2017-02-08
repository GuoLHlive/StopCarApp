package com.example.zoway.stopcarapp.api.lmpl;

import com.example.zoway.stopcarapp.api.ParkingOrderApi;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/18.
 */
public class ParkingOrderInteractorImpl implements ParkingOrderInteractor {

    private ParkingOrderApi parkingOrderApi;


    @Inject
    public ParkingOrderInteractorImpl(ParkingOrderApi parkingOrderApi) {
        this.parkingOrderApi = parkingOrderApi;
    }

    @Override
    public Subscription parkingOrderInfo(String url, String body, BaseSubscriber<String> subscriber) {
        Observable<String> parkingOrderInfo = parkingOrderApi.ParkingOrderInfo(url, body);
        parkingOrderInfo.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscriber;
    }


}
