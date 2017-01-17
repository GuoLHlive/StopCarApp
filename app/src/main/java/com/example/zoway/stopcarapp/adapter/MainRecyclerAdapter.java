package com.example.zoway.stopcarapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.activity.EscapeActivity;
import com.example.zoway.stopcarapp.activity.PayActivity;
import com.example.zoway.stopcarapp.databinding.MainItemLayoutBinding;
import com.example.zoway.stopcarapp.activity.TakeOcrPhotoActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/12.
 *  主界面adapter
 *
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>{

    private ArrayList<String[]> list;
    private Activity activity;

    public MainRecyclerAdapter(ArrayList<String[]> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String[] arrayString = list.get(position);
        holder.setM(arrayString[0]);
        holder.setD(arrayString[1]);
        holder.setCarPlace(arrayString[2]);
        holder.setCarNumber(arrayString[3]);
        holder.setComeTime(arrayString[4]);
        holder.binding.mainItemView.setOnClickListener(new tackClick(holder,activity));
        if (position==1){
            holder.binding.mainItemState.setText("结");
        }
        if (position==2){
            holder.binding.mainItemState.setText("逃");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MainItemLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setM(String m){
            binding.setM("M: "+m);
        }
        public void setD(String d){
            binding.setD("D: "+d);
        }
        //车牌
        public void setCarNumber(String carNumber){
            binding.setCarNumber(carNumber);
        }
        //车位
        public void setCarPlace(String carPlace){
            binding.setCarPlace(carPlace);
        }
        //进场时间
        public void setComeTime(String time){
            binding.setComeTime(time);
        }
    }

    private class tackClick implements View.OnClickListener{

        ViewHolder viewHolder;
        Activity activity;

        public tackClick(ViewHolder viewHolder, Activity activity) {
            this.viewHolder = viewHolder;
            this.activity = activity;
        }

        @Override
        public void onClick(View view) {
            TextView mTv = viewHolder.binding.mainItemState;

            String str = mTv.getText().toString().trim();
            Intent intent = null;
            switch (str){
                case "入":
                    viewHolder.binding.mainItemState.setText("停");
                    notifyDataSetChanged();
                    break;
                case "停":
//                    intent= new Intent(activity, PayActivity.class);

                    intent = new Intent(activity, TakeOcrPhotoActivity.class);
                    break;
                case "逃":
                    intent = new Intent(activity, EscapeActivity.class);
                    break;
                case "结":
                    intent = new Intent(activity, PayActivity.class);
                    break;
            }
            if (intent!=null){
                activity.startActivity(intent);
            }
    }
    }

}
