package com.example.zoway.stopcarapp.activity;

import android.content.Intent;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.adapter.EscapeRecordAdapter;
import com.example.zoway.stopcarapp.databinding.ActivityEscaperecordBinding;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/16.
 */

/**
 * 逃票记录界面
 *
 */
public class EscapeRecordActivity extends BaseActivity {

    private ActivityEscaperecordBinding binding;
    private EscapeRecordAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_escaperecord;
    }

    @Override
    protected void initData(Intent intent) {
        activitys.add(this);
        binding = (ActivityEscaperecordBinding) view;

    }

    @Override
    protected void initView() {
        adapter = new EscapeRecordAdapter();
        binding.escaperecordRecycler.setAdapter(adapter);
    }
}
