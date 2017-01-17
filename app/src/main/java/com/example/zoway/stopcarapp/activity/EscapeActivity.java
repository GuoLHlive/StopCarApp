package com.example.zoway.stopcarapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.zoway.stopcarapp.R;

/**
 * Created by Administrator on 2016/12/13.
 * 逃费界面
 *
 */
public class EscapeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_escape;
    }

    @Override
    protected void initData(Intent intent) {
        activitys.add(this);
    }

    @Override
    protected void initView() {

    }
}
