package com.example.zoway.stopcarapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.zoway.stopcarapp.activity.Admin2Activity;
import com.example.zoway.stopcarapp.activity.AdminActivity;
import com.example.zoway.stopcarapp.activity.BaseActivity;
import com.example.zoway.stopcarapp.activity.EscapeRecordActivity;
import com.example.zoway.stopcarapp.adapter.MainRecyclerAdapter;
import com.example.zoway.stopcarapp.api.lmpl.BaseSubscriber;
import com.example.zoway.stopcarapp.api.lmpl.LoginNfcInteractor;
import com.example.zoway.stopcarapp.api.lmpl.PartInteractor;
import com.example.zoway.stopcarapp.bean.PartBaseInfoBean;
import com.example.zoway.stopcarapp.bean.PartSeatBean;
import com.example.zoway.stopcarapp.bean.post.LogoutPostBean;
import com.example.zoway.stopcarapp.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    //屏蔽home键
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private ActivityMainBinding binding;


    private MainRecyclerAdapter adapter;
    private Menu menu;

    private PartInteractor partInteractor;
    private LoginNfcInteractor loginNfcInteractor;

    private ArrayList<String[]> list;
    private PartBaseInfoBean baseInfoBean;
    private PartSeatBean seatBean;

    private ArrayList<PartSeatBean.DatasBean> lists;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    protected void initData(Intent intent) {
        binding = (ActivityMainBinding) view;
        activitys.add(this);
        loginNfcInteractor = appComponent.getLoginNfcInteractor();
        partInteractor = appComponent.getPartInteractor();
        list = new ArrayList<>();
        Gson gson = new Gson();
        //拿去地区信息 BaseInfo.do
        downBaseInfo(gson);
        //拿去车位信息 Seat/BaseInfo.do
        downSeatBaseInfo(gson);




        String[] str1 = {"418","90","5211","粤X32K68","时间X天X时X分"};
        String[] str2 = {"410","90","5212","粤X32K68","时间X天X时X分"};
        String[] str3 = {"410","90","5213","粤X32K68","时间X天X时X分"};
        list.add(str1);
        list.add(str2);
        list.add(str3);




    }



    @Override
    protected void initView() {
        binding.mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new MainRecyclerAdapter(list,this);
        binding.mRecyclerView.setAdapter(adapter);

    }

    //数据
    private void downBaseInfo(final Gson gson) {
        partInteractor.partInfo("BaseInfo.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
                baseInfoBean = gson.fromJson(result,PartBaseInfoBean.class);
                String title = baseInfoBean.getData().getName();
                getSupportActionBar().setTitle(title);
                Log.i("Bean","BaseInfo:"+result);
            }
        });
    }

    private void downSeatBaseInfo(final Gson gson) {
        partInteractor.partInfo("Seat/BaseInfo.do", "", new BaseSubscriber<String>() {
            @Override
            protected void onSuccess(String result) {
               seatBean = gson.fromJson(result,PartSeatBean.class);
                lists = seatBean.getDatas();
                Log.i("Bean","Seat:"+result);
                Log.i("Bean","Seat_List:"+lists.size());
            }
        });
    }





    //菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.menu_user:
                intent = new Intent(this, Admin2Activity.class);
                startActivity(intent);
                break;
            case R.id.menu_msg:
                intent = new Intent(this, EscapeRecordActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    //数字键盘监听
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_HOME){
            onOptionsItemSelected(menu.findItem(R.id.menu_user));
        }
        if (keyCode == KeyEvent.KEYCODE_BACK){

        }
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //登出
        LogoutPostBean postBean = new LogoutPostBean(0.0,0.0);
        loginNfcInteractor.loginFnc("Logout.do",postBean.toString(), new BaseSubscriber<String>() {
                @Override
                protected void onSuccess(String result) {
                    Log.i("RetrofitLog","result:"+result);
                }});

    }
}
