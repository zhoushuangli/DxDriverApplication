package com.daxi.dxdriverapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daxi.dxdriverapplication.R;


public class ToastUtil {
    public static void showToast(final Activity activity, final String message) {
        if ("main".equals(Thread.currentThread().getName())) {
            Log.e("ToastUtil", "在主线程");
            show(activity,message);
          //  Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("ToastUtil", "不在主线程");

                    show(activity,message);
                 //   Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param
     */
    public static void show(Context context,CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//        View view = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
//        TextView mTvToast = (TextView) view.findViewById(R.id.toast_tv);
//        toast.setGravity(Gravity.BOTTOM, 0, 350);
//        mTvToast.setTextSize(18);
//        mTvToast.setTextColor(Color.WHITE);
//        mTvToast.setText(message);
//        toast.setView(view);
//        toast.show();
    }
}
