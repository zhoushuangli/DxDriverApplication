package com.daxi.dxdriverapplication.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.base.BaseGoodAdapter;
import com.daxi.dxdriverapplication.bean.CarInfoBean;
import com.daxi.dxdriverapplication.utils.MobileInfoUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class BindCarAdapter extends BaseGoodAdapter<BindCarAdapter.ViewHolder> {
    private List<CarInfoBean.DataBean.ParBean> mList;

    public BindCarAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;

    @Override
    public void whenBindViewHolder(ViewHolder holder, int position) {
        String carNumber = mList.get(position).getCarNumber();
        SpannableString spanString = new SpannableString(carNumber + "#");
        //    AbsoluteSizeSpan span = new AbsoluteSizeSpan(40);
////        Spannable.SPAN_INCLUSIVE_EXCLUSIVE：前面包括，后面不包括。
        // StyleSpan span2 = new StyleSpan(Typeface.BOLD);
//        spanString.setSpan(span, 0, String.valueOf(position + 1).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spanString.setSpan(span2, 0, String.valueOf(position + 1).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.textView.setText(spanString);
        int isBinding = mList.get(position).getIsBinding();
        String bindingDevice = mList.get(position).getBindingDevice();
        if (isBinding == 1 && bindingDevice.equals(MobileInfoUtil.getDeviceId(mContext))) { //绑定的是当前车辆
            holder.textView.setBackgroundResource(R.drawable.select_button);
            holder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.tv_1bc088));
        } else if (isBinding == 1) {//所有绑定的车
            holder.textView.setBackgroundResource(R.drawable.item_bind_bg);
            holder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.tv_8ca2b1));
        } else  {
            holder.textView.setBackgroundResource(R.drawable.item_but_no_bg);
            holder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.white90));
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bind, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setList(List<CarInfoBean.DataBean.ParBean> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.but_bind);
        }
    }
}
