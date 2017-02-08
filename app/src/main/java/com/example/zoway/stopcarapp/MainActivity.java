package com.example.zoway.stopcarapp;


import android.content.Intent;

import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;

import android.view.View;


import com.example.zoway.stopcarapp.activity.Admin2Activity;

import com.example.zoway.stopcarapp.activity.BaseActivity;
import com.example.zoway.stopcarapp.activity.EscapeRecordActivity;
import com.example.zoway.stopcarapp.adapter.MainRecyclerAdapter;

import com.example.zoway.stopcarapp.api.lmpl.BaseSubscriber;
import com.example.zoway.stopcarapp.api.lmpl.ParkingOrderInteractor;
import com.example.zoway.stopcarapp.api.lmpl.PartInteractor;
import com.example.zoway.stopcarapp.bean.ParkingOrderListBean;
import com.example.zoway.stopcarapp.bean.PartBaseInfoBean;
import com.example.zoway.stopcarapp.bean.PartSeatBean;
import com.example.zoway.stopcarapp.databinding.ActivityMainBinding;

import com.example.zoway.stopcarapp.http.RetrofitHttp;
import com.example.zoway.stopcarapp.service.ParkingWebSocket;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;




public class MainActivity extends BaseActivity implements View.OnClickListener{
    //屏蔽home键
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private ActivityMainBinding binding;
    private BaseActivity activity;

    private MainRecyclerAdapter adapter;


    private PartInteractor partInteractor;
    private ParkingOrderInteractor parkingOrderInteractor;


    private ArrayList<String[]> list;
    private PartBaseInfoBean baseInfoBean;
    private ArrayList<PartSeatBean.DatasBean> lists;
    private List<ParkingOrderListBean.DatasBean> datas;



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
        //模拟数据
        list = new ArrayList<>();
        String[] str1 = {"418","90","5211","粤X32K68","时间X天X时X分"};
        String[] str2 = {"410","90","5212","粤X32K68","时间X天X时X分"};
        String[] str3 = {"410","90","5213","粤X32K68","时间X天X时X分"};
        list.add(str1);
        list.add(str2);
        list.add(str3);
//        后台信息

    }



    @Override
    protected void initView() {
        Gson gson = new Gson();
        //拿取地区信息 BaseInfo.do
        downBaseInfo(gson);
        initOnClick();
//      跟后台数据通信
        RetrofitHttp.openWebSocket();
    }





    //数据
    private void downBaseInfo(final Gson gson) {
        partInteractor.partInfo("BaseInfo.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                baseInfoBean = gson.fromJson(result,PartBaseInfoBean.class);
                String title = baseInfoBean.getData().getName();
                binding.mainTitle.setText(title);
//                Log.i("Bean","BaseInfo:"+result);

                //拿取车位信息 Seat/BaseInfo.do
                downSeatBaseInfo(gson);
            }
        });
    }

    private void downSeatBaseInfo(final Gson gson) {
        partInteractor.partInfo("Seat","BaseInfo.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                PartSeatBean partSeatBean = gson.fromJson(result, PartSeatBean.class);
                lists = partSeatBean.getDatas();
                //                Log.i("Bean","Seat:"+result);
                Log.i("Bean","Seat_List:"+lists.size());
//                s1.onNext(seatBean);
                downList(gson);
            }
        });
    }


    private void downList(final Gson gson) {

        parkingOrderInteractor.parkingOrderInfo("List.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                Log.i("Bean","数据正在写入!");
                ParkingOrderListBean parkingOrderListBean = gson.fromJson(result, ParkingOrderListBean.class);
                datas = parkingOrderListBean.getDatas();
                Log.i("Bean","datas:"+datas.size());
                binding.mRecyclerView.setLayoutManager(new GridLayoutManager(activity,2));
                adapter = new MainRecyclerAdapter(lists,list,datas,activity);
                binding.mRecyclerView.setAdapter(adapter);
                Log.i("Bean","数据写入成功!");

            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
//      关闭通信
        RetrofitHttp.stopWebSocket();

    }
}
