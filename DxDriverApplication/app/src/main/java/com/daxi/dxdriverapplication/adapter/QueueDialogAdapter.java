package com.daxi.dxdriverapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.base.BaseGoodAdapter;

import com.daxi.dxdriverapplication.bean.BindCarInfo;
import com.daxi.dxdriverapplication.widget.VerticalMarqueeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QueueDialogAdapter extends BaseGoodAdapter<QueueDialogAdapter.ViewHolder> {
    private List<BindCarInfo.DataBean.ParBean> mList;


    public QueueDialogAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;

    @Override
    public void whenBindViewHolder(ViewHolder holder, int position) {
        //跑马灯
//        holder.mLocationTv.setSelected(true);
        holder.setIsRecyclable(false);
        holder.mNumTv.setText(String.valueOf(position + 1) + "");
        holder.mCarNumTv.setText(mList.get(position).getCarNumber() + "#");
        if (mList.get(position).getIsSelect().equals("1")) {
            holder.mNumTv.setTextColor(ContextCompat.getColor(mContext, R.color.pir_sensitivity_hint));
            holder.mCarNumTv.setTextColor(ContextCompat.getColor(mContext, R.color.pir_sensitivity_hint));
            holder.mLocationTv.setTextColor(ContextCompat.getColor(mContext, R.color.pir_sensitivity_hint));
        }
        String partName = mList.get(position).getPartName();
        holder.mLocationTv.setText(partName);
//        if (partName.length()>40){
//            holder.mLocationTv.setTextSize(16);
//            holder.mLocationTv.setGravity(Gravity.TOP);
//            holder.mLocationTv.setGravity(Gravity.LEFT);
//        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_queue, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setList(List<BindCarInfo.DataBean.ParBean> par) {
        this.mList = par;
        Log.e("zuixin", JSON.toJSONString(par));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNumTv;
        private TextView mCarNumTv;
        private TextView mLocationTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(@NonNull final View itemView) {
            mNumTv = (TextView) itemView.findViewById(R.id.tv_num);
            mCarNumTv = (TextView) itemView.findViewById(R.id.tv_car_num);
            mLocationTv = (TextView) itemView.findViewById(R.id.tv_location);
        }
    }
}
