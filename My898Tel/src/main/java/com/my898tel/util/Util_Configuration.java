package com.my898tel.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.my898tel.UIApplication;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util_Configuration {
    private static Util_Configuration instance;
    private SharedPreferences preferences;

    public static Util_Configuration getInstance() {
        if (instance == null) {
            instance = new Util_Configuration();
        }
        return instance;
    }

    public Util_Configuration() {
        preferences = UIApplication.getInstance().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
    }

    /**
     * 获取读写权限
     */
    protected SharedPreferences getPreferences() {
        return preferences;
    }


    /**
     * 保存历史记录
     *
     * @param fialeName
     * @param hashMap
     * @param mList
     */
    public void SaveHistory(String fialeName, HashMap<String, Object> hashMap, List<HashMap<String, Object>> mList) {
        if (mList == null) {
            mList = new ArrayList<HashMap<String, Object>>();
        }
        mList.remove(hashMap);
        mList.add(hashMap);
        if (mList.size() > 10) {
            mList.remove(0);
        }
        ObjectOutputStream out = null;
        try {
            FileOutputStream os = UIApplication.getInstance().openFileOutput(fialeName, Context.MODE_PRIVATE);
            out = new ObjectOutputStream(os);
            out.writeObject(mList);
        } catch (Exception e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * *
     * 获取历史记录
     *
     * @param fialeName
     * @return
     */
    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String, Object>> getHistory(String fialeName) {
        ObjectInputStream in = null;
        try {
            InputStream is = UIApplication.getInstance().openFileInput(fialeName);
            in = new ObjectInputStream(is);
            return (ArrayList<HashMap<String, Object>>) in.readObject();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * get network Protocol
     *
     * @return
     */
    public String getMobile() {
        return preferences.getString("Mobile", "");
    }

    /**
     * save network protocol
     *
     * @param updateInfo
     */
    public void saveMobile(String updateInfo) {
        Editor mEditor = preferences.edit();
        mEditor.putString("Mobile", updateInfo);
        mEditor.commit();
    }

    /**
     * get network Protocol
     *
     * @return
     */
    public String getDEVICE_TOKEN() {
        return preferences.getString("DEVICE_TOKEN", "");
    }

    /**
     * save network protocol
     *
     * @param updateInfo
     */
    public void saveDEVICE_TOKEN(String updateInfo) {
        Editor mEditor = preferences.edit();
        mEditor.putString("DEVICE_TOKEN", updateInfo);
        mEditor.commit();
    }
}
