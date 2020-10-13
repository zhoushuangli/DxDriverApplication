package com.daxi.dxdriverapplication.base;

import android.widget.FrameLayout;


import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.daxi.dxdriverapplication.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BaseBottomSheetDialog extends BottomSheetDialogFragment {
    private FrameLayout bottomSheet;
    public BottomSheetBehavior<FrameLayout> behavior;

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public int getTheme() {
        return R.style.ActionSheetDialogStyle;
    }

    protected int getHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }


}