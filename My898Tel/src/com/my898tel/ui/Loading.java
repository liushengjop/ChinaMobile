package com.my898tel.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.my898tel.R;
import com.my898tel.ui.setting.ActivitySetting;
import com.my898tel.util.Unit_XML;
/***
 * loading 页面
 * @author liusheng
 *
 */
@SuppressLint("NewApi")
public class Loading extends BaseActivity{
	
	Handler handler = new Handler();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
        if(!isAddShortCut()){
            createShortCut();
        }


		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
//		if(Util_file.getIsRunApplication(Loading.this).equals("true"))
//		{
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
                    if( Unit_XML.getOpenCount() == 0){
                        Intent intent = new Intent();
                        intent.setClass(Loading.this, ActivityFirst.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent();
                        intent.setClass(Loading.this, MainActivity.class);
                        startActivity(intent);
                    }
					finish();
				}
			}, 2500);
//		}
//		else
//		{
//			finish();
//		}
		
	}




    public void createShortCut(){
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // 不允许重建

        shortcut.putExtra("duplicate", false);

        // 设置名字

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,this.getString(R.string.app_name));

        // 设置图标

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(this,

                R.drawable.icon));

        // 设置意图和快捷方式关联程序

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,new Intent(this, Loading.class).setAction(Intent.ACTION_MAIN));

        // 发送广播

        sendBroadcast(shortcut);
    }

    public boolean isAddShortCut() {

        boolean isInstallShortcut = false;
        final ContentResolver cr = this.getContentResolver();

        int versionLevel = android.os.Build.VERSION.SDK_INT;
        String AUTHORITY = "com.android.launcher2.settings";

        //2.2以上的系统的文件文件名字是不一样的
        if (versionLevel >= 8) {
            AUTHORITY = "com.android.launcher2.settings";
        } else {
            AUTHORITY = "com.android.launcher.settings";
        }

        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI,
                new String[] { "title", "iconResource" }, "title=?",
                new String[] { getString(R.string.app_name) }, null);

        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        return isInstallShortcut;
    }
       
  
  }
