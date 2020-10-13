package com.daxi.dxdriverapplication.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.base.BaseActivity;
import com.daxi.dxdriverapplication.config.Constant;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.net.JsonCallback;
import com.daxi.dxdriverapplication.utils.SharedPreferenceUtil;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends BaseActivity implements View.OnClickListener {

    private Button mButBindAgain;
    private Button mOutBut;
    private TextView mTextTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
    }

    @Override
    public void initView() {

        mButBindAgain = (Button) findViewById(R.id.again_but_bind);
        mButBindAgain.setOnClickListener(this);
        mOutBut = (Button) findViewById(R.id.but_out);
        mOutBut.setOnClickListener(this);
        mTextTv = (TextView) findViewById(R.id.tv_text_value);
    }

    @Override
    public void initData() {
        String data = getIntent().getStringExtra("data");
        String s = data + "#";
        SpannableString spanString = new SpannableString(s);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(42);
////        Spannable.SPAN_INCLUSIVE_EXCLUSIVE：前面包括，后面不包括。
        StyleSpan span2 = new StyleSpan(Typeface.BOLD);
        spanString.setSpan(span, 0, s.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanString.setSpan(span2, 0, s.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mTextTv.setText(spanString);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.again_but_bind:
                startActivity(HomeActivity.class);
                finish();
                // TODO 19/10/28
                break;
            case R.id.but_out:
                // TODO 19/10/28
                outLoginRequest();
                break;
            default:
                break;
        }
    }

    private void outLoginRequest() {
        OkGo.<String>post(StringUtils.join(getUserUrl(), HttpConstant.OUT_ACCOUNT)).
                params(HttpConstant.TOKEN, SharedPreferenceUtil.getString(ResultActivity.this, HttpConstant.TOKEN)).
                execute(new JsonCallback<String>(String.class, ResultActivity.this) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            String result = jsonObject.getString(HttpConstant.RESULT_CODE);
                            String message = jsonObject.getString(HttpConstant.RESULT_MAG);
                            if (HttpConstant.SUCCESS_CODE.equals(result)) {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent, LoginActivity.class);
                                SharedPreferenceUtil.putString(ResultActivity.this, HttpConstant.TOKEN, "");

                            } else {
                                ToastUtil.showToast(ResultActivity.this, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showToast(ResultActivity.this, HttpConstant.JSON_EXCEPTION);
                        }
                    }
                });

    }

}
