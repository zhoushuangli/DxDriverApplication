package com.daxi.dxdriverapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.daxi.dxdriverapplication.activity.WelcomeActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
//            Intent thisIntent = new Intent(context, WelcomeActivity.class);
//            thisIntent.setAction("android.intent.action.MAIN");
//            thisIntent.addCategory("android.intent.category.LAUNCHER");
//            thisIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(thisIntent);
//        }
    }
}
