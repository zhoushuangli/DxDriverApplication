package com.daxi.dxdriverapplication.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


import com.daxi.dxdriverapplication.base.BaseApplication;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 2018/6/5
 */
@SuppressLint("NewApi")
public class WisdomMyTool {

    private final String TAG = "WisdomMyTool";
    private static Context mContext = BaseApplication.getInstance().getAppContext();
    private static Context ctx = BaseApplication.getInstance().getAppContext();
    public static final String APP_NAME = "DingDong"; //文件名常量,dingdong
    public static final String PERFERS_WIFIACCOUNT = "perfers_wifiAccount"; //shareperfer,WiFi账号
    public static final String PERFERS_SSID_ISHIDDEN = "perfers_ssid_IsHidden"; //shareperfer,ssid是否隐藏

    public WisdomMyTool(Context ctx) {
        this.mContext = ctx;
    }

    /**
     * @return account name
     */

    /**
     * Hide Input Method
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = activity.getCurrentFocus();
        if (focus != null)
            inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * getSystemLanguage
     *
     * @return
     */
    public static String getLanguageEnv() {
        Locale l = Locale.getDefault();
        String language = l.toString();
        if ("zh_CN".equals(language)) {
            language = "zh_CN";

        } else if ("ru_RU".equals(language)) {
            language = "ru_RU";

        } else {
            language = "en_US";
        }
        return language;
    }

    /*public static boolean isEmail(String str) {
		if (str.length() < 4){
			return false;
		}
		String reg = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
		Pattern pat = Pattern.compile(reg);
		Matcher mat = pat.matcher(str);
		return mat.matches();
	}*/

    public static final String MSG_REPLACE_STR = "%s";
    public static int count;

    public static String replace(String src, String... str) {
        if (str == null) {
            return src;
        }
        count = 0;
        int count = countStr(src, MSG_REPLACE_STR);
        if (count != str.length) {
            return null;
        }
        for (int i = 0; i < str.length; i++) {
            src = src.replaceFirst(MSG_REPLACE_STR, str[i]);
        }
        count = 0;
        return src;
    }

