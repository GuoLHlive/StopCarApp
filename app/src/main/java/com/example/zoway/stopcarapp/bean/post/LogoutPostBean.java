package com.example.zoway.stopcarapp.bean.post;

import android.util.Log;

import com.example.zoway.stopcarapp.util.StringForJson;

/**
 * Created by Administrator on 2017/1/16.
 */
public class LogoutPostBean {

    /**
     * longitude : 0.0
     * latitude : 0.0
     */

    private double longitude;
    private double latitude;

    public LogoutPostBean(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
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

    @Override
    public String toString() {
        String s = StringForJson.LogoutStringForJsonBody(longitude, latitude);
        Log.i("Post","LogoutPostBean"+s);
        return s;
    }
}
