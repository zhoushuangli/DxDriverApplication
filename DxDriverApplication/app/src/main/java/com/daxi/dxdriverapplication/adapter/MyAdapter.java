package com.daxi.dxdriverapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.base.BaseApplication;
import com.daxi.dxdriverapplication.base.BaseGoodAdapter;

import java.util.List;

public class MyAdapter extends BaseGoodAdapter<MyAdapter.ViewHolder> {
    private List<String> account;

    @Override
    public void whenBindViewHolder(ViewHolder holder, int position) {
       holder.textView.setText(account.get(position));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_login, parent, false);
        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return account==null?0:account.size();
    }

    public void setData(List<String> strings) {
        this.account=strings;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             textView = itemView.findViewById(R.id.tv_account);
        }
    }
}
