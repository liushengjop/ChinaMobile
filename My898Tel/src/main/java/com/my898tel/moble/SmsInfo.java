package com.my898tel.moble;

import java.io.Serializable;


/**
 * 短信消息实体类
 *
 * @author liusheng
 */
public class SmsInfo implements Serializable, Cloneable {
    /**
     *
     */
    private static final long serialVersionUID = 4008140254428597141L;
    /**
     * 短信内容
     */
    private String smsbody;
    /**
     * 发送短信的电话号码
     */
    private String phoneNumber;
    /**
     * 发送短信的日期和时间
     */
    private String date;
    /**
     * 发送短信人的姓名
     */
    private String name;
    /**
     * 短信类型1是接收到的，2是已发出
     */
    private int type;

    public String getSmsbody() {
        return smsbody;
    }

    public void setSmsbody(String smsbody) {
        this.smsbody = smsbody;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public Object clone() {
        SmsInfo smsInfo = null;
        try {
            smsInfo = (SmsInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return smsInfo;
    }

}
