package com.example.zoway.stopcarapp.api;



import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/13.
 * 定义接口
 */
public interface LoginApi {

    @POST("Worker/{stringPath}")
    Observable<String> login(@Path("stringPath")String path, @Body String body);

    @POST("Worker/{stringPath}")
    @Multipart
    Observable<String> uploadRes(@Path("stringPath")String path,@PartMap Map<String, RequestBody> params);

}
