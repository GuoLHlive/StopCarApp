package com.example.zoway.stopcarapp.util.printer.content;

/**
 * Created by Administrator on 2017/1/5.
 */
public class JsonContent {


    /**
     * license_plate : 粤X32K68
     * stop_number : 测试路段3【5211】
     * long_time : 0天03时16分
     * come_time : 2016-12-08 11:29
     * out_time : 2016-12-08 14：46
     * money : 0.00元
     * qrcode : http：//www.baidu.com
     */

    private String license_plate;
    private String stop_number;
    private String long_time;
    private String come_time;
    private String out_time;
    private String money;
    private String qrcode;
    private int state;
    private String userName;

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getStop_number() {
        return stop_number;
    }

    public void setStop_number(String stop_number) {
        this.stop_number = stop_number;
    }

    public String getLong_time() {
        return long_time;
    }

    public void setLong_time(String long_time) {
        this.long_time = long_time;
    }

    public String getCome_time() {
        return come_time;
    }

    public void setCome_time(String come_time) {
        this.come_time = come_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    //    @Override
//    public String toString() {
//        return  "车牌号：" + license_plate + '\n' +
//                "车位编号：" + stop_number + '\n' +
//                "停车时长：" + long_time + '\n' +
//                "停驻时间：" + come_time + '\n' +
//                "当前时间：" + out_time + '\n' +
//                "总 额 ：" + money + '\n';
//    }

    @Override
    public String toString() {
        return   license_plate + '\n' +
                 stop_number + '\n' +
                long_time + '\n' +
                come_time + '\n' +
                 out_time + '\n' +
                 money + '\n';
    }

}
