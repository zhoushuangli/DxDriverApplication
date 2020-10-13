package com.daxi.dxdriverapplication.activity;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.adapter.QueueDialogAdapter;
import com.daxi.dxdriverapplication.base.BaseActivity;
import com.daxi.dxdriverapplication.base.BaseApplication;
import com.daxi.dxdriverapplication.bean.BindCarInfo;
import com.daxi.dxdriverapplication.bean.DataStringBean;
import com.daxi.dxdriverapplication.bean.UserInfoBean;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.net.JsonCallback;
import com.daxi.dxdriverapplication.utils.SharedPreferenceUtil;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.daxi.dxdriverapplication.widget.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DriverHomeActivity extends BaseActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    MapView mMapView = null;
    private TextView mPdTv;
    private TextView mSxTv;
    private TextView mNumTv;
    private TextView mJzTv;
    private View mView1;
    private RecyclerView mRecycler;
    private LoadingDialog loadingDialog;
    private String location;
    private String address;
    private Button mOutBut;
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
    private AlertDialog alertDialogUser;
    private List<BindCarInfo.DataBean.ParBean> par;
    private AlertDialog alertDialogList;
    private AlertDialog alertDialogDetail;
    private AMap aMap;
    private AMapLocationListener mLocationListener;
    //定位需要的数据
    LocationSource.OnLocationChangedListener mListener;
    private ConstraintLayout mNoTaskCl;
    private TextToSpeech tts;
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private AlertDialog alertDialogNoTask;
    private int orderCount = -1;
    private CoordinatorLayout scakBar;
    private AlertDialog aliertDialogSpeak;
    private String hint;
    private boolean isSpeakShow = true;
    private RecyclerView mRecycler1;
    private QueueDialogAdapter queueDialogAdapter;
    private LinearLayoutManager linearLayoutManager;
    private long newDid;
    private ImageView mZhanKaiTv;
    private boolean isAlertDialogList = true;
    private boolean isAlertDialogDetail = true;
    private boolean isAlertDialogNoTask = true;
    private AlertDialog allStatuDialog;
    private ImageView mZhankaiTv;

    /**
     * setZoomControlsEnabled设置是否显示缩放按钮
     * setMyLocationButtonEnabled设置显示定位按钮
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        initView();
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
//        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_point));// 设置小蓝点的图标
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
//       aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//定位、但不会移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
//        myLocationStyle.showMyLocation(true);
        //  aMap.moveCamera(CameraUpdateFactory.zoomTo(15)); //和 setPosition()方法冲突，只设置缩放大小时使用

    }

    @Override
    public void initView() {
        mOutBut = (Button) findViewById(R.id.but_out);
        mOutBut.setOnClickListener(this);
        mNoinfoCl = (ConstraintLayout) findViewById(R.id.cl_noinfo);
        mImage = (ImageView) findViewById(R.id.image);
        mImage.setOnClickListener(this);
        mNoTaskCl = (ConstraintLayout) findViewById(R.id.cl_no_task);
        //创建好dialog,加载不同的layout
        allStatuDialog = new AlertDialog.Builder(this, R.style.home).create();

//        mZhankaiTv = (ImageView) findViewById(R.id.tv_zhankai);
//        mZhankaiTv.setOnClickListener(this);
    }


    @Override
    public void initData() {
        tts = new TextToSpeech(this, this);
        //设置定位参数
        //初始化定位

        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationOption(getDefaultOption());
        mTimer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        location();
                    }
                });
            }
        };


    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);//可选，设置定位模式，可选的模式有高精度、仅设备GPS、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(8000);//可选，设置网络请求超时时间。默认为15秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setMockEnable(false);//如果您希望位置被模拟，请通过setMockEnable(true);方法开启允许位置模拟
        return mOption;
    }

    private void showDetail() {
        if (alertDialogList != null) {
            alertDialogList.dismiss();
        }
        if (alertDialogUser != null) {
            alertDialogUser.dismiss();
        }
        LayoutInflater factory = LayoutInflater.from(this);
        // 把布局文件中的控件定义在View中
        final View view = factory.inflate(R.layout.car_porgre_dialog, null);
        initDialogDetailView(view);

        if (alertDialogDetail == null) {
            alertDialogDetail = new AlertDialog.Builder(this, R.style.home).create();
        }
        if (!alertDialogDetail.isShowing()) {
            alertDialogDetail.show();
        }
        alertDialogDetail.setContentView(view);
        /* 获取对话框窗口 */

        Window dialogWindow = alertDialogDetail.getWindow();
        /* 设置显示窗口的宽高 */
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = 800;
        layoutParams.height = 1000;
        layoutParams.alpha = 0.95f;
        dialogWindow.setAttributes(layoutParams);
        dialogWindow.setGravity(Gravity.LEFT);
