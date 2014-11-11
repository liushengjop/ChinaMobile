package com.my898tel.ui.contact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.moble.PhoneInfo;
import com.my898tel.ui.message.MessageDetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 联系人适配器
 *
 * @author liusheng
 */
public class AdatperContactSearch extends BaseAdapter {

    private ArrayList<PhoneInfo> mList;

    private Context mContext;

    private LinkedHashMap<String, ArrayList<PhoneInfo>> mMaps;

    /**
     * 搜索字段
     */
    private String mSearchStr;



    public AdatperContactSearch(LinkedHashMap<String, ArrayList<PhoneInfo>> maps, String searchStr, Context mContext) {
        super();
        this.mMaps = maps;
        mSearchStr = searchStr;

        Iterator<ArrayList<PhoneInfo>> iterator = maps.values().iterator();
        mList = new ArrayList<PhoneInfo>();

        while (iterator.hasNext()) {
            ArrayList<PhoneInfo> phoneInfos = iterator.next();
            mList.addAll(phoneInfos);
        }
        this.mContext = mContext;
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

        final PhoneInfo phone = mList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
        }

        ImageView iv_call = (ImageView)convertView.findViewById(R.id.iv_call);

        ImageView opt_message = (ImageView)convertView.findViewById(R.id.opt_message);
        ImageView opt_call = (ImageView)convertView.findViewById(R.id.opt_call);
        opt_call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UIApplication.callPhone(mContext,phone.getDate1());
			}
		});
        
        opt_message.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MessageDetail.class);
				intent.putExtra("phone", phone.getDate1());
				intent.putExtra("name",phone.getDisplay_name());
				mContext.startActivity(intent);
			}
		});
        
        
        iv_call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				UIApplication.callPhone(mContext,phone.getDate1());

			}
		});
        
        
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);

        String nameStr = phone.getDisplay_name();
        String phoneStr = phone.getDate1();

        tv_name.setText(Html.fromHtml(nameStr.replaceAll(mSearchStr,
                "<font color='#cc0000'>" + mSearchStr + "</font>")));


        tv_phone.setText(Html.fromHtml(phoneStr.replace(mSearchStr,
                "<font color='#cc0000'>" + mSearchStr + "</font>")));

        TextView tv_sort = (TextView)convertView.findViewById(R.id.tv_sort);
        tv_sort.setText(phone.getSort_key().toUpperCase());

         tv_sort.setVisibility(View.GONE);

//		tv_name.setText(Html.fromHtml(nameStr.replace(nameStr, 
//				"<font color='#cc0000'>" + mSearchStr + "</font>");
//		tv_phone.setText(Html.fromHtml(phoneStr.replace(nameStr, 
//				"<font color='#cc0000'>" + mSearchStr + "</font>")));

        tv_phone.setVisibility(View.VISIBLE);
        return convertView;
    }

    public String getCurrert(int i) {
        return mMaps.get(mList.get(i)).get(0).getSort_key();
    }

}
