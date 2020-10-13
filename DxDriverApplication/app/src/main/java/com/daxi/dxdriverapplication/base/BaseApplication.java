package com.daxi.dxdriverapplication.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;
import android.util.DisplayMetrics;


import com.daxi.dxdriverapplication.BuildConfig;
import com.daxi.dxdriverapplication.activity.LocationService;
import com.daxi.dxdriverapplication.bean.UserInfoBean;
import com.daxi.dxdriverapplication.utils.ScreenUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
    private static BaseApplication mInstance;
    private static Context context;
    ArrayList<Activity> list = new ArrayList<Activity>();
    public String longitude, latitude, locationAddress;

    public UserInfoBean.DataBean getUser() {
        return user;
    }

    private UserInfoBean.DataBean user;
    public Location LOCATION = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);


    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Intent location = new Intent(getApplicationContext(), LocationService.class);
//        startService(location);
        mInstance = this;
        initOkGo();
        context = getApplicationContext();
//        Intent location = new Intent(getApplicationContext(), LocationService.class);
//        startService(location);
    }

    public Context getAppContext() {
        return getApplicationContext();
    }

    //BaseApplication 只有一个,所以写成单利
    public static BaseApplication getInstance() {
        return mInstance;
    }




    private void initOkGo() {


        //        HttpParams params = new HttpParams();
        //        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        //        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
            loggingInterceptor.setColorLevel(Level.WARNING);                               //log颜色级别，决定了log在控制台显示的颜色
            builder.addInterceptor(loggingInterceptor);
        }//添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(20 * 200, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(20 * 200, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(20 * 200, TimeUnit.MILLISECONDS);   //全局的连接超时时间   默认写了一分钟10秒钟
      //  AddHeaderRequestInterceptor addHeaderRequestInterceptor = new AddHeaderRequestInterceptor();
      // builder.addInterceptor(addHeaderRequestInterceptor);
    //    builder.addNetworkInterceptor(addHeaderRequestInterceptor);
        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
       // builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        //  builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        //   HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        //  HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        // builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        //  builder.hostnameVerifier(new SafeHostnameVerifier());
        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo instance = OkGo.getInstance();
        instance.init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                // .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)//全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0);
                //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
              //  .addCommonHeaders(headers);                  //全局公共头
        //                  .addCommonParams(null);                       //全局公共参数


    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    public void setUserInfo(UserInfoBean.DataBean data) {
        this.user = data;
    }
}
