package com.example.zoway.stopcarapp.dev;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.widget.Toast;

import com.example.zoway.stopcarapp.util.printer.IPrinter;


/**
 * Created by Administrator on 2016/9/12.
 */
public abstract class AbstractDevService implements IDevService, NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback {
    protected IPrinter iPrinter = null;
    protected Context context = null;

    protected NfcAdapter nfcAdapter;

    public AbstractDevService(Context context){
        this.context = context;
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    @Override
    public abstract void openDev();

    @Override
    public abstract void closeDev();


    @Override
    public boolean supportPrint() {
        return iPrinter != null;
    }

    @Override
    public abstract void print(String msg);

    @Override
    public boolean supportNFC(){
        if(nfcAdapter == null){
            return false;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(context, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public abstract boolean devReady();

    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return null;
    }
}
