package com.baichang.library.test.common;

import android.os.Bundle;

import com.baichang.android.widget.BCHttpsWebView;
import com.baichang.library.test.R;
import com.baichang.library.test.base.CommonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HttpsWebViewActivity extends CommonActivity {
    @BindView(R.id.web_view)
    BCHttpsWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_https_web_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mWebView.loadUrl("https://water.weiidu.com/inter/web/interface/view?projectId=8");
    }
}
