package com.example.zoway.stopcarapp.util;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/18.
 */
public class LongTimeOrString {
    /**
     * 把毫秒转化成日期
     * @param dateFormat(日期格式,例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec(毫秒数)
     * @return
     */
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public static String longTimeOrString(Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }


    @BindingAdapter("LongTimeForStringTime")
    public static void LongTimeForText(TextView textView,Long millSec){
        if (millSec == 0L){
            textView.setText("");
        }else {
            textView.setText(longTimeOrString(millSec));
        }

    }

    public static String stringStopTime(Long startT,Long endT){
//        Log.i("ParkingWebSocket","startT:"+startT+"/n"+"endT:"+endT);
        Long time = endT - startT;
//        Log.i("ParkingWebSocket","time:"+time);
        if (time<=0){
            time = 0L;
        }
        int ss = (int) (time / 1000);//总共的秒数
        int s = ss % 60; //秒数
        //分钟
        int mm = (ss - s) / 60; //一共多少分钟
        int hh = 0;
        while (true){
            if (mm>=60){  //分钟大于60
                hh++;  //时间加一
                mm = mm - 60; //总分钟-60
            }
            if (mm<60){
                break;
            }
        }
        String stime =" "+String.format("%s时:%s分:%s秒",hh,mm,s);
        return stime;
    }
    public static String stringStopTime_MainActivity(Long startT,Long endT){
//        Log.i("ParkingWebSocket","startT:"+startT+"/n"+"endT:"+endT);
        Long time = endT - startT;
//        Log.i("ParkingWebSocket","time:"+time);
        if (time<=0){
            time = 0L;
        }
        int ss = (int) (time / 1000);//总共的秒数
        int s = ss % 60; //秒数
        //分钟
        int mm = (ss - s) / 60; //一共多少分钟
        int hh = 0;
        while (true){
            if (mm>=60){  //分钟大于60
                hh++;  //时间加一
                mm = mm - 60; //总分钟-60
            }
            if (mm<60){
                break;
            }
        }
        String stime =" "+String.format("%s时:%s分",hh,mm);
        return stime;
    }

}
