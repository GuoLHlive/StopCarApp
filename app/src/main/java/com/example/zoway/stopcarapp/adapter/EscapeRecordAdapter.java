package com.example.zoway.stopcarapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.databinding.EscapercordItemviewLayoutBinding;

/**
 * Created by Administrator on 2016/12/26.
 */
public class EscapeRecordAdapter extends RecyclerView.Adapter<EscapeRecordAdapter.HolderView> {



    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.escapercord_itemview_layout,parent,false);
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class HolderView extends RecyclerView.ViewHolder {

        private EscapercordItemviewLayoutBinding binding;

        public HolderView(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }


    }
}
