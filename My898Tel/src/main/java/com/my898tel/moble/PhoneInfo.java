package com.my898tel.moble;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 电话号码实体类
 *
 * @author liusheng
 */
public class PhoneInfo implements Parcelable {

    /**
     * 显示的姓名
     */
    private String display_name;

    /**
     * 手机号码
     */
    private String date1;

    private String sort_key;

    private int is_check = 0;

    public int getIs_check() {
        return is_check;
    }

    public void setIs_check(int is_check) {
        this.is_check = is_check;
    }

    private List<PhoneInfo> list;


    public List<PhoneInfo> getList() {
        return list;
    }

    public void setList(List<PhoneInfo> list) {
        this.list = list;
    }

    public String getSort_key() {
        return sort_key;
    }

    public void setSort_key(String sort_key) {
        this.sort_key = sort_key;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public PhoneInfo(String display_name, String date1) {
        super();
        this.display_name = display_name;
        this.date1 = date1;
    }

    public PhoneInfo() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(display_name);
        dest.writeString(date1);
        dest.writeString(sort_key);
        dest.writeInt(is_check);
    }

    public PhoneInfo(Parcel in) {
        display_name = in.readString();
        date1 = in.readString();
        sort_key = in.readString();
        is_check = in.readInt();
    }

    public static final Parcelable.Creator<PhoneInfo> CREATOR = new Creator<PhoneInfo>() {

        @Override
        public PhoneInfo[] newArray(int size) {
            return new PhoneInfo[size];
        }

        @Override
        public PhoneInfo createFromParcel(Parcel source) {
            return new PhoneInfo(source);
        }
    };


}