//                // 设定显示的View
        if (isSpeakShow) {//true 需要播放
            showSettingSpeak(view, hint);
        }
    }

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

        if (status == 14) {
            mStstusTv.setText("正在拌和");
            mStstusTv.setTextColor(ContextCompat.getColor(this, R.color.tv_caveat));
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
            return;
        }
        if (status == 7) {
            mStstusTv.setText("搅拌完成");
            mOkBut.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            return;
        }
        if (status == 8) {
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
                location();
            }
        });

    }

    /**
     * 第二个参数queueMode用于指定发音队列模式，两种模式选择
     * （1）TextToSpeech.QUEUE_FLUSH：该模式下在有新任务时候会清除当前语音任务，执行新的语音任务
     * （2）TextToSpeech.QUEUE_ADD：该模式下会把新的语音任务放到语音任务之后，
     * 等前面的语音任务执行完了才会执行新的语音任务
     *
     * @param hint
     */
    private void showSettingSpeak(View view1, String hint) {
        if (alertDialogDetail != null)
            alertDialogDetail.dismiss();
        if (aliertDialogSpeak == null) {
            aliertDialogSpeak = new AlertDialog.Builder(DriverHomeActivity.this, R.style.home).create();
        }
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_speak, null);

        if (tts == null) {
            tts = new TextToSpeech(this, this);
        }
        tts.speak(hint, TextToSpeech.QUEUE_FLUSH, null);

        if (!aliertDialogSpeak.isShowing()) {
            if (alertDialogDetail != null) {
                alertDialogDetail.dismiss();
            }
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
                alertDialogDetail.cancel();
                isSpeakShow = false;
            }
        });
    }

    /**
     * 　　仔细研读java api，发现：
     * <p>
     * 　　schedule(TimerTask task, long delay)的注释：Schedules the specified task for execution after the specified delay。大意是在延时delay毫秒后执行task。并没有提到重复执行
     * <p>
     * 　　schedule(TimerTask task, long delay, long period)的注释：Schedules the specified task for repeated fixed-delay execution, beginning after the specified delay。大意是在延时delay毫秒后重复的执行task，周期是period毫秒。
     * <p>
     * 　　这样问题就很明确schedule(TimerTask task, long delay)只执行一次，schedule(TimerTask task, long delay, long period)才是重复的执行。关键的问题在于程序员误以为schedule就是重复的执行，而没有仔细的研究API，一方面也是英文能力不够，浏览API的过程中不能很快的理解到含义。
     */
    @Override
    protected void onStart() { //定时任务 拌和站 10秒一次,路上1分钟
        super.onStart();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            location();
                        }
                    });
                }
            };

            mTimer.schedule(timerTask, 1000, 10000);
        }
    }

    private void location() {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    mLocationClient.stopLocation();//定位成功.停止定位
                    double latitude = aMapLocation.getLatitude();
                    double longitude = aMapLocation.getLongitude();
                    address = aMapLocation.getAddress();
                    location = String.valueOf(longitude) + "," + String.valueOf(latitude);
                    reQuest();
                } else {
                    ToastUtil.show(DriverHomeActivity.this, "定位失败,请重新定位");
                }
                Log.e("location", location);
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //定位请求超时时间
        mLocationOption.setHttpTimeOut(5000);
        //启动定位
        mLocationClient.startLocation();
    }

    private void reQuest() {
        HttpParams httpParams = new HttpParams();
        httpParams.put(HttpConstant.TOKEN, getToken());
        httpParams.put("carSiteXy", location);
        OkGo.<BindCarInfo>get(StringUtils.join(getMix(), HttpConstant.CAR_LIST_INFO)).params(httpParams).execute(new JsonCallback<BindCarInfo>(BindCarInfo.class, this) {
            @Override
                        public void onSuccess(Response<BindCarInfo> response) {
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
                        if (newDid != did) {
                            isSpeakShow = true;
                        }
                        newDid = did;
                        if (did != 0) {
                            showDetail();//详情接口展示
                        } else { //没有详情的话我们应该判断是不是没有任务和排队
                            isSpeakShow = true;
                            if (DriverHomeActivity.this.par != null && DriverHomeActivity.this.par.size() > 0) {
                                orderCount++;
                                if (orderCount < 1) {//只播放1次
                                    tts.speak("您有新的订单,请注意查看", TextToSpeech.QUEUE_FLUSH, null);
                                }
                                List<BindCarInfo.DataBean.ParBean> allpar = new ArrayList<>();
                                allpar.clear();
                                allpar.addAll(data.getPar());
                                mNoTaskCl.setVisibility(View.GONE);
                                initList(allpar);
                            } else {
                                orderCount = -1;
                                showNoTask();//没有任务
                            }
                        }
                    } else if (DriverHomeActivity.this.par == null || DriverHomeActivity.this.par.size() == 0) {
                        mNoTaskCl.setVisibility(View.VISIBLE);
                        isSpeakShow = true;
                        return;
                    }

                }
            }
        });
    }


    public void showUser() {
        if (alertDialogList != null) {
            alertDialogList.dismiss();
        }
        LayoutInflater factory = LayoutInflater.from(this);
        // 把布局文件中的控件定义在View中
        final View view = factory.inflate(R.layout.dialog_out_uesr, null);
        initDialogUserView(view);
        if (alertDialogUser == null) {
            alertDialogUser = new AlertDialog.Builder(this, R.style.home).create();
        }
        //   alertDialogUser.setCanceledOnTouchOutside(false);
        //   alertDialog.setCancelable(false);
        alertDialogUser.setView(view);
        alertDialogUser.show();
        /* 获取对话框窗口 */
        Window dialogWindow = alertDialogUser.getWindow();
        /* 设置显示窗口的宽高 */
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = 800;
        layoutParams.height = 1000;
        layoutParams.alpha = 0.95f;
        dialogWindow.setAttributes(layoutParams);
        dialogWindow.setGravity(Gravity.LEFT);
//                // 设定显示的View
    }

    public void showNoTask() {
        if (alertDialogList != null && alertDialogDetail.isShowing()) {
            alertDialogList.dismiss();
        }
        if (alertDialogDetail != null && alertDialogDetail.isShowing()) {
            alertDialogDetail.dismiss();
        }
        if (aliertDialogSpeak != null && aliertDialogSpeak.isShowing()) {
            aliertDialogSpeak.dismiss();
        }
        LayoutInflater factory = LayoutInflater.from(this);
        // 把布局文件中的控件定义在View中
        final View view = factory.inflate(R.layout.dialog_no_task, null);
        if (alertDialogNoTask == null) {
            alertDialogNoTask = new AlertDialog.Builder(this, R.style.home).create();
        }
        view.findViewById(R.id.cl_packUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(scakBar, "Action 被点击", Snackbar.LENGTH_SHORT).show();
                alertDialogNoTask.dismiss();
            }
        });
        //     alertDialogNoTask.setCanceledOnTouchOutside(false);
        //  alertDialogNoTask.setCancelable(false);
        if (!alertDialogNoTask.isShowing()) {
            alertDialogNoTask.show();
        }
        if (!isAlertDialogNoTask) {
            alertDialogNoTask.dismiss();
        }

        alertDialogNoTask.setContentView(view);
        /* 获取对话框窗口 */
        Window dialogWindow = alertDialogNoTask.getWindow();
        /* 设置显示窗口的宽高 */
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = 800;
        layoutParams.height = 500;
        layoutParams.alpha = 0.95f;
        dialogWindow.setAttributes(layoutParams);
        dialogWindow.setGravity(Gravity.LEFT);
