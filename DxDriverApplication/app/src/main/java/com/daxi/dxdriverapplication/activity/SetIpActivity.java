package com.daxi.dxdriverapplication.activity;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.daxi.dxdriverapplication.R;

public class SetIpActivity extends AppCompatActivity {

    private WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ip);
        initView();
    }

    private void initView() {
        mWeb = (WebView) findViewById(R.id.webview);
        mWeb.loadUrl("http://itest.dx185.com/ip.html");
    }
}