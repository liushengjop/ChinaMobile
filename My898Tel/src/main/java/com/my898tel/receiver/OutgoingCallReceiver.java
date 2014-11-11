package com.my898tel.receiver;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.util.Unit_XML;
import com.my898tel.util.Util_file;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

/***
 * 电话拦截
 * 
 * @author liusheng
 * te
 * 
 */
public class OutgoingCallReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		
		if(!Util_file.getIsRunApplication(context).equals("true"))
		{
			Toast.makeText(context, "软件已经被停用", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String phoneNumber = getResultData();
		String cornet = context.getResources().getString(R.string.call_need_add);

        String model = Build.MODEL;
        //软件内拨号 必须弹窗 软件外宁算
        if(Unit_XML.readAppcALL()){
            Unit_XML.saveIsAppCall(false);
            alert(phoneNumber,cornet,context);
        }else{
            Unit_XML.saveIsAppCall(false);
            if(model.contains("MI") || model.contains("MX")){
            }else{
                alert(phoneNumber,cornet,context);
            }
        }


	}

    public void alert(String phoneNumber,String cornet,Context context){
        if(Unit_XML.getLocalCall().equals("99999")){
            Unit_XML.saveLocalCall("");
            return;
        }

        if (phoneNumber != null && !phoneNumber.startsWith(cornet)) {
            setResultData(null);
            SelectDialog.getInstance().createView(context,phoneNumber);
        }

    }
	
	
}
