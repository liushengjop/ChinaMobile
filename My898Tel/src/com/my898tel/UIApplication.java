package com.my898tel;

import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.baidu.frontia.FrontiaApplication;
import com.my898tel.moble.ContactBean;
import com.my898tel.util.Unit_XML;
import com.my898tel.util.Util_file;

public class UIApplication extends FrontiaApplication {
    private List<ContactBean> contactBeanList;
    private HashMap<String, String> hashmap;


    private   boolean isSoftCall = true;

    public HashMap<String, String> getHashmap() {
        return hashmap;
    }

    public void setHashmap(HashMap<String, String> hashmap) {
        this.hashmap = hashmap;
    }

    public List<ContactBean> getContactBeanList() {
        return contactBeanList;
    }

    public void setContactBeanList(List<ContactBean> contactBeanList) {
        this.contactBeanList = contactBeanList;
    }

    private static UIApplication instance;

    public synchronized static UIApplication getInstance() {
        return instance;
    }

    /**
     * 拨号
     */
    public static void callPhone(Context context) {

        if (!Util_file.getIsRunApplication(context).equals("true")) {
            Toast.makeText(context, "软件已经被停用", Toast.LENGTH_SHORT).show();
            return;
        }

        String cornet = context.getResources().getString(R.string.call_need_add);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cornet));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


//    /**
//     * 拨号
//     */
//    public static void callPhone(Context context, String phone) {
//
//        if (!Util_file.getIsRunApplication(context).equals("true")) {
//            Toast.makeText(context, "软件已经被停用", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String cornet = context.getResources().getString(R.string.call_need_add);
//        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cornet + ",9" + phone + "#"));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }


    /**
     * 拨号
     */
    public static void callPhone(Context context, String phone) {

        if (!Util_file.getIsRunApplication(context).equals("true")) {
            Toast.makeText(context, "软件已经被停用", Toast.LENGTH_SHORT).show();
            return;
        }
        String cornet = context.getResources().getString(R.string.call_need_add);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        Unit_XML.saveIsAppCall(true);


    }
    /**
     * 发送短信
     *
     * @param context
     * @param message
     * @param mobile
     */
    public static void sendSmsInsert(Context context, String message, String mobile) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(
                context, 0, new Intent(), 0);
        if (message.length() >= 70) {
            // 短信字数大于70，自动分条
            List<String> ms = smsManager.divideMessage(message);

            for (String str : ms) {
                // 短信发送
                smsManager.sendTextMessage(mobile, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(mobile, null, message, sentIntent, null);
        }

        ContentValues values = new ContentValues();
        // 发送时间
        values.put("date", System.currentTimeMillis());
        // 阅读状态
        values.put("read", 0);
        // 1为收 2为发
        values.put("type", 2);
        // 送达号码
        values.put("address", mobile);
        // 送达内容
        values.put("body", message);
        // 插入短信库

        context.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
    }


    /**
     * init and get the screen Widthand height
     *
     * @return
     */
    int screenWidth = 0, screenHeight = 0;

    public float density;

    public String getScreenWH() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = instance.getResources().getDisplayMetrics();
        density = dm.density;
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        return screenWidth + "*" + screenHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    public int getScreenWidth() {
        if (screenWidth == 0) {
            getScreenWH();
        }
        return screenWidth;
    }

    public int getScreenHeight() {
        if (screenHeight == 0) {
            getScreenWH();
        }
        return screenHeight;
    }

    /**
     * get the device token
     *
     * @return
     */
    public String getDeviceUA() {
        return android.os.Build.MODEL + "(" + android.os.Build.VERSION.RELEASE + ")";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * Retrieves application's version number from the manifest
     *
     * @return
     */
    public String getVersion() {
        String version = "3.4.0";
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    /**
     * 判断是否在前台运行
     *
     * @return
     */
    public boolean isApplication() {
        String packageName = this.getPackageName();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否在前台运行
     *
     * @return
     */
    public boolean isCurrentActivity(Class<?> target) {
        String packageName = target.getName();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取本机Mac 地址方法
     *
     * @return
     */
    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        try {
            return info.getMacAddress().replace(":", "");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }





    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


}
