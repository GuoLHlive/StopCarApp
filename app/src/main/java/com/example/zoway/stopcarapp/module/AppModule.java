package com.example.zoway.stopcarapp.module;


import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.example.zoway.stopcarapp.activity.BaseActivity;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/12/15.
 */

@Module
public class AppModule {

    @Singleton
    @Provides
    public ArrayList<BaseActivity> provideBackActivity() {
        return new ArrayList<>();
    }

}
