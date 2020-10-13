package com.daxi.dxdriverapplication.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.base.BaseBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CreatOrderDialog extends BaseBottomSheetDialog {
    private FrameLayout bottomSheet;
    @Override
    public void onStart() {
        super.onStart();
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
                layoutParams.height = getHeight();


                bottomSheet.setLayoutParams(layoutParams);
                behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setPeekHeight(getHeight());
                // 初始为展开状态
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_no_task2, container);

        return view;
    }
}
