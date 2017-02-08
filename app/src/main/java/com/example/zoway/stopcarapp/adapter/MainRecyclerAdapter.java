package com.example.zoway.stopcarapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.activity.EscapeActivity;
import com.example.zoway.stopcarapp.activity.PayActivity;
import com.example.zoway.stopcarapp.bean.ParkingOrderListBean;
import com.example.zoway.stopcarapp.bean.PartSeatBean;
import com.example.zoway.stopcarapp.bean.PayUIBean;
import com.example.zoway.stopcarapp.databinding.MainItemLayoutBinding;
import com.example.zoway.stopcarapp.activity.TakeOcrPhotoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 *  主界面adapter
 *
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>{

    //模拟数据
    private ArrayList<String[]> list;
    private Activity activity;
    //车位信息
    private ArrayList<PartSeatBean.DatasBean> lists;
    //未完成订单
    private List<ParkingOrderListBean.DatasBean> datas;

    public MainRecyclerAdapter(ArrayList<PartSeatBean.DatasBean> lists,ArrayList<String[]> list, List<ParkingOrderListBean.DatasBean> datas,Activity activity) {
        this.lists = lists;
        this.list = list;
        this.activity = activity;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String[] arrayString = list.get(position);

        PartSeatBean.DatasBean datasBean = lists.get(position);

        ParkingOrderListBean.DatasBean parkingBean = null;
        if (position<1){
            parkingBean = datas.get(position);
            String payStatus = parkingBean.getPayStatus();
            if ("no_pay".equals(payStatus)){
                holder.binding.mainItemState.setText("停");
            }else {
                holder.binding.mainItemState.setText("?");
            }
        }else {
            holder.binding.mainItemState.setText("入");
        }

        holder.binding.mainItemView.setOnClickListener(new tackClick(holder,activity,parkingBean));


        //车位状态修改
        boolean is = isParking(datasBean);
        holder.setBackgroud(is);


        holder.setM(arrayString[0]);
        holder.setD(arrayString[1]);
        holder.setCarPlace(datasBean.getSeatNo());
        holder.setCarNumber(arrayString[3]);
        holder.setComeTime(arrayString[4]);



    }

    private boolean isParking(PartSeatBean.DatasBean datasBean) {
        String isParking = datasBean.getIsParking();
        boolean is = false;
        if ("no".equals(isParking)){
            is = false;
        }else {
            is = true;
        }
        return is;
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

        public void setBackgroud(boolean is){
            if (is){
                binding.mainItemView.setBackgroundColor(activity.getResources().getColor(R.color.pig));
            }else {
                binding.mainItemView.setBackgroundColor(activity.getResources().getColor(R.color.green));
            }
        }


    }

    private class tackClick implements View.OnClickListener{

        private ViewHolder viewHolder;
        private Activity activity;
        private ParkingOrderListBean.DatasBean parkingBean;

        public tackClick(ViewHolder viewHolder, Activity activity) {
            this.viewHolder = viewHolder;
            this.activity = activity;
        }

        public tackClick(ViewHolder viewHolder, Activity activity, ParkingOrderListBean.DatasBean parkingBean) {
            this.viewHolder = viewHolder;
            this.activity = activity;
            this.parkingBean = parkingBean;
        }

        @Override
        public void onClick(View view) {
            Intent intent = null;
            if (parkingBean!=null){
                int parkingOrderId = parkingBean.getParkingOrderId();
                intent = new Intent(activity,PayActivity.class);
                intent.putExtra("PayUI",new PayUIBean(false,parkingOrderId));
            }else {
                intent = new Intent(activity,TakeOcrPhotoActivity.class);
            }
            if (intent!=null){
                activity.startActivity(intent);
            }
    }
    }

}
