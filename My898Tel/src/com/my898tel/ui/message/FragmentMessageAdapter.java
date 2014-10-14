package com.my898tel.ui.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my898tel.R;
import com.my898tel.moble.SmsInfo;
import com.my898tel.myinterface.GoToNextPage;

/**
 * 短信适配器
 * 
 * @author liusheng
 */
public class FragmentMessageAdapter extends BaseAdapter {

	/**
	 * 短信类容集合
	 */
	private List<String> mList;

	/**
	 * 应用上下文
	 */
	private Context mContext;

	private LinkedHashMap<String, ArrayList<SmsInfo>> mHashMap;

	/**
	 * 前往下个页面
	 */
	private GoToNextPage<ArrayList<SmsInfo>> mGoToNext;

	public void setGoToNext(GoToNextPage<ArrayList<SmsInfo>> goToNext) {
		this.mGoToNext = goToNext;
	}

	public FragmentMessageAdapter(
			LinkedHashMap<String, ArrayList<SmsInfo>> hashMap, Context context) {
		super();
		this.mContext = context;

		this.mHashMap = hashMap;
		mList = new ArrayList<String>();
		Set<String> keySet = mHashMap.keySet();
		for (String address : keySet) {
			mList.add(address);
		}
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// mHashMap.get(key);
		final ArrayList<SmsInfo> infos = mHashMap.get(mList.get(position));
		SmsInfo smsInfo = infos.get(0);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.sms_item, null);
		}

		// 电话号码 或者通讯录名称
		TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
		// 短信内容
		TextView tv_content = (TextView) convertView
				.findViewById(R.id.tv_content);
		// 日期
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);

		TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);

		if (smsInfo.getName() == null) {
			if (infos.size() > 1)
				tv_phone.setText(smsInfo.getPhoneNumber() + "(" + infos.size()
						+ ")");
			else
				tv_phone.setText(smsInfo.getPhoneNumber());
		} else {
			if (infos.size() > 1)
				tv_phone.setText(smsInfo.getName() + "(" + infos.size() + ")");
			else
				tv_phone.setText(smsInfo.getName());
		}
		
		tv_content.setText(smsInfo.getSmsbody());

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.valueOf(smsInfo.getDate()));

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
		tv_date.setText(dateFormat.format(calendar.getTime()));

		// ALL=0;INBOX=1;SENT=2;DRAFT=3;OUTBOX=4;FAILED=5;QUEUED=6;
		switch (smsInfo.getType()) {
		case 3:
			tv_type.setVisibility(View.VISIBLE);
			tv_type.setText(Html.fromHtml("<font color='#1b9b8'>" + "草稿"
					+ "</font>"));
			break;
		case 5:
			tv_type.setVisibility(View.VISIBLE);
			tv_type.setText(Html.fromHtml("<font color='#cc0000'>" + "发送失败"
					+ "</font>"));
			break;
		default:
			tv_type.setVisibility(View.INVISIBLE);
			break;
		}

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mGoToNext != null) {
					mGoToNext.goToNextPage(infos);
				}
			}
		});
		return convertView;
	}

	public void setData(LinkedHashMap<String, ArrayList<SmsInfo>> hashMap) {
		if (hashMap != null) {
			this.mHashMap = hashMap;
			mList = new ArrayList<String>();
			Set<String> keySet = mHashMap.keySet();
			for (String address : keySet) {
				mList.add(address);
			}
		}
		notifyDataSetChanged();
	}

}
