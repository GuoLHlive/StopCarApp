package com.example.zoway.stopcarapp.dev.ww808_emmc;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.zoway.stopcarapp.dev.IDevService;
import com.example.zoway.stopcarapp.util.printer.IPrinter;
import com.example.zoway.stopcarapp.util.printer.content.ContentType;
import com.example.zoway.stopcarapp.util.printer.content.JsonContent;
import com.example.zoway.stopcarapp.util.printer.content.SaxHandler;
import com.example.zoway.stopcarapp.util.printer.content.XmlContent;
import com.google.gson.Gson;
import com.smartdevicesdk.printer.BarcodeCreater;
import com.smartdevicesdk.printer.PrintService;
import com.smartdevicesdk.printer.PrinterClassSerialPort3502;
import com.smartdevicesdk.printer.PrinterCommand;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;


import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Tonny on 2016/6/3.
 */
public class WW808EmmcPrinter implements IPrinter {
    private Context context;
    private IDevService iDevService;

    protected static final String TAG = "PrintDemo";

    private String device = "/dev/ttyMT0";
    private int baudrate = 115200;// 38400
    PrinterClassSerialPort3502 printerClass = null;

    public WW808EmmcPrinter(final Context context, IDevService iDevService){
        super();
        this.context = context;
        this.iDevService = iDevService;

        Handler mhandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PrinterCommand.MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        Log.i(TAG, "readBuf:" + readBuf[0]);
                        if (readBuf[0] == 0x13) {
                            PrintService.isFUll = true;
                            //printToast("缓存已满");
                        } else if (readBuf[0] == 0x11) {
                            PrintService.isFUll = false;
                            //printToast("缓存清空");
                        } else if (readBuf[0] == 0x08) {
                            printToast("没有打印纸");
                        } else if (readBuf[0] == 0x01) {
                            //printToast("打印中");
                        } else if (readBuf[0] == 0x04) {
                            printToast("温度过高");
                        } else if (readBuf[0] == 0x02) {
                            printToast("低电量");
                        } else if (readBuf[0] == 0x00) {

                        } else {
                            /*String readMessage = new String(readBuf, 0, msg.arg1);
                            if (readMessage.contains("800")){
                                // 80mm paper
                                PrintService.imageWidth = 72;
                                Toast.makeText(context, "80mm",
                                        Toast.LENGTH_SHORT).show();
                            } else if (readMessage.contains("580")){
                                // 58mm paper
                                PrintService.imageWidth = 48;
                                Toast.makeText(context, "58mm",
                                        Toast.LENGTH_SHORT).show();
                            }*/
                        }
                        break;
                    case PrinterCommand.MESSAGE_STATE_CHANGE:// 6��l��״
                        switch (msg.arg1) {
                            case PrinterCommand.STATE_CONNECTED:// �Ѿ�l��
                                break;
                            case PrinterCommand.STATE_CONNECTING:// ����l��
                                //printToast("连接中");
                                break;
                            case PrinterCommand.STATE_LISTEN:
                            case PrinterCommand.STATE_NONE:
                                break;
                            case PrinterCommand.SUCCESS_CONNECT:
                                printerClass.write(new byte[] { 0x1b, 0x2b });// ����ӡ���ͺ�
                                //printToast("连接成功");
                                break;
                            case PrinterCommand.FAILED_CONNECT:
                                //printToast("连接失败");
                                break;
                            case PrinterCommand.LOSE_CONNECT:
                                //printToast("失去连接");
                        }
                        break;
                    case PrinterCommand.MESSAGE_WRITE:

                        break;
                }
                super.handleMessage(msg);
            }
        };

        printerClass = new PrinterClassSerialPort3502(context, device, baudrate, mhandler);
    }

    @Override
    public void print(final String content) throws Exception {
//        SaxHandler handler = new SaxHandler();
//        XMLReader xr = SaxHandler.getXMLReader();
//        xr.setContentHandler(handler);
//        xr.parse(new InputSource(new ByteArrayInputStream(content.getBytes("UTF-8"))));
//        List<XmlContent> contents = handler.getContents();
//        iDevService.openDev();
//        printerClass.open();
//
//        Observable.from(contents)
//                .delay(3, TimeUnit.SECONDS)
//                .onBackpressureBuffer()
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<XmlContent>(){
//
//                    @Override
//                    public void onCompleted() {
//                        new Thread(){
//                            @Override
//                            public void run() {
//                                super.run();
//                                try{
//                                    Thread.sleep(20000);}catch(Exception ex){}
//                                printerClass.close();
//                                iDevService.closeDev();
//                            }
//                        }.start();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        printerClass.close();
//                        iDevService.closeDev();
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(XmlContent item) {
//                        if(item.getContentType() == ContentType.QRCODE){
//                            //打印二维码
//                            printerClass.printImage(BarcodeCreater.encode2dAsBitmap(item.getValue(), 300, 300));
////                            printerClass.printImage(BarcodeCreater.creatBarcode(
////                                    context, item.getValue(), 384, 50, false, BarcodeFormat.CODE_128));
//                        }else if(item.getContentType() == ContentType.H1){
////                            printerClass.write(PrinterCommand.CMD_FONTSIZE_NORMAL);
//                            printerClass.printText(item.getValue());
//                        }else{
////                            printerClass.write(PrinterCommand.CMD_FONTSIZE_DOUBLE);
//                            printerClass.printText(item.getValue());
//                        }
//                    }
//                });
        Gson gson = new Gson();
        JsonContent jsonContent = gson.fromJson(content, JsonContent.class);
        iDevService.openDev();
        printerClass.open();

        Observable.just(jsonContent)
                .delay(3, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonContent>() {
                    @Override
                    public void onCompleted() {
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try{
                                    Thread.sleep(20000);}catch(Exception ex){}
                                printerClass.close();
                                iDevService.closeDev();
                            }
                        }.start();
                    }

                    @Override
                    public void onError(Throwable e) {
                        printToast(e.toString());
                        printerClass.close();
                        iDevService.closeDev();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(JsonContent jsonContent) {

                        try {
                            byte[] json = jsonContent.toString().getBytes("GBK");
                            printerClass.write(json);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        printerClass.printImage(BarcodeCreater.encode2dAsBitmap(jsonContent.getQrcode(), 300, 300));
                        printerClass.printText(""+"\n");
                    }
                });
    }

    public void printToast(String msg){
        Toast.makeText(context, "[打印机]"+ msg, Toast.LENGTH_SHORT).show();
    }
}
