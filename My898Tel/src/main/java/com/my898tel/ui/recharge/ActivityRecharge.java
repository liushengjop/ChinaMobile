package com.my898tel.ui.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.my898tel.R;
import com.my898tel.ui.BaseActivity;

/**
 * 充值页面
 *
 * @author liusheng
 */
public class ActivityRecharge extends BaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.fragment_recharge);

        findViewById(R.id.linear_china_moble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ActivityRecharge.this, ActivityChinaMoble.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.linear_network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ActivityRecharge.this, ActivityBank.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.linear_paypal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ActivityRecharge.this, ActivityPayPal.class);
                startActivity(intent);
            }
        });


        setTitleNoRightBtn(R.string.recharge);
    }

}
