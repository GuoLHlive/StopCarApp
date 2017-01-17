package com.example.zoway.stopcarapp.dev.P900;

import android.content.Context;

import com.example.zoway.stopcarapp.util.printer.IPrinter;
import com.example.zoway.stopcarapp.util.printer.content.ContentType;
import com.example.zoway.stopcarapp.util.printer.content.SaxHandler;
import com.example.zoway.stopcarapp.util.printer.content.XmlContent;
import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.device.Printer.Format;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.QrCode;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.util.List;


/**
 * Created by Tonny on 2016/6/3.
 */
public class P990Printer implements IPrinter {
    private Context context;
    public P990Printer(Context context){
        super();
        this.context = context;
    }

    public boolean tryActivatePrinter() {
        for (int i = 0; i < 3; i++) {
            try {
                DeviceService.login(context);
                return true;
            } catch (RequestException e) {
                e.printStackTrace();
                try{
                    Thread.sleep(300);}catch(Exception ex){}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void print(final String content) throws Exception {
        SaxHandler handler = new SaxHandler();
        XMLReader xr = SaxHandler.getXMLReader();
        xr.setContentHandler(handler);
        xr.parse(new InputSource(new ByteArrayInputStream(content.getBytes("UTF-8"))));
        List<XmlContent> contents = handler.getContents();
        new Print(contents).start();
    }

    /**
     * To gain control of the device service, you need invoke this method before
     * any device operation.
     */
//    public void bindDeviceService() {
//        Observable.create(new Observable.OnSubscribe<String>() {
//                    @Override
//                    public void call(Subscriber<? super String> subscriber) {
//                        while (true) {
//                            try {
//                                DeviceService.login(activity);
//                                break;
//                            } catch (RequestException e) {
//                                e.printStackTrace();
//                                try{Thread.sleep(300);}catch(Exception ex){}
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .doOnSubscribe(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//
//                    }
//                });
//
//    }

    /**
     * Release the right of using the device.
     */
    public void unbindDeviceService() {
        DeviceService.logout();
    }

    private class Print extends Printer.Progress {
        private List<XmlContent> contents;
        public Print(List<XmlContent> contents){
            this.contents = contents;
        }

        @Override
        public void doPrint(Printer printer) throws Exception {
            Format format = new Format();
            format.setAscScale(Format.ASC_SC1x1);
            printer.setFormat(format);
            for(XmlContent item : contents){
                if(item.getContentType() == ContentType.QRCODE){
                    //打印二维码
                    printer.printQrCode(35, new QrCode(item.getValue(), QrCode.ECLEVEL_M), 312);
                }else if(item.getContentType() == ContentType.H1){
                    //大字  不能换行,否则只能显示最后一行
//                    printer.printMixText(Format.hz(Format.HZ_DOT16x16, Format.HZ_SC2x2), "  ");
                    String[] strs = item.getValue().split("\n");
                    for(int i = 0; i < strs.length; i++){
                        printer.printMixText(Format.hz(Format.HZ_DOT16x16, Format.HZ_SC2x2),
                                strs[i] + (i == strs.length - 1 && !item.getValue().endsWith("\n") ? "" : "\n"));
                    }
//                    printer.printMixText(Format.hz(Format.HZ_DOT16x16, Format.HZ_SC2x2), "  ");
                }else{
                    //普通字
//                    printer.printMixText(Printer.Format.hz(Printer.Format.HZ_DOT24x24, Printer.Format.HZ_SC1x1), item.getValue());
                    String[] strs = item.getValue().split("\n");
                    for(int i = 0; i < strs.length; i++){
                        printer.printMixText(Format.hz(Format.HZ_DOT24x24, Format.HZ_SC1x1),
                                strs[i] + (i == strs.length - 1 && !item.getValue().endsWith("\n") ? "" : "\n"));
                    }
//                    printer.printText(item.getValue());
                }
            }
//            printer.printMixText(
//                    Format.hz(Format.HZ_DOT24x24, Format.HZ_SC1x1), "本系统数据已与广东");
//            printer.printMixText(
//                    Format.hz(Format.HZ_DOT16x16, Format.HZ_SC2x2), "  德信行  \n");
//            printer.printMixText(
//                    Format.hz(Format.HZ_DOT24x24, Format.HZ_SC1x1),
//                    "信用系统对接，请注重诚信，自觉缴费。\n\n");
            printer.feedLine(4);
        }

        @Override
        public void onFinish(int i) { }

        @Override
        public void onCrash() { }
    }
}
