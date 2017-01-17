package com.example.zoway.stopcarapp.bean;

/**
 * Created by Administrator on 2017/1/13.
 *  密码登录bean
 */
public class LoginUserBean {

    private String WorkerNo;
    private String Password;

    public LoginUserBean() {
    }

    public LoginUserBean(String workerNo, String password) {
        WorkerNo = workerNo;
        Password = password;
    }

    public String getWorkerNo() {
        return WorkerNo;
    }

    public void setWorkerNo(String workerNo) {
        WorkerNo = workerNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "LoginUserBean{" +
                "WorkerNo='" + WorkerNo + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
