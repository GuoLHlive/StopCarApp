package com.example.zoway.stopcarapp.dev.msm8909;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.zoway.stopcarapp.bean.Config;
import com.example.zoway.stopcarapp.dev.IDevService;
import com.example.zoway.stopcarapp.util.printer.IPrinter;
import com.example.zoway.stopcarapp.util.printer.content.JsonContent;
import com.google.gson.Gson;
import com.smartdevice.aidl.IZKCService;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/23.
 */
public class Msm8909Printer implements IPrinter {

    private IDevService iDevService;
    private Context context;

    public Msm8909Printer(Context context,IDevService iDevService) {
        this.iDevService = iDevService;
        this.context = context;
    }

    @Override
    public void print(String content) throws Exception {
        Gson gson = new Gson();
        JsonContent jsonContent = gson.fromJson(content, JsonContent.class);
        Log.i("Print",jsonContent.toString());

        Observable.just(jsonContent)
                .delay(3, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonContent>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Print",e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(JsonContent jsonContent) {
//                            byte[] json = jsonContent.toString().getBytes("GBK");


                        if (iDevService instanceof  Msm8909DevService){
                            IZKCService mIzkcService = ((Msm8909DevService) iDevService).mIzkcService;

                            int state = jsonContent.getState();
                            String json = jsonContent.toString();
                            String userName = jsonContent.getUserName();
                            String license_plate = jsonContent.getLicense_plate();
                            String stop_number = jsonContent.getStop_number();
                            String qrcode = jsonContent.getQrcode();

                            if (state == 1){
                                String txt = license_plate+"\n"+stop_number+"\n"+
                                        "操作员  ："+userName+"\n"+Config.PRINTTEXT1;
                                try {
                                    mIzkcService.printGBKText("\n");
                                    mIzkcService.printGBKText(txt);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (state == 2){
                                String txt = json+  "操作员  ："+userName+"\n"
                                        +"\n"+ Config.PRINTTEXT2+"\n";
                                try {
                                    mIzkcService.printGBKText("\n");
                                    mIzkcService.printGBKText(txt);
                                    Bitmap qrCode = mIzkcService.createQRCode(qrcode, 300, 300);
                                    mIzkcService.printBitmap(qrCode);
                                    mIzkcService.printGBKText(""+"\n");
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }

                            }


                        }




                    }
                });



    }



}
