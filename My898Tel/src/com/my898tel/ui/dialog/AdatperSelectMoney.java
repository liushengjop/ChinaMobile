package com.my898tel.ui.dialog;

import java.util.ArrayList;

import com.my898tel.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdatperSelectMoney extends BaseAdapter{
	
	private ArrayList<String> mList;
	
	private Context  mContext;
	
	
	

	public AdatperSelectMoney(ArrayList<String> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_money, null);
		}
		TextView tv_money = (TextView) convertView.findViewById(R.id.tv_money);
		tv_money.setText(mList.get(position));
		return convertView;
	}
	
	

}
