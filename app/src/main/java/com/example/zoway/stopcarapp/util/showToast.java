package com.example.zoway.stopcarapp.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/16.
 */
public class showToast {

    public static void showToastTxt(Context context,String txt){
        Toast.makeText(context,txt,Toast.LENGTH_SHORT).show();
    }

}
