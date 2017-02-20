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
import com.example.zoway.stopcarapp.bean.UIsBean;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private UIsBean uIsBean;

    @Override
    protected int getLayoutId() {
        payUI = (PayUIBean) getIntent().getSerializableExtra("PayUI");
        Log.i("Bean",payUI.toString()+"");
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
        uIsBean = appComponent.getUIsBean();
    }

    @Override
    protected void initView() {
        //拿取支付订单信息
        if (payUI.isTakePhote()){
            CarNumber = payUI.getCarNumber();
            parkingOrderId = payUI.getParkingOrderId();
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
        long thisTime = new Date().getTime();
        binding.payLicensePlate.setText("小型汽车："+data.getVehicleNo());
        binding.payStopNumber.setText("车位位置： 测试路段3【5211】");
        binding.payLongTime.setText("停车时长： "+LongTimeOrString.longTimeOrString(data.getParkingTime()));
        binding.payComeTime.setText("停驻时间： "+LongTimeOrString.stringStopTime(data.getParkingTime(),thisTime));
        binding.payOutTime.setText("当前时间： "+LongTimeOrString.longTimeOrString(thisTime));
        binding.payStopMoney.setText("停车费用： "+data.getDueFare()+"元");
        binding.payOweMoney.setText("欠        费："+(data.getDueFare()-data.getRealFare())+"元");
        binding.payMoney.setText("已付        额： "+data.getRealFare()+"元");
        if (payUI.isTakePhote()){
            binding.payLicensePlate.setText("小型汽车： "+data.getVehicleNo());
        }


        //UI界面写入车牌
        ArrayList<UIsBean.UIBean> lists = uIsBean.getLists();
        if (lists!=null&&lists.size()!=0){
            int parkSeatId = data.getParkSeatId();
            for (int i=0;i<lists.size();i++){
                UIsBean.UIBean uiBean = lists.get(i);
                int stopId = uiBean.getParkSeatId();
                if (parkSeatId == stopId){
                    uiBean.setPhoto(true);
                    uiBean.setVehicleNo(data.getVehicleNo());
                }

            }
        }

    }

    private void addOnClick() {
        binding.payBack.setOnClickListener(this);
        binding.payCash.setOnClickListener(this);
        binding.payPrint.setOnClickListener(this);
    }

    //ID 转为json（post请求body）根据订单号查询订单
    private String id_stringOrJson(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("parkingOrderId",parkingOrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //封装支付数据
    private String stringOrJsonPay(double realFare){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("parkingOrderId",parkingOrderId);
            jsonObject.put("realFare",realFare);
            jsonObject.put("payType","cash");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();

    }




    //请求 转为json（post请求body）
    private String stringOrJson(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("parkingOrderId",parkingOrderId);
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
            //支付
            Pay_Dialog_View dialogView = new Pay_Dialog_View(activity, new Pay_Dialog_View.getUserInput() {
                @Override
                public void Input(String userInput) {
                    Toast.makeText(activity,"已支付:"+userInput, Toast.LENGTH_SHORT).show();

                    if (!"".equals(userInput)){
                        Double money = Double.valueOf(userInput);
                        if (money!=0.0){
                            parkingOrderInteractor.parkingOrderInfo("Pay.do", stringOrJsonPay(money), new BaseSubscriber<String>() {
                                @Override
                                protected void onSuccess(String result) {
                                    Log.i("Bean","支付成功："+result);

                                    //UI界面修改背景颜色
                                    ArrayList<UIsBean.UIBean> lists = uIsBean.getLists();
                                    if (lists!=null&&lists.size()!=0){
                                        int parkSeatId = data.getParkSeatId();
                                        for (int i=0;i<lists.size();i++){
                                            UIsBean.UIBean uiBean = lists.get(i);
                                            int stopId = uiBean.getParkSeatId();
                                            if (parkSeatId == stopId){
                                                uiBean.setPhoto(true);
                                                uiBean.setVehicleNo(data.getVehicleNo());
                                                uiBean.setIsParking("yes_pay");
                                            }
                                        }
                                    }
                                }
                            });
                        }


                    }
                }
            });
            dialogView.setCanceledOnTouchOutside(true);
            dialogView.show();

        }
        if (id == binding.payPrint.getId()){
            //打印功能
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
                jsonObject.put("qrcode","www.baidu.com");

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Log.i("Print",jsonObject.toString());
            iDevService.print(jsonObject.toString());

        }


    }





}
