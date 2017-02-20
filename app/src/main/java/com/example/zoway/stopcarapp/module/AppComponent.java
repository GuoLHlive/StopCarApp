package com.example.zoway.stopcarapp.module;

import com.example.zoway.stopcarapp.MyApp;
import com.example.zoway.stopcarapp.activity.BaseActivity;
import com.example.zoway.stopcarapp.api.LoginApi;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractor;
import com.example.zoway.stopcarapp.api.lmpl.ParkingOrderInteractor;
import com.example.zoway.stopcarapp.api.lmpl.PartInteractor;
import com.example.zoway.stopcarapp.bean.PartSeatBean;
import com.example.zoway.stopcarapp.bean.UIsBean;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/12/14.
 * dagger2
 */


@Singleton
@Component( modules = {AppModule.class,RetrofitUtilModule.class,BeanModule.class})
public interface AppComponent {
    void inject(MyApp myApp);
    LoginNfcInteractor getLoginNfcInteractor();
    ArrayList<BaseActivity> getAppActivitys();
    PartInteractor getPartInteractor();
    ParkingOrderInteractor getParkingOrderInteractor();
    UIsBean getUIsBean();

}
