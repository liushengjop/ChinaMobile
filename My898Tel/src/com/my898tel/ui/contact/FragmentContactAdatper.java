package com.my898tel.ui.contact;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.moble.PhoneInfo;
import com.my898tel.ui.message.MessageDetail;

/**
 * 联系人适配器
 *
 * @author liusheng
 */
public class FragmentContactAdatper extends BaseAdapter {
    private String[] letters = new String[]{"#", "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};


    private ArrayList<String> mList;

    private Context mContext;


    private LinkedHashMap<String, ArrayList<PhoneInfo>> mMaps;

    public FragmentContactAdatper(LinkedHashMap<String, ArrayList<PhoneInfo>> maps, ArrayList<String> list, Context mContext) {
        super();
        this.mMaps = maps;
        mList = list;
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


        String key = mList.get(position);
        ArrayList<PhoneInfo> phoneInfos = mMaps.get(key);

        final PhoneInfo phone = phoneInfos.get(0);



        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
        }

        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView)convertView.findViewById(R.id.tv_phone);
        ImageView iv_call = (ImageView)convertView.findViewById(R.id.iv_call);

        TextView tv_sort = (TextView)convertView.findViewById(R.id.tv_sort);
        tv_sort.setText(phone.getSort_key().toUpperCase());

        if(position != 0){
            String old = mList.get(position-1);

            PhoneInfo oldPhone = mMaps.get(old).get(0);
            if(phone.getSort_key().equals(oldPhone.getSort_key())){
                tv_sort.setVisibility(View.GONE);
            }else{
                tv_sort.setVisibility(View.VISIBLE);
            }
        }
        
        iv_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UIApplication.callPhone(mContext,phone.getDate1());
			}
		});
//        convertView.setOnClickListener(new View.OnClickListener() {
//
//        	@Override
//        	public void onClick(View v) {
//        		UIApplication.callPhone(mContext,phone.getDate1());
//        	}
//        });
        
        
//        ImageView opt_message = (ImageView)convertView.findViewById(R.id.opt_message);
//        ImageView opt_call = (ImageView)convertView.findViewById(R.id.opt_call);
//        opt_call.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				UIApplication.callPhone(mContext,phone.getDate1());
//			}
//		});
//
//        opt_message.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(mContext, MessageDetail.class);
//				intent.putExtra("phone", phone.getDate1());
//				intent.putExtra("name",phone.getDisplay_name());
//				mContext.startActivity(intent);
//			}
//		});
        


        tv_name.setText(phone.getDisplay_name());
        
        tv_phone.setText(phone.getDate1());
        return convertView;
    }

    public String getCurrert(int i) {
        return mMaps.get(mList.get(i)).get(0).getSort_key();
    }

}
