package com.example.zoway.stopcarapp.api;

import retrofit2.http.Body;

import retrofit2.http.POST;

import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017/1/16.
 * 车位信息
 */
public interface PartApi {

    @POST("Park/{stringPath}")
    Observable<String> PartInfo(@Path("stringPath")String path, @Body String body);


}
