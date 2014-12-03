package com.my898tel.config;

/**
 * 所有查询的URI
 *
 * @author liusheng
 */
public interface MyUri {

    /**
     * 所有的短信消息URI地址
     */
    String ALL_SMS = "content://sms/";

    /**
     * 所有联系人
     */
    String ALL_PHONE = "content://com.android.contacts/data/phones";
    /**
     * 所有联系人
     */
    String URI_LEFT_MONEY = "http://www.webtodll.com/project_mobile/app/1.0/index.php/getmoney";
    /**
     * 所有联系人
     */
    String URI_UPDATE = "http://www.webtodll.com/project_mobile/app/1.0/index.php/update";
    /**
     * 所有联系人
     */
    String URI_FEEDBACK = "http://www.webtodll.com/project_mobile/app/1.0/index.php/feedback";

}
