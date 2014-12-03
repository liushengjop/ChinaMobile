package com.my898tel.ui.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.my898tel.R;
import com.my898tel.config.MyUri;
import com.my898tel.moble.CheckUpdate;
import com.my898tel.ui.BaseActivity;
import com.my898tel.ui.dialog.DialogCustomeFragment;
import com.my898tel.ui.widget.UpdateFragment;
import com.my898tel.util.Util_Configuration;
import com.my898tel.util.Util_G;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shengliu on 14-9-27.
 */
public class ActivityComplain extends BaseActivity implements View.OnClickListener {
    EditText et_feedback;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_complain);
        setTitleNoRightBtn(R.string.complain);
        initView();
    }

    public void initView() {
        et_feedback = (EditText) findViewById(R.id.id_et_feedback);
    }
    @Override
    public void onClick(View v) {
        String text = et_feedback.getText().toString();
        if (TextUtils.isEmpty(text)) {
            Util_G.DisplayToast(R.string.plz_input_your_msg, Toast.LENGTH_SHORT);
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("content", text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            submit(1, MyUri.URI_FEEDBACK, jsonObject);
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        super.onResponse(jsonObject);
        if (netWorkUnit.getmTag() == 1) {
            try {
                int responseStatus = jsonObject.getInt("responseStatus");
                if (responseStatus == 200) {
                    Util_G.DisplayToast(R.string.plz_feedback_success, Toast.LENGTH_SHORT);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
