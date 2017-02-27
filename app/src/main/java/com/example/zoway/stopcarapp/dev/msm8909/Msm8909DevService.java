package com.example.zoway.stopcarapp.dev.msm8909;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.zoway.stopcarapp.dev.AbstractDevService;
import com.smartdevice.aidl.IZKCService;
import com.smartdevicesdk.printer.PrintService;

/**
 * Created by Administrator on 2017/2/23.
 */
public class Msm8909DevService extends AbstractDevService {

    public IZKCService mIzkcService;
    private boolean bindSuccessFlag = false;
    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("client","onServiceConnected");
            mIzkcService = IZKCService.Stub.asInterface(iBinder);
            if (mIzkcService!=null){
                Log.i("client",mIzkcService.toString());
                try {
                    mIzkcService.setModuleFlag(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                bindSuccessFlag = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("client","onServiceDisconnected");
            mIzkcService = null;
            bindSuccessFlag = false;
        }
    };

    public Msm8909DevService(Context context) {
        super(context);
        iPrinter = new Msm8909Printer(context,this);
    }

    @Override
    public void openDev() {
        bindService();
    }

    @Override
    public void closeDev() {
        unbindService();
    }

    @Override
    public void print(String msg) {
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

    @Override
    public boolean devReady() {
        return iPrinter != null;
    }

    public void bindService() {
        if (context!=null){
            Intent intent = new Intent("com.zkc.aidl.all");
            intent.setPackage("com.smartdevice.aidl");
            context.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
        }
    }

    public void unbindService() {
        if (context!=null){
            context.unbindService(mServiceConn);
        }
    }

}
