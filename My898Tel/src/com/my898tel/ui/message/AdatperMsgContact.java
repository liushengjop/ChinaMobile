package com.my898tel.ui.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.my898tel.R;
import com.my898tel.moble.PhoneInfo;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 联系人适配器
 *
 * @author liusheng
 */
public class AdatperMsgContact extends BaseAdapter {

    public ArrayList<PhoneInfo> mList;

    private Context mContext;

    private LinkedHashMap<String, ArrayList<PhoneInfo>> mMaps;

    /**
     * 搜索字段
     */
    private String mSearchStr;

    public ArrayList<PhoneInfo> selectList;

    public AdatperMsgContact(LinkedHashMap<String, ArrayList<PhoneInfo>> maps, String searchStr, Context mContext) {
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

        selectList = new ArrayList<PhoneInfo>();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sms_contact_item, null);
        }
       final CheckBox cb_select_contact = (CheckBox) convertView.findViewById(R.id.cb_select_contact);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);

        String nameStr = phone.getDisplay_name();
        String phoneStr = phone.getDate1();

        tv_name.setText(Html.fromHtml(nameStr.replaceAll(mSearchStr,
                "<font color='#cc0000'>" + mSearchStr + "</font>")));


        tv_phone.setText(Html.fromHtml(phoneStr.replace(mSearchStr,
                "<font color='#cc0000'>" + mSearchStr + "</font>")));


        cb_select_contact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    phone.setIs_check(1);
                } else {
                    phone.setIs_check(0);
                }
            }
        });

        if (phone.getIs_check() == 1) {
            cb_select_contact.setChecked(true);
        } else {
            cb_select_contact.setChecked(false);
        }
        
        convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(cb_select_contact.isChecked())
				{
					cb_select_contact.setChecked(false);
				}
				else
				{
					cb_select_contact.setChecked(true);
				}
			}
		});
        
        
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
