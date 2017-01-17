package com.example.zoway.stopcarapp.dev;

/**
 * Created by Administrator on 2016/9/7.
 */
public interface IDevService {
    void openDev();

    void closeDev();

    boolean supportPrint();

    void print(String msg);

    boolean supportNFC();

    boolean devReady();

    //IPrinter getPrinter();
}
