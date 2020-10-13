package com.daxi.dxdriverapplication.config;

public class HttpConstant {
    public static final String RESULT_CODE = "code";
    public static final String RESULT_MAG = "msg";
    public static final String RESULT_DATA = "data";
    public static final String TOKEN = "Token";

    public static final String SUCCESS_CODE = "200";
    public static final String ERROR_SYSTEM = "500";
    public static final String UN_LOGIN = "100";
    public static final String SESSION_TIMEOUT = "010000";
    public static final String NO_DATA = "020000";
    public static final String OUT_TOKEN = "101";
    public static final String JSON_EXCEPTION = "网络解析异常，请稍后再试";

    //正式
    public static String BASE_URL = "https://user.dx185.com";
    //测试2
   // public static String BASE_URL = "http://itest-user.dx185.com";
    //分发地址
    public static String MODEl_BASE_URL = "/user/app/getLastInfo";
    //密码登陆接口
    public static String LOGIN_PWD = "/user/login/loginWithPassword";
    //退出登录
    public static String OUT_ACCOUNT = "/user/login/loginout";
    //用户信息
    public static String USER_INFO_APP = "/user/emp/getUserInfoApp";
    /**
     *调度员
     */

    //获取绑定信息
    public static String DEVICE_BIND_CAR_INFO="/mix/imctCar/getCarDatas";
    //绑定车辆
    public static  String BIND_CAR="/mix/imctCar/bindingDevice";
    //获取车辆信息
    public static String CAR_LIST_INFO="/mix/imctCar/getCarLevel";
    //到达浇筑地点
    public static String CAR_ARRIVALS = "/mix/imctCar/arrivalss" ;
    //车辆状态反馈
    public static String CAR_CHANGES_STATUS="/mix/imctCar/changeCarState";
}
