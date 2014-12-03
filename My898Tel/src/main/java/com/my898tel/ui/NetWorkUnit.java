package com.my898tel.ui;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.my898tel.UIApplication;
import com.my898tel.ui.dialog.DialogLoading;


import org.json.JSONException;
import org.json.JSONObject;

/***
 * 网络连接框架层 就是稍微封装了下Volley 1 添加了一个弹出框 2 设置了下连接超时时间
 * 
 * @author liusheng
 * 
 */
public class NetWorkUnit extends JsonObjectRequest {

    private String mUrl;

    private int mTag=0;

	/** 应用上下文 */
	private Context mContext;

	/** 网络请求 */
	private RequestQueue mRequestQueue;

	/** 设置连接超时时间 */
	private int SOCKET_TIMEOUT = 6 * 1000;

    JSONObject jsonRequest;
	/****
	 * 无效
	 */
	@Override
	public RetryPolicy getRetryPolicy() {
		RetryPolicy retryPolicy = new DefaultRetryPolicy(SOCKET_TIMEOUT, 0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return retryPolicy;
	}

	/** 弹出框 */
	public Dialog pDialog;
	

	public NetWorkUnit(Context context, int method, String url,
                       JSONObject jsonRequest, Listener<JSONObject> listener,
                       ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);

        this.jsonRequest=jsonRequest;
		mUrl = url;
		
		setRetryPolicy(getRetryPolicy());

		mRequestQueue = UIApplication.getInstance().getRequestQueue();

		mRequestQueue.start();

		mContext = context;



        pDialog = DialogLoading.loadDialog(context);

		mRequestQueue.add(this);

	}


	private boolean mIsShowDialog = true;

	public NetWorkUnit(Context context, int method, String url,
                       JSONObject jsonRequest, Listener<JSONObject> listener,
                       ErrorListener errorListener, boolean isShowDialog) {
		super(method, url, jsonRequest, listener, errorListener);
		mUrl = url;
		setRetryPolicy(getRetryPolicy());
        this.jsonRequest=jsonRequest;
		mRequestQueue = UIApplication.getInstance().getRequestQueue();

		mRequestQueue.start();

		mContext = context;

		mRequestQueue.add(this);
	}
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
		return super.parseNetworkResponse(response);
	}

    public int getmTag() {
        return mTag;
    }

    public void setmTag(int mTag) {
        this.mTag = mTag;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
	protected void deliverResponse(JSONObject response) {
        Log.d("NetworkResponse-->>",jsonRequest+" \nNetworkResponse-->>"+response.toString());
		super.deliverResponse(response);
	}

    @Override
    public void deliverError(VolleyError error) {
        if(pDialog != null )
            pDialog.dismiss();

        super.deliverError(error);

    }
}
