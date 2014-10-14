package com.my898tel.util;

import android.content.Context;

import com.baidu.mobstat.StatService;

public class StatWrapper {
	
	public static void onResume(Context context) {

		/**
		 * 此处调用基本统计代码
		 */
		StatService.onResume(context);
	}

	public static void onPause(Context context) {

		/**
		 * 此处调用基本统计代码
		 */
		StatService.onPause(context);
	}


}
