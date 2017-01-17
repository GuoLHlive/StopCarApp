package com.example.zoway.stopcarapp.dev.P900;

import android.content.Context;
import android.widget.Toast;

import com.example.zoway.stopcarapp.dev.AbstractDevService;


/**
 * Created by Administrator on 2016/9/7.
 */
public class P990DevService extends AbstractDevService {

    public P990DevService(Context context){
        super(context);
        P990Printer p990Printer = new P990Printer(context);
        if(p990Printer.tryActivatePrinter()) {
            iPrinter = p990Printer;
        }
    }

    public void print(String msg){
        if(!supportPrint()){
            Toast.makeText(context, "不支付打印机功能", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            iPrinter.print(msg);
        }catch(Exception ex){
            Toast.makeText(context, "打印出错:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public IPrinter getPrinter() {
//        return iPrinter;
//    }

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
