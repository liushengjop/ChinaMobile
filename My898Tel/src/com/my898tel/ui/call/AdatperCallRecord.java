package com.my898tel.ui.call;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.moble.CallLogBean;

import java.util.List;

/**
 * Created by shengliu on 14-9-23.
 */
public class AdatperCallRecord extends BaseAdapter {


    private List<CallLogBean> mList;

    private Context mContext;


    private LayoutInflater mInflater;

    public AdatperCallRecord(List<CallLogBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       final CallLogBean callLogBean = mList.get(i);

        if(view == null){
            view = mInflater.inflate(R.layout.item_call_log,null);
        }
        //日期
        TextView tv_date = (TextView)view.findViewById(R.id.tv_date);
        //名称
        TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
        //手机号码
        TextView tv_phone = (TextView)view.findViewById(R.id.tv_phone);


        ImageView iv_call = (ImageView)view.findViewById(R.id.iv_call);
        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIApplication.getInstance().callPhone(mContext,callLogBean.getNumber());
            }
        });
        String phoneStr = "";

//        来电:1，拨出:2,未接:3
        switch (callLogBean.getType()){
            case 1:
                tv_name.setTextColor(mContext.getResources().getColor(R.color.color697a7c));
                phoneStr = mContext.getResources().getString(R.string.phone_incoming);
                break;
            case 2:
                tv_name.setTextColor(mContext.getResources().getColor(R.color.color697a7c));
                phoneStr = mContext.getResources().getString(R.string.phone_outcoming);
                break;
            case 3:
                phoneStr = mContext.getResources().getString(R.string.phone_no_recive);
                tv_name.setTextColor(mContext.getResources().getColor(R.color.colorf74c31));
                break;
        }
        phoneStr = phoneStr +"\u3000"+callLogBean.getTime()+"\u3000"+callLogBean.getNumber();

        tv_name.setText(callLogBean.getName());

        tv_phone.setText(phoneStr);
        tv_date.setText(callLogBean.getDate());
        if(i != 0){
            CallLogBean callLogBeanOld = mList.get(i-1);
            if(!callLogBean.getDate().equals(callLogBeanOld.getDate())){
                tv_date.setVisibility(View.VISIBLE);

            }else{
                tv_date.setVisibility(View.GONE);
            }
        }else{
            tv_date.setVisibility(View.VISIBLE);
        }


        return view;
    }


    public void setData(List<CallLogBean> list){
        if(list != null){
            mList = list;
        }
        notifyDataSetChanged();
    }

    public void addAllData(List<CallLogBean> list){
        if (list != null){
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public List<CallLogBean> getData(){
        return mList;
    }
}
