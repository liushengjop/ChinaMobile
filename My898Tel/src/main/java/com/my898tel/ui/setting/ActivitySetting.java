package com.my898tel.ui.setting;


import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.config.MyUri;
import com.my898tel.ui.BaseActivity;
import com.my898tel.ui.MainActivity;
import com.my898tel.ui.RegisterActivity;
import com.my898tel.ui.dialog.DialogCustomeFragment;
import com.my898tel.ui.dialog.DialogLoading;
import com.my898tel.ui.message.MessageDetail;
import com.my898tel.ui.recharge.ActivityChinaMoble;
import com.my898tel.ui.recharge.ActivityRecharge;
import com.my898tel.util.AppManager;
import com.my898tel.util.Unit_XML;
import com.my898tel.util.Util_G;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置主页面
 *
 * @author liusheng
 */
public class ActivitySetting extends BaseActivity implements View.OnClickListener {

    private final static int WHATSEARCH = 1;

    private Dialog mydialog;
    TextView tv_over;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mydialog.dismiss();

            if (msg.what == WHATSEARCH) {
                Intent intent = new Intent(ActivitySetting.this, MessageDetail.class);
                intent.putExtra("phone", "10086");
                intent.putExtra("name", getString(R.string.str_lable_02));
                startActivity(intent);
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.linear_new_user_register).setOnClickListener(this);
        findViewById(R.id.linear_cz).setOnClickListener(this);
        findViewById(R.id.linear_search_over).setOnClickListener(this);
        findViewById(R.id.linear_search_cornet).setOnClickListener(this);
        findViewById(R.id.linear_cancle_cornet).setOnClickListener(this);

        findViewById(R.id.linear_check_version).setOnClickListener(this);
        findViewById(R.id.linear_share).setOnClickListener(this);
        findViewById(R.id.relat_complain).setOnClickListener(this);
        findViewById(R.id.relat_about).setOnClickListener(this);
        findViewById(R.id.relat_help).setOnClickListener(this);
        tv_over = (TextView) findViewById(R.id.tv_over);
        setTitleNoRightBtn(R.string.setting);
        if (Unit_XML.getOpenCount() == 0) {
            ib_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentMain = new Intent(ActivitySetting.this, MainActivity.class);
                    startActivity(intentMain);
                }
            });
        }
        Unit_XML.saveOpenCount();
        AppManager.getAppManager().addActivity(this);
    }


    public void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        // 这里是你发送的文本
        shareIntent.putExtra(Intent.EXTRA_TEXT, "我在用乐呼，享受群内短号免费互打，群外通话1毛钱1分钟，市话长途统一价。真的很省。\n" +
                "你快试试：http://wap.ailehu.com.cn\n");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.recommend_friend)));
    }


    /**
     * 网络是否连接
     *
     * @return
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_new_user_register:
                Intent intent = new Intent();
                intent.setClass(ActivitySetting.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_cz:
                Intent intentCZ = new Intent();
                intentCZ.setClass(ActivitySetting.this, ActivityRecharge.class);
                startActivity(intentCZ);
                break;
            case R.id.linear_search_over:
                if (!isConnectingToInternet()) {
                    Toast.makeText(instance, "请检查网络连接", Toast.LENGTH_SHORT).show();
                } else {
                    DialogCustomeFragment.Builder builder = new DialogCustomeFragment.Builder();
                    builder.setTitle(getString(R.string.input_mobile_number));
                    final EditText editText = new EditText(instance);
                    editText.setBackgroundColor(Color.TRANSPARENT);
                    editText.setMinHeight(Util_G.dip2px(35));
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(11);
                    editText.setFilters(filterArray);
                    builder.setView(editText);
                    builder.setBtn1(getString(R.string.sure), new DialogCustomeFragment.Listener() {
                        @Override
                        public void onClick(Dialog dialog) {
                            String text = editText.getText().toString();
                            if (TextUtils.isEmpty(text)) {
                                Util_G.DisplayToast(R.string.input_mobile_number, Toast.LENGTH_SHORT);
                            } else if (!Util_G.isMobileNO(text)) {
                                Util_G.DisplayToast(R.string.input_right_number, Toast.LENGTH_SHORT);
                            } else {
                                Util_G.hideSoftInput(instance, editText);
                                dialog.dismiss();
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("mobile", text);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                submit(1, MyUri.URI_LEFT_MONEY, jsonObject);
                            }
                        }
                    });
                    builder.setBtn2(getString(R.string.str_public_cancel), null);
                    builder.show(getSupportFragmentManager(), DialogCustomeFragment.class.getName());
                    Util_G.showSoftInput(instance, editText);
                }
                break;
            case R.id.linear_search_cornet:
                UIApplication.sendSmsInsert(ActivitySetting.this, "CXDH", "10086");
                mydialog = DialogLoading.loadDialog(instance);
                handler.sendEmptyMessageDelayed(WHATSEARCH, 15000);
                break;
            case R.id.linear_cancle_cornet:

                DialogCustomeFragment.Builder builder = new DialogCustomeFragment.Builder();
                builder.setTitle(getString(R.string.cancle_cornet));
                builder.setMessage(getString(R.string.cancle_cornet_cotent));
                EditText editText = new EditText(instance);
                builder.setView(editText);
                builder.setBtn1(getString(R.string.sure), new DialogCustomeFragment.Listener() {
                    @Override
                    public void onClick(Dialog dialog) {
                        dialog.dismiss();
                        UIApplication.sendSmsInsert(ActivitySetting.this, "QXDH", "10086");
                        mydialog = DialogLoading.loadDialog(instance);
                        handler.sendEmptyMessageDelayed(WHATSEARCH, 15000);
                    }
                });
                builder.setBtn2(getString(R.string.str_public_cancel), null);
                builder.show(getSupportFragmentManager(), DialogCustomeFragment.class.getName());
                break;
            case R.id.linear_check_version:
                DialogCustomeFragment.Builder builderCheckUpdate = new DialogCustomeFragment.Builder();
                builderCheckUpdate.setTitle(getString(R.string.check_version));
                builderCheckUpdate.setMessage(getString(R.string.cancle_cornet_cotent));
                builderCheckUpdate.setBtn1(getString(R.string.sure), new DialogCustomeFragment.Listener() {
                    @Override
                    public void onClick(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
                builderCheckUpdate.show(getSupportFragmentManager(), DialogCustomeFragment.class.getName());
                break;
            case R.id.linear_share:
                share();
                break;
            case R.id.relat_complain:
                Intent intentComplain = new Intent();
                intentComplain.setClass(ActivitySetting.this, ActivityComplain.class);
                startActivity(intentComplain);
                break;
            case R.id.relat_about:
                Intent intentAbout = new Intent();
                intentAbout.setClass(ActivitySetting.this, ActivityAbout.class);
                startActivity(intentAbout);
                break;
            case R.id.relat_help:
                Intent intentHelp = new Intent();
                intentHelp.setClass(ActivitySetting.this, ActivtyHelp.class);
                startActivity(intentHelp);
                break;
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        super.onResponse(jsonObject);
        Log.d("JSONObject-->>", jsonObject.toString());
        if (netWorkUnit.getmTag() == 1) {
            try {
                int desc = jsonObject.getInt("desc");
                if (desc > 0) {
                    tv_over.setText(getString(R.string.over_point_unit,desc));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }
}
