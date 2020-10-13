package com.daxi.dxdriverapplication.activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.adapter.QueueDialogAdapter;
import com.daxi.dxdriverapplication.base.BaseActivity;
import com.daxi.dxdriverapplication.base.BaseApplication;
import com.daxi.dxdriverapplication.bean.BindCarInfo;
import com.daxi.dxdriverapplication.bean.DataStringBean;
import com.daxi.dxdriverapplication.bean.UserInfoBean;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.net.JsonCallback;
import com.daxi.dxdriverapplication.utils.MarkerOverlay;
import com.daxi.dxdriverapplication.utils.MyTimeTask;
import com.daxi.dxdriverapplication.utils.SharedPreferenceUtil;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.daxi.dxdriverapplication.widget.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DriverHome2Activity extends BaseActivity implements View.OnClickListener, TextToSpeech.OnInitListener, AMapLocationListener {

    MapView mMapView = null;
    private TextView mPdTv;
    private TextView mNumTv;
    private TextView mJzTv;
    private Button mOutBut;

    private View mView1;
    private RecyclerView mRecycler;
    private String location;
    private String address;
    private ConstraintLayout mNoinfoCl;
    private ImageButton mImage;
    private TextView mNameValueTv;
    private TextView mJobValueTv;
    private TextView mPhoneValueTv;
    private Button mDialogOutBut;
    private Timer mTimer;
    private TimerTask timerTask;
    private TextView mBianhaoTv;
    private TextView mTimeTv;
    private TextView mStstusTv;
    private TextView mNameTv;
    private TextView mFangliangTv;
    private TextView mGsTv;
    private TextView mCarPhoneTv;
    private TextView mLocationTv;
    private Button mOkBut;
    private BindCarInfo.DataBean.OrderCarBean orderCar;
    private BindCarInfo.DataBean data;

    private List<BindCarInfo.DataBean.ParBean> par;


    private AMap aMap;

    //定位需要的数据
    LocationSource.OnLocationChangedListener mListener;
    private ConstraintLayout mNoTaskCl;
    private TextToSpeech tts;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private int orderCount = -1;
    private AlertDialog aliertDialogSpeak;
    private String hint;
    private boolean isSpeakShow = true;
    private RecyclerView mRecycler1;
    private QueueDialogAdapter queueDialogAdapter;
    private LinearLayoutManager linearLayoutManager;
    private long newDid;
    private ImageButton mZhanKaiTv;
    private AlertDialog allStatuDialog;
    private LayoutInflater factory;
    private List<BindCarInfo.DataBean.ParBean> allpar;
    private boolean isShowDialog;
    private int newStatus;
    private float zoom = 12;//地图缩放级别
    private PolylineOptions mPolylineOptions;

    private MarkerOverlay markerOverlay;
    private String mixXy;
    private String pourSiteXy;
    private Polyline polyline;
    private Polyline polylineRed;
    private int status;
    private String url;
    private static final int TIMER = 999;


    private ArrayList<LatLng> allNewLatLng = new ArrayList<>();//新建的去浇筑地点的位置
    private ArrayList<LatLng> allNewLatLngGo = new ArrayList<>();//新建的去浇筑地点的位置
    private int runingStatu = 1;
    private int bhz = -1;
    private int jz = -1;
    private Handler handler = new Handler();
    private Runnable r = new Runnable() {
        @Override
        public void run() {

                reQuest();
        }
    };
    private AMapLocationClientOption mOption;
    private TextView mSxTv;
    private TextView mClassValueTv;
    private LatLng latLngJZ;
    private ImageButton mShouQiTv;
    private boolean isshowToux;
    private AlertDialog outDialog;
    private AMapLocationClient locationClient;

    /**
     * setZoomControlsEnabled设置是否显示缩放按钮
     * setMyLocationButtonEnabled设置显示定位按钮
     * 首先在View创建之前设置两个Flag，一个设置窗口为非模式的，这样除窗口外的内容就可以获得touch事件，
     * 然后设置窗口外部touch事件发生时的通知。最后重写onTouchEvent，
     * 监听窗口外的Touch事件。这样就可以在监听方法中自定义窗口外点击事件的响应，是否关闭窗口或者其他操作。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make us non-modal, so that others can receive touch events.
        setContentView(R.layout.activity_driver_home);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        //自定义系统定位小蓝点
// 设置小蓝点的图标
//        MyLocationStyle   myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromResource(R.drawable.ic_car));// 设置小蓝点的图标
//        aMap.setMyLocationStyle(myLocationStyle);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);//是否显示缩放按钮
        uiSettings.setMyLocationButtonEnabled(false);//是否显示定位按钮
        uiSettings.setScaleControlsEnabled(true);//是否显示缩放级别
        uiSettings.setCompassEnabled(false);//设置显示指蓝针
        aMap.setMyLocationEnabled(false);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom)); //和 setPosition()方法冲突，只设置缩放大小时使用
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                Log.e("sss", cameraPosition.toString());
                if (null != cameraPosition) {
                    zoom = cameraPosition.zoom;
                }
            }

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }
        });
        runingStatu = 1;
        locationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置签到场景，相当于设置为：
         * option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
         * option.setOnceLocation(false);
         * option.setOnceLocationLatest(false);
         * option.setMockEnable(false);
         * option.setWifiScan(true);
         *
         * 其他属性均为模式属性。
         * 如果要改变其中的属性，请在在设置定位场景之后进行
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        locationClient.setLocationOption(option);
        //设置定位监听
        locationClient.setLocationListener(this);
        if (null != locationClient) {
            //开始定位
            locationClient.startLocation();
        }
    }


    private void setDialogLayout(int status) {

        if (status == 1) { //没有任务
            final View notask = factory.inflate(R.layout.dialog_no_task, null);
            allStatuDialog.setContentView(notask);
             return;
        } else if (status == 2) { //排队
            final View list = factory.inflate(R.layout.dialog, null);
            allStatuDialog.setContentView(list);
            initDialogView(list, allpar);
        } else {
            final View details = factory.inflate(R.layout.car_porgre_dialog, null);
            allStatuDialog.setContentView(details);
            initDialogDetailView(details);
        }

    }


    @Override
    public void initView() {
        allStatuDialog = new AlertDialog.Builder(this, R.style.home).create();
        outDialog = new AlertDialog.Builder(this, R.style.home).create();
        //  dialog弹出后会点击屏幕或物理返回键，dialog不消失
        //  dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        factory = LayoutInflater.from(this);
        mOutBut = (Button) findViewById(R.id.but_out);
        mOutBut.setOnClickListener(this);
        mNoinfoCl = (ConstraintLayout) findViewById(R.id.cl_noinfo);
        mImage = (ImageButton) findViewById(R.id.image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = factory.inflate(R.layout.dialog_out_uesr, null);
                initDialogUserView(view);
                outDialog.show();
                outDialog.setContentView(view);
                Window dialogWindow = outDialog.getWindow();
                WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                layoutParams.width = 800;
                layoutParams.height = 500;
                layoutParams.alpha = 0.95f;
                dialogWindow.setAttributes(layoutParams);
                dialogWindow.setGravity(Gravity.LEFT);
            }
        });

//        mZhanKaiTv = (ImageButton) findViewById(R.id.tv_zhankai);
//        mZhanKaiTv.setOnClickListener(this);
//        mShouQiTv = (ImageButton) findViewById(R.id.tv_shouqi);
//        mShouQiTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (allStatuDialog != null) {
//                    allStatuDialog.dismiss();
//                }
//            }
//        });
//        mZhanKaiTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (status==0||status==100||status == 1){//这样速度比较快
//                    final View notask = factory.inflate(R.layout.dialog_no_task, null);
//                    allStatuDialog.show();
//                    allStatuDialog.setContentView(notask);
//                    Window dialogWindow = allStatuDialog.getWindow();
//                    WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
//                    layoutParams.width = 800;
//                    layoutParams.height = 500;
//                    layoutParams.alpha = 0.95f;
//                    dialogWindow.setAttributes(layoutParams);
//                    dialogWindow.setGravity(Gravity.LEFT);
//                    return;
//                }else {
//                    allStatuDialog.show();
//                    Window dialogWindow = allStatuDialog.getWindow();
//                    WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
//                    layoutParams.width = 800;
//                    layoutParams.height = 500;
//                    layoutParams.alpha = 0.95f;
//                    dialogWindow.setAttributes(layoutParams);
//                    dialogWindow.setGravity(Gravity.LEFT);
//                }
//            }
//        });

    }

    @Override
    public void initData() {
        try {
            tts = new TextToSpeech(this, this);
            tts.setLanguage(Locale.CHINA);
        } catch (Exception e) {
            Log.e("cesi", e.getMessage());
        }
        url = StringUtils.join(getMix(), HttpConstant.CAR_LIST_INFO);
        url = StringUtils.join(getMix(), HttpConstant.CAR_LIST_INFO);
    }

    /**
     * 在这里创建一个位置，
     */

    private void initDialogDetailView(View view) {
        mBianhaoTv = (TextView) view.findViewById(R.id.tv_bianhao);
        mTimeTv = (TextView) view.findViewById(R.id.tv_time);
        mStstusTv = (TextView) view.findViewById(R.id.tv_ststus);
        mNameTv = (TextView) view.findViewById(R.id.tv_name);
        mFangliangTv = (TextView) view.findViewById(R.id.tv_fangliang);
        mGsTv = (TextView) view.findViewById(R.id.tv_gs);
        mCarPhoneTv = (TextView) view.findViewById(R.id.tv_car_phone);
        mCarPhoneTv = (TextView) view.findViewById(R.id.tv_car_phone);
        TextView mProgress = (TextView) view.findViewById(R.id.tv_progress);
        TextView location = (TextView) view.findViewById(R.id.tv_location_progress);
        mView1 = (View) view.findViewById(R.id.view1);
        mOkBut = (Button) view.findViewById(R.id.but_ok_location);

        mOkBut.setOnClickListener(this);
        mOkBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDao();
            }
        });
        if (!TextUtils.isEmpty(orderCar.getOrderNumber())) {
            mBianhaoTv.setText(orderCar.getOrderNumber());
        }
        if (!TextUtils.isEmpty(orderCar.getOrderCreateTime())) {
            mTimeTv.setText(orderCar.getOrderCreateTime());
        }
        hint = orderCar.getUnit() + "#机组正在搅拌,请" + data.getCarNumber() + "#车前往该机组";
        mProgress.setText(orderCar.getUnit() + "#机组正在搅拌,请" + data.getCarNumber() + "#车前往该机组");
        mGsTv.setText(orderCar.getLaborForceStr());
        mNameTv.setText("技术员:" + orderCar.getDutyPerson() + "  " + orderCar.getDutyPhone());
        location.setText(orderCar.getPartName());
        if (TextUtils.isEmpty(orderCar.getMixCube()) || orderCar.getMixCube().equals("0")) {
            mFangliangTv.setVisibility(View.GONE);
        } else {
            mFangliangTv.setText("运输方量:" + orderCar.getMixCube() + "方");
        }
        mCarPhoneTv.setText(orderCar.getLaborForceName() + "  " + orderCar.getLaborForcePhone());
        int status = orderCar.getStatus();
        if (status == 14) {//开始拌和
//            if (isSpeakShow) {//true 需要播放
//                showSettingSpeak(hint);
//            }
            mStstusTv.setText("正在拌和");
            mStstusTv.setTextColor(ContextCompat.getColor(this, R.color.tv_caveat));
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
            return;
        }
        if (status == 7) {//在状态7的时候关闭语音播报 搅拌完成
            if (aliertDialogSpeak != null) {
                aliertDialogSpeak.dismiss();
            }
            if (tts != null) {
                tts.stop();//语音停止
                tts.shutdown();//释放资源
                tts = null;//赋值为空
            }
            mStstusTv.setText("搅拌完成");
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            return;
        }
        if (status == 8) {  //正在运输
            mStstusTv.setText("正在运输");
            mStstusTv.setTextColor(ContextCompat.getColor(this, R.color.tv_19b3ea));
            mOkBut.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
            return;
        }
        if (status == 9) {
            mStstusTv.setText("空车返回");
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            return;
        }

        if (status == 1) {
            mStstusTv.setText("到达浇筑地点");
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            return;
        }
        if (status == 13) {
            mStstusTv.setText("开始浇筑");
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            return;
        }
        if (status == 11) {
            mStstusTv.setText("完成浇筑");
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            return;
        }

        mOkBut.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
    }

    private void getDao() {
        HttpParams httpParams = new HttpParams();
        httpParams.put(HttpConstant.TOKEN, getToken());
        httpParams.put("siteXy", location);
        httpParams.put("did", orderCar.getDid());
        httpParams.put("id", orderCar.getId());
        OkGo.<DataStringBean>post(StringUtils.join(getMix(), HttpConstant.CAR_ARRIVALS)).params(httpParams).execute(new JsonCallback<DataStringBean>(DataStringBean.class, this) {
            @Override
            public void onSuccess(Response<DataStringBean> response) {
                //  location();
                //  ToastUtil.show();
            }
        });

    }

    private void showSettingSpeak(String hint) {
        if (aliertDialogSpeak == null) {
            aliertDialogSpeak = new AlertDialog.Builder(DriverHome2Activity.this, R.style.home).create();
        }
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_speak, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(hint)) {
            textView.setText(hint);
        }

        try {
            if (tts == null) {
                tts = new TextToSpeech(this, this);
                tts.speak(hint, TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Exception e) {
            return;
        }


        if (!aliertDialogSpeak.isShowing()) {
            aliertDialogSpeak.show();
        }
        aliertDialogSpeak.setContentView(view);
        Window dialogWindow = aliertDialogSpeak.getWindow();
        /* 设置显示窗口的宽高 */
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = 600;
        layoutParams.height = 300;
        layoutParams.alpha = 0.95f;
        dialogWindow.setAttributes(layoutParams);
        dialogWindow.setGravity(Gravity.CENTER);
        view.findViewById(R.id.tv_ok_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.shutdown();//释放资源
                aliertDialogSpeak.dismiss();
                aliertDialogSpeak.cancel();
                isSpeakShow = false;
            }
        });
    }

    @Override
    protected void onStart() { //定时任务 拌和站 10秒一次,路上1分钟
        super.onStart();

        reQuest();

    }

    private void reQuest() {
        if (TextUtils.isEmpty(location)) {
            handler.postDelayed(r, 2000);
            return;
        }
//        final com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
//        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
//        jsonObject.put("xy", location);
//        jsonArray.add(jsonObject);
//        String   s = com.alibaba.fastjson.JSONObject.toJSONString(jsonArray);
        HttpParams httpParams = new HttpParams();
        httpParams.put(HttpConstant.TOKEN, getToken());
        httpParams.put("carSiteXy", location);
        httpParams.put("runingStatu", runingStatu);
        OkGo.<BindCarInfo>get(StringUtils.join(getMix(), HttpConstant.CAR_LIST_INFO)).params(httpParams).tag(this).execute(new JsonCallback<BindCarInfo>(BindCarInfo.class, this) {

            @Override
            public void onSuccess(Response<BindCarInfo> response) {
                //获取上次数据成功返回的时间
                data = response.body().getData();
                String isBingding = data.getIsBingding();
                if (!TextUtils.isEmpty(isBingding) && isBingding.equals("2")) {
                    mNoinfoCl.setVisibility(View.VISIBLE);
                    mZhanKaiTv.setVisibility(View.GONE);
                } else {
                    mZhanKaiTv.setVisibility(View.VISIBLE);
                    orderCar = data.getOrderCar();
                    par = data.getPar();
                    if (orderCar != null) {
                        long did = orderCar.getDid();
                        if (did != 0) {
                            //拌和站位置
                            mixXy = data.getMixXy();
                            pourSiteXy = data.getPourSiteXy();
                            if (newDid != did) {  //如果订单变了 就重新创建
                                isSpeakShow = true;
                            }
                            newDid = did;
                            if (!TextUtils.isEmpty(pourSiteXy) && !TextUtils.isEmpty(mixXy)) {
                                status = data.getOrderCar().getStatus();
                                if (status == 8 || status == 7 || status == 14) {//正在运输  14开始拌合   7 拌合完成
                                    //浇筑的位置
                                    //   mPolylineOptions.
                                    latLngJZ = new LatLng(Double.parseDouble(pourSiteXy.split(",")[0]), Double.parseDouble(pourSiteXy.split(",")[1]));
                                } else if (status == 11 || status == 9) {//9空车返回  //11完成浇筑
                                    latLngJZ = new LatLng(Double.parseDouble(mixXy.split(",")[0]), Double.parseDouble(mixXy.split(",")[1]));
                                }
                            } else { //所有的都不记录
                                allNewLatLngGo.clear();
                                allNewLatLng.clear();
                            }
                            setDialogLayout(3); //在去的时候 完成浇筑以后  清除掉之前绘制的路程 重新开始绘制  在没有did订单的时候重新开始绘制

                        } else { //没有详情的话我们应该判断是不是没有任务和排队
                            status=120;
                            isSpeakShow = true;
                            mixXy = data.getMixXy();
                            pourSiteXy = data.getPourSiteXy();
                            if (DriverHome2Activity.this.par != null && DriverHome2Activity.this.par.size() > 0) {
                                orderCount++;
                                if (orderCount < 1) {//只播放1次
                                    try {
                                        if (tts == null) {
                                            tts = new TextToSpeech(DriverHome2Activity.this, DriverHome2Activity.this);
                                            tts.speak("您有新的订单,请注意查看", TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    } catch (Exception e) {
                                        Log.e("speak", e.getMessage());
                                    }

                                }
                                allpar = new ArrayList<>();
                                allpar.clear();
                                allpar.addAll(data.getPar());
                                setDialogLayout(2);
                            } else {
                                orderCount = -1;
                                setDialogLayout(1);
                                isSpeakShow = true;
                            }
                            if (polyline != null) { //删除绘制的轨迹路线
                                polyline.remove();

                            }
                            if (polylineRed != null) { //删除绘制的路线
                                polylineRed.remove();
                            }

                        }

                    } else {
                        status=100;
                        setDialogLayout(1);
                        markerOverlay = new MarkerOverlay(aMap, location, DriverHome2Activity.this);
                        markerOverlay.addToMap();
                        if (polyline != null) { //删除绘
                            // 制的轨迹路线
                            polyline.remove();
                        }
                        if (polylineRed != null) {//绘制的红线删除掉
                            polylineRed.remove();
                        }
                    }
                    handler.postDelayed(r, 8000);
                }

            }


            @Override
            public void onError(Response<BindCarInfo> response) {//在这里进行存储,传输的时候一起传输
                super.onError(response);
                handler.postDelayed(r, 8000);

            }
        });
    }

    private void initDialogUserView(View view) {
        mNameValueTv = (TextView) view.findViewById(R.id.tv_name_value);
        mJobValueTv = (TextView) view.findViewById(R.id.tv_job_value);
        mPhoneValueTv = (TextView) view.findViewById(R.id.tv_phone_value);
        mClassValueTv = (TextView) view.findViewById(R.id.tv_class_value);
        mDialogOutBut = (Button) view.findViewById(R.id.but_dialog_out);
        mDialogOutBut.setOnClickListener(this);
        String simpleName = SharedPreferenceUtil.getString(DriverHome2Activity.this, "simpleName");
        String userName = SharedPreferenceUtil.getString(DriverHome2Activity.this, "userName");
        String jobName = SharedPreferenceUtil.getString(DriverHome2Activity.this, "jobName");
        String phone = SharedPreferenceUtil.getString(DriverHome2Activity.this, "phone");
        mJobValueTv.setText(jobName);
        mNameValueTv.setText(userName);
        mPhoneValueTv.setText(phone);
        mClassValueTv.setText(simpleName);

    }


    private void initDialogView(View view, List<BindCarInfo.DataBean.ParBean> allpar) {
        mPdTv = (TextView) view.findViewById(R.id.tv_pd_num);
        String maxLevel = data.getMaxLevel();
        if (!TextUtils.isEmpty(maxLevel)) {
            mPdTv.setText(maxLevel);
        }
        mSxTv = (TextView) view.findViewById(R.id.tv_sx);
        mNumTv = (TextView) view.findViewById(R.id.tv_num);
        mJzTv = (TextView) view.findViewById(R.id.tv_jz);
        mView1 = (View) view.findViewById(R.id.view1);
        mRecycler1 = (RecyclerView) view.findViewById(R.id.recycler);
        if (queueDialogAdapter == null) {
            queueDialogAdapter = new QueueDialogAdapter(this);
        }

        linearLayoutManager = new LinearLayoutManager(this);
        mRecycler1.setLayoutManager(linearLayoutManager);
        mRecycler1.setAdapter(queueDialogAdapter);
        queueDialogAdapter.setList(allpar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_out:
            case R.id.but_dialog_out:// TODO 19/10/28
                // TODO 19/10/28
                outLoginRequest();
                break;
//            case R.id.image:// TODO 19/10/28
//                setDialogLayout(4);
//                break;
//            case R.id.tv_zhankai:// TODO 19/10/28
//                allStatuDialog.show();
//                break;
            case R.id.but_ok:// TODO 19/10/28
                break;
            default:
                break;
        }
    }

    private void outLoginRequest() {
        OkGo.<String>post(StringUtils.join(getUserUrl(), HttpConstant.OUT_ACCOUNT)).
                params(HttpConstant.TOKEN, SharedPreferenceUtil.getString(DriverHome2Activity.this, HttpConstant.TOKEN)).
                execute(new JsonCallback<String>(String.class, DriverHome2Activity.this) {
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
                                SharedPreferenceUtil.putString(DriverHome2Activity.this, HttpConstant.TOKEN, "");

                            } else {
                                ToastUtil.showToast(DriverHome2Activity.this, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showToast(DriverHome2Activity.this, HttpConstant.JSON_EXCEPTION);
                        }
                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
        // 不管是否正在朗读TTS都被打断
//        if (tts != null) {
//            tts.stop();
//            // 关闭，释放资源
//            tts.shutdown();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(r);
        if (locationClient!=null){
            locationClient.stopLocation();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            //不管是不是在阅读,都打断
            //关闭,释放资源
            tts = null;
        }

    }

    @Override
    public void onInit(int i) {

    }
    private com.alibaba.fastjson.JSONObject jsonObject;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {//mix 拌合站
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            Double latitude = aMapLocation.getLatitude();
            Double longitude = aMapLocation.getLongitude();
            location = String.valueOf(latitude) + "," + String.valueOf(longitude);
            LatLng latLng = new LatLng(latitude, longitude);
            if (polylineRed != null) {//绘制的红线删除掉
                polylineRed.remove();
            }
            if (markerOverlay != null) {//确定的是车的位置
                markerOverlay.removeFromMap();
            }
            if (!TextUtils.isEmpty(mixXy) && !TextUtils.isEmpty(pourSiteXy)) {
                markerOverlay = new MarkerOverlay(aMap, mixXy, pourSiteXy, location, DriverHome2Activity.this);
                markerOverlay.addToMap();
                if (zoom == 12) {
                    markerOverlay.zoomToSpanWithCenter();
                }

            } else {
                markerOverlay = new MarkerOverlay(aMap, location, DriverHome2Activity.this);
                markerOverlay.addToMap();//
                if (zoom == 12) {
                    markerOverlay.zoomToSpanWithCenter();
                }

                if (zoom == 12) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));//移动位置到定位点
                }
            }
            if (status == 8) {//8代表是正在运输 去浇筑的地点,7代表搅拌完成

                if (bhz < 0) { //小于0的意思是集合的第一个数据是拌合站的位置
                    if (!TextUtils.isEmpty(mixXy)) {
                        allNewLatLng.add(new LatLng(Double.parseDouble(mixXy.split(",")[0]), Double.parseDouble(mixXy.split(",")[1])));
                    }
                }
                bhz = 1;
                allNewLatLng.add(latLng);//每次绘制

                if (allNewLatLngGo.size() > 0) {//
                    allNewLatLngGo.clear();
                }
                if (latLngJZ != null) {
                    polylineRed = aMap.addPolyline(new PolylineOptions().setDottedLine(false).setUseTexture(false).visible(true).color(Color.parseColor("#FFFA0257")).width(3).add(latLng, latLngJZ));//绘制红线
                }
                if (polyline != null) { //删除绘制的轨迹路线
                    polyline.remove();
                }
                polyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
                        .addAll(allNewLatLng)
                        .useGradient(true)
                        .width(14));
                 return;
            } else if (11 == status || 9 == status) {//11完成浇筑，9空车返回

                if (allNewLatLng.size() > 0) {
                    allNewLatLng.clear();
                }
                if (jz < 0) { //小于0的意思是集合的第一个数据是拌合站的位置
                    if (!TextUtils.isEmpty(mixXy)) {
                        allNewLatLngGo.add(new LatLng(Double.parseDouble(pourSiteXy.split(",")[0]), Double.parseDouble(pourSiteXy.split(",")[1])));
                    }
                }
                jz = 1;
                allNewLatLngGo.add(latLng);//每次绘制

                if (latLngJZ != null) {
                    polylineRed = aMap.addPolyline(new PolylineOptions().setDottedLine(false).setUseTexture(false).visible(true).color(Color.parseColor("#FFFA0257")).width(3).add(latLng, latLngJZ));//绘制红线
                }
                if (polyline != null) { //删除绘
                    // 制的轨迹路线
                    polyline.remove();
                }

                polyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
                        .addAll(allNewLatLngGo)
                        .useGradient(true)
                        .width(14));
                return;
            }else {//删除红线和轨迹
                if (polyline != null) { //删除绘
                    // 制的轨迹路线
                    polyline.remove();
                }
                if (polylineRed != null) {//绘制的红线删除掉
                    polylineRed.remove();
                }
            }

        } else {
            String errorInfo = aMapLocation.getErrorInfo();
            ToastUtil.show(DriverHome2Activity.this, "卫星信号差，获取定位失败");
        }
    }

}
