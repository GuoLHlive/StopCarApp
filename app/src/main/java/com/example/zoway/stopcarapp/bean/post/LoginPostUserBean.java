package com.example.zoway.stopcarapp.bean.post;

/**
 * Created by Administrator on 2017/1/16.
 * 密码请求的bean
 *
 */
public class LoginPostUserBean {


    /**
     * deviceId : ABCDEFG
     * longitude : 0.0
     * latitude : 0.0
     * workerNo : 100000
     * password : 123456
     */

    private String deviceId;
    private double longitude;
    private double latitude;
    private String workerNo;
    private String password;

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

    public String getWorkerNo() {
        return workerNo;
    }

    public void setWorkerNo(String workerNo) {
        this.workerNo = workerNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "LoginPostUserBean{" +
                "deviceId='" + deviceId + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", workerNo='" + workerNo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
