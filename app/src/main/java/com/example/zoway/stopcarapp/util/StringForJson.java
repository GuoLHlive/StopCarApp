package com.example.zoway.stopcarapp.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/14.
 */
public class StringForJson {

    /**
     *  把Rid 与 认证ID 拼成
     * @return
     */
    public static String stringForJsonBody(String deviceId,String workCardNo,double longitude,double latitude){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceId",deviceId);
            jsonObject.put("longitude",longitude);
            jsonObject.put("latitude",latitude);
            jsonObject.put("workCardNo",workCardNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String stringbody = jsonObject.toString();
        return stringbody;
    }

    public static String LogoutStringForJsonBody(double longitude,double latitude){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("longitude",longitude);
            jsonObject.put("latitude",latitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String stringbody = jsonObject.toString();
        return stringbody;
    }


}
