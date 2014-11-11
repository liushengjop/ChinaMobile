package com.my898tel.util;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;

import com.my898tel.UIApplication;
import com.my898tel.config.MyUri;
import com.my898tel.moble.CallLogBean;
import com.my898tel.moble.SmsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ObserverCallLog extends ContentObserver {



	private Handler myHandler;

    private List<CallLogBean> callLogs;


	public ObserverCallLog(Context context, Handler handler) {
		super(handler);
		myHandler = handler;
		asyncQueryHandler = new MyAsyncQueryHandler(
				context.getContentResolver());

	}


	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
	    init();

	}

    public void init(){
        Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
        // 查询的列
        String[] projection = { CallLog.Calls.DATE, // 日期
                CallLog.Calls.NUMBER, // 号码
                CallLog.Calls.TYPE, // 类型
                CallLog.Calls.CACHED_NAME, // 名字
                CallLog.Calls._ID, // id
        };
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "date DESC limit 100");
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
			ObserverCallLog.this.onQueryComplete(token, cookie, cursor);
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
        if (cursor != null && cursor.getCount() > 0) {
            List<CallLogBean> callLogs = new ArrayList<CallLogBean>();
            SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfHour = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            cursor.moveToFirst(); // 游标移动到第一项
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                date = new Date(cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DATE)));
                String number = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                int type = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                String cachedName = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.CACHED_NAME));// 缓存的名称与电话号码，如果它的存在
                int id = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls._ID));

                CallLogBean callLogBean = new CallLogBean();
                callLogBean.setId(id);
                callLogBean.setNumber(number);
                callLogBean.setName(cachedName);
                if (null == cachedName || "".equals(cachedName)) {
                    callLogBean.setName(number);
                }
                callLogBean.setType(type);
                callLogBean.setDate(sdfYear.format(date));
                callLogBean.setTime(sdfHour.format(date));
                callLogs.add(callLogBean);
            }
            if (callLogs.size() > 0) {
                Message message = myHandler.obtainMessage();
                    message.obj = callLogs;
                myHandler.sendMessage(message);
            }
        }

	}
}
