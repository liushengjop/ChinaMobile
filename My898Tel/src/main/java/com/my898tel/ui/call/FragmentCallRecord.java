package com.my898tel.ui.call;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.my898tel.R;
import com.my898tel.moble.CallLogBean;
import com.my898tel.ui.BaseFragment;
import com.my898tel.util.ObserverCallLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shengliu on 14-9-23.
 */
public class FragmentCallRecord extends BaseFragment {

    private ListView listView;

    private List<CallLogBean> callLogs;

    /**重第0页开始*/
    private int currentPage = 0;

    /**每页显示条数*/
    private int pageSize = 100;

    private  AdatperCallRecord adatperCallRecord;

    private PullToRefreshListView pull_refresh_list;

    private Handler callloghandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            callLogs = (List<CallLogBean>) msg.obj;
            currentPage = 0;
            setAdapter(callLogs);
        };
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_record,container,false);

        pull_refresh_list = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        listView = pull_refresh_list.getRefreshableView();

        ObserverCallLog observerCallLog = new ObserverCallLog(getActivity(),callloghandler);

        getActivity().getContentResolver().registerContentObserver(android.provider.CallLog.Calls.CONTENT_URI, true, observerCallLog);
        asyncQueryHandler = new MyAsyncQueryHandler(getActivity()
                .getContentResolver());



        if(callLogs == null){
            currentPage = 0;
            init();
        }else{
            setAdapter(callLogs);
        }

        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 0;
                init();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = currentPage + 1;
                init();
            }
        });

        return view;
    }

    public void init(){
        Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
        // 查询的列
        String[] projection = { CallLog.Calls.DATE, // 日期
                CallLog.Calls.NUMBER, // 号码
                CallLog.Calls.TYPE, // 类型
                CallLog.Calls.CACHED_NAME, // 名字
                CallLog.Calls._ID, // id
        };
        if(callLogs != null && callLogs.size() > 0){
            CallLogBean bean = callLogs.get(callLogs.size()-1);

            asyncQueryHandler.startQuery(0, null, uri, projection, CallLog.Calls.DATE +"<?", new String[]{bean.getAllTime()},
                    CallLog.Calls.DATE + " DESC limit "+pageSize+ " offset " + currentPage*pageSize);
        }else{
            asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                    CallLog.Calls.DATE + " DESC limit "+pageSize+ " offset " + currentPage*pageSize);
        }

    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        pull_refresh_list.onRefreshComplete();
        if (cursor != null && cursor.getCount() > 0) {
           List<CallLogBean> callLogs = new ArrayList<CallLogBean>();
            SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");

            SimpleDateFormat sdfAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            cursor.moveToFirst(); // 游标移动到第一项
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                Long totalTime = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DATE));
                date = new Date(totalTime);
                String number = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                int type = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                String cachedName = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.CACHED_NAME));// 缓存的名称与电话号码，如果它的存在
                int id = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls._ID));

                CallLogBean callLogBean = new CallLogBean();
                callLogBean.setId(id);
                callLogBean.setNumber(number);
                callLogBean.setName(cachedName);
                if (null == cachedName || "".equals(cachedName)) {
                    callLogBean.setName(number);
                }
                callLogBean.setType(type);
                callLogBean.setDate(sdfYear.format(date));
                callLogBean.setTime(sdfHour.format(date));
                callLogBean.setAllTime(sdfAll.format(date));
                callLogs.add(callLogBean);
            }
            if (callLogs.size() > 0) {
                if(this.callLogs == null ||this.callLogs.size() == 0|| pageSize == 0){
                    this.callLogs =callLogs;
                }else{
                    this.callLogs.addAll(callLogs);
                }
                setAdapter(this.callLogs);
            }
        }

    }

    public void setAdapter( List<CallLogBean> callLogs){
        if(adatperCallRecord == null){
            currentPage = 0;
            adatperCallRecord = new AdatperCallRecord(callLogs,getActivity());
        }else{
            adatperCallRecord.setData(callLogs);
        }

        if(listView.getAdapter() == null){
            currentPage = 0;
            listView.setAdapter(adatperCallRecord);
        }

        if(pageSize*(currentPage+1) <= adatperCallRecord.getData().size()){
            pull_refresh_list.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        }else{
            pull_refresh_list.setMode(PullToRefreshBase.Mode.DISABLED);
        }

    }
}