//                // 设定显示的View
    }

    private void initDialogUserView(View view) {
        mNameValueTv = (TextView) view.findViewById(R.id.tv_name_value);
        mJobValueTv = (TextView) view.findViewById(R.id.tv_job_value);
        mPhoneValueTv = (TextView) view.findViewById(R.id.tv_phone_value);
        mDialogOutBut = (Button) view.findViewById(R.id.but_dialog_out);
        mDialogOutBut.setOnClickListener(this);
        UserInfoBean.DataBean user = BaseApplication.getInstance().getUser();
        if (user != null) {
            mJobValueTv.setText(user.getJobName());
            mNameValueTv.setText(user.getUserName());
            mPhoneValueTv.setText(user.getPhone());
        }

    }

    private void initList(List<BindCarInfo.DataBean.ParBean> allpar) {
        if (alertDialogUser != null && alertDialogUser.isShowing()) {
            alertDialogUser.dismiss();
        }
        if (alertDialogDetail != null && alertDialogDetail.isShowing() && isAlertDialogDetail) {
            alertDialogDetail.dismiss();
        }
        if (alertDialogNoTask != null && alertDialogNoTask.isShowing()) {
            alertDialogNoTask.dismiss();
        }
        LayoutInflater factory = LayoutInflater.from(this);
        // 把布局文件中的控件定义在View中
        final View view = factory.inflate(R.layout.dialog, null);
        if (alertDialogList == null) {
            alertDialogList = new AlertDialog.Builder(this, R.style.home).create();
        }
        if (!alertDialogList.isShowing() && isAlertDialogList) {
            alertDialogList.show();
        }
        alertDialogList.setContentView(view);

        /* 获取对话框窗口 */
        Window dialogWindow = alertDialogList.getWindow();
        /* 设置显示窗口的宽高 */
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = 800;
        layoutParams.height = 1000;
        layoutParams.alpha = 0.95f;
        dialogWindow.setAttributes(layoutParams);
        dialogWindow.setGravity(Gravity.LEFT);
//                // 设定显示的View
        initDialogView(view, allpar);
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
        if (mTimer != null) {
            mTimer.cancel();
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
            case R.id.image:// TODO 19/10/28
                showUser();
                break;
//            case R.id.tv_zhankai:// TODO 19/10/28
//                setShowDialog();
//                break;
            case R.id.but_ok:// TODO 19/10/28
                break;
            default:
                break;
        }
    }

    private void setShowDialog() {
        if (alertDialogUser != null && alertDialogUser.isShowing()) {
            alertDialogUser.dismiss();

        }
        if (alertDialogDetail != null && alertDialogDetail.isShowing()) {
            alertDialogDetail.dismiss();
            isAlertDialogDetail = false;
        }
        if (alertDialogNoTask != null && alertDialogNoTask.isShowing()) {
            alertDialogNoTask.dismiss();
            isAlertDialogNoTask = false;
        }
        if (alertDialogList != null && alertDialogList.isShowing()) {
            alertDialogList.dismiss();
            isAlertDialogList = false;
        }
    }

    private void outLoginRequest() {
        OkGo.<String>post(StringUtils.join(getUserUrl(), HttpConstant.OUT_ACCOUNT)).
                params(HttpConstant.TOKEN, SharedPreferenceUtil.getString(DriverHomeActivity.this, HttpConstant.TOKEN)).
                execute(new JsonCallback<String>(String.class, DriverHomeActivity.this) {
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
                                SharedPreferenceUtil.putString(DriverHomeActivity.this, HttpConstant.TOKEN, "");

                            } else {
                                ToastUtil.showToast(DriverHomeActivity.this, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showToast(DriverHomeActivity.this, HttpConstant.JSON_EXCEPTION);
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
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mTimer != null) {
            mTimer.cancel();
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

}