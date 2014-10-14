package com.my898tel.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.my898tel.UIApplication;
import com.my898tel.config.MyUri;
import com.my898tel.moble.SmsInfo;
import com.my898tel.ui.message.MsgContact;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class ObserverSms extends ContentObserver {

	public static final int ALL = 0;

	public static final int SINGLE = 1;

	private Handler myHandler;

	private LinkedHashMap<String, ArrayList<SmsInfo>> maps = null;

	public int mCurrentType;

	private String mPhone;

	public ObserverSms(Context context, Handler handler, int type) {
		super(handler);
		mCurrentType = type;
		myHandler = handler;
		asyncQueryHandler = new MyAsyncQueryHandler(
				context.getContentResolver());
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		if (mCurrentType == ALL)
			queryAll();
		else
			querySingleNuber(mPhone);

	}

	public void querySingleNuber(String phone) {

		String[] projection = new String[] { "_id", "address", "person",
				"body", "date", "type" };

		asyncQueryHandler.startQuery(0, null, Uri.parse(MyUri.ALL_SMS),
				projection, "address=? or address=?", new String[] { phone,"+86"+phone }, "date desc");
//		asyncQueryHandler.startQuery(0, null, Uri.parse(MyUri.ALL_SMS),
//				projection, "address=?", new String[] { phone }, "date desc");
	}

	public void queryAll() {
		String[] projection = new String[] { "_id", "address", "person",
				"body", "date", "type" };

		asyncQueryHandler.startQuery(0, null, Uri.parse(MyUri.ALL_SMS),
				projection, null, null, "date desc");

	}

	/**
	 * 查询助手
	 */
	protected MyAsyncQueryHandler asyncQueryHandler;

	public class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			super.onQueryComplete(token, cookie, cursor);
			ObserverSms.this.onQueryComplete(token, cookie, cursor);
		}
	}

	/**
	 * 需要覆盖的查询方法
	 * 
	 * @param token
	 * @param cookie
	 * @param cursor
	 */
	public void onQueryComplete(int token, Object cookie, Cursor cursor) {
		if (maps == null) {
			maps = new LinkedHashMap<String, ArrayList<SmsInfo>>();
		} else {
			maps.clear();
		}
		if (cursor != null && cursor.getCount() > 0) {

			cursor.moveToFirst();
			int nameColumn = cursor.getColumnIndex("person");
			int phoneNumberColumn = cursor.getColumnIndex("address");
			int smsbodyColumn = cursor.getColumnIndex("body");
			int dateColumn = cursor.getColumnIndex("date");
			int typeColumn = cursor.getColumnIndex("type");

			HashMap<String, String> hashMap = UIApplication.getInstance()
					.getHashmap();
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
				smsinfo.setPhoneNumber(cursor.getString(phoneNumberColumn));
				smsinfo.setSmsbody(cursor.getString(smsbodyColumn));
				smsinfo.setType(cursor.getInt(typeColumn));

				if (smsinfo.getType() != 3) {
					if (maps.containsKey(phoneNumber)) {
						maps.get(phoneNumber).add(smsinfo);
					} else {
						ArrayList<SmsInfo> tempsList = new ArrayList<SmsInfo>();
						tempsList.add(smsinfo);
						maps.put(smsinfo.getPhoneNumber(), tempsList);
					}
				}
			}
		}
		Message message = myHandler.obtainMessage();
		if (mCurrentType == ALL)
			message.obj = maps;
		else
			message.obj = maps.get(mPhone);
		myHandler.sendMessage(message);
	}
}
