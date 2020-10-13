/*
 * Apache DropDownView
 *
 * Copyright 2017 The Apache Software Foundation
 * This product includes software developed at
 * The Apache Software Foundation (http://www.apache.org/).
 */

package com.daxi.dxdriverapplication.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.adapter.DropDownAdapter;
import com.daxi.dxdriverapplication.widget.DropDownView;

public class TestMainActivity extends AppCompatActivity {

    public static final int NUM_OF_STANDS = 4;
    private RecyclerView recyclerView;
    private DropDownAdapter adapter;
    private int selectedStandId;
    private String[] waitTimes;
    private TextView selectedStandTitleTV;
    private TextView selectedStandStatusTV;
    private View collapsedView;
    private DropDownView dropDownView;
    private View expandedView;
    private ImageView headerChevronIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        setupViews();
        setupList();

        dropDownView.setHeaderView(collapsedView);
        dropDownView.setExpandedView(expandedView);
        dropDownView.setDropDownListener(dropDownListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        headerChevronIV.setRotation(dropDownView.isExpanded()
                ? 180f : 0f);
    }

    public void setStandStateWithId(String waitTime, int standId) {
        if (standId >= 0 && standId < NUM_OF_STANDS) {
            waitTimes[standId] = waitTime;
            adapter.notifyItemChanged(standId);
        }

        // Should update currently selected stand wait time as well
        if (selectedStandId == standId) {
            selectedStandStatusTV.setText(waitTime);
        }
    }

    private void setupList() {
        waitTimes = new String[] {"3 minute wait", "Closed", "No wait time", "10 minute wait"};
        viewActions.setSelectedStand(1);
        adapter = new DropDownAdapter(viewActions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < waitTimes.length; i++) {
            setStandStateWithId(waitTimes[i], i);
        }
    }

    private void setupViews() {
        dropDownView = (DropDownView) findViewById(R.id.drop_down_view);
        collapsedView = LayoutInflater.from(this).inflate(R.layout.view_my_drop_down_header, null, false);
        expandedView = LayoutInflater.from(this).inflate(R.layout.view_my_drop_down_expanded, null, false);

        selectedStandTitleTV = (TextView) collapsedView.findViewById(R.id.selected_stand_title);
        selectedStandStatusTV = (TextView) collapsedView.findViewById(R.id.selected_stand_status);
        recyclerView = (RecyclerView) expandedView.findViewById(R.id.recyclerView);
        headerChevronIV = (ImageView) collapsedView.findViewById(R.id.chevron_image);
    }

    private final DropDownView.DropDownListener dropDownListener = new DropDownView.DropDownListener() {
        @Override
        public void onExpandDropDown() {
            adapter.notifyDataSetChanged();
            ObjectAnimator.ofFloat(headerChevronIV, View.ROTATION.getName(), 180).start();
        }

        @Override
        public void onCollapseDropDown() {
            ObjectAnimator.ofFloat(headerChevronIV, View.ROTATION.getName(), -180, 0).start();
        }
    };

    private final DropDownAdapter.ViewActions viewActions = new DropDownAdapter.ViewActions() {
        @Override
        public void collapseDropDown() {
            dropDownView.collapseDropDown();
        }

        @Override
        public void setSelectedStand(int standId) {
            selectedStandTitleTV.setText(getStandTitle(standId ));
            selectedStandStatusTV.setText(getStandStatus(standId));
            selectedStandId = standId;
        }

        @Override
        public String getStandTitle(int standId) {
            String title = "";
            switch (standId) {
                case 0:
                    title = "www";
                    break;
                case 1:
                    title ="2332";
                    break;
                case 2:
                    title = "23232";
                    break;
                case 3:
                    title = "232323";
                    break;
            }
            return title;
        }

        @Override
        public String getStandStatus(int standId) {
            return waitTimes[standId] != null ? waitTimes[standId] : "";
        }

        @Override
        public int getSelectedStand() {
            return selectedStandId;
        }
    };

}
