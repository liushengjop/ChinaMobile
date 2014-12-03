package com.my898tel;

import android.os.Environment;

import java.io.File;

public class Config {
	// 公用接口
	public static File sdcardRootPath = Environment.getExternalStorageDirectory();
	public static boolean bFileOperationInSDCard = true; // true：文件保存在SD卡；false：保存在/data/data/<myAppPackage
	// title>/files目录；原因：真机系统文件需要root才能看到
	public static final String fileOperationInSDCardPath = sdcardRootPath + "/mobile/898/";// SD卡下指定保存的路径；
	// title>/files目录；原因：真机系统文件需要root才能看到
	public static final int CONNECT_TIMEOUT = 60 * 1000;// 单位：ms
	public static int READ_TIMEOUT = 60 * 1000;// 单位：ms
	public static final int LISTVIEW_IMAGEBUF_MAX = 12;// 图片缓存个数
	public static final int groupMemberCount = 9; // 每个餐厅的属性个数，跟随协议变动
	public static final String REGULAR_EXPRESSION_0 = "<`>";// 分隔符，别乱改
	public static final String REGULAR_EXPRESSION_1 = "<~>";// 分隔符，别乱改
	public static boolean ifNetworkConnection = true; // 网络连接是否正常
	public static final String IMAGE_FORMATE = ".jpg"; // 图片格式
	public static boolean bIsHaveMapModule = true; // true：文件保存在SD卡；false：保存在/data/data/<myAppPackage
	public static final int UPDATE_MAX_COUNT = 5;// 图片上传个数
	public static final int UPDATE_MAX_WIDTH = 640;// 图片上传
}