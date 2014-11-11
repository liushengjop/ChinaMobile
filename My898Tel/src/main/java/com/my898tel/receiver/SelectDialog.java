package com.my898tel.receiver;

import com.my898tel.R;
import com.my898tel.UIApplication;

import com.my898tel.util.Unit_XML;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SelectDialog {  
	
	
	Handler handler = new Handler();
	//定义浮动窗口布局  
    LinearLayout mFloatLayout;  
    WindowManager.LayoutParams wmParams;  
    //创建浮动窗口设置布局参数的对象  
    WindowManager mWindowManager;  
    
    private Context mContext;
    
    private  View view;
    
    private String phoneNum;
    
    static SelectDialog selectDialog ;
    
    /**是否显示了对话款*/
    public boolean isHasShowWindow = false;
    
    
    private SelectDialog() {
    		
	}
    
    public static SelectDialog getInstance()
    {
    		if(selectDialog == null)
    		{
    			selectDialog = new SelectDialog(); 
    		}
    		return selectDialog;
    }
    
    public void createView(Context context,String phoneNum)
    {
    		mContext = context;
		this.phoneNum = phoneNum;
		if(!isHasShowWindow)
			createView();
    }
    
    private void createView()
    {
    		isHasShowWindow = true;
    		mContext = UIApplication.getInstance();
    		mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wmParams = new WindowManager.LayoutParams();
		wmParams.type =WindowManager.LayoutParams.TYPE_PHONE; // type是关键，这里的2002表示系统级窗口，你也可以试试2003。
		wmParams.format = 1;
		/**
		 * 这里的flags也很关键 代码实际是wmParams.flags |= FLAG_NOT_FOCUSABLE;
		 * 40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
		 */
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.BOTTOM;
		wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		wmParams.height = LayoutParams.MATCH_PARENT;
		
        LayoutInflater inflater = LayoutInflater.from(mContext);  
        //获取浮动窗口视图所在布局  
        view = (View) inflater.inflate(R.layout.dialog_select, null);
        //添加mFloatLayout  
        mWindowManager.addView(view, wmParams);  
        
        
        ImageButton btn_cancle = (ImageButton) view.findViewById(R.id.btn_cancle);
		Button btn_soft_call = (Button) view.findViewById(R.id.btn_soft_call);
		Button btn_local_call = (Button) view.findViewById(R.id.btn_local_call);
		
		btn_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Myfinish();
			}
		});
		
		btn_soft_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Unit_XML.saveLocalCall("");
				String cornet = mContext.getResources().getString(R.string.call_need_add);
				
				if(phoneNum.startsWith("+86"))
				{
					phoneNum = phoneNum.replace("+86", "");
				}
				if(phoneNum.startsWith("86"))
				{
					phoneNum = phoneNum.replace("86", "");
				}
				callPhone(mContext, cornet + ",9" + phoneNum + "#");
				insertCallLog(mContext, phoneNum, "10", Integer.toString(2), "1", 0L);
			}
		});

		btn_local_call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Unit_XML.saveLocalCall("99999");
				callPhone(mContext,phoneNum);
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						Unit_XML.saveLocalCall("");
					}
				}, 5000);
			}
		});


		
		
        
    }
    
    public void Myfinish()
    {
    		isHasShowWindow = false;
    		mWindowManager.removeView(view);
    }
	
    public void callPhone(Context context, String phoneNum) {
    		Myfinish();
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void insertCallLog(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4, long paramLong) {
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("number", paramString1);
		localContentValues.put("date", Long.valueOf(paramLong + System.currentTimeMillis()));
		localContentValues.put("duration", paramString2);
		localContentValues.put("type", paramString3);
		localContentValues.put("new", paramString4);
		paramContext.getContentResolver().insert(CallLog.Calls.CONTENT_URI, localContentValues);
	}

}
