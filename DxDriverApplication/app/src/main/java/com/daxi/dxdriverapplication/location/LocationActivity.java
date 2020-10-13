package com.daxi.dxdriverapplication.location;

import android.graphics.Bitmap;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.bean.DataStringBean;
import com.daxi.dxdriverapplication.net.JsonCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;


import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.daxi.dxdriverapplication.utils.MarkerOverlay.convertViewToBitmap;


public class LocationActivity extends AppCompatActivity {

    private LocationManager lm;
    private static final String TAG = "MainActivity";
    private long minTime = 1000 * 5;
    private float minDistance = 0;
    private Polyline polyline;
    private static int period = 1000 * 5; // 5s
    private String url = "";// 提交的接口url
    private long lastMillis = 0;// 上一次提交的时间

    private String longitude = ""; // 经度
    private String latitude = ""; // 维度

    SimpleDateFormat format = new SimpleDateFormat("yyyy年mm月dd日 HH:mm:ss");
    private Button mStartBut;
    private TextView mLocationTv;
    private TextView mTimeTv;
    private TextView mVxTv;
    private GPSUtils gpsUtils;
    Handler handler = new Handler();
    private Location location;
    private AMap aMap;
    private Date date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat formatter;
    private Marker marker;
    private ArrayList<LatLng> allNewLatLngGo = new ArrayList<>();//新建的去浇筑地点的位置

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_demo);
        //获取地图控件引用
        MapView mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        date = new Date();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14)); //和 setPosition()方法冲突，只设置缩放大小时使用
        initView();
        //初始化GPS
        gpsUtils = new GPSUtils(LocationActivity.this);
        handler.postDelayed(runnable, 0);
        formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    private void initView() {
        mStartBut = (Button) findViewById(R.id.but_start);

        mLocationTv = (TextView) findViewById(R.id.tv_location);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mVxTv = (TextView) findViewById(R.id.tv_vx);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //获取位置信息
            location = gpsUtils.getLocation();

            if (location != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateView(location);
                    }
                });
                //  handler.removeCallbacks(runnable);
                handler.postDelayed(this, 2000);
            } else {//失败的话马上开始
                handler.postDelayed(this, 0);
            }
        }
    };

    /**
     * 实时更新文本内容
     *
     * @param location
     */
    private void updateView(Location location) {
        if (location != null) {
            mLocationTv.setText("定位成功:" + "精度" + location.getLongitude() + "     " + "纬度" + location.getLatitude());
            long now = location.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            String format = formatter.format(calendar.getTime());
            mTimeTv.setText("定位返回时间" + format);
            LatLng latLng = new LatLng(Double.parseDouble(location.getLatitude() + ""), Double.parseDouble(location.getLongitude() + ""));
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String s = latitude + "," + longitude;
            Map<String,String> map =new ArrayMap<>();
            map.put("xy", s);
        //    reQuestLocation(map);
            initCenterMarker(latLng);
        } else {
            // 清空EditText对象
            mLocationTv.getEditableText().clear();
        }

    }

    //初始化中心点Marker
    private void initCenterMarker(LatLng centerPoint) {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.marker_car, null);
        Bitmap bitmap = convertViewToBitmap(view);
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(centerPoint));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        if (marker != null) marker.remove();
        marker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_car))
                .position(centerPoint));
        allNewLatLngGo.add(centerPoint);
        if (polyline != null) polyline.remove();
        polyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
                .addAll(allNewLatLngGo)
                .useGradient(true)
                .width(12));

        //


    }

    public void reQuestLocation(Map map) {
        String s = JSONObject.toJSONString(map);
        HttpParams httpParams = new HttpParams();
        httpParams.put("xy", s);
        OkGo.<DataStringBean>get("http://192.168.88.114:8100/dx-internal/assessGradeLog/addXy").params(httpParams).execute(new JsonCallback<DataStringBean>(DataStringBean.class, this) {
            @Override
            public void onSuccess(Response<DataStringBean> response) {

            }
        });
    }

}
