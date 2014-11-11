package com.my898tel.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.my898tel.R;

/**
 * @author herozhou1314
 * 
 * @email heozhou1314@163.com
 * 
 * @Create @2013-4-20
 * 
 */
public class EndlessBaseAdapter extends EndlessAdapter {
	private boolean hasMoreData = false;
	Context mContext;
	LayoutInflater mInflater;
	PageNextListene pageNextListene;
	int resouseId = R.layout.load_more_footer;

	/***
	 * 
	 * @param mContext
	 * @param adapter
	 * 
	 * @param hasRunInBackground
	 *            自带的加载器
	 */
	public EndlessBaseAdapter(Context mContext, BaseAdapter adapter, boolean hasRunInBackground) {
		super(adapter);
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
		setRunInBackground(hasRunInBackground);// 不用自带的加载器
		hasMoreData = false;
		stopAppending();
		setHasMoreData(false);
	}

	/***
	 * 
	 * @param mContext
	 * @param adapter
	 * 
	 * @param hasRunInBackground
	 *            自带的加载器
	 */
	public EndlessBaseAdapter(Context mContext, BaseAdapter adapter, boolean hasRunInBackground, int resouseId) {
		super(adapter);
		this.resouseId = resouseId;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
		this.resouseId = resouseId;
		setRunInBackground(hasRunInBackground);// 不用自带的加载器
		hasMoreData = false;
		stopAppending();
		setHasMoreData(false);
	}

	public void setPageNextListene(PageNextListene pageNextListene) {
		this.pageNextListene = pageNextListene;
	}

	@Override
	protected View getPendingView(ViewGroup parent) {
		View row = mInflater.inflate(resouseId, null);
		return (row);
	}

	@Override
	protected boolean cacheInBackground() throws Exception {
		if (pageNextListene != null) {
			pageNextListene.PageNext();
		}
		return hasMoreData;
	}

	public void setHasMoreData(boolean hasMoreData) {
		this.hasMoreData = hasMoreData;
	}
	@Override
	protected void appendCachedData() {
	}
	public interface PageNextListene {
		void PageNext();
	}
	public boolean checkNexptPage(int pageSize, int listCount) {
		// TODO Auto-generated method stub
		if (pageSize != 0 && (listCount >= pageSize)) {// 当加载过来的条数不等于(这种情况下只有小于)规定条数的时候,就预示着没有下一页
			restartAppending();
			setHasMoreData(true);
			return true;
		} else if (listCount < pageSize) {
			stopAppending();
			setHasMoreData(false);
		}
		return false;
	}
}