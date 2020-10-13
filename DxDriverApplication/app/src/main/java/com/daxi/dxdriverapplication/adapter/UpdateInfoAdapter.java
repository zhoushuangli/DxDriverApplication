package com.daxi.dxdriverapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.activity.WelcomeActivity;
import com.daxi.dxdriverapplication.base.BaseGoodAdapter;

import java.util.List;

public class UpdateInfoAdapter extends BaseGoodAdapter<UpdateInfoAdapter.ViewHolder> {
    private final WelcomeActivity context;

    private List<String> mlist;

    public UpdateInfoAdapter(WelcomeActivity welcomeActivity) {
        this.context = welcomeActivity;
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_update_info, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void whenBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(position + 1) +"."+ mlist.get(position));
    }

    public void setList(List<String> fixInfo) {
        this.mlist = fixInfo;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_info);

        }
    }
}
