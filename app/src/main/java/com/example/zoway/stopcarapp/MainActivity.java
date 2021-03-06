package com.example.zoway.stopcarapp;


import android.app.Activity;
import android.content.Intent;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;

import android.view.View;
import android.widget.Toast;


import com.example.zoway.stopcarapp.activity.Admin2Activity;

import com.example.zoway.stopcarapp.activity.BaseActivity;
import com.example.zoway.stopcarapp.activity.EscapeRecordActivity;
import com.example.zoway.stopcarapp.adapter.MainRecyclerAdapter;

import com.example.zoway.stopcarapp.api.lmpl.BaseSubscriber;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractor;
import com.example.zoway.stopcarapp.api.lmpl.ParkingOrderInteractor;
import com.example.zoway.stopcarapp.api.lmpl.PartInteractor;
import com.example.zoway.stopcarapp.bean.Config;
import com.example.zoway.stopcarapp.bean.ParkingOrderListBean;
import com.example.zoway.stopcarapp.bean.PartBaseInfoBean;
import com.example.zoway.stopcarapp.bean.PartSeatBean;
import com.example.zoway.stopcarapp.bean.UIsBean;
import com.example.zoway.stopcarapp.databinding.ActivityMainBinding;

import com.example.zoway.stopcarapp.http.RetrofitHttp;
import com.example.zoway.stopcarapp.service.ParkingWebSocket;
import com.example.zoway.stopcarapp.util.LongTimeOrString;
import com.example.zoway.stopcarapp.util.SharedPreferencesUtils;
import com.google.gson.Gson;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    //屏蔽home键
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private ActivityMainBinding binding;
    private BaseActivity activity;
    private MainRecyclerAdapter adapter;

    //网络工具类
    private PartInteractor partInteractor;
    private ParkingOrderInteractor parkingOrderInteractor;


    //网络通信拿取数据
    private PartBaseInfoBean baseInfoBean;
    private PartSeatBean partSeatBean;
    private ParkingOrderListBean parkingOrderListBean;
    private LoginNfcInteractor loginNfcInteractor;

    //数据UI界面
    private UIsBean uIsBean;

    //时间
    private ArrayList<UIsBean.UIBean> upTimeData;
    public Timer mTime;
    private Long time;
    public boolean isTask = false;
    public TimerTask mTimerTask = new TimerTask() {
        @Override
            public void run() {
            if (upTimeData!=null&&upTimeData.size()!=0){
                for (int i=0;i<upTimeData.size();i++){
                    UIsBean.UIBean uiBean = upTimeData.get(i);
                    Long parkingTime = uiBean.getParkingTime();
                    if (parkingTime==0L){
                        uiBean.setStringParkingTime("");
                    }
                    uiBean.setStringParkingTime(LongTimeOrString.stringStopTime_MainActivity(parkingTime,time));

                }
                time = time +1000;
            }
            }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    protected void initData(Intent intent) {
        binding = (ActivityMainBinding) view;
        activity = this;
        activitys.add(activity);

        //停车项目信息
        partInteractor = appComponent.getPartInteractor();
        //停车场信息
        parkingOrderInteractor = appComponent.getParkingOrderInteractor();
        //车位信息
        uIsBean = appComponent.getUIsBean();
        //用户信息
        loginNfcInteractor = appComponent.getLoginNfcInteractor();


        time = new Date().getTime();
        //需要计时的车位数据存储
        upTimeData = uIsBean.getUpTimeData();




    }




    @Override
    protected void initView() {
        Gson gson = new Gson();
        //拿取地区信息 BaseInfo.do
        downBaseInfo(gson);
        initOnClick();
    }





    //数据
    private void downBaseInfo(final Gson gson) {
        partInteractor.partInfo("BaseInfo.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                baseInfoBean = gson.fromJson(result,PartBaseInfoBean.class);
                String title = baseInfoBean.getData().getName();
                SharedPreferencesUtils.setParam(activity,Config.STOPTITLE,title);
                binding.mainTitle.setText(title);
                //拿取车位信息 Seat/BaseInfo.do
                downSeatBaseInfo(gson);
            }
        });
    }

    private void downSeatBaseInfo(final Gson gson) {
        partInteractor.partInfo("Seat","BaseInfo.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                partSeatBean = gson.fromJson(result, PartSeatBean.class);
                Log.i("Bean","partSeatBean加载成功!");
                //没有处理的订单信息
                downList(gson);
            }
        });
    }


    private void downList(final Gson gson) {

        parkingOrderInteractor.parkingOrderInfo("List.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                Log.i("Bean","parkingOrderListBean加载成功!");
                parkingOrderListBean = gson.fromJson(result, ParkingOrderListBean.class);
                downUserInfo(gson);
            }
        });

    }

    //拿取用户信息
    private void downUserInfo(final Gson gson){

        loginNfcInteractor.loginFnc("LoginWorkerDetail.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                UIsBean.UserInfo userInfo = gson.fromJson(result, UIsBean.UserInfo.class);
                uIsBean.setUserInfo(userInfo);
                if (partSeatBean.getDatas() != null){
                    UpDataUI();
                    return;
                }
                Toast.makeText(activity,"网络发生未知错误，请把程序退出，重新登录!",Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void UpDataUI(){
        Log.i("Bean","加载UI!");
        ArrayList<UIsBean.UIBean> lists = new ArrayList<>();
        //停车场车位信息(先写入数据)
        ArrayList<PartSeatBean.DatasBean> partDatas = partSeatBean.getDatas();
        //未完成订单信息(后修改数据)
        List<ParkingOrderListBean.DatasBean> orderDatas = parkingOrderListBean.getDatas();



        //把UI的模型定制好
        for (int i=0;i<partDatas.size();i++){
            PartSeatBean.DatasBean partData = partDatas.get(i);
            UIsBean.UIBean uiBean = new UIsBean.UIBean(partData.getParkSeatId(),
                    partData.getSeatNo(),1000000,
                    activity,View.GONE,"",partData.getIsParking(),
                    "","",0L,0L);
            lists.add(uiBean);
        }

        //UI的数据加入(未完成的订单)
        Map<Integer,ParkingOrderListBean.DatasBean> map = new HashMap<>();
        if (orderDatas!=null&&orderDatas.size()!=0){
            for (int i=0;i<orderDatas.size();i++){
                ParkingOrderListBean.DatasBean orderData = orderDatas.get(i);
                int orderId = orderData.getParkSeatId();
                //筛选 取最新的
                map.put(orderId,orderData);
            }
         //对比 把未处理的订单数据加入list


            Iterator<Map.Entry<Integer, ParkingOrderListBean.DatasBean>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Integer, ParkingOrderListBean.DatasBean> next = iterator.next();
                Integer orderId = next.getKey();
                ParkingOrderListBean.DatasBean orderData = next.getValue();
                for (int i=0;i<lists.size();i++){
                    UIsBean.UIBean uiBean = lists.get(i);
                    int parkSeatId = uiBean.getParkSeatId();
                    if (orderId == parkSeatId){
                        Log.i("ParkingWebSocket","orderId:"+orderId+"");
                        uiBean.setParkingOrderId(orderData.getParkingOrderId());
                        uiBean.setPayStatus(orderData.getPayStatus());
                        uiBean.setVehicleNo(orderData.getVehicleNo());
                        uiBean.setParkingTime(orderData.getParkingTime());
                        uiBean.setIsParking(orderData.getIsParking());

                        //已经支付
//                        if ("payed".equals(orderData.getPayStatus())){
//                            //修改背景
//                            uiBean.setIsParking("free");
//                        }

                        if (uiBean.getVehicleNo()==null){
                            uiBean.setPhoto(false);
                            uiBean.setIsParking("yes");
                        }else {
                            uiBean.setPhoto(true);
                            uiBean.setIsParking("yes_photo");
                        }
                        upTimeData.add(uiBean);
                        isTask = true;
                        //时间
                        if (mTime == null){
                            mTime = new Timer();
                            mTime.schedule(mTimerTask,1000,1000);
                        }
                    }
                }
            }



        }
       uIsBean.setLists(lists);
        Log.i("Bean","list:"+uIsBean.getLists());
        //把数据写入adapter
        binding.mRecyclerView.setLayoutManager(new GridLayoutManager(activity,2));
        adapter = new MainRecyclerAdapter(uIsBean);
        binding.mRecyclerView.setAdapter(adapter);
        Log.i("Bean","数据写入成功!");
        Log.i("Bean","打开Socket!");
        //      跟后台数据通信
//        RetrofitHttp.openWebSocket(partSeatBean,parkingOrderListBean,adapter);
        RetrofitHttp.openWebSocket(uIsBean);
    }



    private void initOnClick() {
        binding.mainEscapeRecord.setOnClickListener(this);
        binding.mainMenu.setOnClickListener(this);
    }




    //数字键盘监听
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_HOME){
            Intent intent = new Intent(this,Admin2Activity.class);
            startActivity(intent);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK){

        }
        return true;
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.main_menu:
                intent = new Intent(this,Admin2Activity.class);
                break;
            case R.id.main_escapeRecord:
                intent = new Intent(this,EscapeRecordActivity.class);
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//      关闭通信
        if (mTime!=null){
            mTime.cancel();
        }
        RetrofitHttp.stopWebSocket();
    }




}
