package com.my898tel.ui;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.my898tel.R;
import com.my898tel.ui.dialog.DialogCustomeFragment;
import com.my898tel.ui.setting.ActivitySetting;

import android.app.Activity;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * 基础Fragment
 *
 * @author liusheng
 */
public class BaseFragment extends Fragment implements  Response.ErrorListener,Response.Listener<JSONObject>{

    /**
     * 初始化查询标示
     */
    protected static final int INIT = 0;

    /**
     * 查询标示
     */
    protected static final int SEARCH = 1;

    /**
     * 查询助手
     */
    protected MyAsyncQueryHandler asyncQueryHandler;


    public class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            BaseFragment.this.onQueryComplete(token, cookie, cursor);
        }
    }

    /**
     * 需要覆盖的查询方法
     *
     * @param token
     * @param cookie
     * @param cursor
     */
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {

    }


    public void setListener(){

    }
    @Override
    public void onErrorResponse(VolleyError error) {

        DialogCustomeFragment.Builder builder = new DialogCustomeFragment.Builder();
        builder.setTitle(getString(R.string.tip_alert));
        builder.setMessage("网络连接失败");
        builder.setBtn1(getString(R.string.sure),new DialogCustomeFragment.Listener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        builder.show(getFragmentManager(),DialogCustomeFragment.class.getName());
    }

    @Override
    public void onResponse(JSONObject jsonObject) {

    }


    protected  NetWorkUnit netWorkUnit;

    public void submit(int tag,String url,JSONObject obj){
        netWorkUnit = new NetWorkUnit(getActivity(), Request.Method.POST,url,obj,this,this);
        netWorkUnit.setmTag(tag);
    }


}
