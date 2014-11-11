package com.my898tel.ui.recharge;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.my898tel.R;
import com.my898tel.ui.BaseActivity;
import com.my898tel.ui.dialog.DialogLoading;

/**
 * Created by shengliu on 14-10-6.
 */
public class ActivityWebView extends BaseActivity {


    private WebView wv_rechage;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        String url = getIntent().getStringExtra("url");

        setContentView(R.layout.activity_web_view);

        wv_rechage = (WebView)findViewById(R.id.wv_rechage);
        wv_rechage.loadUrl(url);
        wv_rechage.getSettings().setSupportZoom(true);
        wv_rechage.getSettings().setBuiltInZoomControls(true);

//        wv_rechage.setInitialScale();
        wv_rechage.getSettings().setJavaScriptEnabled(true);
        wv_rechage.setWebViewClient(new LoadingClient());

        setTitleNoRightBtn(R.string.recharge);

    }


    public class LoadingClient extends WebViewClient {


        private Dialog dialog;

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            dialog = DialogLoading.loadDialog(ActivityWebView.this);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            dialog.dismiss();
        }
    }
}
