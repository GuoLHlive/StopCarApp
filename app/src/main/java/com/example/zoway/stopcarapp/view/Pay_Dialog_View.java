package com.example.zoway.stopcarapp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.zoway.stopcarapp.R;


/**
 * Created by Administrator on 2016/12/22.
 */

//Alertdialog 4.0后并不能屏蔽home键
public class Pay_Dialog_View extends AlertDialog {

    private EditText mMoney;
    private Button mSave;
    private Button mEsc;
    private getUserInput input;
    private Context context;

    public interface getUserInput{
        void Input(String s);
    }

    public Pay_Dialog_View(Context context,getUserInput input) {
        super(context);
        this.context = context;
        this.input = input;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_dialog_layout);
        initData();
        initView();
    }

    private void initView() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = mMoney.getText().toString().trim();
                input.Input(money);
                dismiss();
            }
        });

        mEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.Input("");
                dismiss();
            }
        });

    }

    private void initData() {
        mEsc = (Button) this.findViewById(R.id.mEsc);
        mSave = (Button) this.findViewById(R.id.mSave);
        mMoney = (EditText) findViewById(R.id.mMoney);
        mMoney.setInputType(InputType.TYPE_NULL);

    }

}
