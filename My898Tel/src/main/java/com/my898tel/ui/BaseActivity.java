package com.my898tel.ui;

import java.util.HashMap;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.myinterface.Consts;
import com.my898tel.ui.dialog.DialogCustomeFragment;
import com.my898tel.ui.dialog.DialogLoading;
import com.my898tel.ui.setting.ActivitySetting;
import com.my898tel.util.Util_Configuration;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivity extends FragmentActivity implements Consts, Response.ErrorListener, Response.Listener<JSONObject> {
    public BaseActivity instance;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        instance = this;
    }

    /**
     * 标题栏
     */
    protected TextView tv_title;
    /**
     * 余额
     */
    protected TextView tv_over;

    /**
     * 左侧返回按钮
     */
    protected ImageButton ib_left;

    /**
     * 右侧按钮
     */
    protected Button ib_right;

    /**
     * 初始化查询标示
     */
    protected static final int INIT = 0;

    /**
     * 查询标示
     */
    protected static final int SEARCH = 1;

    /**
     * 查询助手
     */
    protected MyAsyncQueryHandler asyncQueryHandler;

    /**
     * 存放存在的汉语拼音首字母和与之对应的列表位置
     */
    protected HashMap<String, Integer> alphaIndexer;
    /**
     * 存放存在的汉语拼音首字母
     */
    protected String[] sections;

    public class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            BaseActivity.this.onQueryComplete(token, cookie, cursor);
        }
    }

    /**
     * 需要覆盖的查询方法
     *
     * @param token
     * @param cookie
     * @param cursor
     */
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {

    }

    private void setTopValue() {
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_over = (TextView) findViewById(R.id.tv_over);

        ib_left = (ImageButton) findViewById(R.id.ib_left);

        ib_right = (Button) findViewById(R.id.ib_right);

        ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ib_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BaseActivity.this, ActivitySetting.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 设置标题
     */
    public void setTitle(int titleId) {
        setTopValue();
        tv_title.setText(titleId);
    }

    public void setTitleAndRight(int titleId, int rightId) {
        setTopValue();
        tv_title.setText(titleId);
        ib_right.setText(rightId);
    }

    public void setTitleNoRightBtn(int titleId) {
        setTopValue();
        tv_title.setText(titleId);
        hideRight();
    }

    /**
     * 隐藏右边按钮
     */
    public void hideRight() {
        if (ib_right == null) {
            setTopValue();
        } else {
            ib_right.setVisibility(View.INVISIBLE);
        }
    }

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


    protected NetWorkUnit netWorkUnit;

    public void submit(int tag, String url, JSONObject obj) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SCREEN_SIZE",UIApplication.getInstance().getScreenWH());
            jsonObject.put("OAUTH_TOKEN","");
            jsonObject.put("PLATFORM","Android");
            jsonObject.put("DEVICE_TOKEN", Util_Configuration.getInstance().getDEVICE_TOKEN());
            jsonObject.put("VERSION",UIApplication.getInstance().getVersion());
            jsonObject.put("USER_AGENT",UIApplication.getInstance().getDeviceUA());
//        "head" : {
//            "SCREEN_SIZE" : "640x960",
//                    "OAUTH_TOKEN" : "",
//                    "PLATFORM" : "Android",
//                    "DEVICE_TOKEN" : "3ccd2d32216d87ace41c45d49a658546",
//                    "VERSION" : "2.0.0",
//                    "USER_AGENT" : "Android4 (7.1)"
//        }

            if (obj == null) {
                obj = new JSONObject();
            }

            obj.put("head", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        netWorkUnit = new NetWorkUnit(BaseActivity.this, Request.Method.POST, url, obj, this, this);
        netWorkUnit.setmTag(tag);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

        DialogCustomeFragment.Builder builder = new DialogCustomeFragment.Builder();
        builder.setTitle(getString(R.string.tip_alert));
        builder.setMessage("网络连接失败");
        builder.setBtn1(getString(R.string.sure), new DialogCustomeFragment.Listener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        builder.show(getSupportFragmentManager(), DialogCustomeFragment.class.getName());
    }

    @Override
    public void onResponse(JSONObject jsonObject) {

    }


    public void showMessage(String content) {
        DialogCustomeFragment.Builder builder = new DialogCustomeFragment.Builder();
        builder.setTitle(getString(R.string.tip_alert));
        builder.setMessage(content);
        builder.setBtn1(getString(R.string.sure), new DialogCustomeFragment.Listener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        builder.show(getSupportFragmentManager(), DialogCustomeFragment.class.getName());
    }

}
