package com.daxi.dxdriverapplication.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.adapter.UpdateInfoAdapter;
import com.daxi.dxdriverapplication.base.BaseActivity;

import com.daxi.dxdriverapplication.bean.ModeBaseUrlBean;
import com.daxi.dxdriverapplication.config.Constant;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.net.JsonCallback;
import com.daxi.dxdriverapplication.utils.SharedPreferenceUtil;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.daxi.dxdriverapplication.utils.WisdomMyTool;
import com.daxi.dxdriverapplication.widget.AppUpdateProgressDialog;
import com.lzy.okgo.BuildConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {



    private UpdateInfoAdapter updateInfoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Dialog dialog;
    private ModeBaseUrlBean.DataBean data;
    private AppUpdateProgressDialog appUpdateProgressDialog;
    private String push;
    private TextView mUpdaTv;
    private RecyclerView mVersionIntroductionRv;
    private TextView mTitleTv;
    private ImageView mDialogCloseIv;
    //1、首先声明一个数组permissions，将所有需要申请的权限都放在里面
    String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET};
  //  第二步：创建一个mPermissionList，逐个判断哪些权限未授权，将未授权的权限存储到mPermissionList中
    List<String>  mPermissionList = new ArrayList<>();
   // 第三步：声明一个请求码，在请求权限的回调方法onRequestPermissionsResult中需要判断使用
   private static final int REQUEST_PERMISSION_CODE =999 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    @Override
    public void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        updateInfoAdapter = new UpdateInfoAdapter(this);
    }

    @Override
    public void initData() {
        push = getIntent().getStringExtra("push");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPermission();
    }

    //4、权限判断和申请
    private void initPermission(){
        mPermissionList.clear();//清空已经允许的没有通过的权限
        //逐个判断是否还有未通过的权限
        for (int i = 0;i<permissions.length;i++){
            if (ContextCompat.checkSelfPermission(this,permissions[i])!=
                    PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(permissions[i]);//添加还未授予的权限到mPermissionList中
            }
        }
        //申请权限
        if (mPermissionList.size()>0){//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this,permissions,REQUEST_PERMISSION_CODE);
        }else {
            //权限已经都通过了，可以将程序继续打开了
            initRequest();
        }
    }



    /**
     * 5.请求权限后回调的方法
     * @param requestCode 是我们自己定义的权限请求码
     * @param permissions 是我们请求的权限名称数组
     * @param grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限
     *                     名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (REQUEST_PERMISSION_CODE==requestCode){
            for (int i=0;i<grantResults.length;i++){
                if (grantResults[i]==-1){
                    hasPermissionDismiss=true;
                    break;
                }
            }
        }
        if (hasPermissionDismiss){//如果有没有被允许的权限
            //showPermissionDialog();
            showTipGoSetting();
        }else {
            //权限已经都通过了，可以将程序继续打开了
            initRequest();
           // init();
        }
    }
    /**
     * 用于在用户勾选“不再提示”并且拒绝时，再次提示用户
     */
    private void showTipGoSetting() {
        new AlertDialog.Builder(this)
                .setTitle("权限未开启，APP无法使用")
                .setMessage("请在-应用设置-权限-中，请在APP权限中允许")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        System.exit(0);
                    }
                }).setCancelable(false).show();

    }

    /**
     * 打开Setting
     */
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 123);
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                   // startActivity(callIntent);//权限申请完毕
//                    initRequest();
//                } else {
//                    //如果拒绝授予权限,且勾选了再也不提醒
//                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setTitle("说明")
//                                .setMessage("需要使用电话权限，进行电话测试")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        showTipGoSetting();
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                        return;
//                                    }
//                                })
//                                .create()
//                                .show();
//                    } else {
//                        showTipGoSetting();
//                    }
//                }
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    /**
     * eva 劳务队url
     * finance 考勤url
     * materials 物资url
     * user 用户权限 注册url
     * hrs  人力资源url
     * device  设备url
     * mix    拌和站url 混凝土调度
     * proplans   拉动式生产url
     * force false 不强制 true 强制
     */

    private void initRequest() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("device", "3");
        httpParams.put("version", com.daxi.dxdriverapplication.BuildConfig.VERSION_NAME);
        OkGo.<ModeBaseUrlBean>post(StringUtils.join(HttpConstant.BASE_URL, HttpConstant.MODEl_BASE_URL)).params(httpParams)
                .execute(new JsonCallback<ModeBaseUrlBean>(ModeBaseUrlBean.class, this) {
                    @Override
                    public void onSuccess(Response<ModeBaseUrlBean> response) {
                        data = response.body().getData();
                        ModeBaseUrlBean.DataBean.ApiHostBean apiHost = data.getApiHost();
                        String eva = apiHost.getEva();
                        String finance = apiHost.getFinance();
                        String materials = apiHost.getMaterials();
                        String hrs = apiHost.getHrs();
                        String device = apiHost.getDevice();
                        String mix = apiHost.getMix();
                        String user = apiHost.getUser();
                        String proplans = apiHost.getProplans();
                        SharedPreferenceUtil.putString(WelcomeActivity.this, Constant.USER_URL, user);
                        SharedPreferenceUtil.putString(WelcomeActivity.this, Constant.EVR_URL, eva);
                        SharedPreferenceUtil.putString(WelcomeActivity.this, Constant.FINANCE_URL, finance);
                        SharedPreferenceUtil.putString(WelcomeActivity.this, Constant.MATERIALS_URL, materials);
                        SharedPreferenceUtil.putString(WelcomeActivity.this, Constant.HRS_URL, hrs);
                        SharedPreferenceUtil.putString(WelcomeActivity.this, Constant.DEVICE_URL, device);
                        SharedPreferenceUtil.putString(WelcomeActivity.this, Constant.MIX_URL, mix);
                        SharedPreferenceUtil.putString(WelcomeActivity.this, Constant.PROPLANS_URL, proplans);
                        String version = data.getVersion();
                        List<String> fixInfo = data.getFixInfo();
                        final String downloadUrl = data.getDownloadUrl();
                        if (!version.equals(String.valueOf(com.daxi.dxdriverapplication.BuildConfig.VERSION_NAME))) {
                            dialog = new Dialog(WelcomeActivity.this, R.style.ActionSheetDialogStyle);
                            //点击返回键
                            dialog.setCancelable(false);
                            //点击窗体外
                            dialog.setCanceledOnTouchOutside(false);
                            //setOnKeyListener,这个方法可以禁用返回键。
                            //填充对话框的布局
                            View view = LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.diaog_updata_apk, null);
                            mDialogCloseIv = (ImageView) view.findViewById(R.id.iv_dialog_close);
                            mDialogCloseIv.setOnClickListener(WelcomeActivity.this);
                            mTitleTv = (TextView) view.findViewById(R.id.tv_title);
                            mVersionIntroductionRv = (RecyclerView) view.findViewById(R.id.rv_version_introduction);
                            mUpdaTv = (TextView) view.findViewById(R.id.tv_upda);
                            mUpdaTv.setOnClickListener(WelcomeActivity.this);
                            mVersionIntroductionRv.setLayoutManager(linearLayoutManager);
                            mVersionIntroductionRv.setAdapter(updateInfoAdapter);
                            updateInfoAdapter.setList(fixInfo);
                            if (data.isForce()) {
                                mDialogCloseIv.setVisibility(View.GONE);
                            }
                            dialog.setContentView(view);
                            dialog.show();
                        } else {
                            startActivity(LoginActivity.class);
                        //    finish();
                        }
                    }

                    @Override
                    public void onError(Response<ModeBaseUrlBean> response) {
                        super.onError(response);
                        startActivity(LoginActivity.class);
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_close:
                // TODO 19/09/18
                dialog.dismiss();
                startActivity(LoginActivity.class);
                finish();
                break;
            case R.id.tv_upda:
                downloadAPP();
                // TODO 19/09/18
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null)
            dialog.cancel();
    }

    /***
     * APP 下载
     * @param
     * @param
     */
    private void downloadAPP() {
        if (!TextUtils.isEmpty(data.getDownloadUrl()))
            OkGo.<File>get(data.getDownloadUrl())
                    .execute(new FileCallback("dxCar.apk") {
                        @Override
                        public void onStart(Request<File, ? extends Request> request) {
                            super.onStart(request);
                            dialog.dismiss();
                            appUpdateProgressDialog = new AppUpdateProgressDialog(WelcomeActivity.this);
                            appUpdateProgressDialog.show();
                        }

                        @Override
                        public void onSuccess(Response<File> response) {
                            dialog.cancel();
                            appUpdateProgressDialog.dismiss();
                            final File absoluteFile = response.body().getAbsoluteFile();
                            Log.e("检", absoluteFile.getPath());
                            installApk(absoluteFile);
                        }

                        @Override
                        public void onError(Response<File> response) {
                            String message = response.message();
                            Log.e("下载0", message);
                            //  dialogToo2.appDoloadButton(R.string.dialog_title, R.string.update_dialog, R.string.confirm);
                            appUpdateProgressDialog.dismiss();
                            ToastUtil.showToast(WelcomeActivity.this, "下载失败,请您稍后再试!");
                        }

                        @Override
                        public void downloadProgress(Progress progress) {
                            int currentSize = (int) progress.currentSize;
                            int totalSize = (int) progress.totalSize;
                            double result = new BigDecimal((float) currentSize / totalSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            int i = (int) (result * 100);
                            appUpdateProgressDialog.setProgress(i);
                            appUpdateProgressDialog.show();
                        }

                    });
    }

    /***
     * APP 安装
     * @param file
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
}

