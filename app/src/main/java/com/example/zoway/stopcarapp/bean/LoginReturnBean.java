package com.example.zoway.stopcarapp.bean;

/**
 * Created by Administrator on 2016/12/13.
 *
 * 登录成功
 *
 */
public class LoginReturnBean {


    /**
     * success : true
     * msg : 登录成功。
     * date : 2017-01-13 15:25:54
     * type : data
     * code : 0
     */

    private boolean success;
    private String msg;
    private String date;
    private String type;
    private String code;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "LoginReturnBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
