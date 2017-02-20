package com.example.zoway.stopcarapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.bean.UIsBean;
import com.example.zoway.stopcarapp.databinding.MainItemLayoutBinding;
import java.util.ArrayList;


/**
 * Created by Administrator on 2016/12/12.
 *  主界面adapter
 *
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>{


//    private Activity activity;
//
//    //未完成订单
//    private ParkingOrderListBean parkingOrderListBean;
//    //车位信息
//    private PartSeatBean partSeatBean;
//
//    public MainRecyclerAdapter(PartSeatBean partSeatBean, ParkingOrderListBean parkingOrderListBean,Activity activity) {
//        this.partSeatBean = partSeatBean;
//        this.activity = activity;
//        this.parkingOrderListBean = parkingOrderListBean;
//    }

    private UIsBean uIsBean;
    private ArrayList<UIsBean.UIBean> lists;

    public MainRecyclerAdapter(UIsBean uIsBean) {
        this.uIsBean = uIsBean;
        this.lists= uIsBean.getLists();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        //把所有点击事件变空(初始化)
//        holder.binding.mainItemView.setOnClickListener(null);
//        holder.binding.mainItemState.setVisibility(View.GONE);

//        //需要的数据
//        List<ParkingOrderListBean.DatasBean> datas = parkingOrderListBean.getDatas();
//        PartSeatBean.DatasBean partSeat = partSeatBean.getDatas().get(position);

        //车位状态修改
        holder.setPostion(position);
        holder.setData(uIsBean);
//        holder.setParkSeatBean(partSeatBean);

//
//        //未完成订单与对应停车ID号匹对
//        if (datas.size()!=0&&datas!=null){
//            for (int i=0;i<datas.size();i++){
//                ParkingOrderListBean.DatasBean datasBean = datas.get(i);
//                int parkSeatId = datasBean.getParkSeatId();
//                int sectionId = partSeat.getParkSeatId();
//                if (parkSeatId == sectionId){
//                    //匹对成功
//                    //改变状态
//                    if ("yes".equals(datasBean.getIsParking())){
//                        //没有支付
//                        holder.binding.mainItemState.setVisibility(View.VISIBLE);
//                        holder.binding.mainItemState.setText("停");
//                        if ("no_pay".equals(datasBean.getPayStatus())){
//                            partSeat.setIsParking("yes");
//                        }else {
//                            partSeat.setIsParking("yes_pay");
//                        }
//
//                    }else{
//                        partSeat.setIsParking("no");
//                        holder.binding.mainItemState.setVisibility(View.GONE);
//                    }
//                    //写入监听
//                    int parkingOrderId = datasBean.getParkingOrderId();
//                    holder.binding.mainItemView.setOnClickListener(new tackClick(parkingOrderId));
//                    //修改属性
//                    holder.binding.mainItemCarNumber.setText("粤X12345");
//                    holder.binding.mainItemComeTime.setText(LongTimeOrString.longTimeOrString(datasBean.getParkingTime()));
//                }
//            }
//        }





    }

    @Override
    public int getItemCount() {
        return this.lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MainItemLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

//        public void setParkSeatBean(PartSeatBean parkSeatBean){
//            binding.setPartSeatBean(parkSeatBean);
//        }

        public void setPostion(int postion){
            binding.setPostion(postion);
        }

        public void setData(UIsBean uIsBean){
            binding.setUisBean(uIsBean);
        }


    }

//    private class tackClick implements View.OnClickListener{
//
//        private int parkingOrderId;
//
//        public tackClick(int parkingOrderId) {
//            this.parkingOrderId = parkingOrderId;
//        }
//
//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(activity,PayActivity.class);
//            intent.putExtra("PayUI",new PayUIBean(false,parkingOrderId));
//            activity.startActivity(intent);
//    }
//    }

}
