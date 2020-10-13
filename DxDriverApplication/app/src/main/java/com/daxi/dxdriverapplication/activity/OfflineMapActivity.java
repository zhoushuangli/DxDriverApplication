package com.daxi.dxdriverapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.utils.ToastUtil;
import com.daxi.dxdriverapplication.widget.AppUpdateProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Reimu on 2016/12/8.
 */

public class OfflineMapActivity extends Activity implements AMap.OnMapLoadedListener, AMapLocationListener, LocationSource, AMap.OnMarkerClickListener, OfflineMapManager.OfflineMapDownloadListener, View.OnClickListener {

    private MapView mapView;
    private AMap aMap;
    private AMapLocationClientOption opsition = null;
    private AMapLocationClient locationClient = null;

    private OfflineMapManager manager;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private OnLocationChangedListener mListener;//定位监听器
    private ProgressDialog dialog;
    private Button mOnclick;
    private int isDownload = 0;
    private Button mOnDetail;
    private AppUpdateProgressDialog appUpdateProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        initView();
        findViewById();
        mapView.onCreate(savedInstanceState);
        //初始化定位
        locationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        locationClient.setLocationListener(this);
        //初始化地图变量
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setMapType(AMap.MAP_TYPE_NAVI);
        aMap.setLocationSource(this);
        //设置显示定位按钮 并且可以点击
        // 是否可触发定位并显示定位层
        UiSettings settings = aMap.getUiSettings();
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        setMapoPtion();
    }

    private void initView() {
        mOnclick = (Button) findViewById(R.id.onclick);
        mOnclick.setOnClickListener(this);
        manager = new OfflineMapManager(this, this);

        manager.setOnOfflineLoadedListener(new OfflineMapManager.OfflineLoadedListener() {
            @Override
            public void onVerifyComplete() {
                ToastUtil.show(OfflineMapActivity.this, "成功加载");
            }
        });
        mOnDetail = (Button) findViewById(R.id.onDetail);
        mOnDetail.setOnClickListener(this);
        appUpdateProgressDialog = new AppUpdateProgressDialog(OfflineMapActivity.this);
    }

    /**
     * 初始化
     */
    public void findViewById() {
        mapView = (MapView) findViewById(R.id.map);
    }

    /**
     * 设置地位参数
     *
     * @param
     * @return
     */
    public void setMapoPtion() {
        //初始化定位参数
        opsition = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        opsition.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        opsition.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        opsition.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        opsition.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        opsition.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        opsition.setInterval(5000);
        //给定位客户端对象设置定位参数
        locationClient.setLocationOption(opsition);
        //启动定位
        locationClient.startLocation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null) {
            locationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            locationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 地图加载完成
     */
    @Override
    public void onMapLoaded() {

//        Toast.makeText(getApplicationContext(), "地图加载完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            aMapLocation.getLatitude();//获取纬度
            aMapLocation.getLongitude();//获取经度
            aMapLocation.getAccuracy();//获取精度信息
            aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            aMapLocation.getCountry();//国家信息
            aMapLocation.getProvince();//省信息
            aMapLocation.getCity();//城市信息
            aMapLocation.getDistrict();//城区信息
            aMapLocation.getStreet();//街道信息
            aMapLocation.getCityCode();//城市编码
            aMapLocation.getAdCode();//地区编码
            aMapLocation.getFloor();//获取当前室内定位的楼层
            //获取定位时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(aMapLocation.getTime());
            df.format(date);
            //设置缩放级别
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 17));
            //将地图移动到定位点
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
            //点击定位按钮 能够将地图的中心移动到定位点
            mListener.onLocationChanged(aMapLocation);
//            //添加图钉
//            aMap.addMarker( getMarkerOptions( aMapLocation ) );
            isFirstLoc = false;
            try {
                ArrayList<OfflineMapCity> downloadOfflineMapCityList = manager.getDownloadOfflineMapCityList();
                if (downloadOfflineMapCityList.size()>1){
                    for (int i = 0; i < downloadOfflineMapCityList.size(); i++) {
                        String city = downloadOfflineMapCityList.get(i).getCity();
                        if (city.contains(aMapLocation.getCity())) { //下载的城市列表中包含定位的城市 //设定一个变量,如果包含就是1,不好办就是0
                            isDownload = 1;
                            return;
                        } else {//不包含
                            isDownload = -1;
                        }
                    }

                }else {
                    isDownload = -1;
                }
                if (isDownload == -1) {
                    manager.downloadByCityCode(aMapLocation.getCityCode());
//                        //城市编码
//                        manager.updateOfflineCityByCode(aMapLocation.getCityCode());
                }

            } catch (AMapException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(OfflineMapActivity.this, "被点击", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 下载地图
     *
     * @param i
     * @param i1
     * @param s
     */
    @Override
    public void onDownload(int i, int i1, String s) {
        dialog = new ProgressDialog(this);


        switch (i) {
            case OfflineMapStatus.LOADING:
//                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                dialog.setMessage("正在下载地图请稍候...");
//                dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
//                dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//                dialog.show();
                appUpdateProgressDialog.setProgress(i1);
                appUpdateProgressDialog.show();
                Log.i("download", "正在下载" + i1);
                break;
            case OfflineMapStatus.ERROR:
                Log.i("download", "解压失败错误，数据有可能有问题，所以重新下载");
                break;
            case OfflineMapStatus.START_DOWNLOAD_FAILD:
                Log.i("download", "开始下载失败，已下载该城市地图");
                Toast.makeText(OfflineMapActivity.this, "下载失败，已下载该城市地图", Toast.LENGTH_SHORT).show();
                break;
            case OfflineMapStatus.PAUSE:
                Log.i("download", "暂停下载");
                break;
            case OfflineMapStatus.STOP:
                Log.i("download", "停止下载");
                break;
            case OfflineMapStatus.WAITING:
                Log.i("download", "等待下载");
                break;
            case OfflineMapStatus.SUCCESS:
                aMap.setLoadOfflineData(false);
                aMap.setLoadOfflineData(true);
                aMap.reloadMap();
                appUpdateProgressDialog.dismiss();
                Toast.makeText(OfflineMapActivity.this, "地图加载成功", Toast.LENGTH_SHORT).show();
                break;
            case OfflineMapStatus.UNZIP:
                Log.i("download", "正在解压");
                break;

        }

    }

    /**
     * 更新
     *
     * @param b
     * @param s
     */
    @Override
    public void onCheckUpdate(boolean b, String s) {
        Log.i("download", "Update:" + s + "--" + b);

    }

    /**
     * 删除
     *
     * @param b
     * @param s
     * @param s1
     */
    @Override
    public void onRemove(boolean b, String s, String s1) {
        Log.i("download", "Remove" + s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onclick:
                // TODO 20/03/06
                manager.remove("西安市");
                break;
            case R.id.onDetail:// TODO 20/03/09
                manager.remove("西安市");
                break;
            default:
                break;
        }
    }
}