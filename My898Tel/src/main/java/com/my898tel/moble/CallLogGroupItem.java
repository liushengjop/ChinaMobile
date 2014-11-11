package com.my898tel.moble;

import java.util.ArrayList;

import com.my898tel.util.Util_G;

/**
 * @author herozhou1314@gmail.com
 * @version V 1.0
 * @Title: CallLogItem.java
 * @Package com.my898tel.models
 * @Description: TODO
 * @date 2014-4-8 上午11:43:19
 */
public class CallLogGroupItem {
	public String groupDate;
	public ArrayList<CallLogItem> callLogItems;

	public ArrayList<CallLogItem> getCallLogItems() {
		return callLogItems;
	}

	public void setCallLogItems(ArrayList<CallLogItem> callLogItems) {
		this.callLogItems = callLogItems;
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CallLogGroupItem))
			return false;
		final CallLogGroupItem other = (CallLogGroupItem) o;
		if (groupDate == other.groupDate) {
			return true;
		}
		return false;
	}
}
