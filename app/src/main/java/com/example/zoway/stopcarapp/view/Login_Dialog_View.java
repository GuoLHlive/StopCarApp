package com.example.zoway.stopcarapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.bean.LoginUserBean;


/**
 * Created by Administrator on 2016/12/22.
 *  activity（以alretDialog创建）
 *
 */

//Alertdialog 4.0后并不能屏蔽home键
public class Login_Dialog_View extends AlertDialog {

    private Button mSave;
    private Button mEsc;
    private EditText mEt_username;
    private EditText mET_password;

    private getUserInput input;
    private Context context;

    public interface getUserInput{
        void Input(LoginUserBean s);
    }

    public Login_Dialog_View(Context context,getUserInput input) {
        super(context);
        this.context = context;
        this.input = input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog_layout);
        initData();
        initView();
    }

    private void initView() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUserBean bean = new LoginUserBean();
                String username = mEt_username.getText().toString().trim();
                String password = mET_password.getText().toString().trim();
                if ("".equals(username)||"".equals(password)){
                    showToast("员工编号或密码不能为空");
                }else {
                    bean.setWorkerNo(username);
                    bean.setPassword(password);
                    input.Input(bean);
                }
                dismiss();
            }
        });

        mEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUserBean bean = null;
                input.Input(bean);
                dismiss();
            }
        });

    }

    private void initData() {
        mEsc = (Button) this.findViewById(R.id.mEsc);
        mSave = (Button) this.findViewById(R.id.mSave);
        mET_password = (EditText) this.findViewById(R.id.login_password);
        mEt_username = (EditText) this.findViewById(R.id.login_username);
    }

    private void showToast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
