package com.example.zoway.stopcarapp.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.adapter.MyRecyclerViewLinear;
import com.example.zoway.stopcarapp.adapter.SettingRecyclerViewAdapter;
import com.example.zoway.stopcarapp.databinding.ActivitySettingBinding;
import com.example.zoway.stopcarapp.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/15.\
 * 设置界面
 *
 */
public class SettingActivity extends BaseActivity {

    private ActivitySettingBinding binding;

    private SettingRecyclerViewAdapter adapter;

    private Map<String,Object> map;

    private String[] stringArray;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData(Intent intent) {
        binding = (ActivitySettingBinding) view;
        if (map == null){
            map = new HashMap<>();
            stringArray = getResources().getStringArray(R.array.ArrayStringKeyName);
            for (int i=0;i<stringArray.length;i++){
                if (i!=2){
                    map.put(stringArray[i], SharedPreferencesUtils.getParam(this,stringArray[i],""));
                }else {
                    map.put(stringArray[i], SharedPreferencesUtils.getParam(this,stringArray[i],1));
                }
            }

        }

    }

    @Override
    protected void initView() {
        adapter = new SettingRecyclerViewAdapter(stringArray,map);
        binding.settingRecycler.setLayoutManager(new MyRecyclerViewLinear(getApplicationContext()));
        binding.settingRecycler.setAdapter(adapter);
        //两个check的处理
        SetCheck();
        binding.settingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                map = null;
                finish();
            }
        });
        binding.settingSure.setOnClickListener(new View.OnClickListener() {
            Context context;

            @Override
            public void onClick(View view) {
                context = getApplicationContext();
                for (int i = 0; i<stringArray.length;i++){
                    if (i!=2){
                        String text = (String) map.get(stringArray[i]);
                        SharedPreferencesUtils.setParam(context,stringArray[i],text);
                    }else {
                        Integer port = Integer.valueOf((String) map.get(stringArray[i]));
                        SharedPreferencesUtils.setParam(context,stringArray[i],port);
                    }
                }
                boolean enable = binding.settingCheckEnable.isChecked();
                boolean show = binding.settingCheckShow.isChecked();
                SharedPreferencesUtils.setParam(context, "enableEditSetting", enable);
                SharedPreferencesUtils.setParam(context, "showDebugToolbar", show);
                map.clear();
                map = null;
                finish();
            }
        });
    }

    private void SetCheck() {
        boolean enableEditSetting = (boolean) SharedPreferencesUtils.getParam(this, "enableEditSetting", false);
        boolean showDebugToolbar = (boolean) SharedPreferencesUtils.getParam(this, "showDebugToolbar", false);
        binding.settingCheckEnable.setChecked(enableEditSetting);
        binding.settingCheckShow.setChecked(showDebugToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivityA","退出设置模式");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }
}
