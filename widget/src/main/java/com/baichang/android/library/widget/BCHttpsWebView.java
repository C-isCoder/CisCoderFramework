package com.baichang.android.library.widget;

import android.content.Context;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by iscod.
 * Time:2016/12/6-16:28.
 */

public class BCHttpsWebView extends WebView {
    public BCHttpsWebView(Context context) {
        super(context);
        init();
    }

    public BCHttpsWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BCHttpsWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("HttpsWebView", error.toString());
                handler.proceed();
            }
        });
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
    }
}
