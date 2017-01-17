package com.example.zoway.stopcarapp.activity;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.databinding.ActivityPayBinding;
import com.example.zoway.stopcarapp.dev.IDevService;
import com.example.zoway.stopcarapp.module.DaggerDevComponent;
import com.example.zoway.stopcarapp.module.DevComponent;
import com.example.zoway.stopcarapp.module.DevServiceModule;
import com.example.zoway.stopcarapp.view.Pay_Dialog_View;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/12/12.
 *  支付界面
 */
public class PayActivity extends BaseActivity implements View.OnClickListener{

    private ActivityPayBinding binding;
    private BaseActivity activity;
    private DevComponent devComponent;

    IDevService iDevService = null;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initData(Intent intent) {
        devComponent = DaggerDevComponent.builder().build();
        devComponent.inject(this);
        iDevService = devComponent.getIDevService();
        binding = (ActivityPayBinding) view;
        activity = this;
        activitys.add(activity);
    }

    @Override
    protected void initView() {
        binding.payBack.setOnClickListener(this);
        binding.payCash.setOnClickListener(this);
        binding.payPrint.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==binding.payBack.getId()){
            finish();
        }
        if (id == binding.payCash.getId()){
            Pay_Dialog_View dialogView = new Pay_Dialog_View(activity, new Pay_Dialog_View.getUserInput() {
                @Override
                public void Input(String userInput) {
                    Toast.makeText(activity,userInput, Toast.LENGTH_SHORT).show();
                }
            });
            dialogView.setCanceledOnTouchOutside(true);
            dialogView.show();

        }
        if (id == binding.payPrint.getId()){
            if(!iDevService.supportPrint()){
                Toast.makeText(activity, "不支付打印机功能", Toast.LENGTH_SHORT).show();
                return;
            }


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("license_plate","粤x32k68");
                jsonObject.put("stop_number","测试路段3【5211】");
                jsonObject.put("long_time","0天03时16分");
                jsonObject.put("come_time","2016-12-08 11:29");
                jsonObject.put("out_time","2016-12-08 14:46分");
                jsonObject.put("money","0.00元");
                jsonObject.put("qrcode","http://www.baidu.com");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            iDevService.print(jsonObject.toString());

        }


    }



}
