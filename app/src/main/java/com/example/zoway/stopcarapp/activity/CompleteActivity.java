package com.example.zoway.stopcarapp.activity;

import android.content.Intent;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.databinding.ActivityAdminBinding;

/**
 * Created by Administrator on 2016/12/13.
 *  完成拍照的界面
 *
 */
public class CompleteActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_complete;
    }

    @Override
    protected void initData(Intent intent) {
        activitys.add(this);
    }

    @Override
    protected void initView() {

    }
}
