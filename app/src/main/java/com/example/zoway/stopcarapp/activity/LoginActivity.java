package com.example.zoway.stopcarapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.zoway.stopcarapp.MainActivity;
import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.api.lmpl.BaseSubscriber;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractor;
import com.example.zoway.stopcarapp.bean.LoginReturnBean;
import com.example.zoway.stopcarapp.bean.LoginUserBean;
import com.example.zoway.stopcarapp.bean.post.LoginPostBean;
import com.example.zoway.stopcarapp.databinding.ActivityLoginBinding;
import com.example.zoway.stopcarapp.file.SystemService;
import com.example.zoway.stopcarapp.util.showToast;
import com.example.zoway.stopcarapp.view.Login_Dialog_View;
import com.google.gson.Gson;


/**
 * Created by Administrator on 2016/12/15.
 * 登录界面
 *
 */
public class LoginActivity extends BaseActivity {

    private LoginNfcInteractor loginNfcInteractor;
    private BaseActivity activity;
    private SystemService systemService;
    private ActivityLoginBinding binding;

    @Override
    protected int getLayoutId() {
        return  R.layout.activity_login;
    }

    @Override
    protected void initData(Intent intent) {
        activity = this;
        activitys.add(activity);
        loginNfcInteractor = appComponent.getLoginNfcInteractor();
        systemService  = new SystemService();
        binding = (ActivityLoginBinding) view;
    }

    @Override
    protected void initView() {

        //账号密码登录
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login_Dialog_View dialogView = new Login_Dialog_View(activity, new Login_Dialog_View.getUserInput() {
                    @Override
                    public void Input(LoginUserBean s) {
                        if (s != null){
                            Toast.makeText(activity,s.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogView.setCanceledOnTouchOutside(true);
                dialogView.show();
            }
        });



    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getEventTime() - event.getDownTime() > 5000 && keyCode == KeyEvent.KEYCODE_0) {
            // TODO 长按输入密码并进入调试设置界面
//            Log.i("MainActivityA","进入设置模式");
            Intent intent = new Intent(LoginActivity.this,SettingActivity.class);
            startActivity(intent);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            //处理该intent
            final String Rid = systemService.getAndroidID();
            final String Uid = systemService.receiveNFC(getIntent());

            final LoginPostBean loginPostBean = new LoginPostBean(Rid,0.0,0.0,Uid);

            loginNfcInteractor.loginFnc("LoginByCard.do", loginPostBean.toString(), new BaseSubscriber<String>() {
                @Override
                protected void onSuccess(String result) {
                    Log.i("RetrofitLog",result);
                    Gson gson = new Gson();
                    LoginReturnBean loginReturnBean = gson.fromJson(result, LoginReturnBean.class);
                    String code = loginReturnBean.getCode();
                    if ("0".equals(code)){
                        activitys.remove(activity);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }else {
                        showToast.showToastTxt(activity,loginReturnBean.getMsg());
                    }
                }
            });

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
            int size = activitys.size();
            if (size!=0){
                for (Activity activity:activitys){
                    activity.finish();
                }
                activitys.clear();
            }
        startActivity(getIntent());

    }

}
