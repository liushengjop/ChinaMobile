package com.my898tel.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/***
 * 判断内存的类
 * 
 * @author Administrator
 * 
 */
public class Util_MemoryStatus {
	static final int ERROR = -1;

	static public boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	// 判断本机内存是否可用
	static public boolean isInternalMemoryAvailable() {
		if (getAvailableInternalMemorySize() > 50) {
			return true;
		} else {
			return false;
		}
	}

	// 判断Sdcard 内存 >20m 时可用
	static public boolean isExternalMemorAvailable() {
		if (getAvailableExternalMemorySize() > 50) {
			return true;
		} else {
			return false;
		}
	}

	// 判断Sdcard 内存 >50m 时可用
	static public boolean isMemorAvailable() {
		// System.out.println("getAvailableExternalMemorySize()===>>" +
		// getAvailableExternalMemorySize());
		// System.out.println("getAvailableInternalMemorySize()===>>" +
		// getAvailableInternalMemorySize());
		if ((getAvailableExternalMemorySize() + getAvailableInternalMemorySize()) > 50) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 可用内存 M为单位
	 * 
	 * @return
	 */
	static public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		// 是否可以写
		// System.out.println("getAvailableInternalMemorySize--->>path.canWrite"
		// + path.canWrite());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize / (1024 * 1024);
	}

	/**
	 * get free of the sdcard
	 * 
	 * @return
	 */
	public static long getFreeSizeSD() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize); // 单位bit
	}
	/**
	 * get free of internal memory size
	 * 
	 * @return
	 */
	public static long getFreeSize() {
		File root = Environment.getRootDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long availCount = sf.getAvailableBlocks();
		return (availCount * blockSize);// bit
	}

	/**
	 * 可用内存 M为单位
	 * 
	 * @return
	 */
	static public long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			// System.out.println("ExternalStorage path===>>" +
			// path.getAbsolutePath());
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize / (1024 * 1024);
		} else {
			return ERROR;
		}
	}

	static public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	// <<<<<--------暂时无用------------->>>>
	static public long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	static public String formatSize(long size) {
		String suffix = null;
		if (size >= 1024) {
			suffix = "KiB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MiB";
				size /= 1024;
			}
		}
		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}
}