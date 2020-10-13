package com.daxi.dxdriverapplication.net;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.daxi.dxdriverapplication.widget.LoadingDialog;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.request.base.Request;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class JsonCallback<T> extends AbsCallback<T> {
    private Class<T> clazz;
    private Context activity;
    private LoadingDialog loadingDialog;
    private boolean isShow = true;

    private LocalBroadcastManager localBroadcastManager;

    public void setOnDilogDisMissClickListener(OnDilogDisMissClickListener onDilogDisMissClickListener) {
        this.onDilogDisMissClickListener = onDilogDisMissClickListener;
    }

    public OnDilogDisMissClickListener onDilogDisMissClickListener;

    public interface OnDilogDisMissClickListener {
        void disMissDialog();
    }

    public JsonCallback(Class clazz, Activity activity) {
        this.clazz = clazz;
        this.activity = activity;


        if (loadingDialog==null){
            loadingDialog = new LoadingDialog(activity, R.style.Loading);
        }
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
    }
    public JsonCallback(Class clazz, Context context) {
        this.clazz = clazz;
        this.activity = context;

    }


    @Override
    public void onStart(Request<T, ? extends Request> request) {
        Log.e("request", request.getBaseUrl());
        super.onStart(request);

        try {
            if (loadingDialog != null && isShow && !loadingDialog.isShowing()) {//没有
                loadingDialog.show();
            }
        }catch (Exception e){

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dismissDialog(String tag) {
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }

    @Override

    public T convertResponse(Response response) throws Throwable {


        try {
            if (loadingDialog != null)
                loadingDialog.dismiss();
        }catch (Exception e){

        }
        ResponseBody body = response.body();

        JSONReader jsonReader = new JSONReader(body.charStream());
        if (clazz == null)
            throw new NullPointerException("class不能为空");
        if (body == null)
            return null;
        if (clazz == String.class) {
            String result = jsonReader.readObject(String.class);
          //  ToastUtil.show(activity,result);
            Log.e("result", result);
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString(HttpConstant.RESULT_CODE);
            if (!TextUtils.isEmpty(code) && HttpConstant.SESSION_TIMEOUT.equals(code)) {
                throw new IllegalStateException(HttpConstant.SESSION_TIMEOUT);
            }
            return (T) result;

        } else if (clazz == JSONObject.class) {
            return (T) new JSONObject(body.string());
        } else if (clazz == JSONArray.class) {
            return (T) new JSONArray(body.string());
        } else {
            String result = jsonReader.readObject(String.class);
           // String result = JSON.toJSONString(body);
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("msg");
            String messageData = jsonObject.getString("data");

//            if (!TextUtils.isEmpty(messageData)){
//                ToastUtil.showToast((Activity) activity,messageData);
//            }
            if (HttpConstant.SUCCESS_CODE.equals(code)) {
                T t1 = com.alibaba.fastjson.JSONObject.parseObject(result, clazz);
                return t1;
            } else if (code.equals(HttpConstant.NO_DATA)) {
                throw new IllegalStateException("");
            } else if (code.equals(HttpConstant.UN_LOGIN) || code.equals(HttpConstant.OUT_TOKEN)) {
                throw new IllegalStateException("未登录");
            } else if (code.equals(HttpConstant.SESSION_TIMEOUT)) {
                throw new IllegalStateException(HttpConstant.SESSION_TIMEOUT);
            } else if (code.equals(HttpConstant.ERROR_SYSTEM)) {
                throw new IllegalStateException(message);
            } else {
                throw new IllegalStateException(message);
            }

        }

    }

    /**
     * 子线程.可以更新UI
     */
    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        try {
            if (loadingDialog != null)
                loadingDialog.dismiss();
        }catch (Exception e){

        }
        Throwable exception = response.getException();
        String message = exception.getMessage();
//        if (!TextUtils.isEmpty(message)) {
//            ToastUtil.show(activity,message);
//            return;
//        }ton
        if (message.equals("未登录")) {
//            SharedPreferenceUtil.putString(activity, HttpConstant.TOKEN, "");
//            showDialog();
            return;
        } else if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            ToastUtil.show(activity, "网络连接失败,请连接网络");
            return;
        } else if (exception instanceof SocketTimeoutException) {
            ToastUtil.show(activity, "网络连接失败,请连接网络");
            return;
        } else if (exception instanceof HttpException) {
            ToastUtil.show(activity, "未找到服务器资源");
            return;
        } else if (exception instanceof IllegalStateException) {
            ToastUtil.show(activity, message);
            return;
        } else if (exception instanceof JSONException) {

            ToastUtil.show(activity, "服务器数据解析异常,请稍后再试");
            return;
        } else if (exception instanceof NoRouteToHostException) {
            ToastUtil.show(activity, "未连接到服务器");
            return;
        }else if (exception instanceof IllegalArgumentException){
            ToastUtil.show(activity, "网络连接失败,请连接网络");
            return;
        } else {
            ToastUtil.show(activity, message);
            return;
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (loadingDialog != null)
            loadingDialog.dismiss();

       // EventBus.getDefault().unregister(this);
    }

}
