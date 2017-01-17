package com.example.zoway.stopcarapp.dev.ww808_emmc;

import android.content.Context;
import android.widget.Toast;

import com.example.zoway.stopcarapp.dev.AbstractDevService;
import com.smartdevicesdk.device.DeviceInfo;
import com.smartdevicesdk.device.PDA3502;
import com.smartdevicesdk.printer.PrintService;



/**
 * Created by Administrator on 2016/9/7.
 */
public class WW808EmmcDevService extends AbstractDevService {
    public DeviceInfo devInfo =new PDA3502();

    public WW808EmmcDevService(Context context){
        super(context);
        iPrinter = new WW808EmmcPrinter(context, this);
    }

    @Override
    public void print(String msg){
        if(!supportPrint()){
            Toast.makeText(context, "不支付打印机功能", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            iPrinter.print(msg);
            PrintService.imageWidth = 48;
        }catch(Exception ex){
            Toast.makeText(context, "打印出错:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openDev() {
        devInfo.openModel();
    }

    @Override
    public void closeDev() {
        devInfo.closeModel();
    }

    @Override
    public boolean devReady(){
        return iPrinter != null;
    }
}
