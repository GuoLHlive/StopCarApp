package com.example.zoway.stopcarapp.util;

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
}
