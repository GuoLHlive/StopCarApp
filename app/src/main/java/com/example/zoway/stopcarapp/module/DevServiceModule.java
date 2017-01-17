package com.example.zoway.stopcarapp.module;

import android.util.Log;

import com.example.zoway.stopcarapp.MyApp;
import com.example.zoway.stopcarapp.dev.DefaultDevService;
import com.example.zoway.stopcarapp.dev.IDevService;
import com.example.zoway.stopcarapp.dev.P900.P990DevService;
import com.example.zoway.stopcarapp.dev.ww808_emmc.WW808EmmcDevService;

import javax.inject.Singleton;


import dagger.Module;
import dagger.Provides;

/**
 * Created by Tonny on 2016/5/19.
 */
@Module()
public class DevServiceModule {
    @Singleton
    @Provides
    public IDevService providesCheckVersionService(){
        String model = android.os.Build.MODEL;
        IDevService iDevService = null;
        if(model.equals("P990")){
            iDevService = new P990DevService(MyApp.getApp());
        }else if(model.equals("ww808_emmc")){
            iDevService = new WW808EmmcDevService(MyApp.getApp());
        }else{
            iDevService = new DefaultDevService(MyApp.getApp());
        }
        return iDevService;
    }
}
