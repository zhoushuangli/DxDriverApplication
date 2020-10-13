package com.daxi.dxdriverapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.daxi.dxdriverapplication.R;
import com.mingle.widget.LoadingView;
import com.wang.avi.AVLoadingIndicatorView;


public class LoadingDialog extends Dialog {

    private TextView mMessage;
    private RelativeLayout mLoadingbg;
    private LoadingView mLoadView;

    public LoadingDialog(@NonNull Context context, int myDialog) {
        super(context,myDialog);
        //  getWindow().setDimAmount(0f);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loading_layout);
        mMessage = (TextView) findViewById(R.id.message);
    }




    /**
     * 自定义主题及布局的构造方法 * @param context * @param theme// 去除顶部蓝色线条
     */
    /**
     * 为加载进度个对话框设置不同的提示消息 * * @param message 给用户展示的提示信息 * @return build模式设计，可以链式调用
     */
    public LoadingDialog setMessage(String message) {
        mMessage.setText(message);
        return this;
    }

//    /*** * 设置loading背景色 * @param Colorbg * @return */
//    public LoadingDialog setLoadingBg(int Colorbg) {
//        mLoadingbg.setBackgroundColor(Colorbg);
//        return this;
//    }

}


