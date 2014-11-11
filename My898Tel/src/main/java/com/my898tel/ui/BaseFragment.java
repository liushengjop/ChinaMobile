package com.my898tel.ui;


import com.my898tel.R;
import com.my898tel.ui.setting.ActivitySetting;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 基础Fragment
 *
 * @author liusheng
 */
public class BaseFragment extends Fragment {

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


}
