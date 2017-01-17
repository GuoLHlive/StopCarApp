package com.example.zoway.stopcarapp.util.printer;

import android.content.Context;

import com.example.zoway.stopcarapp.dev.P900.P990Printer;

import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by Tonny on 2016/6/3.
 */
public class PrinterFactory {
    private static IPrinter iPrinter = null;

    @Deprecated
    public static IPrinter _getPrinter(){
        return iPrinter;
    }

    @Deprecated
    public static boolean _initPrinter(Context context){
        if(iPrinter != null) return true;
        //找出打印机
        P990Printer p990Printer = new P990Printer(context);
        if(p990Printer.tryActivatePrinter()) {
            iPrinter = p990Printer;
            return true;
        }
        return false;
    }

    @Deprecated
    public static XMLReader _getXMLReader(){
        XMLReader xr = null;
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            //通过SAXParserFactory得到SAXParser的实例
            SAXParser sp = spf.newSAXParser();
            //通过SAXParser得到XMLReader的实例
            xr = sp.getXMLReader();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return xr;
    }
}
