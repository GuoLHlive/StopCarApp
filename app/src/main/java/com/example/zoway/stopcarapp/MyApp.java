package com.example.zoway.stopcarapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.example.zoway.stopcarapp.api.config.Config;

import com.example.zoway.stopcarapp.module.AppComponent;

import com.example.zoway.stopcarapp.module.AppModule;

import com.example.zoway.stopcarapp.module.BeanModule;
import com.example.zoway.stopcarapp.module.DaggerAppComponent;

import com.example.zoway.stopcarapp.module.RetrofitUtilModule;
import com.example.zoway.stopcarapp.util.SharedPreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/12/14.
 */
public class MyApp extends Application {
    private AppComponent appComponent;
    private static MyApp myApp;


    public static Map<Integer,String> PayState;

    //SharePreferences存储地址
    private String absolutePath = "";

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        PayState = new HashMap<>();
        setDraggerConfig();
        //设置初始化
        setSharePreferences();


    }

    public AppComponent component(){
        return appComponent;
    }

    public static MyApp getApp(Context context){
        return ((MyApp) context.getApplicationContext());
    }
    public static MyApp getApp(){
        return myApp;
    }

    public static Map<Integer,String> getPayState(){
        return PayState;
    }

    private void setDraggerConfig() {
        appComponent =  DaggerAppComponent.builder().
                retrofitUtilModule(new RetrofitUtilModule(this))
                .appModule(new AppModule()).beanModule(new BeanModule()).build();
        appComponent.inject(this);

    }
    private void setSharePreferences() {
        absolutePath = Environment.getDataDirectory()
                .getAbsolutePath()+"/data/"+myApp.getPackageName()+"/shared_prefs";
        File file = new File(absolutePath);
        if (!file.exists()){
            //数据初始化

            Log.i("MainActivityA","数据初始化");
            Resources resources = getResources();
            SharedPreferencesUtils.setParam(this, Config.TypeName,resources.getString(R.string.buildTypeName));
            SharedPreferencesUtils.setParam(this,Config.FlavorName,resources.getString(R.string.productFlavorName));
            SharedPreferencesUtils.setParam(this,Config.FlavorCode,resources.getString(R.string.productFlavorCode));
            SharedPreferencesUtils.setParam(this, Config.AppHost,resources.getString(R.string.serverAppHost));
            SharedPreferencesUtils.setParam(this, Config.AppPath,resources.getString(R.string.serverAppPath));
            SharedPreferencesUtils.setParam(this,Config.IP,resources.getString(R.string.serverIP));
            SharedPreferencesUtils.setParam(this,Config.PORT,resources.getInteger(R.integer.serverPort));
            SharedPreferencesUtils.setParam(this,Config.VersionHost,resources.getString(R.string.serverVersionHost));
            SharedPreferencesUtils.setParam(this,Config.VersionPath,resources.getString(R.string.serverVersionPath));
            SharedPreferencesUtils.setParam(this,Config.EditSetting,resources.getBoolean(R.bool.enableEditSetting));
            SharedPreferencesUtils.setParam(this,Config.DebugToolbar,resources.getBoolean(R.bool.showDebugToolbar));
        }
    }
}
