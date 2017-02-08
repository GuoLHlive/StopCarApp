package com.example.zoway.stopcarapp.activity;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.api.lmpl.BaseSubscriber;
import com.example.zoway.stopcarapp.api.lmpl.ParkingOrderInteractor;
import com.example.zoway.stopcarapp.bean.ParkingOrderDetailBean;
import com.example.zoway.stopcarapp.bean.PayUIBean;
import com.example.zoway.stopcarapp.databinding.ActivityPayBinding;
import com.example.zoway.stopcarapp.dev.IDevService;
import com.example.zoway.stopcarapp.module.DaggerDevComponent;
import com.example.zoway.stopcarapp.module.DevComponent;
import com.example.zoway.stopcarapp.module.DevServiceModule;
import com.example.zoway.stopcarapp.util.LongTimeOrString;
import com.example.zoway.stopcarapp.view.Pay_Dialog_View;
import com.google.gson.Gson;

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

    private ParkingOrderInteractor parkingOrderInteractor;

    private ParkingOrderDetailBean.DataBean data;
    private PayUIBean payUI;
    private int parkingOrderId;//订单号
    private String CarNumber; // 车牌

    @Override
    protected int getLayoutId() {
        payUI = (PayUIBean) getIntent().getSerializableExtra("PayUI");
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

        parkingOrderInteractor = appComponent.getParkingOrderInteractor();

    }

    @Override
    protected void initView() {
        //拿取支付订单信息
        if (payUI.isTakePhote()){
            CarNumber = payUI.getCarNumber();
            downParkingOrderInfoCarNumber();
        }else {
            parkingOrderId = payUI.getParkingOrderId();
            downParkingOrderInfo();
        }


    }

    //这里是登记订单点入
    private void downParkingOrderInfoCarNumber() {
        parkingOrderInteractor.parkingOrderInfo("RegisterInfo.do",stringOrJson(), new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                Gson gson = new Gson();
                ParkingOrderDetailBean parkingOrderDetailBean = gson.fromJson(result, ParkingOrderDetailBean.class);
                data = parkingOrderDetailBean.getData();
                addOnClick();
                initDataView();
            }
        });


    }


    //这是未处理订单点入
    private void downParkingOrderInfo() {
        parkingOrderInteractor.parkingOrderInfo("Detail.do", id_stringOrJson(), new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                Log.i("Bean","parkingOrderId:"+parkingOrderId);
                Gson gson = new Gson();
                ParkingOrderDetailBean parkingOrderDetailBean = gson.fromJson(result, ParkingOrderDetailBean.class);
                data = parkingOrderDetailBean.getData();
                addOnClick();
                initDataView();
            }
        });

    }

    private void initDataView() {
        binding.payLicensePlate.setText("小型汽车： 粤X32K68");
        binding.payStopNumber.setText("车位位置： 测试路段3【5211】");
        binding.payLongTime.setText("停车时长： "+LongTimeOrString.longTimeOrString(data.getParkingTime()));
        binding.payComeTime.setText("停驻时间： 2016-12-08 11:29");
        binding.payOutTime.setText("当前时间： 2016-12-08 14:46");
        binding.payStopMoney.setText("停车费用： 0.01元");
        binding.payOweMoney.setText("欠        费： 0.00元");
        binding.payMoney.setText("总        额： 0.00元");
        if (payUI.isTakePhote()){
            binding.payLicensePlate.setText("小型汽车： "+data.getVehicleNo());
        }
    }

    private void addOnClick() {
        binding.payBack.setOnClickListener(this);
        binding.payCash.setOnClickListener(this);
        binding.payPrint.setOnClickListener(this);
    }

    //ID 转为json（post请求body）
    private String id_stringOrJson(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("parkingOrderId",parkingOrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //请求 转为json（post请求body）
    private String stringOrJson(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("parkingOrderId",0);
            jsonObject.put("vehicleNo",CarNumber);
            jsonObject.put("vehicleType","02");
            jsonObject.put("isSpecial","no");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==binding.payBack.getId()){
            BaseActivity baseActivity = activitys.get(activitys.size() - 1);
            baseActivity.finish();
            activitys.remove(activitys.size()-1);
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
                jsonObject.put("license_plate",binding.payLicensePlate.getText());
                jsonObject.put("stop_number",binding.payStopNumber.getText());
                jsonObject.put("long_time",binding.payLongTime.getText());
                jsonObject.put("come_time",binding.payComeTime.getText());
                jsonObject.put("out_time",binding.payOutTime.getText());
                jsonObject.put("money",binding.payMoney.getText());
                jsonObject.put("qrcode",data.getMobileUrl());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            iDevService.print(jsonObject.toString());

        }


    }





}
