package com.my898tel.ui.message;

import java.util.ArrayList;
import java.util.List;

import com.my898tel.R;
import com.my898tel.moble.PhoneInfo;
import com.my898tel.ui.BaseActivity;
import com.my898tel.ui.widget.FlowLayout;
import com.my898tel.util.AppManager;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * 新建短信
 * 
 * @author liusheng
 */
public class MessageWrite extends BaseActivity {

	public final int mRequestCode = 9999;

	private FlowLayout flowLayout_content;

	private EditText et_content;

	private EditText et_sent_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_new_msg);

		flowLayout_content = (FlowLayout) findViewById(R.id.flowLayout_content);

		et_content = (EditText) LayoutInflater.from(MessageWrite.this).inflate(
				R.layout.eidt_item, null);
		flowLayout_content.addView(et_content);
		et_sent_content = (EditText) findViewById(R.id.et_sent_content);

		findViewById(R.id.ib_select_contact).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MessageWrite.this,
								MsgContact.class);
						startActivityForResult(intent, mRequestCode);
					}
				});

        setTitle(R.string.create_new_msg);
		et_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(et_content.getText().toString())) {
					et_content.setTag((Integer)et_content.getTag()+1);
				}else{
                    et_content.setTag(0);
                }
			}
		});
        et_content.setTag(0);
		et_content.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP
						&& event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
					if (TextUtils.isEmpty(et_content.getText().toString())) {
                        if((Integer)et_content.getTag() != 1){
                            if (flowLayout_content.getChildCount() > 1) {
                                flowLayout_content.removeViewAt(flowLayout_content.getChildCount()-1-1);
                            }
                        }
                        et_content.setTag((Integer)et_content.getTag()+1);

						return true;
					}
				}
				return false;
			}
		});

		findViewById(R.id.ib_sent_msg).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String message = et_sent_content.getText().toString();
						if (TextUtils.isEmpty(message)) {
							return;
						}

						if (!TextUtils.isEmpty(et_content.getText().toString())) {
							sentMsg(message, et_content.getText().toString());
						}
						if (phones != null) {
							for (int i = 0; i < phones.size(); i++) {
								sentMsg(message, phones.get(i).getDate1());
							}
						}

					}
				});
		AppManager.getAppManager().addActivity(this);
	}

	public void sentMsg(String message, String mobile) {
		sendSmsInsert(message, mobile);
		Intent intent = new Intent();
		intent.setClass(MessageWrite.this, MessageDetail.class);
		intent.putExtra("phone", mobile);
		startActivity(intent);
		finish();
	}

	public void sendSmsInsert(String message, String mobile) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sentIntent = PendingIntent.getBroadcast(
				MessageWrite.this, 0, new Intent(), 0);
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

		getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}

	private ArrayList<PhoneInfo> phones = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mRequestCode == requestCode) {
			if (data == null)
				return;
			phones = (ArrayList<PhoneInfo>) data.getSerializableExtra("phones");

			if (phones != null) {

				if (phones.size() > 0) {
					et_content.setHint(null);
					et_content.setSelection(et_content.getText().length());
				}
				for (int i = 0; i < phones.size(); i++) {
					PhoneInfo phoneInfo = phones.get(i);

                    TextView tv = (TextView)LayoutInflater.from(getApplicationContext()).inflate(R.layout.text_item,null);
					tv.setText(phoneInfo.getDisplay_name());
					tv.setTag(phoneInfo.getDate1());
					flowLayout_content.addView(tv, 0);
				}
			}

		}
	}

}
