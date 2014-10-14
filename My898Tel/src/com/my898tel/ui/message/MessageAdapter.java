package com.my898tel.ui.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my898tel.R;
import com.my898tel.moble.SmsInfo;

/**
 * 短信类容详细页面适配器
 * 
 * @author liusheng
 */
public class MessageAdapter extends BaseAdapter {
	private ArrayList<SmsInfo> mList;
	private Context mContext;

	public MessageAdapter(ArrayList<SmsInfo> list, Context context) {
		super();
		this.mList = list;
		this.mContext = context;
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

		SmsInfo smsInfo = mList.get((mList.size() - 1) - position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.sms_detail_item, null);
		}
		TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
		TextView tv_left_content = (TextView) convertView.findViewById(R.id.tv_left_content);
		TextView tv_right_content = (TextView) convertView.findViewById(R.id.tv_Right_content);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date date = new Date(Long.valueOf(smsInfo.getDate()));

		tv_time.setText(simpleDateFormat.format(date));

		if (smsInfo.getType() == 1) {
			tv_left_content.setBackgroundResource(R.drawable.recive_bg);
			tv_left_content.setVisibility(View.VISIBLE);
			tv_right_content.setVisibility(View.GONE);
			tv_left_content.setText(smsInfo.getSmsbody());
		} else {
			tv_right_content.setBackgroundResource(R.drawable.sent_bg);
			tv_right_content.setVisibility(View.VISIBLE);
			tv_left_content.setVisibility(View.GONE);
			tv_right_content.setText(smsInfo.getSmsbody());
		}
		return convertView;
	}

	public void setData(ArrayList<SmsInfo> list) {
		if (list != null)
			mList = list;

		notifyDataSetChanged();
	}

}
