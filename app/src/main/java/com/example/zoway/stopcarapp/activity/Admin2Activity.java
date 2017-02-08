package com.example.zoway.stopcarapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.api.lmpl.BaseSubscriber;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractor;
import com.example.zoway.stopcarapp.bean.post.LogoutPostBean;
import com.example.zoway.stopcarapp.databinding.ActivityAdmin2Binding;
import com.example.zoway.stopcarapp.databinding.ActivityAdminBinding;

/**
 * Created by Administrator on 2016/12/13.
 * 用户界面(正式)
 *
 */
public class Admin2Activity extends BaseActivity {

    private ActivityAdmin2Binding binding;

    private LoginNfcInteractor loginNfcInteractor;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin2;
    }

    @Override
    protected void initData(Intent intent) {
        binding = (ActivityAdmin2Binding) view;
        activitys.add(this);
        loginNfcInteractor = appComponent.getLoginNfcInteractor();
    }

    @Override
    protected void initView() {
        binding.adminBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity baseActivity = activitys.get(activitys.size() - 1);
                baseActivity.finish();
                activitys.remove(activitys.size()-1);
            }
        });

        binding.adminEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutPostBean postBean = new LogoutPostBean(0.0,0.0);
                loginNfcInteractor.loginFnc("Logout.do", postBean.toString(), new BaseSubscriber<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        int size = activitys.size();
                        if (size!= 0){
                            for (Activity activity:activitys){
                                activity.finish();
                            }
                            activitys.clear();
                        }
                        Intent intent = new Intent(Admin2Activity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
