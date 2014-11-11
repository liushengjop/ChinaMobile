package com.my898tel.moble;

import com.my898tel.util.Util_G;
/**
 * @author herozhou1314@gmail.com
 * @version V 1.0
 * @Title: CallLogItem.java
 * @Package com.my898tel.models
 * @Description: TODO
 * @date 2014-4-8 上午11:43:19
 */
public class CallLogItem {
	/**
	 * The type of the call (incoming, outgoing or missed).
	 * <p/>
	 * Type: INTEGER (int) Call log type for incoming calls.
	 * <p/>
	 * INCOMING_TYPE = 1;
	 * <p/>
	 * Call log type for outgoing calls.
	 * <p/>
	 * OUTGOING_TYPE = 2;
	 * <p/>
	 * Call log type for missed calls.
	 * </P>
	 * MISSED_TYPE = 3; </P>
	 */
	public int TYPE;
	public int _COUNT = 1;
	public String photoId;
	public String number;
	public String name;
	public String typeStr;
	public String date;
	public String groupDate;
	public String duration;
	public boolean boolisShowSubMenu;
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CallLogItem))
			return false;
		final CallLogItem other = (CallLogItem) o;
		if (Util_G.isEquals(number, other.number) && TYPE == other.TYPE) {
			return true;
		}
		return false;
	}
}
