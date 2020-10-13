package com.daxi.dxdriverapplication.config;


import com.daxi.dxdriverapplication.base.BaseApplication;

public class Constant {
    /**
     * 模块地址
     */
    public final static String EVR_URL = "eva";
    public final static String FINANCE_URL = "finance";
    public final static String MATERIALS_URL = "materials";
    public final static String USER_URL = "user";
    public final static String HRS_URL = "hrs";
    public final static String DEVICE_URL = "device";
    public final static String MIX_URL = "mix";
    public final static String PROPLANS_URL = "proplans";


    /**
     * BaseDialog 按钮下标
     */
    public final static int DIALOG_WHICH_OK = 0;
    public final static int DIALOG_WHICH_CANCEL = 1;
    public final static int DIALOG_TITLE_DEFAULT = -1;

    /**
     * 全局tag
     */
    public final static String TAG = BaseApplication.getInstance().getPackageName();

    /**
     * 常用参数
     */
    public final static String TYPE = "type";

    public final static String BEAN = "bean";

    public final static String ID= "id";

    public final static String TIME="time";

    public final static String WIFI="wifi";
    /**
     * 账号
     */

    public final static String ACCOUNT = "account";
    public final static String PWD = "pwd";
    public final static String REGISTER_ID = "register";
    public static final String LOCAL_BROADCAST = "com.daxi.application.receiver.LOCAL_BROADCAST";
    //选择图片
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
}
