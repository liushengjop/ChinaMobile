package com.my898tel.util;

import java.util.HashMap;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.my898tel.UIApplication;
import com.my898tel.config.MyUri;

public class ObserverContact extends ContentObserver {
	private AsyncQueryHandler asyncQueryHandler;
	public HashMap<String, String> hashMap;
	public ObserverContact(Context context, Handler handler) {
		super(handler);

		asyncQueryHandler = new AsyncQueryHandler(context.getContentResolver()) {

			@Override
			protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
				// TODO Auto-generated method stub
				super.onQueryComplete(token, cookie, cursor);

				hashMap = new HashMap<String, String>();
				if (cursor != null && cursor.getCount() > 0) {

					cursor.moveToFirst();
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						String name = cursor.getString(1);
						String number = cursor.getString(2);
						if(number!= null && number.startsWith("+86"))
						{
							number = number.substring(3);
						}
						hashMap.put(number.replaceAll(" ", ""), name);
					}
					UIApplication.getInstance().setHashmap(hashMap);
				}
			}
		};
		init();
	}
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		init();
	}

	public void init() {
		String[] projection = { "_id", "display_name", "data1", "sort_key" };
		asyncQueryHandler.startQuery(0, null, Uri.parse(MyUri.ALL_PHONE), projection, null, null, "sort_key COLLATE LOCALIZED asc");
	}

}
