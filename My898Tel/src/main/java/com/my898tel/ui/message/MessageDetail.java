package com.my898tel.ui.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.config.MyUri;
import com.my898tel.moble.SmsInfo;
import com.my898tel.ui.BaseActivity;
import com.my898tel.util.AppManager;
import com.my898tel.util.ObserverSms;

/**
 * 短信详细页面
 * 
 * @author liusheng
 */
public class MessageDetail extends BaseActivity {
	private ListView listView;
	private MessageAdapter mAdapter;
	private TextView tv_title;
	private EditText et_sent_content;
	private ArrayList<SmsInfo> smsinfos = null;
	private EditText et_hide;
	private String phone;
	private String name;

	private Handler smsHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			smsinfos = (ArrayList<SmsInfo>) msg.obj;
			if(smsinfos != null)
				setAdatper();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_message_detail);

		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());

		phone = getIntent().getStringExtra("phone");
		name = getIntent().getStringExtra("name");

		ObserverSms smsObserver = new ObserverSms(MessageDetail.this, smsHandler, ObserverSms.SINGLE);
		smsObserver.setPhone(phone);
		getContentResolver().registerContentObserver(Uri.parse(MyUri.ALL_SMS), true, smsObserver);

		et_hide = (EditText) findViewById(R.id.et_hide);
		listView = (ListView) findViewById(R.id.listView);

		tv_title = (TextView) findViewById(R.id.tv_title);

		et_sent_content = (EditText) findViewById(R.id.et_sent_content);

		hideSoftWindow();

		findViewById(R.id.ib_left).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.ib_right).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (phone != null && !TextUtils.isEmpty(phone)) {
					UIApplication.callPhone(MessageDetail.this, phone);
				}
			}
		});

		findViewById(R.id.ib_sent_msg).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = et_sent_content.getText().toString();
				if (!TextUtils.isEmpty(content)) {
					sendSmsInsert(content, phone);
				}
			}
		});
		if (CHINA_MOBILE_TAG.equals(phone)) {
			name=getString(R.string.str_lable_02);
		}
		tv_title.setText(TextUtils.isEmpty(name) ? phone : name);
		querySingleNuber(phone);
		
		AppManager.getAppManager().addActivity(this);
	}

	public void sendSmsInsert(String message, String mobile) {
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sentIntent = PendingIntent.getBroadcast(MessageDetail.this, 0, new Intent(), 0);
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

	public void querySingleNuber(String phone) {

		String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };

		asyncQueryHandler.startQuery(0, null, Uri.parse(MyUri.ALL_SMS), projection, "address=? or address=?", new String[] { phone,"+86"+phone }, "date desc");
	}

	@Override
	public void onQueryComplete(int token, Object cookie, Cursor cursor) {
		// TODO Auto-generated method stub
		super.onQueryComplete(token, cookie, cursor);

		if (smsinfos == null) {
			smsinfos = new ArrayList<SmsInfo>();
		} else {
			smsinfos.clone();
		}

		if (cursor != null && cursor.getCount() > 0) {

			cursor.moveToFirst();
			int nameColumn = cursor.getColumnIndex("person");
			int phoneNumberColumn = cursor.getColumnIndex("address");
			int smsbodyColumn = cursor.getColumnIndex("body");
			int dateColumn = cursor.getColumnIndex("date");
			int typeColumn = cursor.getColumnIndex("type");
			HashMap<String, String> hashMap = UIApplication.getInstance().getHashmap();
			for (int i = 0; i < cursor.getCount(); i++) {

				cursor.moveToPosition(i);
				SmsInfo smsinfo = new SmsInfo();

				String phoneNumber = cursor.getString(phoneNumberColumn);
				if (hashMap != null) {
					if (hashMap.containsKey(phoneNumber)) {
						smsinfo.setName(hashMap.get(phoneNumber));
					}
				}

				if (smsinfo.getName() == null) {
					smsinfo.setName(phoneNumber);
				}

				smsinfo.setDate(cursor.getString(dateColumn));
				smsinfo.setPhoneNumber(phoneNumber);
				smsinfo.setSmsbody(cursor.getString(smsbodyColumn));
				smsinfo.setType(cursor.getInt(typeColumn));

				// if(smsinfo.getType() != 3)
				// {
				smsinfos.add(smsinfo);
				// }

			}
		}

		setAdatper();
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param et
	 */
	public void hideSoftWindow() {

		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(et_hide.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		et_hide.requestFocus();
	}

	public void setAdatper() {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < smsinfos.size(); i++) {
			SmsInfo smsInfo = smsinfos.get(i);
			if (smsInfo.getType() == 3) {
				sb.append(smsInfo.getSmsbody());
				smsinfos.remove(smsInfo);
			}
		}
		if (mAdapter == null)
			mAdapter = new MessageAdapter(smsinfos, MessageDetail.this);
		else
			mAdapter.setData(smsinfos);
		listView.setAdapter(mAdapter);
		
		listView.setSelection(mAdapter.getCount()-1);  
		// 如果是草稿 设置值
		et_sent_content.setText(sb.toString());
		et_sent_content.setSelection(sb.length());
		

	}
}
