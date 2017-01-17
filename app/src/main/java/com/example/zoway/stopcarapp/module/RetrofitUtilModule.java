package com.example.zoway.stopcarapp.module;

import android.content.Context;

import com.example.zoway.stopcarapp.api.LoginApi;
import com.example.zoway.stopcarapp.api.PartApi;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractor;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractorImpl;
import com.example.zoway.stopcarapp.api.lmpl.PartInteractor;
import com.example.zoway.stopcarapp.api.lmpl.PartInteractorImpl;
import com.example.zoway.stopcarapp.http.RetrofitHttp;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/1/16.
 */


@Module
public class RetrofitUtilModule {

    private final Context context;

    public RetrofitUtilModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    public Retrofit provideRetrofit(){
        return RetrofitHttp.getnstance(context);
    }

    @Provides
    public LoginApi provideLoginApi(Retrofit retrofit){
        return retrofit.create(LoginApi.class);
    }

    @Provides
    public LoginNfcInteractor provideLoginNfcInteractor(LoginApi loginApi){
        return new LoginNfcInteractorImpl(loginApi);
    }

    @Provides
    public PartApi providePartApi(Retrofit retrofit){
        return retrofit.create(PartApi.class);
    }

    @Provides
    public PartInteractor providePartInteractor(PartApi partApi){
        return new PartInteractorImpl(partApi);
    }

}
