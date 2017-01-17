package com.example.zoway.stopcarapp.http;


import android.content.Context;

import android.util.Log;

import com.example.zoway.stopcarapp.MyApp;
import com.example.zoway.stopcarapp.api.config.Config;
import com.example.zoway.stopcarapp.util.SharedPreferencesUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/29.
 * 网络工具
 *
 *
 */
public class RetrofitHttp {
    private static Retrofit retrofit = null;
    public static Retrofit getnstance(Context context){
        if (retrofit == null){

            //设置数据
            String host = (String) SharedPreferencesUtils.getParam(context, Config.AppHost, Config.serverAppHost);
            String path = (String) SharedPreferencesUtils.getParam(context, Config.AppPath, Config.serverAppPath);
            String ip = (String) SharedPreferencesUtils.getParam(context,Config.IP,Config.server_ip);
            Integer port = (Integer) SharedPreferencesUtils.getParam(context,Config.PORT,Config.serverPort);

            //打印
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    Log.i("RetrofitLog","retrofitBack = "+message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            CookieJar cookieJar = new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            };
            //代理
            Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ip,port));


            OkHttpClient build = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .addInterceptor(loggingInterceptor)
                    .proxy(proxy)
                    .connectTimeout(8 * 3600, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(8 * 3600, TimeUnit.SECONDS)
                    .writeTimeout(8 * 3600, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();


            retrofit = new Retrofit.Builder()
                    .client(build)
                    .baseUrl("http://"+host+"/"+path+"/")
                    .addConverterFactory(new ToStringConverterFactory())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static class ToStringConverterFactory extends Converter.Factory {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/encrypt-json");


        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (String.class.equals(type)) {
                return new Converter<ResponseBody, String>() {
                    @Override
                    public String convert(ResponseBody value) throws IOException {
                        return value.string();
                    }
                };
            }
            return null;
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

            if (String.class.equals(type)) {
                return new Converter<String, RequestBody>() {
                    @Override
                    public RequestBody convert(String value) throws IOException {
                        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, value);
                        return requestBody;
                    }
                };
            }
            return null;
        }
    }

}
