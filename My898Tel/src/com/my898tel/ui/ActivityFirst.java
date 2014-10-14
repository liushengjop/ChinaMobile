package com.my898tel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.my898tel.R;
import com.my898tel.ui.setting.ActivitySetting;

/**
 * Created by shengliu on 14-10-6.
 */
public class ActivityFirst extends  BaseActivity {


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

       setContentView(R.layout.activity_first);

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ActivityFirst.this, ActivitySetting.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
