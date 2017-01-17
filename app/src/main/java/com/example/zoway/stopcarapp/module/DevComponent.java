package com.example.zoway.stopcarapp.module;

import android.app.Activity;

import com.example.zoway.stopcarapp.dev.IDevService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/12/28.
 */

@Singleton
@Component(modules = {DevServiceModule.class})
public interface DevComponent {
    void inject(Activity needPrint);
    IDevService getIDevService();
}
