package com.my898tel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.my898tel.R;

/**
 * 文件保存在系统或SDCard，设置："Config.bFileOperationInSDCard;"+
 * "Config.fileOperationInSDCardPath;" 注：调试时，建议保存在SDCard，因为真机系统文件需要root才能看到；
 */
@SuppressLint("SimpleDateFormat")
public class Util_Date {

    public static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd EE HH:mm");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateFormat4 = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat dateFormat5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormat6 = new SimpleDateFormat("MM-dd EE HH:mm");
    public static SimpleDateFormat dateFormat7 = new SimpleDateFormat("  MM dd EE");
    public static SimpleDateFormat dateFormat9 = new SimpleDateFormat("MM-dd HH:mm");
    public static SimpleDateFormat dateFormat10 = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat dateFormat8 = new SimpleDateFormat(Util_G.getString(R.string.str_public_get_Date));
    private static Util_Date instance;

    public static Util_Date getInstance() {
        if (instance == null) {
            instance = new Util_Date();
        }
        return instance;
    }

    // 获取标准时间
    public static String getLoactionTime() {
        java.util.Date current = new java.util.Date();
        return instance.dateFormat5.format(current);
    }

    // 获取标准时间
    public String getTime(String str) {
        Date date;
        try {
            date = instance.dateFormat5.parse(str);
            return instance.dateFormat4.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    // 获取标准时间
    public String getTime2(String str) {
        Date date;
        try {
            date = instance.dateFormat2.parse(str);
            return instance.dateFormat10.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    // 获取标准时间
    public String getTime4(String str) {
        Date date;
        try {
            date = instance.dateFormat2.parse(str);
            return instance.dateFormat4.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    // 获取标准时间
    public String getTime1(String str) {
        Date date;
        try {
            date = instance.dateFormat5.parse(str);
            return instance.dateFormat3.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    // 获取标准时间
    public String getOrderTime1(String str) {
        Date date;
        try {
            date = instance.dateFormat2.parse(str);
            return instance.dateFormat6.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    // 获取标准时间
    public static String getLoactionTime(String str) {
        SimpleDateFormat sdfTimeb = new SimpleDateFormat("yyyy/MM/dd EE HH:mm");
        SimpleDateFormat sdfTimeb1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdfTimeb1.parse(str);
            return sdfTimeb.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    // 获取标准时间
    public String getTime() {
        Date date = new Date();
        return instance.dateFormat4.format(date);
    }

}