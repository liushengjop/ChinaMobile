package com.my898tel.ui;

import java.util.HashMap;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.my898tel.R;

public class SearchBaseFragment extends BaseFragment {


    /**
     * 存放存在的汉语拼音首字母和与之对应的列表位置
     */
    protected HashMap<String, Integer> alphaIndexer;
    /**
     * 存放存在的汉语拼音首字母
     */
    protected String[] sections;

    // 获得汉语拼音首字母
    public String getAlpha(String str) {
        if (str == null) {
            return "#";
        }

        if (str.trim().length() == 0) {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }


}
