package com.example.zoway.stopcarapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.api.lmpl.BaseSubscriber;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractor;
import com.example.zoway.stopcarapp.databinding.ActivityAdminBinding;

/**
 * Created by Administrator on 2016/12/13.
 * 用户界面(重庆)
 *
 */
public class AdminActivity extends BaseActivity {

    private ActivityAdminBinding binding;

    private LoginNfcInteractor loginNfcInteractor;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin;
    }

    @Override
    protected void initData(Intent intent) {
        binding = (ActivityAdminBinding) view;
        activitys.add(this);
        loginNfcInteractor = appComponent.getLoginNfcInteractor();
    }

    @Override
    protected void initView() {
        binding.adminBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.adminEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginNfcInteractor.loginFnc("logout.do", "", new BaseSubscriber<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        int size = activitys.size();
                        if (size!= 0){
                            for (Activity activity:activitys){
                                activity.finish();
                            }
                            activitys.clear();
                        }
                        Intent intent = new Intent(AdminActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
