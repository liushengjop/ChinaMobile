package com.my898tel.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.my898tel.R;
import com.my898tel.ui.dialog.DialogLoading;
import com.my898tel.ui.message.MessageDetail;

public class GetMoneyActivity extends BaseActivity implements OnClickListener {

	private Dialog dialog;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_getmoney);
        setTitleNoRightBtn(R.string.get_money);

	}

	@Override
	public void onClick(View v) {
		dialog= DialogLoading.loadDialog(GetMoneyActivity.this);
		handler.sendEmptyMessageDelayed(0, 5000);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			dialog.dismiss();
			Intent intent = new Intent(GetMoneyActivity.this, MainActivity.class);
			intent.putExtra("call", true);
			intent.putExtra("phone", "10086");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		};
	};

}
