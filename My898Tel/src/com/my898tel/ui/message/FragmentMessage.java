package com.my898tel.ui.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.config.MyUri;
import com.my898tel.moble.SmsInfo;
import com.my898tel.myinterface.GoToNextPage;
import com.my898tel.ui.BaseFragment;
import com.my898tel.util.ObserverSms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 信息主页面
 * 
 * @author liusheng
 */
@SuppressLint("HandlerLeak")
public class FragmentMessage extends BaseFragment {

	/**
	 * 短信列表
	 */
	private ListView listView;

	private Handler smsHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			maps = (LinkedHashMap<String, ArrayList<SmsInfo>>) msg.obj;
			setAdatper();
		};
	};

	/**
	 * 短信列表适配器
	 */
	private FragmentMessageAdapter mAdapter;

	private LinkedHashMap<String, ArrayList<SmsInfo>> maps = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_message_main, container, false);
		listView = (ListView) view.findViewById(R.id.listView);

		asyncQueryHandler = new MyAsyncQueryHandler(getActivity().getContentResolver());

		view.findViewById(R.id.linear_write_new_msg).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MessageWrite.class);
				startActivity(intent);
			}
		});

		ObserverSms smsObserver = new ObserverSms(getActivity(), smsHandler, ObserverSms.ALL);

		getActivity().getContentResolver().registerContentObserver(Uri.parse(MyUri.ALL_SMS), true, smsObserver);
        if(mAdapter != null){
            listView.setAdapter(mAdapter);
        }else{
            query();
        }
		return view;
	}

    public void query(){

        String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };

        asyncQueryHandler.startQuery(0, null, Uri.parse(MyUri.ALL_SMS), projection, null, null, "date desc");
    }



	@Override
	public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token,cookie,cursor);
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

			HashMap<String, String> hashMaps = UIApplication.getInstance().getHashmap();
			for (int i = 0; i < cursor.getCount(); i++) {

				cursor.moveToPosition(i);
				SmsInfo smsinfo = new SmsInfo();
				String phoneNumber = cursor.getString(phoneNumberColumn);
				
				if(phoneNumber!= null && phoneNumber.startsWith("+86"))
				{
					phoneNumber = phoneNumber.substring(3);
				}
				
				if (hashMaps != null) {
					if (hashMaps.containsKey(phoneNumber)) {
						smsinfo.setName(hashMaps.get(phoneNumber));
					}
				}
				if (smsinfo.getName() == null) {
					smsinfo.setName(phoneNumber);
				}

				smsinfo.setDate(cursor.getString(dateColumn));
				smsinfo.setPhoneNumber(phoneNumber);
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
		setAdatper();

	}

	public void setAdatper() {
		if (maps != null && maps.size() > 0) {
			mAdapter = new FragmentMessageAdapter(maps, getActivity());
			mAdapter.setGoToNext(new GoToNextPage<ArrayList<SmsInfo>>() {

				@Override
				public void goToNextPage(ArrayList<SmsInfo> t) {

					Intent intent = new Intent(getActivity(), MessageDetail.class);
					intent.putExtra("phone", t.get(0).getPhoneNumber());
					intent.putExtra("name", t.get(0).getName());
					startActivity(intent);
				}
			});
			listView.setAdapter(mAdapter);
		}
	}
}
