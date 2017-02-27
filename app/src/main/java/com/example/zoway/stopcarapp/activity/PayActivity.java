package com.example.zoway.stopcarapp.activity;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import com.example.zoway.stopcarapp.MyApp;
import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.api.lmpl.BaseSubscriber;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractor;
import com.example.zoway.stopcarapp.api.lmpl.ParkingOrderInteractor;
import com.example.zoway.stopcarapp.bean.Config;
import com.example.zoway.stopcarapp.bean.ParkingOrderDetailBean;
import com.example.zoway.stopcarapp.bean.PayUIBean;
import com.example.zoway.stopcarapp.bean.UIsBean;
import com.example.zoway.stopcarapp.databinding.ActivityPayBinding;
import com.example.zoway.stopcarapp.dev.IDevService;
import com.example.zoway.stopcarapp.dev.msm8909.Msm8909DevService;
import com.example.zoway.stopcarapp.module.DaggerDevComponent;
import com.example.zoway.stopcarapp.module.DevComponent;
import com.example.zoway.stopcarapp.module.DevServiceModule;
import com.example.zoway.stopcarapp.util.BntNotRepeatClick;
import com.example.zoway.stopcarapp.util.LongTimeOrString;
import com.example.zoway.stopcarapp.view.Pay_Dialog_View;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/12/12.
 *  支付界面
 *
 *  功能：拍照完至这界面 现金收费 重拍
 *
 */
public class PayActivity extends BaseActivity implements View.OnClickListener{

    private ActivityPayBinding binding;
    private BaseActivity activity;
    private DevComponent devComponent;

    IDevService iDevService = null;

    private ParkingOrderInteractor parkingOrderInteractor;
    private LoginNfcInteractor loginNfcInteractor;

    private ParkingOrderDetailBean.DataBean data;
    private PayUIBean payUI;
    private int parkingOrderId;//订单号
    private String CarNumber; // 车牌
    private Resources resources;

    //当前Activity状态
    private String currentState = "";
    private Map<Integer,String> payState;

    private UIsBean uIsBean;
    private String userName = "";

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
        resources = getResources();

        //默认当前是拍照完至此
        payState = MyApp.getPayState();


        parkingOrderInteractor = appComponent.getParkingOrderInteractor();
        loginNfcInteractor = appComponent.getLoginNfcInteractor();

        uIsBean = appComponent.getUIsBean();
        if (iDevService instanceof Msm8909DevService){
            iDevService.openDev();
        }



