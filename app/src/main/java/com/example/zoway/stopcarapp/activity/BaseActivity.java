package com.example.zoway.stopcarapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zoway.stopcarapp.MainActivity;
import com.example.zoway.stopcarapp.MyApp;
import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.file.SystemService;
import com.example.zoway.stopcarapp.module.AppComponent;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/13.
 */
public abstract class BaseActivity extends AppCompatActivity{
    protected ViewDataBinding view;
    protected ArrayList<BaseActivity> activitys;
    protected AppComponent appComponent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(MainActivity.FLAG_HOMEKEY_DISPATCHED, MainActivity.FLAG_HOMEKEY_DISPATCHED);
//        iniWindow();
        view = DataBindingUtil.setContentView(this, getLayoutId());
        appComponent = MyApp.getApp(getApplicationContext()).component();
        activitys = appComponent.getAppActivitys();

        initData(getIntent());
        initView();
    }

    //设置属性
//    protected abstract void iniWindow();
    protected abstract int getLayoutId();
    protected abstract void initData(Intent intent);
    protected abstract void initView();





    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_3||keyCode==KeyEvent.KEYCODE_BACK){
            Log.i("RetrofitLog","activitys:"+activitys.size());
            try {
                if (activitys.size()==1){
                    activitys.get(0).finish();
                    activitys.clear();
                }else {
                    activitys.get(activitys.size()-1).finish();
                    activitys.remove(activitys.size()-1);
                }
            } catch (Exception e) {
                finish();
            }
        }

        return true;
    }

}
