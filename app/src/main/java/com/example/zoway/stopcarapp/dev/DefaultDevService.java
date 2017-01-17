package com.example.zoway.stopcarapp.dev;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/7.
 */
public class DefaultDevService extends AbstractDevService {

    public DefaultDevService(Context context){
        super(context);
    }

    @Override
    public void print(String msg){
        Toast.makeText(context, "不支付打印机功能", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openDev() {
    }

    @Override
    public void closeDev() {
    }

    @Override
    public boolean devReady(){
        return iPrinter != null;
    }
}
