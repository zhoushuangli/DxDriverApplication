/*
 * Apache DropDownView
 *
 * Copyright 2017 The Apache Software Foundation
 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).
 */

package com.daxi.dxdriverapplication.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.activity.TestMainActivity;

/**
 * @author Anthony Fermin (Fuzz)
 */
public class DropDownAdapter extends RecyclerView.Adapter<DropDownAdapter.StandViewHolder> {

    private final ViewActions actionViewDelegate;

    public DropDownAdapter(final ViewActions actionViewDelegate) {
        this.actionViewDelegate = actionViewDelegate;
    }

    @Override
    public StandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stand_drop_down, parent, false);
        return new StandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StandViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return TestMainActivity.NUM_OF_STANDS;
    }

    class StandViewHolder extends RecyclerView.ViewHolder {

        private final TextView standTitleTV;
        private final TextView standStatusTV;

        public StandViewHolder(View itemView) {
            super(itemView);
            standTitleTV = (TextView) itemView.findViewById(R.id.cell_stand_title);
            standStatusTV = (TextView) itemView.findViewById(R.id.cell_stand_status);
            itemView.setBackgroundDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.stand_drop_down_selector));
            itemView.setOnClickListener(standViewItemClickListener);
        }

        public void bind(int position) {
            standTitleTV.setText(actionViewDelegate.getStandTitle(position));
            standStatusTV.setText(actionViewDelegate.getStandStatus(position));
            itemView.setSelected(actionViewDelegate.getSelectedStand() == position);
        }

        private final View.OnClickListener standViewItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastSelectedPosition = actionViewDelegate.getSelectedStand();
                actionViewDelegate.setSelectedStand(getAdapterPosition());
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(getAdapterPosition());
                actionViewDelegate.collapseDropDown();
            }
        };
    }

    public interface ViewActions {
        void collapseDropDown();
        void setSelectedStand(int standId);
        String getStandTitle(int standId);
        String getStandStatus(int standId);
        int getSelectedStand();
    }
}

