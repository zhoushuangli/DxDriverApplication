package com.daxi.dxdriverapplication.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daxi.dxdriverapplication.R;
import com.daxi.dxdriverapplication.config.Constant;
import com.daxi.dxdriverapplication.config.HttpConstant;
import com.daxi.dxdriverapplication.utils.ActivityUtils;
import com.daxi.dxdriverapplication.utils.SharedPreferenceUtil;
import com.lzy.okgo.OkGo;

/**
 * Created by jason on 2018/5/31
 */
public abstract class BaseActivity extends AppCompatActivity {


    private RelativeLayout mEmptyRl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); //设置横屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ActivityUtils.addActivity(this);
    }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(R.layout.activity_base, null);
        //设置填充activity_base布局
        super.setContentView(view);
//        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
//            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
//        }
        //加载子类Activity的布局
        FrameLayout container = (FrameLayout) findViewById(R.id.add_framelayout);
        View childView = LayoutInflater.from(this).inflate(layoutResID, null);
        container.addView(childView, 0);
        initView();
        initData();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }
    public abstract void initView();

    public abstract void initData() ;

    /**
     * 用户
     */
    public String getUserUrl() {
    //  return "http://192.168.15.119:9001";
        return SharedPreferenceUtil.getString(this, Constant.USER_URL);
    }

    /**
     * 物资
     */

    public String getMaterials() {

        return SharedPreferenceUtil.getString(this, Constant.MATERIALS_URL);
    }

    /**
     * 拌和站
     */
    public String getMix() {
      //  return "http://192.168.15.119:9002";
        return SharedPreferenceUtil.getString(this, Constant.MIX_URL);
    }

    /**
     * 考勤
     */
    public String getAttendance() {
        return SharedPreferenceUtil.getString(this, Constant.FINANCE_URL);
    }

    /**
     * Token
     */
    public String getToken() {
        return SharedPreferenceUtil.getString(this, HttpConstant.TOKEN);
    }


    /**
     * 空数据显示
     */
    /*
    app字体不跟随系统变化大小,这个放在基类
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        res.getConfiguration().fontScale = 1;
        res.updateConfiguration(null, null);
        return res;
    }

    // 设置返回按钮的监听事件


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
        overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
        overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);

    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);

    }

    /**
     * 通过Intent传递
     */
    public void startActivity(Intent intent, Class<?> cls) {
        intent.setClass(this, cls);
        startActivity(intent);
        this.overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);

    }

    /**
     * 通过Intent传递
     */
    public void startActivity(Intent intent, Class<?> cls, Bundle bundle) {
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        startActivity(intent);
        this.overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);

    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
    }

    public void startActivityIntent(Class<?> cls, Intent intent) {
        intent.setClass(this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
    }

    /**
     * onResume统计时长
     * onResume onPageStart统计页面
     * （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。
     */


    @Override
    protected void onPause() {
        super.onPause();


    }

    /**
     * OkGo.getInstance().cancelTag(this); 销毁的时候取消请求
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.removeActivity(this);
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in, R.anim.to_right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
