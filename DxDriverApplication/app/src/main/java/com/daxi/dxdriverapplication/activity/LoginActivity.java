package com.daxi.dxdriverapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.daxi.dxdriverapplication.BuildConfig;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.adapter.MyAdapter;
import com.daxi.dxdriverapplication.base.BaseActivity;
import com.daxi.dxdriverapplication.base.BaseGoodAdapter;
import com.daxi.dxdriverapplication.bean.AccountNumberBean;
import com.daxi.dxdriverapplication.bean.UserInfoBean;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.net.JsonCallback;
import com.daxi.dxdriverapplication.utils.MobileInfoUtil;
import com.daxi.dxdriverapplication.utils.SharedPreferenceUtil;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.daxi.dxdriverapplication.widget.LoadingDialog;
import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private Button mBut;
    private boolean isNeedCheck = true;
    private static final int PERMISSION_REQUEST_CODE = 0;
    private LoadingDialog loadingDialog;
    private String location;
    private String address;
    private FrameLayout mFrgemnt;
    private Fragment fragmentw;
    private FragmentTransaction transaction;
    private AlertDialog alertDialog;
    private Button mLoginBut;
    private TextView mTitleTv;
    private EditText mAccountEd;
    private EditText mPwdEd;
    private ConstraintLayout mLoginCl;
    private Button mSetBut;
    private ConstraintLayout mListIv;
    private List<String> accountListSp;
    private CustomPopWindow customPopWindow;
    private TextView mTextView;
    private ImageView mImageView2;
    private ConstraintLayout mConstraintLayout;
    private AlertDialog aliertDialog;
    private List<String> pwdListSp;
    private TextView mVersion;
    private ConstraintLayout mGnCon;
    private ImageView mGoneIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public void initData() {
        String account = SharedPreferenceUtil.getString(this, "account");
        String pwd = SharedPreferenceUtil.getString(this, "pwd");
        if (!TextUtils.isEmpty(account)) {
            accountListSp = JSON.parseArray(account, String.class);
            pwdListSp = JSON.parseArray(pwd, String.class);
            mListIv.setVisibility(View.VISIBLE);
        } else {
            mListIv.setVisibility(View.GONE);
        }
    }



    @Override
    public void initView() {
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mAccountEd = (EditText) findViewById(R.id.ed_account);
        mPwdEd = (EditText) findViewById(R.id.ed_pwd);
        mLoginBut = (Button) findViewById(R.id.but_login);
        mLoginBut.setOnClickListener(this);
        mLoginCl = (ConstraintLayout) findViewById(R.id.cl_login);
        mListIv = (ConstraintLayout) findViewById(R.id.iv_list);
        mListIv.setOnClickListener(this);
        mTitleTv.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setOnClickListener(this);
        mImageView2 = (ImageView) findViewById(R.id.imageView2);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        mVersion = (TextView) findViewById(R.id.version);
        mVersion.setText("版本号:  " + BuildConfig.VERSION_NAME);
        mVersion.setOnClickListener(this);
        mLoginCl.setOnClickListener(this);
        mGnCon = (ConstraintLayout) findViewById(R.id.con_gn);
        mGnCon.setOnClickListener(this);
        mGoneIv = (ImageView) findViewById(R.id.iv_gone);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_login:// TODO 19/10/26
                // startActivity(DriverHomeActivity.class);
                accountNumberLogin();
                break;
            case R.id.iv_list:// TODO 19/10/29
                showPopListView();
                break;
            case R.id.tv_title:// TODO 19/10/29
                break;
            case R.id.version:// TODO 19/11/04
                 showSetting();
                //  aliertDialog.dismiss();
               // startActivity(new Intent(this, SetIpActivity.class));
                break;
            case R.id.cl_login:// TODO 20/09/27
                break;
            case R.id.con_gn:// TODO 20/09/27

                if (isOpenPassword) {
                    mGoneIv.setBackground(getResources().getDrawable(R.drawable.login_ic_eye_press));
                    mPwdEd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isOpenPassword = false;
                } else {
                    mGoneIv.setBackground(getResources().getDrawable(R.drawable.login_ic_eye_normal));
                    mPwdEd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isOpenPassword = true;
                }
                break;
            default:
                break;
        }
    }
    private boolean isOpenPassword = false;//用于密码是否可见

    private void showSetting() {
        aliertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.ActionSheetDialogStyle).create();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_set, null);
        aliertDialog.setView(view);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aliertDialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        aliertDialog.show();
    }

    /**
     * //手机号登录
     ***/
    private void accountNumberLogin() {
        yincangkey();
        HttpParams httpParams = new HttpParams();
        final String zhanghu = mAccountEd.getText().toString().trim();
        final String mima = mPwdEd.getText().toString().trim();
        if (TextUtils.isEmpty(zhanghu)) {
            ToastUtil.show(this, "请输入账号");
            return;
        }
        if (TextUtils.isEmpty(mima)) {
            ToastUtil.show(this, "请输入密码");
            return;
        }
        httpParams.put("phone", zhanghu);
        httpParams.put("password", mima);
        // httpParams.put("password", "dxIT_Gc");
        httpParams.put("longLife", "true");
        httpParams.put("verifyCode", "");
        httpParams.put("registrationID", MobileInfoUtil.getDeviceId(this));
        //  getUserUrl();


        OkGo.<AccountNumberBean>post(StringUtils.join(getUserUrl(), HttpConstant.LOGIN_PWD)).
                params(httpParams).
                execute(new JsonCallback<AccountNumberBean>(AccountNumberBean.class, this) {
                    @Override
                    public void onSuccess(Response<AccountNumberBean> response) {//如果包含
                        String token = response.body().getData().getToken();
                        SharedPreferenceUtil.putString(LoginActivity.this, HttpConstant.TOKEN, token);
                        ArrayList<String> accountList = new ArrayList<>();
                        ArrayList<String> pwdList = new ArrayList<>();
                        if (LoginActivity.this.accountListSp != null && !LoginActivity.this.accountListSp.contains(zhanghu)) {//不变
                            LoginActivity.this.accountListSp.add(zhanghu);
                            LoginActivity.this.pwdListSp.add(mima);
                        } else {//第一次
                            accountList.add(zhanghu);
                            pwdList.add(mima);
                        }
                        if (accountList != null && accountListSp != null && LoginActivity.this.accountListSp.size() > 0) {
                            String account = JSONObject.toJSONString(accountListSp);
                            String pwdString = JSONObject.toJSONString(pwdListSp);
                            SharedPreferenceUtil.putString(LoginActivity.this, "account", account);
                            SharedPreferenceUtil.putString(LoginActivity.this, "pwd", pwdString);
                        } else {
                            String account = JSONObject.toJSONString(accountList);
                            String pwdString = JSONObject.toJSONString(pwdList);
                            SharedPreferenceUtil.putString(LoginActivity.this, "account", account);
                            SharedPreferenceUtil.putString(LoginActivity.this, "pwd", pwdString);
                        }
                        getUserinFo();
                    }
                });

    }

    private void getUserinFo() {
        OkGo.<UserInfoBean>get(StringUtils.join(getUserUrl(), HttpConstant.USER_INFO_APP)).params(HttpConstant.TOKEN, getToken()).execute(new JsonCallback<UserInfoBean>(UserInfoBean.class, this) {
            @Override
            public void onSuccess(Response<UserInfoBean> response) {
                UserInfoBean body = response.body();
                UserInfoBean.DataBean data = body.getData();
                String userName = data.getUserName();
                String jobName = data.getJobName();
                String phone = data.getPhone();
                String simpleName = data.getSimpleName();
                SharedPreferenceUtil.putString(LoginActivity.this, "simpleName", simpleName);
                SharedPreferenceUtil.putString(LoginActivity.this, "userName", userName);
                SharedPreferenceUtil.putString(LoginActivity.this, "jobName", jobName);
                SharedPreferenceUtil.putString(LoginActivity.this, "phone", phone);
                int mixAttribute = data.getMixAttribute();
                if (mixAttribute == 2 || mixAttribute == 5) { //2运输车司机  5搅拌车司机
                    startActivity(DriverHome3Activity.class);
                    finish();
                } else if (mixAttribute == 1 || mixAttribute == 3) { //3调度员 1站长
                    finish();
                    startActivity(HomeActivity.class);
                } else {
                    ToastUtil.show(LoginActivity.this, "无操作权限");
                }

            }
        });
    }

    /**
     * 键盘隐藏
     ***/
    //隐藏键盘
    public void yincangkey() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void showPopListView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_list, null);
        //处理popWindow 显示内容
        handleListView(contentView);
        //创建并显示popWindow
        customPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(519, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .create()
                .showAsDropDown(mAccountEd, 0, 0);
    }

    private void handleListView(View contentView) {
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.login_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        MyAdapter adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setData(accountListSp);
        adapter.setSelectMode(BaseGoodAdapter.SelectMode.CLICK);
        adapter.setOnItemClickListener(new BaseGoodAdapter.OnItemClickListener() {
            @Override
            public void onClicked(int itemPosition, View view) {
                String account = accountListSp.get(itemPosition);
                String pwd = pwdListSp.get(itemPosition);
                mAccountEd.setText(account);
                mPwdEd.setText(pwd);
                customPopWindow.dissmiss();
            }
        });
    }
}
