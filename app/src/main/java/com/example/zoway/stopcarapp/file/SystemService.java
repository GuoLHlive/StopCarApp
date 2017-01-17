package com.example.zoway.stopcarapp.file;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.zoway.stopcarapp.MyApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016/12/21.
 * 管理文件，读取文件
 *
 */
public class SystemService {
    public static TelephonyManager telephonyManager = null;
    public static WifiManager wifiManager = null;

    public static String RID = null;
    private static String appFileName = "Zparking";

    //目录
    //系统SD卡根目录
    public static File rootFile = null;
    static {
        //查找外置SD卡
        for(int i = 2; i >= 0; i--){
            String pathStr = "/storage/sdcard" + i;
            File path = new File(pathStr);
            if(path.exists()){
                rootFile = path;

                break;
            }
        }
        //rootFile = Environment.getExternalStorageDirectory();

        //读取SD卡中的 RID文件
        File idFile = new File(rootFile.getPath() + "/" + appFileName + "/RID");
        if(idFile.exists()){
            BufferedReader reader = null;
            try {
                System.out.println("以行为单位读取文件内容，一次读一行");
                reader = new BufferedReader(new FileReader(idFile));
                RID = reader.readLine();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
    }

    //程序目录
    public static final File appFile = new File(rootFile.getAbsolutePath() + "/" + appFileName);
    //数据文件目录
    public static final File dbFile = new File(appFile.getAbsolutePath() + "/db");
    //日志目录
    public static final File logFile = new File(appFile.getAbsolutePath() + "/log");
    //资源目录
    public static final File resFile = new File(appFile.getAbsolutePath() + "/res");
    //运行版本目录
    public static final File proFile = new File(appFile.getAbsolutePath() + "/pro");
    //临时文件目录
    public static final File tempFile = new File(appFile.getAbsolutePath() + "/temp");
    //图片文件目录
    public static final File photoFile = new File(appFile.getAbsolutePath() + "/photo");

    static {
        Context context = MyApp.getApp();
        telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE );
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE );

        //建立文件目录
        if(!SystemService.appFile.exists()) SystemService.appFile.mkdirs();
        if(!SystemService.logFile.exists()) SystemService.logFile.mkdirs();
        if(!SystemService.resFile.exists()) SystemService.resFile.mkdirs();
        if(!SystemService.proFile.exists()) SystemService.proFile.mkdirs();
        if(!SystemService.tempFile.exists()) SystemService.tempFile.mkdirs();
        if(!SystemService.photoFile.exists()) SystemService.photoFile.mkdirs();
    }


    public static void init(){
        //设置好文件目录
    }

    public static String getMEID(){
        String v = null;
        try{
            v = telephonyManager.getDeviceId();
        }catch(Exception ex){
            Log.e(SystemService.class.getName(), ex.getMessage(), ex);
        }
        return v;
    }

    public static String getPhoneNumber(){
        String v = null;
        try{
            v = telephonyManager.getLine1Number();
        }catch(Exception ex){
            Log.e(SystemService.class.getName(), ex.getMessage(), ex);
        }
        return v;
    }

    public static String getMAC(){
        String v = null;
        try{
            v = wifiManager.getConnectionInfo().getMacAddress();
        }catch(Exception ex){
            Log.e(SystemService.class.getName(), ex.getMessage(), ex);
        }
        return v;
    }

    public String getAndroidID(){
//        String v = Settings.Secure.ANDROID_ID;
//        if(v.equals("android_id"))
//            v = "864536020096986";
//        return v;
        if(!"".equals(getRegId())){
            return "RID." + getRegId();
        }
        else if(!"".equals(getPhoneNumber())){
            return "PHN." + getPhoneNumber();//采用电话卡号码来作为机器的ID
        }else if(!telephonyManager.getDeviceId().equals("")){
            return "DID." + telephonyManager.getDeviceId();
        }else{
            return "AID." + Settings.Secure.ANDROID_ID;
        }
    }

    //得到
    public String getRegId(){
        return RID;
    }

    public String getIP(){
        String v = null;
        try{
            //先尝试获取GPRS的IP，没有后，再获取 WIFI
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()){
                        v = inetAddress.getHostAddress().toString();
                    }
                }
            }
            if(v == null){
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                v = (ipAddress & 0xFF ) + "." +
                        ((ipAddress >> 8 ) & 0xFF) + "." +
                        ((ipAddress >> 16 ) & 0xFF) + "." +
                        ( ipAddress >> 24 & 0xFF) ;
            }
        } catch (SocketException ex) {
            Log.e(SystemService.class.getName(), ex.getMessage(), ex);
        }
        return v;
    }


    /*
        读取NFC ID 卡号读取
     */
    public String receiveNFC(Intent intent){
        byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        String uid = bytesToHexString(myNFCID);
        return uid;
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }



}
