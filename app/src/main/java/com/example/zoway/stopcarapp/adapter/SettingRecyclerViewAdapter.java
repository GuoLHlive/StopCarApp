package com.example.zoway.stopcarapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zoway.stopcarapp.R;
import com.example.zoway.stopcarapp.databinding.SettingItemviewLayoutBinding;

import java.util.Map;


/**
 * Created by Administrator on 2016/12/12.
 * 设置界面的adapter
 *
 */
public class SettingRecyclerViewAdapter extends RecyclerView.Adapter<SettingRecyclerViewAdapter.ViewHolder> {

    private String[] keyNames;
    private Map<String, Object> map;

    public SettingRecyclerViewAdapter(String[] keyNames, Map<String, Object> map) {
        this.keyNames = keyNames;
        this.map = map;
    }

    @Override
    public SettingRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_itemview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindKeyName(keyNames[position]+" :");

        if (position!=2){
            String txt = (String) map.get(keyNames[position]);
            holder.bindkeContent(txt);
        }else {
            Integer txt = (Integer) map.get(keyNames[position]);
            holder.bindkeContent(String.valueOf(txt));


        }
        holder.binding.settingEdit.setTag(position);
        holder.binding.settingEdit.addTextChangedListener(new TextSwitcher(map,holder,keyNames));

    }

    @Override
    public int getItemCount() {
        return keyNames.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SettingItemviewLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bindKeyName(String keyName){
            binding.setKeyName(keyName);
        }
        public void bindkeContent(String keContent){
            binding.setKeyContent(keContent);
        }
    }

    //自定义EditText的监听类
    class TextSwitcher implements TextWatcher {

        private Map<String, Object> map;
        private ViewHolder holder;
        private String[] keyName;

        public TextSwitcher(Map<String, Object> map, ViewHolder holder, String[] keyNames) {
            this.holder = holder;
            this.map = map;
            this.keyName = keyNames;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable!=null){
                String txt = holder.binding.settingEdit.getText().toString();
                int position = (int) holder.binding.settingEdit.getTag();
                map.put(keyName[position],txt);
            }
        }
    }

}
