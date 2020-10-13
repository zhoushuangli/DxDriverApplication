package com.daxi.dxdriverapplication.activity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSONArray;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.adapter.QueueDialogAdapter;
import com.daxi.dxdriverapplication.base.BaseActivity;
import com.daxi.dxdriverapplication.base.BaseApplication;
import com.daxi.dxdriverapplication.bean.BindCarInfo;
import com.daxi.dxdriverapplication.bean.DataStringBean;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.net.JsonCallback;
import com.daxi.dxdriverapplication.utils.MarkerOverlay;
import com.daxi.dxdriverapplication.utils.SharedPreferenceUtil;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
public class DriverHome3Activity extends BaseActivity implements View.OnClickListener {
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
    private ImageView mImage;
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
    private JSONArray jsonArrayFail = new JSONArray();
    private List<BindCarInfo.DataBean.ParBean> par;
    private AMap aMap;
    //定位需要的数据
    LocationSource.OnLocationChangedListener mListener;
    private ConstraintLayout mNoTaskCl;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private int orderCount = -1;
    private AlertDialog aliertDialogSpeak;
    private String hint;
    private boolean isSpeakShow = true;
    private RecyclerView mRecycler1;
    private QueueDialogAdapter queueDialogAdapter;
    private LinearLayoutManager linearLayoutManager;
    private long newDid;
    private ImageView mZhanKaiTv;
    private AlertDialog allStatuDialog;
    private LayoutInflater factory;
    private List<BindCarInfo.DataBean.ParBean> allpar;
    private boolean isShowDialog = true;
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
    private AMapLocationClientOption mOption;
    private TextView mSxTv;
    private TextView mClassValueTv;
    private LatLng latLngJZ;
    private ImageView mShouQiTv;
    private boolean isshowToux;
    private AlertDialog outDialog;
    private AMapLocationClient locationClient;
    private int speagCount = -1;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//代表去刷新网络请求
                handler.post(runnable);
            } else {
                reQuest();
            }
        }
    };
    private String s;
    private com.alibaba.fastjson.JSONObject jsonObject;
    private String carId;
    private Button mZCBut;
    private Button mJYBut;
    private Button mGZBut;
    private String newLocation;
    private TextToSpeech textToSpeech;
    private TextView mProgress;
    private long ysID;
    private boolean isSpeak=true;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//取消状态栏的信息
        Intent location = new Intent(getApplicationContext(), LocationService.class);
        startService(location);
        carId = SharedPreferenceUtil.getString(DriverHome3Activity.this, "carId");
        carNumber = SharedPreferenceUtil.getString(DriverHome3Activity.this, "carNumber");
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
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
                if (null != cameraPosition) {
                    zoom = cameraPosition.zoom;
                }
            }
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }
        });
        runingStatu = 1;
        handler.postDelayed(runnable, 0);
        initTTS();
    }
    private void initTTS() {
        //实例化自带语音对象
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速
//                    //判断是否支持下面两种语言
                    int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
//                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
//                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);
                } else {
                    Toast.makeText(DriverHome3Activity.this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void startAuto(String data) {//呼叫的hint
//        if (speagCount == 3) {//代表最后一次呼叫
//            isSpeak = false;
//            speagCount=-1;
//        }
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        textToSpeech.setPitch(1.0f);
        // 设置语速
        textToSpeech.setSpeechRate(1f);
        textToSpeech.speak(data,//输入中文，若不支持的设备则不会读出来
                TextToSpeech.QUEUE_ADD, null);//新的任务会排在队伍后面

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //算出请求的时间值；
            //获取位置信息
            // location = gpsUtils.getLocation();
            final Location lc = BaseApplication.getInstance().LOCATION;
            if (lc != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateView(lc);
                    }
                });
                handler.sendEmptyMessageDelayed(0, 1000);
            } else {//失败的话马上开始
                handler.sendEmptyMessageDelayed(0, 0);
            }
        }
    };

    private void updateView(Location lc) {
        if (lc != null) {
            Double latitude = lc.getLatitude();
            Double longitude = lc.getLongitude();
            double[] doubles = GPSUtil.gps84_To_Gcj02(latitude, longitude);
            LatLng latLng = new LatLng(doubles[0], doubles[1]);
            location = doubles[0] + "," + doubles[1];
            newLocation = doubles[1] + "," + doubles[0];
            jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("xy", location);
            if (polylineRed != null) {//绘制的红线删除掉
                polylineRed.remove();
            }
            if (markerOverlay != null) {//确定的是车的位置
                markerOverlay.removeFromMap();
            }
            if (!TextUtils.isEmpty(mixXy) && !TextUtils.isEmpty(pourSiteXy)) {
                markerOverlay = new MarkerOverlay(aMap, mixXy, pourSiteXy, location, DriverHome3Activity.this);
                markerOverlay.addToMap();
                if (zoom == 12) {
                    markerOverlay.zoomToSpanWithCenter();
                }

            } else {
                if (markerOverlay != null) {//空车返回的时候清掉点
                    markerOverlay.removeFromMap();
                }
                markerOverlay = new MarkerOverlay(aMap, location, DriverHome3Activity.this);
                markerOverlay.addToMap();//
                if (zoom == 12) {
                    markerOverlay.zoomToSpanWithCenter();
                }

                if (zoom == 12) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));//移动位置到定位点
                }
            }
            if (status == 8 || status == 13) {//8代表是正在运输 去浇筑的地点,7代表搅拌完成
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
                        .width(10));
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
                        .width(10));
                return;
            } else {//删除红线和轨迹
                if (polyline != null) { //删除绘
                    // 制的轨迹路线
                    polyline.remove();
                }
                if (polylineRed != null) {//绘制的红线删除掉
                    polylineRed.remove();
                }
            }

        }

    }
    private String carNumber;
    @Override
    public void initView() {
        allStatuDialog = new AlertDialog.Builder(this, R.style.top_notify_dialog_style).create();
        outDialog = new AlertDialog.Builder(this, R.style.top_notify_dialog_style).create();
        //  dialog弹出后会点击屏幕或物理返回键，dialog不消失
        //  dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        factory = LayoutInflater.from(this);
        mOutBut = (Button) findViewById(R.id.but_out);
        mOutBut.setOnClickListener(this);
        mNoinfoCl = (ConstraintLayout) findViewById(R.id.cl_noinfo);
        mImage = (ImageView) findViewById(R.id.image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = factory.inflate(R.layout.dialog_out_uesr, null);
                initDialogUserView(view);
                outDialog.show();
                outDialog.setContentView(view);
                Window dialogWindow = outDialog.getWindow();
//设置布局顶部显示
                //设置背景透明后设置该属性，可去除dialog边框
                //   getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
                WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                layoutParams.width = 820;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.alpha = 0.95f;
                dialogWindow.setAttributes(layoutParams);
                dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
            }
        });


        mZhanKaiTv = (ImageView) findViewById(R.id.but_close);
        mZhanKaiTv.setOnClickListener(this);
        allStatuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mZhanKaiTv.setBackground(getResources().getDrawable(R.drawable.zhankai));
                isShowDialog = false;
            }
        });
        allStatuDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mZhanKaiTv.setBackground(getResources().getDrawable(R.drawable.shouqi));
                isShowDialog = true;
            }
        });
        mZhanKaiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowDialog && allStatuDialog.isShowing()) {//显示dialog
                    allStatuDialog.dismiss();
                } else {
                    if (outDialog.isShowing()) {
                        outDialog.dismiss();
                    }
                    allStatuDialog.show();
                }
            }
        });
    }

    private void setDialogLayout(int status) {
        if (status == 1) { //没有任务的时候,删除掉开始浇筑和完成浇筑
            if (!TextUtils.isEmpty(mixXy)) {
                mixXy = "";
            }

            if (TextUtils.isEmpty(pourSiteXy)) {
                pourSiteXy = "";
            }
            if (aliertDialogSpeak != null && aliertDialogSpeak.isShowing()) {
                allStatuDialog.dismiss();
                return;
            }
            if (isShowDialog) {
                final View notask = factory.inflate(R.layout.dialog_no_task, null);
                allStatuDialog.show();
                allStatuDialog.setContentView(notask);
                Window dialogWindow = allStatuDialog.getWindow();
                WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                layoutParams.width = 820;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.alpha = 0.95f;
                dialogWindow.setAttributes(layoutParams);
                dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
            }
            return;
        } else if (status == 2) { //排队
            if (isShowDialog) {
                final View list = factory.inflate(R.layout.dialog, null);
                allStatuDialog.show();
                allStatuDialog.setContentView(list);
                Window dialogWindow = allStatuDialog.getWindow();
                WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                layoutParams.width = 820;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.alpha = 0.95f;
                dialogWindow.setAttributes(layoutParams);
                dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
                initDialogView(list, allpar);
            }
        } else {
            if (isShowDialog) {
                final View details = factory.inflate(R.layout.car_porgre_dialog, null);
                allStatuDialog.show();
                allStatuDialog.setContentView(details);
                initDialogDetailView(details);
                Window dialogWindow = allStatuDialog.getWindow();
                WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
                layoutParams.width = 820;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.alpha = 0.95f;
                dialogWindow.setAttributes(layoutParams);
                dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
            }
        }

    }

    @Override
    public void initData() {
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
        mProgress = (TextView) view.findViewById(R.id.tv_progress);
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
            mStstusTv.setText("正在拌和");
            mStstusTv.setTextColor(ContextCompat.getColor(this, R.color.tv_caveat));
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
            return;
        }
        if (status == 7) {//在状态7的时候关闭语音播报 搅拌完成,并且关闭语音播报
            if (aliertDialogSpeak != null) {
                aliertDialogSpeak.dismiss();
            }
            mStstusTv.setTextColor(ContextCompat.getColor(this, R.color.tv_1bc088));
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
        if (status == 9) {//isSpeak 重置状态
            isSpeak=true;
            speagCount=-1;
            mStstusTv.setText("空车返回");
            mStstusTv.setTextColor(ContextCompat.getColor(this, R.color.tab_text_nomal));
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
            mStstusTv.setTextColor(ContextCompat.getColor(this, R.color.tv_8ca2b1));
            mStstusTv.setText("开始浇筑");
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            return;
        }
        if (status == 11) {
            mStstusTv.setTextColor(ContextCompat.getColor(this, R.color.tv_51f6e8));
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
            }
        });

    }

    private void showSettingSpeak(String hint) {
        if (aliertDialogSpeak == null) {
            aliertDialogSpeak = new AlertDialog.Builder(DriverHome3Activity.this, R.style.home).create();
//            aliertDialogSpeak.setCanceledOnTouchOutside(false);//点击屏幕不消失
//            aliertDialogSpeak.setCancelable(false);//点击返回键不消失
        }
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_speak, null);
        final TextView textView = (TextView) view.findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(hint)) {
            textView.setText(hint);
        }
        if (!aliertDialogSpeak.isShowing()) {
            aliertDialogSpeak.show();
        }

        startAuto(hint);
        aliertDialogSpeak.setContentView(view);
        Window dialogWindow = aliertDialogSpeak.getWindow();
        /* 设置显示窗口的宽高 */
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = 700;
        layoutParams.height = 400;
        layoutParams.alpha = 0.95f;
        dialogWindow.setAttributes(layoutParams);
        dialogWindow.setGravity(Gravity.CENTER);
        view.findViewById(R.id.tv_ok_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.stop();
                //  textToSpeech.shutdown();//释放资源
                aliertDialogSpeak.dismiss();
                aliertDialogSpeak.cancel();
                isSpeakShow = false;
                speagCount = -1;
            }
        });
    }

    @Override
    protected void onStart() { //定时任务 拌和站 10秒一次,路上1分钟
        super.onStart();
        reQuest();
    }

    private void reQuest() {
        final JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        if (jsonArrayFail.size() > 0) {
            s = com.alibaba.fastjson.JSONObject.toJSONString(jsonArrayFail);
        } else {
            s = com.alibaba.fastjson.JSONObject.toJSONString(jsonArray);
        }
        if (TextUtils.isEmpty(location)) {
            handler.sendEmptyMessageDelayed(1, 1000);//1秒给服务器传输地图
            return;
        }

        HttpParams httpParams = new HttpParams();
        httpParams.put(HttpConstant.TOKEN, getToken());
        httpParams.put("carId", carId);
        httpParams.put("carNumber", carNumber);
        httpParams.put("location", newLocation);//长在前，短在后
        httpParams.put("carSiteXy", s);
        OkGo.<BindCarInfo>post(StringUtils.join(getMix(), HttpConstant.CAR_LIST_INFO)).params(httpParams).tag(this).execute(new JsonCallback<BindCarInfo>(BindCarInfo.class, this) {
            @Override
            public void onSuccess(Response<BindCarInfo> response) {
                //获取上次数据成功返回的时间
                jsonArrayFail.clear();
                data = response.body().getData();
                String isBingding = data.getIsBingding();
                if (!TextUtils.isEmpty(isBingding) && isBingding.equals("2")) {
                    mNoinfoCl.setVisibility(View.VISIBLE);
                    mZhanKaiTv.setVisibility(View.GONE);
                } else {
                    orderCar = data.getOrderCar();
                    par = data.getPar();
                    BindCarInfo.DataBean.NoticeBean notice = data.getNotice();
                    if (notice != null) {
                        BindCarInfo.DataBean.NoticeBean.InfoBean info = notice.getInfo();
                        String canCube = info.getCanCube();
                        long carIdLong = info.getCarId();
                        String carNumberStr = info.getCarNumber();//车
                        int unit = info.getUnit();//机组
                        if (carIdLong!=0){
                            if (!TextUtils.isEmpty(DriverHome3Activity.this.carId)) {
                                if (DriverHome3Activity.this.carId.equals(String.valueOf(carIdLong))&&isSpeak) {//信息相等的话就开始
                                        speagCount++;
                                    if (speagCount < 4 && isSpeakShow) {//连续呼叫四次
                                        hint = unit + "#机组正在搅拌,请" + carNumberStr + "#车前往该机组";
                                        showSettingSpeak(hint);
                                    } else {
                                        if (textToSpeech != null) {//释放资源
                                            textToSpeech.stop();
                                        }
                                        if (aliertDialogSpeak != null && aliertDialogSpeak.isShowing()) {
                                            aliertDialogSpeak.dismiss();
                                        }
                                    }
                                }else {
                                    if (textToSpeech != null) {//释放资源
                                        textToSpeech.stop();
                                    }
                                    if (aliertDialogSpeak != null && aliertDialogSpeak.isShowing()) {
                                        aliertDialogSpeak.dismiss();
                                    }
                                }
                            }
                        }else {//将拌合站和浇筑地点为空
                            pourSiteXy="";
                            mixXy="";
                            isSpeak=true;
                            speagCount=-1;
                        }

                    }
                    if (orderCar != null) {
                        long did = orderCar.getDid();//订单ID
                        long id = orderCar.getId(); //任务ID
                        if (did != 0) {
                            //拌和站位置
                            mixXy = data.getMixXy();
                            pourSiteXy = data.getPourSiteXy();
                            if (newDid != did) {  //如果订单变了 就重新创建
                                jz = -1;
                                bhz = -1;
                                isSpeakShow = true;
                                speagCount = -1;
                            }
                            if (id != 0 && ysID != id) {
                                jz = -1;
                                bhz = -1;
                                isSpeakShow = true;
                                speagCount = -1;
                            }
                            ysID = id;
                            newDid = did;
                            if (!TextUtils.isEmpty(pourSiteXy) && !TextUtils.isEmpty(mixXy)) {
                                status = data.getOrderCar().getStatus();
                                if (status == 8 || status == 7 || status == 14 || status == 13) {//正在运输  14开始拌合   7 拌合完成
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
                            status = 120;
                            isSpeakShow = true;
                            mixXy = data.getMixXy();
                            pourSiteXy = data.getPourSiteXy();
                            if (DriverHome3Activity.this.par != null && DriverHome3Activity.this.par.size() > 0) {
                                orderCount++;
                                if (orderCount < 3) {//只播放2次,需要重新写
                                    try {
                                        if (aliertDialogSpeak != null && aliertDialogSpeak.isShowing()) {
                                            aliertDialogSpeak.dismiss();
                                        }
                                        startAuto("您有新的订单,请注意查收");
                                    } catch (Exception e) {
                                        Log.e("speak", e.getMessage());
                                    }

                                }
                                allpar = new ArrayList<>();
                                allpar.clear();
                                allpar.addAll(data.getPar());
                                setDialogLayout(2);
                            } else {
                                if (markerOverlay != null) {
                                    markerOverlay.removeFromMap();
                                }
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
                        status = 100;
                        setDialogLayout(1);
                        if (markerOverlay != null) {
                            markerOverlay.removeFromMap();
                        }
                        if (polyline != null) { //删除绘
                            // 制的轨迹路线
                            polyline.remove();
                        }
                        if (polylineRed != null) {//绘制的红线删除掉
                            polylineRed.remove();
                        }
                    }
                    handler.sendEmptyMessageDelayed(1, 5000);//5秒给服务器传输地图
                }
            }


            @Override
            public void onError(Response<BindCarInfo> response) {//在这里进行存储,传输的时候一起传输
                super.onError(response);
                //8代表是正在运输 去浇筑的地点,7代表搅拌完成,9空车返回
                if (status == 8 || status == 9) {
                    if (jsonArray != null) {
                        jsonArrayFail.addAll(jsonArray);
                    }
                }
                handler.sendEmptyMessageDelayed(1, 2000);//2秒给服务器传输地图
            }
        });
    }

    private void initDialogUserView(View view) {
        mNameValueTv = (TextView) view.findViewById(R.id.tv_name_value);
        mJobValueTv = (TextView) view.findViewById(R.id.tv_job_value);
        mPhoneValueTv = (TextView) view.findViewById(R.id.tv_phone_value);
        mClassValueTv = (TextView) view.findViewById(R.id.tv_class_value);
        mDialogOutBut = (Button) view.findViewById(R.id.but_dialog_out);
        mZCBut = (Button) view.findViewById(R.id.but_zc);
        mJYBut = (Button) view.findViewById(R.id.but_jy);
        mGZBut = (Button) view.findViewById(R.id.but_gz);
        mZCBut.setOnClickListener(this);
        mJYBut.setOnClickListener(this);
        mGZBut.setOnClickListener(this);
        mDialogOutBut.setOnClickListener(this);
        String simpleName = SharedPreferenceUtil.getString(DriverHome3Activity.this, "simpleName");
        String userName = SharedPreferenceUtil.getString(DriverHome3Activity.this, "userName");
        String jobName = SharedPreferenceUtil.getString(DriverHome3Activity.this, "jobName");
        String phone = SharedPreferenceUtil.getString(DriverHome3Activity.this, "phone");
        mJobValueTv.setText(jobName);
        mNameValueTv.setText(userName);
        mPhoneValueTv.setText(phone);
        mClassValueTv.setText(simpleName);
        String carStatus = SharedPreferenceUtil.getString(DriverHome3Activity.this, "carStatus", "1");
        if (!TextUtils.isEmpty(carStatus)) {
            if (carStatus.equals("1")) {
                mZCBut.setBackground(getResources().getDrawable(R.drawable.green_but));
                mJYBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                mGZBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                return;
            } else if (carStatus.equals("2")) {
                mJYBut.setBackground(getResources().getDrawable(R.drawable.confrim_but));
                mZCBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                mGZBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                return;
            } else {
                mGZBut.setBackground(getResources().getDrawable(R.drawable.red_but));
                mJYBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                mZCBut.setBackground(getResources().getDrawable(R.drawable.butfause));
            }
        }

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
                //  showSettingSpeak("1#机组开始搅拌,请2#车前往该机组");

                break;
            case R.id.but_zc:// TODO 19/10/28
                requestCarStatus("1");
                break;
            case R.id.but_jy:// TODO 19/10/28
                requestCarStatus("2");
                break;
            case R.id.but_gz:// TODO 19/10/28
                requestCarStatus("3");
                break;
            default:
                break;
        }
    }

    private void requestCarStatus(final String state) {
        OkGo.<String>post(StringUtils.join(getMix(), HttpConstant.CAR_CHANGES_STATUS)).
                params(HttpConstant.TOKEN, SharedPreferenceUtil.getString(DriverHome3Activity.this, HttpConstant.TOKEN)).
                params("state", state).
                params("carId", carId).
                execute(new JsonCallback<String>(String.class, DriverHome3Activity.this) {
                    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(Response<String> response) {
                        SharedPreferenceUtil.putString(DriverHome3Activity.this, "carStatus", state);
                        ToastUtil.show(DriverHome3Activity.this, "状态提交成功");
                        if (state.equals("1")) {
                            mZCBut.setBackground(getResources().getDrawable(R.drawable.green_but));
                            mJYBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                            mGZBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                            //  mZCBut.setClickable(false);
                            return;
                        } else if (state.equals("2")) {
                            mJYBut.setBackground(getResources().getDrawable(R.drawable.confrim_but));
                            mZCBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                            mGZBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                            //  mJYBut.setClickable(false);
                            return;
                        } else {
                            mGZBut.setBackground(getResources().getDrawable(R.drawable.red_but));
                            mJYBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                            mZCBut.setBackground(getResources().getDrawable(R.drawable.butfause));
                            //  mGZBut.setClickable(false);
                        }

                    }
                });

    }

    private void outLoginRequest() {
        OkGo.<String>post(StringUtils.join(getUserUrl(), HttpConstant.OUT_ACCOUNT)).
                params(HttpConstant.TOKEN, SharedPreferenceUtil.getString(DriverHome3Activity.this, HttpConstant.TOKEN)).
                execute(new JsonCallback<String>(String.class, DriverHome3Activity.this) {
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
                                SharedPreferenceUtil.putString(DriverHome3Activity.this, HttpConstant.TOKEN, "");
                                finish();
                            } else {
                                ToastUtil.showToast(DriverHome3Activity.this, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showToast(DriverHome3Activity.this, HttpConstant.JSON_EXCEPTION);
                        }
                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
//        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
//        textToSpeech.shutdown(); // 关闭，释放资源
        if (textToSpeech != null) {
            textToSpeech.stop();//语音停止
        //    textToSpeech.shutdown();//释放资源以后,第二次不播放
            //  textToSpeech = null;//赋值为空
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeCallbacks(runnable);
        if (locationClient != null) {
            locationClient.stopLocation();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            //不管是不是在阅读,都打断
            //关闭,释放资源
            textToSpeech = null;
        }
    }
}