    /**
     * 计算src中包含str的个数
     *
     * @return
     */
    public static int countStr(String src, String str) {
        if (src.indexOf(str) == -1) {
            return 0;
        } else if (src.indexOf(str) != -1) {
            count++;
            countStr(src.substring(src.indexOf(str) + str.length()), str);
            return count;
        }
        return 0;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getWisdomVersion(Context ctx) {
        String version = null;
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static int getVersionCode() {
        PackageManager manager = mContext.getPackageManager();
        PackageInfo info = null;
        int versionCode = -1;
        try {
            info = manager.getPackageInfo(mContext.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private NotificationManager manager;

    private boolean SDK_VERDION = false;
    private Notification notification;
    private java.lang.reflect.Field field;

    //    判断当前版本以及修改背景颜色
    public boolean getSdkVersion(Context ctx) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            try {
//    	    	设置通知背景色
//    	    	Class<?> clazz = notification.getClass();
//        	    field = clazz.getDeclaredField("color");
//        	    field.setAccessible(true);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /*public void reminders(Context ctx, int id, int count, String content, String bid, String nick, int type) {
		nm = (NotificationManager) ctx.getSystemService(Activity.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.app_logo_40;
		notification.tickerText = content;
		notification.when = System.currentTimeMillis();
		Intent intent = new Intent(ctx, AlarmManager.class);
		intent.putExtra(BuddyInfo.BID, bid);
		intent.putExtra(BuddyInfo.NICK, nick);
		intent.putExtra(BuddyInfo.TYPE, type);
		//intent.putExtra(Constant.MAIN_TAB_CURRENTTAB, Constant.CURRENT_TAB_ALARMINFO);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(ctx, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		String str = String.valueOf(count);
		if(count < 0){
			str = "1";
		}else if(count > 99){
			str = "99+";
		}
		String text = ctx.getString(R.string.unread_alarm_count);
		text = text.replaceFirst("%", str);

		notification.setLatestEventInfo(ctx, ctx.getString(R.string.app_name), text, contentIntent);
		notification.defaults = Notification.DEFAULT_ALL;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(id, notification);
	}*/

    /**
     * 自定义Notification 新方法
     * 新的方法，本人在手机测试会崩溃，如果不行的话，可以继续使用旧的构建方法，毕竟高版本会兼容低版本的
     */

    public static void cancelNoftify(int noftifyId, Context ctx) {
        NotificationManager notifyManager = (NotificationManager) ctx.getSystemService(Activity.NOTIFICATION_SERVICE);
        notifyManager.cancel(noftifyId);
    }

    /**
     * 系统退出
     */
    public static void systemExit(Context context) {
        NotificationManager notiManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            notiManager.cancelAll();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /*public static boolean OutofMemory(Context ctx){
    	boolean bo = true;
    	if(!FileHelper.hasSDCard()){
    		bo = false;
    		Helper.showToast(ctx, ctx.getString(R.string.not_found_sdcard));
    	}

    	if(FileHelper.getSdInfo(ctx) <= 0){
    		bo = false;
    		Helper.showToast(ctx, ctx.getString(R.string.insufficient_phone_memory));
    	}

    	return bo;
    }*/
    public static boolean isCN(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            if (bytes.length == str.length()) {
                return false;
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("DefaultLocale")
    public static boolean isEmail(String email) {
        Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        if (email.equals("")) {
            return false;
        } else {
            email = email.toLowerCase();
            if (email.endsWith(".con")) return false;
            if (email.endsWith(".cm")) return false;
            if (email.endsWith("@gmial.com")) return false;
            if (email.endsWith("@gamil.com")) return false;
            if (email.endsWith("@gmai.com")) return false;
        }
        return emailer.matcher(email).matches();
    }

    /**
     * 用户名判断（字母、数字、下划线）
     *
     * @param username
     * @return
     */
    public static boolean userNameMatcher(String username) {
        String reg = "^(?!_+$)" + //not all are '_'
//				"^(?!\\d+$)" +  //not all are numbers
                "[a-zA-Z0-9_]" +   //only alpha and numbers and '_'
                "{6,20}$";      //length 6-64
        Pattern pattern = Pattern.compile(reg);
        Matcher math = pattern.matcher(username);
        if (!math.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 设备昵称判断（特殊字符过滤）
     *
     * @param devNickname
     * @return
     */
    public static boolean devNicknameMatcher(String devNickname) {
        String reg =
                "[a-zA-Z0-9\u4E00-\u9FA5_]" +       //字母、数字、下划线、中文匹配
                        "{1,10}$";           //length 1-10
        Pattern pattern = Pattern.compile(reg);
        Matcher math = pattern.matcher(devNickname);
        if (!math.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 开锁昵称判断（特殊字符过滤）
     *
     * @param devNickname
     * @return
     */
    public static boolean lockNicknameMatcher(String devNickname) {
        String reg =
                "[a-zA-Z0-9\u4E00-\u9FA5]" +       //字母、数字、下划线、中文匹配
                        "{1,10}$";           //length 1-10
        Pattern pattern = Pattern.compile(reg);
        Matcher math = pattern.matcher(devNickname);
        if (!math.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 手机号码
     * 移动：134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通：130,131,132,145,152,155,156,1709,171,176,185,186
     * 电信：133,134,153,1700,177,180,181,189
     */
    public static boolean checkMobileOne(String mobile) {
        String regex = "(\\+\\d+)?1(3[0-9]|4[579]|5[0-35-9]|6[0-35-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
        return Pattern.matches(regex, mobile);
    }

    public static boolean checkMobileTwo(String mobile) {
        String regex = "^1(3[0-9]|4[579]|5[0-35-9]|7[0135678]|8[0-9])\\\\d{8}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 手机号中间4为替换为*
     *
     * @param mobile
     * @return
     */
    public static String replaceMobile(String mobile) {
        String str = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return str;
    }

    /**
     * 大陆地区固话及小灵通
     * 区号：010,020,021,022,023,024,025,027,028,029
     * 号码：七位或八位
     */
    public static boolean checkPhone(String phone) {
        String regex = "^0(10|2[0-5789]|\\d{3})\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }


    public static String getMD5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(str.getBytes()); //MD5加密算法只是对字符数组而不是字符串进行加密计算，得到要加密的对象
        byte[] bs = md.digest();   //进行加密运算并返回字符数组
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bs.length; i++) {    //字节数组转换成十六进制字符串，形成最终的密文
            int v = bs[i] & 0xff;
            if (v < 16) {
                sb.append(0);
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    public static String cvtHex(byte[] bin) {
        StringBuffer stringBuf = new StringBuffer(bin.length * 2);
        int len = bin.length;
        for (int i = 0; i < len; i++) {
            if (((int) bin[i] & 0xff) < 0x10) {
                stringBuf.append("0");
            }
            stringBuf.append(Integer.toHexString(bin[i] & 0xFF));
        }
        return stringBuf.toString();
    }

    public static String md5(String text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cvtHex(digest.digest());
    }

	/*public static void openActionView(String url, Context ctx, String titleName){
    	Intent in = new Intent(ctx, WebView_Html5Activity.class);
    	in.putExtra("url", url);
    	in.putExtra("titleName", titleName);
    	ctx.startActivity(in);
    }*/

    public static boolean isWorked(String className, Context ctx) {
        ActivityManager myManager = (ActivityManager) ctx
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = ctx.getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static AnimationDrawable anim;

    public static void runFrame(String type, ImageView image, int count, Context ctx) {
        //完全编码实现的动画效果
        anim = new AnimationDrawable();
        for (int i = 1; i <= count; i++) {
            //根据资源名称和目录获取R.java中对应的资源ID
            int id = ctx.getResources().getIdentifier(type + i, "drawable", ctx.getPackageName());
            //根据资源ID获取到Drawable对象
            Drawable drawable = ctx.getResources().getDrawable(id); // java.lang.OutOfMemoryError
            //将此帧添加到AnimationDrawable中
            anim.addFrame(drawable, 100);
        }
        anim.setOneShot(false); //设置为loop
        image.setBackgroundDrawable(anim);  //将动画设置为ImageView背景
        anim.start();   //开始动画
    }


    public static void stopFrame() {
        if (anim != null && anim.isRunning()) {
            anim.stop();
        }
    }

    /**
     * 自动弹出输入框
     */
    public static void showInputMethod(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 100);
    }

    private int recBufSize;
    private AudioRecord audioRecord;
    private int frequency = 8000; //录制频率，单位hz.这里的值注意了,写的不好，可能实例化AudioRecord对象的时候,会出错,这取决于硬件设备
    private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int encodingBitRate = AudioFormat.ENCODING_PCM_16BIT;

    private void createAudioRecord() {
        //上传的字节流需要限制在 4096以内，否则处理有问题, 采用 22050(缓存3584) ，32000(5120)
        recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration,
                encodingBitRate);  //最低 7680

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                frequency,
                channelConfiguration,
                encodingBitRate,
                recBufSize);
    }

    /**
     * 判断是否有录音权限
     * bo: true 有录音权限  false： 没有录音权限
     */
    public boolean audioRecordPermissionCheck() {
        boolean bo = false;
        createAudioRecord();
        try {
            audioRecord.startRecording();//开始录制
            //定义缓冲
            byte[] buffer = new byte[recBufSize];

            int read = audioRecord.read(buffer, 0, buffer.length);

            if (AudioRecord.ERROR_INVALID_OPERATION != read
                    && AudioRecord.STATE_UNINITIALIZED != read) {
                // 做正常的录音处理
                bo = true;
            } else {
                //录音可能被禁用了，做出适当的提示
                bo = false;
            }
        } catch (Exception e) {
            bo = false;
        }

        try {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        } catch (IllegalStateException e1) {
        }
        return bo;
    }

    /**
     * 判断是否有摄像头权限
     */
    public static boolean checkCameraPermission(Context context) {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    /**
     * 判断是否有联系人权限和发送信息权限
     */
    public static boolean checkContactsPermission(Context context) {
        boolean bo = false;
        PackageManager pm = context.getPackageManager();
        boolean contacts = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(Manifest.permission.SEND_SMS, "com.com.wisdom"));
        boolean sms = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(Manifest.permission.READ_CONTACTS, "com.com.wisdom"));
        if (contacts && sms) {
            bo = true;
        }
        return bo;
    }

    /**
     * 获取当前登录的用户名
     *
     * @return
     */
    public static boolean isLogin(Context mCtx) {
        return true;
    }


    /**
     * 创建自定义圆形加载进度条
     *
     * @param ctx
     * @param msg
     * @return
     */
    private Dialog loadingDialog;

//    public Dialog createProgressDialog(Context ctx, int msg, boolean showFlag) {
//        if (loadingDialog == null) {
//            loadingDialog = MyProgressDialog.createLoadingDialog(ctx);
//            if (msg != Constant.PROGRESS_ERROR_CODE) {
//                TextView tv_loading = (TextView) loadingDialog
//                        .findViewById(R.id.tv_loading);
//                tv_loading.setText(msg);
//                if (!showFlag) {
//                    tv_loading.setVisibility(View.GONE);
//                } else {
//                    tv_loading.setVisibility(View.VISIBLE);
//                }
//            }
//            loadingDialog.show();
//        }
//        return loadingDialog;
//    }

    /**
     * 取消加载进度条
     */
    public void stopProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * 登录界面停止已经消失的提示框
     */
    public void stopDisappearProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * 退出当前账号,清理用户登录数据,注销Jpush推送
     *
     * @param context
     */

    /**
     * 消息管理,字符串截取
     */
    public static String WisdomSubString(String strTemp, String startStr, String endStr) {

        if (StringUtils.isBlank(strTemp)) {
            throw new NullPointerException("Exception, WisdomMyTool wisdomSubString StrTemp is Null...");
        }

        int startIndex = 0;
        int endIndex = strTemp.length();

        if (startStr != null) {
            startIndex = strTemp.indexOf(startStr);
        }

        if (endStr != null) {
            endIndex = strTemp.indexOf(endStr);
        }
        String str = strTemp.substring(startIndex, endIndex);
        return str;
    }


    public HashMap.Entry<Long, Integer> getMapByIndex(HashMap<Long, Integer> map, int index) {
        //这里将map.entrySet()转换成list
        List<HashMap.Entry<Long, Integer>> list = new ArrayList<Map.Entry<Long, Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator<HashMap.Entry<Long, Integer>>() {
            //降序排序
            public int compare(HashMap.Entry<Long, Integer> o1,
                               HashMap.Entry<Long, Integer> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });
        HashMap.Entry<Long, Integer> mapping = list.get(index);
        return mapping;
    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活
     * Created by cafeting on 2017/2/4.
     *
     * @param context 上下文
     * @param uid     已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (RunningServiceInfo appProcess : runningServiceInfos) {
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }


    //获取已安装应用的 uid，-1 表示未安装此应用或程序异常
    public static int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 赋值华为推送
     */
   /* public static void setHuaweiToken(Context ctx, String token){
        if(wisdomPreference == null){
            wisdomPreference = new WisdomPreference(ctx);
        }
        wisdomPreference.putString(Constant.HUAWEI_TOKEN, token);
    }*/

    /*public static String getHuaweiToken(Context ctx){
        if(wisdomPreference == null){
            wisdomPreference = new WisdomPreference(ctx);
        }
        return wisdomPreference.getString(Constant.HUAWEI_TOKEN);
    }*/

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean serviceIsRunning(Context context, String ServiceName) {
        if (StringUtils.isBlank(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得分发地址
     *
     * @param
     * @return
     */


    //log4k问题
    public static void log4K(String tag, String str) {
        int index = 0; // 当前位置
        int max = 3800;// 需要截取的最大长度,别用4000
        String sub;    // 进行截取操作的string
        while (index < str.length()) {
            if (str.length() < max) { // 如果长度比最大长度小
                max = str.length();   // 最大长度设为length,全部截取完成.
                sub = str.substring(index, max);
            } else {
                sub = str.substring(index, max);
            }
            index = max;
            max += 3800;
        }
    }


}
