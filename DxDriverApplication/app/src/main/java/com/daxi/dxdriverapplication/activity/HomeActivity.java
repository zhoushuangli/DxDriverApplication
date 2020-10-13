package com.daxi.dxdriverapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.adapter.BindCarAdapter;
import com.daxi.dxdriverapplication.base.BaseActivity;
import com.daxi.dxdriverapplication.base.BaseGoodAdapter;
import com.daxi.dxdriverapplication.bean.CarInfoBean;
import com.daxi.dxdriverapplication.bean.DataStringBean;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.net.JsonCallback;
import com.daxi.dxdriverapplication.utils.MobileInfoUtil;
import com.daxi.dxdriverapplication.utils.SharedPreferenceUtil;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private TextView mValueTv;
    private RecyclerView mRecycler;
    private Button mOkBut;
    private Button mOutLoginBut;
    private BindCarAdapter bindCarAdapter;
    private AlertDialog alertDialog;
    private List<CarInfoBean.DataBean.ParBean> par;
    private long carId;
    private String bindingDevice;
    private int isBinding;
    private String carNumber;
    private String bindNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void initView() {
        mValueTv = (TextView) findViewById(R.id.tv_value);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        mOkBut = (Button) findViewById(R.id.but_ok);
        mOkBut.setOnClickListener(this);
        mOutLoginBut = (Button) findViewById(R.id.but_out_login);
        mOutLoginBut.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 8);
        mRecycler.setLayoutManager(gridLayoutManager);
        bindCarAdapter = new BindCarAdapter(this);
        bindCarAdapter.setSelectMode(BaseGoodAdapter.SelectMode.SINGLE_SELECT);
        bindCarAdapter.setSelected(-1);
        mRecycler.setAdapter(bindCarAdapter);
        bindCarAdapter.setOnItemSelectListener(new BaseGoodAdapter.OnItemSelectListener() {
            @Override
            public void onSelected(int itemPosition, View view) {
//                mRecycler.setAdapter(null);
//                mRecycler.setAdapter(bindCarAdapter);
                isBinding = par.get(itemPosition).getIsBinding();
                carId = par.get(itemPosition).getCarId();
                carNumber = par.get(itemPosition).getCarNumber();
                bindingDevice = par.get(itemPosition).getBindingDevice();
                if (isBinding == 1 && bindingDevice.equals(MobileInfoUtil.getDeviceId(HomeActivity.this))) { //没有绑定
                    ToastUtil.showToast(HomeActivity.this,"当前车辆已经绑定"+carNumber+"号");
                } else {
                    showWaiterAuthorizationDialog();

                }
            }
        });
    }

    public void showWaiterAuthorizationDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        // 把布局文件中的控件定义在View中
        final View view = factory.inflate(R.layout.dialog_replace_bind, null);
        initDialogView(view);
        alertDialog = new AlertDialog.Builder(this, R.style.ActionSheetDialogStyle).create();
        alertDialog.setCancelable(false);
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void initDialogView(View view) {
        TextView textView = view.findViewById(R.id.tv_num_key);

        if (!TextUtils.isEmpty(bindNum)){//carNumber+"#已绑定，是否替换绑定信息？"
            textView.setText("当前车辆已绑定"+bindNum+"号，是否替换绑定信息？");
        }else {
            textView.setText("是否将设备绑定为"+carNumber+"号车？");

        }
        view.findViewById(R.id.dialog_but_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.dialog_but_ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                bindCar();
                alertDialog.dismiss();
            }
        });
    }
    private void outLoginRequest() {
        OkGo.<String>post(StringUtils.join(getUserUrl(), HttpConstant.OUT_ACCOUNT)).
                params(HttpConstant.TOKEN, SharedPreferenceUtil.getString(HomeActivity.this, HttpConstant.TOKEN)).
                execute(new JsonCallback<String>(String.class, HomeActivity.this) {
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
                                SharedPreferenceUtil.putString(HomeActivity.this, HttpConstant.TOKEN, "");
                            } else {
                                ToastUtil.showToast(HomeActivity.this, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showToast(HomeActivity.this, HttpConstant.JSON_EXCEPTION);
                        }
                    }
                });

    }

    private void bindCar() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("deviceNumber", MobileInfoUtil.getDeviceId(this));
        httpParams.put("carId", String.valueOf(carId));
        httpParams.put(HttpConstant.TOKEN, getToken());
        OkGo.<DataStringBean>post(StringUtils.join(getMix(), HttpConstant.BIND_CAR)).params(httpParams).execute(new JsonCallback<DataStringBean>(DataStringBean.class, this) {
            @Override
            public void onSuccess(Response<DataStringBean> response) {
                SharedPreferenceUtil.putString(HomeActivity.this,"carId",carId+"");
                SharedPreferenceUtil.putString(HomeActivity.this,"carNumber",carNumber+"");
                String data = response.body().getData();
                Bundle bundle = new Bundle();
                bundle.putString("data", data);
                startActivity(ResultActivity.class, bundle);
            }
        });
    }

    @Override
    public void initData() {
        getCarInfo();
    }

    private void getCarInfo() {
        OkGo.<CarInfoBean>get(StringUtils.join(getMix(), HttpConstant.DEVICE_BIND_CAR_INFO)).params(HttpConstant.TOKEN, getToken()).params("deviceNumber", MobileInfoUtil.getDeviceId(this))
                .execute(new JsonCallback<CarInfoBean>(CarInfoBean.class, this) {
                    @Override
                    public void onSuccess(Response<CarInfoBean> response) {
                        CarInfoBean.DataBean data = response.body().getData();
                        bindNum = data.getBindNum();
                        if (TextUtils.isEmpty(bindNum)) {//没绑定
                            mValueTv.setVisibility(View.GONE);
                        } else {
                            mValueTv.setText("(当前设别已绑定" + bindNum + "号)");
                        }
                        par = data.getPar();
                        bindCarAdapter.setList(par);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_ok:
                // TODO 19/10/28
                if (TextUtils.isEmpty(String.valueOf(carId))){
                    ToastUtil.show(this,"请选择车辆");
                    return;
                }
                bindCar();
                break;
            case R.id.but_out_login:
                outLoginRequest();
                // TODO 19/10/28
                break;
            default:
                break;
        }
    }
}
