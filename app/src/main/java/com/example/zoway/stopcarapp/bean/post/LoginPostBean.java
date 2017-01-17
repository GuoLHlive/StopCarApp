package com.example.zoway.stopcarapp.bean.post;

import android.util.Log;

import com.example.zoway.stopcarapp.util.StringForJson;

/**
 *
 * 免密码登录
 *
 */
public class LoginPostBean {


    /**
     * deviceId : ABCDEFG
     * longitude : 0.0
     * latitude : 0.0
     * workCardNo : PP12345677
     */

    private String deviceId; //机号
    private double longitude;//经度
    private double latitude;//纬度
    private String workCardNo; //读卡

    public LoginPostBean(String deviceId, double longitude, double latitude, String workCardNo) {
        this.deviceId = deviceId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.workCardNo = workCardNo;
    }

    @Override
    public String toString() {
        String msg = StringForJson.stringForJsonBody(deviceId, workCardNo,longitude,latitude);
        Log.i("StopCarApp:","LoginPostBean--:"+msg);
        return msg;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getWorkCardNo() {
        return workCardNo;
    }

    public void setWorkCardNo(String workCardNo) {
        this.workCardNo = workCardNo;
    }
}
