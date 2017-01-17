package com.example.zoway.stopcarapp.util;

/**
 * Created by Administrator on 2016/11/29.
 */
public class TimeTextUtil {
    //时间 年月日
    public static String TimeYMDString(String time){
        return time.substring(0,10);
    }
    //时间 时分秒
    public static String TimeHMSString(String time){
        return time.substring(11,19);
    }
}
