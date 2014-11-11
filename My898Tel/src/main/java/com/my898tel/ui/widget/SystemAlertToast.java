package com.my898tel.ui.widget;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.util.Unit_XML;

/*****
 * 全局消息提示框
 * 
 * @author herozhou1314
 * 
 *         类似于toast 但是背景是黑色的
 */
public class SystemAlertToast extends Dialog {
	private Window mWindow;
	private static SystemAlertToast alterToast;
	private String phoneNum;
	private Handler handler = new Handler();

	public static SystemAlertToast getInstance() {
		if (alterToast == null) {
			alterToast = new SystemAlertToast(UIApplication.getInstance());
		}
		return alterToast;
	}

	public SystemAlertToast(Context context) {
		super(context, R.style.ThemeCustomBootmDialog);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // 将布局文件转换成相应的View对象
		View layout = inflater.inflate(R.layout.dialog_select, null);
		Button btn_soft_call = (Button) layout.findViewById(R.id.btn_soft_call);
		Button btn_local_call = (Button) layout.findViewById(R.id.btn_local_call);
		Button btn_cancle = (Button) layout.findViewById(R.id.btn_cancle);
		btn_soft_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Unit_XML.saveLocalCall("");
				String cornet = UIApplication.getInstance().getResources().getString(R.string.call_need_add);
				callPhone(UIApplication.getInstance(), cornet + ",9" + phoneNum + "#");
				insertCallLog(UIApplication.getInstance().getApplicationContext(), phoneNum, "10", Integer.toString(2), "1", 0L);
				dismiss();
			}
		});
		btn_local_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Unit_XML.saveLocalCall("99999");
				callPhone(UIApplication.getInstance(), phoneNum);
				dismiss();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						Unit_XML.saveLocalCall("");
					}
				}, 5000);
			}
		});

		btn_cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		getWindow().setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM);
//		WindowManager.LayoutParams param = getWindow().getAttributes();
//		param.width = ViewGroup.LayoutParams.MATCH_PARENT;
//		param.height = ViewGroup.LayoutParams.MATCH_PARENT;
//		getWindow().setAttributes(param);
		setContentView(layout);
		// 从layout中按照id查找imageView对象
		mWindow = this.getWindow();
		final WindowManager.LayoutParams params = mWindow.getAttributes();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = UIApplication.getInstance().getScreenWidth();
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		getWindow().setAttributes(params);
		setCanceledOnTouchOutside(false);
	}

	/***
	 * show toast message
	 * 
	 * @param message
	 */
	public void showMessage(String phoneNum_, int mDuration) {
		// 设置TextView的text内容
		this.phoneNum = phoneNum_;
		show();
	}

	// private class CloseSystemDialogsReceiver extends BroadcastReceiver {
	// final String SYSTEM_DIALOG_REASON_KEY = "reason";
	// final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
	// String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
	// if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
	// SystemAlertToast.this.dismiss();
	// mWindow.getContext().unregisterReceiver(mCloseSystemDialogsReceiver);
	// }
	// }
	// }
	// }

	public void callPhone(Context context, String phoneNum) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void insertCallLog(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4, long paramLong) {
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("number", paramString1);
		localContentValues.put("date", Long.valueOf(paramLong + System.currentTimeMillis()));
		localContentValues.put("duration", paramString2);
		localContentValues.put("type", paramString3);
		localContentValues.put("new", paramString4);
		paramContext.getContentResolver().insert(CallLog.Calls.CONTENT_URI, localContentValues);
	}
}
