package com.daxi.dxdriverapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.text.TextUtils.isEmpty;

public class MobileInfoUtil {

    private static String wifiTag = "wlan0";//无线标志，关闭wifi开关后获取不到
    private static String localTag = "eth0";//有线标志,插不插网线都能获取到

    public static String getDeviceId(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId;
    }
}
