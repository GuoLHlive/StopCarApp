package com.example.zoway.stopcarapp.api.lmpl;

import com.example.zoway.stopcarapp.api.PartApi;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/16.
 */
public class PartInteractorImpl implements PartInteractor {

    private PartApi partApi;


    @Inject
    public PartInteractorImpl(PartApi partApi) {
        this.partApi = partApi;
    }



    @Override
    public Subscription partInfo(String url, String body, BaseSubscriber<String> subscriber) {

        Observable<String> partInfo = partApi.PartInfo(url, body);
        partInfo.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscriber;
    }
}
