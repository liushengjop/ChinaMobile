package com.my898tel.ui.dialog;

import com.my898tel.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/***
 * loading 对话框
 * @author liusheng
 *
 */
public class DialogLoading {
	private ProgressDialog dialog;
	
	
	public static ProgressDialog ShowLoading(Context context)
	{
		ProgressDialog dialog=new ProgressDialog(context,R.style.ThemeNoBlackDialog);
		dialog.setCancelable(false);
		dialog.setMessage(context.getResources().getString(R.string.str_lable_0３));
		dialog.show();
		return dialog;
	}
	
	public static ProgressDialog ShowLoading(Context context,int resId)
	{
		ProgressDialog dialog=new ProgressDialog(context,R.style.ThemeNoBlackDialog);
		dialog.setCancelable(false);
		dialog.setMessage(context.getResources().getString(resId));
		dialog.show();
		return dialog;
	}



    /**
     * 得到自定义的progressDialog
     * @param context
     * @return
     */
    public static Dialog loadDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog_alert, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog pDialog = new Dialog(context, R.style.SampleTheme);// 创建自定义样式dialog

        pDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        return pDialog;

    }

}