        //添加订单信息
        if (payState==null){
            return;
        }
        UIsBean.UserInfo userInfo = uIsBean.getUserInfo();
        UIsBean.UserInfo.DataBean data = userInfo.getData();
        if (data!=null){
            userName = data.getName();
        }
        addOnClick();

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
                Log.i("Bean","Pay:"+data.toString());
                initDataView();
            }
        });

    }

    private void initDataView() {



        long thisTime = new Date().getTime();
        if (data!=null){
            double v = data.getDueFare() - data.getRealFare();
            binding.payLicensePlate.setText("小型汽车： "+data.getVehicleNo());
            binding.payStopNumber.setText("车位位置： "+payUI.getSeatNo());
            binding.payLongTime.setText("停车时长： "+LongTimeOrString.longTimeOrString(data.getParkingTime()));
            binding.payComeTime.setText("停驻时间："+LongTimeOrString.stringStopTime(data.getParkingTime(),thisTime));
            binding.payOutTime.setText("当前时间： "+LongTimeOrString.longTimeOrString(thisTime));
            binding.payStopMoney.setText("停车费用： "+data.getDueFare()+"元");
            binding.payOweMoney.setText("欠        费："+String.valueOf(v)+"元");
            binding.payMoney.setText("当前已付： "+data.getRealFare()+"元");

            //修改PayActivity状态
            String payStatus = data.getPayStatus();
            if ("no_pay".equals(payStatus)){
                if (data.getDueFare()>0.0){
                    upDataPayState(Config.PAYCHARGE);
                }

            }
            if ("escape".equals(payStatus)||"arrearage".equals(payStatus)||"payed".equals(payStatus)){
                upDataPayState(Config.PAYPRINT);
            }



            //UI界面写入车牌
            setUIStopId();

            //修改按钮数据
            //判断状态：拍照后 保存照片后（查询订单） 收费 收费后
            //       takePhoto  savePhoto     payCharge  payPrint
            //查询当前页面状态
            queryState();
            //修改按钮
            if (currentState.equals(Config.TAKEPHOTO)){
                binding.payBntOne.setText(getSourString(R.string.pay_save));
                binding.payBntTwo.setText(getSourString(R.string.pay_photo));
                binding.payBntThree.setText(getSourString(R.string.pay_print));
                binding.payBntTwo.setVisibility(View.VISIBLE);
                binding.payBntThree.setVisibility(View.VISIBLE);
                binding.payBntFour.setVisibility(View.INVISIBLE);
                return;
            }
            if (currentState.equals(Config.SAVEPHOTO)){
                binding.payBntOne.setText(getSourString(R.string.pay_taskPhone_print));
                binding.payBntTwo.setVisibility(View.INVISIBLE);
                binding.payBntThree.setVisibility(View.INVISIBLE);
                binding.payBntFour.setVisibility(View.INVISIBLE);
                return;
            }
            if (currentState.equals(Config.PAYCHARGE)){
                binding.payBntOne.setText(getSourString(R.string.pay_cash));
                binding.payBntTwo.setText(getSourString(R.string.pay_task));
                binding.payBntThree.setText(getSourString(R.string.pay_escape));
                binding.payBntTwo.setVisibility(View.VISIBLE);
                binding.payBntThree.setVisibility(View.VISIBLE);
                binding.payBntFour.setVisibility(View.VISIBLE);
                return;
            }
            if (currentState.equals(Config.PAYPRINT)){
                binding.payBntOne.setText(getSourString(R.string.pay_print_money));
                binding.payBntTwo.setVisibility(View.INVISIBLE);
                binding.payBntThree.setVisibility(View.INVISIBLE);
                binding.payBntFour.setVisibility(View.INVISIBLE);
                return;
            }


        }
    }


    private String getSourString(int id) {
        return resources.getString(id);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String state = "";
        if (id!= R.id.pay_back){
            Button bnt = (Button) view;
            state = bnt.getText().toString();
        }
        switch (id){
            case R.id.pay_back://返回键
                BackActivity();
                break;
            case R.id.pay_bnt_one:
                if (state.equals(getSourString(R.string.pay_save))){//保存
                    //直接打印
                    Print(1);
                    upDataPayState(Config.SAVEPHOTO);
                    //刷新界面
                    initView();
                    break;
                }
                if (state.equals(getSourString(R.string.pay_taskPhone_print))){//补打
                    //直接打印
                    Print(1);
                    break;
                }
                if (state.equals(getSourString(R.string.pay_cash))){//确定收费
                    PostMoney(stringOrJsonPay(data.getDueFare()));
                    break;
                }
                if (state.equals(getSourString(R.string.pay_print_money))){//打印收据
                    Print(2);
                    break;
                }

                break;
            case R.id.pay_bnt_two:
                if (state.equals(getSourString(R.string.pay_photo))){//重新拍照
                    BackActivity();
                    Intent intent = new Intent(activity, TakeOcrPhotoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("seatNo",payUI.getSeatNo());
                    bundle.putInt("parkingOrderId",parkingOrderId);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    break;
                }
                if (state.equals(getSourString(R.string.pay_task))){//确定走费
                    takeCharge();
                    break;
                }
            case R.id.pay_bnt_three:
                if (state.equals(getSourString(R.string.pay_print))){
                    //直接打印
                    Print(1);
                    break;
                }
                if (state.equals(getSourString(R.string.pay_escape))){//逃费






                    parkingOrderInteractor.parkingOrderInfo("Escape.do", id_stringOrJson(), new BaseSubscriber<String>() {
                        @Override
                        protected void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String success = jsonObject.getString("success");
                                if ("true".equals(success)){
                                    upDataPayState(Config.PAYPRINT);
                                    initView();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(activity,"json解析错误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    break;
                }

            case R.id.pay_bnt_four:
                //直接打印
                Print(1);
                break;
        }

    }

    //添加按钮事件
    private void addOnClick() {
        binding.payBntOne.setOnClickListener(this);
        binding.payBntTwo.setOnClickListener(this);
        binding.payBntThree.setOnClickListener(this);
        binding.payBntFour.setOnClickListener(this);
        binding.payBack.setOnClickListener(this);
    }



    private void PostMoney(String postBody) {
        parkingOrderInteractor.parkingOrderInfo("Pay.do",postBody, new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");
                    if ("true".equals(success)){
                        upDataPayState(Config.PAYPRINT);
                        initView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"json解析错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void queryState() {
        Iterator<Map.Entry<Integer, String>> iterator = payState.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, String> entry = iterator.next();
            Integer key = entry.getKey();
            if (key==parkingOrderId){
                currentState = entry.getValue();
            }
        }

        //默认 wocket已经默认
//        if ("".equals(currentState)){
//            currentState = "takePhoto";
//        }
    }





    //修改PayActivity状态
    private void upDataPayState(String state){
        if (payState==null){
            Toast.makeText(activity,"当前状态发生未知错误",Toast.LENGTH_SHORT).show();
            return;
        }

        payState.put(parkingOrderId,state);

    }


    private void setUIStopId() {
        ArrayList<UIsBean.UIBean> lists = uIsBean.getLists();
        if (lists!=null&&lists.size()!=0){
            int parkSeatId = data.getParkSeatId();
            for (int i=0;i<lists.size();i++){
                UIsBean.UIBean uiBean = lists.get(i);
                int stopId = uiBean.getParkSeatId();
                if (parkSeatId == stopId){
                    uiBean.setPhoto(true);
                    uiBean.setVehicleNo(data.getVehicleNo());
                    uiBean.setIsParking("yes_photo");
                }

            }
        }
    }

    //打印功能(1：凭条、2：收据)
    private void Print(int state) {
        if (BntNotRepeatClick.isFastClick()){
            printToast("正在打印中，请稍等!");
            PayPrint(state);
        }
    }



    //走费功能
    private void takeCharge() {
        Pay_Dialog_View dialogView = new Pay_Dialog_View(activity, new Pay_Dialog_View.getUserInput() {
            @Override
            public void Input(String userInput) {
                Toast.makeText(activity,"已支付:"+userInput, Toast.LENGTH_SHORT).show();

                if (!"".equals(userInput)){
                    final Double money = Double.valueOf(userInput);
                    if (money!=0.0){
                        PostMoney(stringOrJsonPay(money));
                    }


                }
            }
        });
        dialogView.setCanceledOnTouchOutside(true);
        dialogView.show();
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



    //封装支付数据 支付
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




    //请求 转为json（post请求body） 查询订单
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



    private void BackActivity() {
        BaseActivity baseActivity = activitys.get(activitys.size() - 1);
        baseActivity.finish();
        activitys.remove(activitys.size()-1);
    }

    //打印
    private void PayPrint(int state) {
        if(!iDevService.supportPrint()){
            Toast.makeText(activity, "不支付打印机功能", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("state",state);
            jsonObject.put("license_plate",binding.payLicensePlate.getText());
            jsonObject.put("stop_number",binding.payStopNumber.getText());
            jsonObject.put("long_time",binding.payLongTime.getText());
            jsonObject.put("come_time",binding.payComeTime.getText());
            jsonObject.put("out_time",binding.payOutTime.getText());
            jsonObject.put("money",binding.payMoney.getText());
            jsonObject.put("userName",userName);

            if (data.getMobileUrl()==null){
                jsonObject.put("qrcode","www.baidu.com");
            }else {
                jsonObject.put("qrcode",data.getMobileUrl());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
//            Log.i("Print",jsonObject.toString());
        iDevService.print(jsonObject.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iDevService instanceof Msm8909DevService){
            iDevService.closeDev();
        }
    }

    public void printToast(String msg){
        Toast.makeText(this, "[打印机]"+ msg, Toast.LENGTH_SHORT).show();
    }

}
