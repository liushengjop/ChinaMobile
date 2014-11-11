package com.my898tel.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.my898tel.UIApplication;

/**
 * XML读取工具类
 *
 * @author liusheng
 */
public class Unit_XML {
    /**
     * 软件打开次数
     */
    public static String OPENCOUNT = "openCount";
    /**是否是本地拨号*/
    public static String ISLOCALCALL = "islocalcall";
    /**是否运行软件*/
    public static String ISRUNAPPLICATION = "isRunApplication";

    public static String APPCALL = "appCall";
//
    public static void saveOpenCount() {
        SharedPreferences preferences = UIApplication.getInstance().getSharedPreferences(OPENCOUNT, Activity.MODE_PRIVATE);
        preferences.edit().putInt(OPENCOUNT, getOpenCount() + 1).commit();
    }

    public static int getOpenCount() {
        SharedPreferences preferences = UIApplication.getInstance().getSharedPreferences(OPENCOUNT, Activity.MODE_PRIVATE);
        return preferences.getInt(OPENCOUNT, 0);
    }

    public static void saveIsAppCall(boolean flag){
        SharedPreferences preferences = UIApplication.getInstance().getSharedPreferences(APPCALL, Activity.MODE_PRIVATE);
        preferences.edit().putBoolean(APPCALL, flag).commit();
    }

    public static boolean readAppcALL(){
        SharedPreferences preferences = UIApplication.getInstance().getSharedPreferences(APPCALL, Activity.MODE_PRIVATE);
        return preferences.getBoolean(APPCALL,false);
    }
    
    /**
     * 是否是本地拨号

     */
    public static void saveLocalCall(String flag)
    {
    		SharedPreferences preferences = UIApplication.getInstance().getSharedPreferences(OPENCOUNT, Activity.MODE_PRIVATE);
        preferences.edit().putString(ISLOCALCALL, flag).commit();
    }
    
    /***
     * 读取是否是软件拨号的标示
     * @return
     */
    public static String getLocalCall()
    {
    	  SharedPreferences preferences = UIApplication.getInstance().getSharedPreferences(OPENCOUNT, Activity.MODE_PRIVATE);
          return preferences.getString(ISLOCALCALL, "");
    }
    
    public static void saveIsRunApplication(boolean flag)
    {
    		SharedPreferences preferences = UIApplication.getInstance().getSharedPreferences(ISRUNAPPLICATION, Activity.MODE_PRIVATE);
        preferences.edit().putBoolean(ISRUNAPPLICATION, flag).commit();
    }
    
    public static boolean getIsRunApplication()
    {
    		SharedPreferences preferences = UIApplication.getInstance().getSharedPreferences(ISRUNAPPLICATION, Activity.MODE_PRIVATE);
        return preferences.getBoolean(ISLOCALCALL, true);
    }
}
