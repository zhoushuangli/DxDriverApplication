package com.daxi.dxdriverapplication.widget;


import android.app.Dialog;
import android.content.Context;

import com.daxi.dxdriverapplication.R;


/**
 * Created by zhh on 2017/4/10.
 */

public class OfflineMapUpdateProgressDialog extends Dialog {

    private NumberProgressBar numberProgressBar;

    public OfflineMapUpdateProgressDialog(Context context) {
        super(context, R.style.Custom_Progress);
        initLayout();
    }

    public OfflineMapUpdateProgressDialog(Context context, int theme) {
        super(context, R.style.Custom_Progress);
        initLayout();
    }

    private void initLayout() {
        this.setContentView(R.layout.offlinemap_update_progress_layout);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        numberProgressBar = (NumberProgressBar) findViewById(R.id.number_progress);
        this.setCanceledOnTouchOutside(false);//点击dialog背景部分不消失
        this.setCancelable(false);//dialog出现时，点击back键不消失
    }

    public void setProgress(int mProgress) {
        numberProgressBar.setProgress(mProgress);

    }
}
