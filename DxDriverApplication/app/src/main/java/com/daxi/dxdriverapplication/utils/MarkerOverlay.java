package com.daxi.dxdriverapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.activity.DriverHome2Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2016/12/19.
 */

public class MarkerOverlay {


    private AMap aMap;
    private LatLng centerPoint;
    private Marker centerMarker;
    private AppCompatActivity appCompatActivity;
    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();
    private ArrayList<String> mtitle = new ArrayList<String>();
    private Bitmap bitmap;
    private List<LatLng> pointList;


    public MarkerOverlay(AMap amap,  String centerpoint, AppCompatActivity mainMapActivity) {
        this.aMap = amap;
        this.appCompatActivity = mainMapActivity;
        this.centerPoint = new LatLng(Double.parseDouble(centerpoint.split(",")[0]), Double.parseDouble(centerpoint.split(",")[1]));
        initCenterMarker();

    }

    public MarkerOverlay(AMap map, String mixXy, String pourSiteXy, String location, AppCompatActivity mainMapActivity) {
        this.aMap = map;
        this.centerPoint = new LatLng(Double.parseDouble(location.split(",")[0]), Double.parseDouble(location.split(",")[1]));
        this.appCompatActivity = mainMapActivity;
        initList(mixXy,pourSiteXy);
         initCenterMarker();
        //setCenterPoint(centerPoint);
    }

    private void initList(String mixXy,String pourSiteXy) {
        pointList = new ArrayList<>();
        pointList.clear();
        if (!TextUtils.isEmpty(mixXy)){
            pointList.add(new LatLng(Double.parseDouble(mixXy.split(",")[0]),Double.parseDouble(mixXy.split(",")[1])));
        }
        if (!TextUtils.isEmpty(pourSiteXy)){
            pointList.add(new LatLng(Double.parseDouble(pourSiteXy.split(",")[0]),Double.parseDouble(pourSiteXy.split(",")[1])));
        }
    }

    //初始化中心点Marker
    private void initCenterMarker() {
        LayoutInflater mInflater = LayoutInflater.from(appCompatActivity);
            View view = mInflater.inflate(R.layout.marker_car, null);
        Bitmap bitmap = convertViewToBitmap(view);
        this.centerMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_car))
                .position(centerPoint));
        centerMarker.showInfoWindow();
    }

    /**
     * 设置改变中心点经纬度
     *
     * @param centerpoint 中心点经纬度
     */
    public void setCenterPoint(LatLng centerpoint) {
        this.centerPoint = centerpoint;
        if (centerMarker == null)
              //  initCenterMarker();
            this.centerMarker.setPosition(centerpoint);
        centerMarker.setVisible(true);
        centerMarker.showInfoWindow();
    }

    /**
     * 添加Marker到地图中。
     */
    public void addToMap() {
        try {
            if (pointList==null||pointList.size()==0){
                return;
            }
         if (pointList.size()<1){
             return;
         }
            for (int i = 0; i < pointList.size(); i++) {
                setMarker( pointList.get(i).latitude, pointList.get(i).longitude, i);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setMarker(double wd, double jd, int mid) {
        LayoutInflater mInflater = LayoutInflater.from(appCompatActivity);
        if (mid==0){//拌和站
            View view = mInflater.inflate(R.layout.marker_bhz, null);
            TextView textView = view.findViewById(R.id.tv_bhz_name);
            textView.setText("拌和站");
            bitmap = convertViewToBitmap(view);
            Marker marker = aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wd, jd)).period(mid )//添加markerID
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_start))
                    .setFlat(true)
                    .draggable(false));
            marker.showInfoWindow();
        }else {
            View view = mInflater.inflate(R.layout.marker_jz, null);
            TextView textView = view.findViewById(R.id.tv_bhz_name);
            textView.setText("浇筑地点");
            bitmap = convertViewToBitmap(view);
            Marker marker = aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wd, jd)).period(mid)//添加markerID
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_end))
                    .setFlat(true)
                    .draggable(false));
            marker.showInfoWindow();
        }
    }
    //view 转bitmap
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 去掉MarkerOverlay上所有的Marker。
     */
    public void removeFromMap() {
        for (Marker mark : mMarkers) {
            mark.remove();
        }
        centerMarker.remove();
    }

    /**
     * 缩放移动地图，保证所有自定义marker在可视范围中，且地图中心点不变。
     */
    public void zoomToSpanWithCenter() {
        if (pointList != null && pointList.size() > 0) {
            centerMarker.setVisible(true);
            centerMarker.showInfoWindow();
            LatLngBounds bounds = getLatLngBounds(centerPoint, pointList);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds(LatLng centerpoint, List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (centerpoint != null) {
            for (int i = 0; i < pointList.size(); i++) {
                LatLng p = pointList.get(i);
                LatLng p1 = new LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    /**
     * 缩放移动地图，保证所有自定义marker在可视范围中。
     */
    public void zoomToSpan() {
        if (pointList != null && pointList.size() > 0) {
            if (aMap == null)
                return;
            centerMarker.setVisible(false);
            LatLngBounds bounds = getLatLngBounds(pointList);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    /**
     * 根据自定义内容获取缩放bounds
     */
    private LatLngBounds getLatLngBounds(List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < pointList.size(); i++) {
            LatLng p = pointList.get(i);
            b.include(p);
        }
        return b.build();
    }

    /**
     * 添加一个Marker点
     *
     * @param latLng 经纬度
     */
    public void addPoint(LatLng latLng) {
        pointList.add(latLng);
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        marker.setObject(pointList.size() - 1);
        mMarkers.add(marker);

    }
}
