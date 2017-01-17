package com.example.zoway.stopcarapp.util;

/**
 * Created by Administrator on 2016/11/29.
 */
public class CarTextUtil {
    //地方 粤X
    public static String CarFromString(String carNumber){
        return carNumber.substring(0,2);
    }
    //车牌
    public static String CarNumberString(String carNumber){
        return carNumber.substring(2,7);
    }




}
