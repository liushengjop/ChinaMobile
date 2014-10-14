package com.my898tel.ui.dialog;

import java.util.List;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.myinterface.Consts;
import com.my898tel.ui.GetMoneyActivity;
import com.my898tel.ui.MainActivity;
import com.my898tel.ui.message.MessageDetail;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class DialogCancleCornet extends DialogFragment {
	
	
	private ProgressDialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(R.style.SampleTheme, R.style.SampleTheme);
	}
	
	Context context;
	
	public void setContext(Context context)
	{
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dilaog_cancle_cornet, container, false);

		view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
				sendSmsInsert("QXDH", "10086");
				
				dialog= DialogLoading.ShowLoading(getActivity());
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						dialog.dismiss();
						Intent intent = new Intent(context, MessageDetail.class);
						intent.putExtra("phone",Consts.CHINA_MOBILE_TAG);
						intent.putExtra("name", context.getString(R.string.str_lable_02));
						context.startActivity(intent);
						
					}
				}, 2000);
				
				
			}
		});

		view.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		return view;
	}

	public void sendSmsInsert(String message, String mobile) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sentIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(), 0);
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

		ContentValues values = new ContentValues();
		// 发送时间
		values.put("date", System.currentTimeMillis());
		// 阅读状态
		values.put("read", 0);
		// 1为收 2为发
		values.put("type", 2);
		// 送达号码
		values.put("address", mobile);
		// 送达内容
		values.put("body", message);
		// 插入短信库

		getActivity().getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		Window window = getDialog().getWindow();
		WindowManager.LayoutParams p = window.getAttributes();
		p.width = (int) (UIApplication.getInstance().getScreenWidth() * 0.7); // 宽度设置为屏幕的0.95
		p.height = (int) (UIApplication.getInstance().density * 150); // 高度设置为屏幕的0.6

		p.gravity = Gravity.CENTER;
		window.setAttributes(p);
	}
	
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
						dialog.dismiss();
		};
	};

}
