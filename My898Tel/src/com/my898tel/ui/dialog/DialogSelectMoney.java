package com.my898tel.ui.dialog;

import java.util.ArrayList;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.myinterface.PassData;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

public class DialogSelectMoney extends DialogFragment{
	
	private PassData<String> passData;
	
	private AdatperSelectMoney adatperSelectMoney;
	
	private ListView listview;
	
	
	public PassData<String> getPassData() {
		return passData;
	}


	public void setPassData(PassData<String> passData) {
		this.passData = passData;
	}

	 ArrayList<String> arrayList ;
	 
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setStyle(R.style.SampleTheme, R.style.SampleTheme);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getDialog().setCanceledOnTouchOutside(false);
		arrayList = new ArrayList<String>();
		arrayList.add("30元");
		arrayList.add("50元");
		arrayList.add("100元");
		
		View view = inflater.inflate(R.layout.dialog_select_money, container, false);
		listview = (ListView) view.findViewById(R.id.listview);
		
		adatperSelectMoney = new AdatperSelectMoney(arrayList, getActivity());
		listview.setAdapter(adatperSelectMoney);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getDialog().dismiss();
				if(passData != null)
					passData.passData(arrayList.get(position));
			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		
		Window window = getDialog().getWindow();
		WindowManager.LayoutParams p = window.getAttributes();
		p.width = (int) (UIApplication.getInstance().getScreenWidth()*0.7); // 宽度设置为屏幕的0.95
		p.height = (int) (UIApplication.getInstance().density * 200); // 高度设置为屏幕的0.6

		p.gravity = Gravity.CENTER;
		window.setAttributes(p);
	}

}
