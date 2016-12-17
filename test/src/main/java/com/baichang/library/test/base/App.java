package com.baichang.library.test.base;


import android.content.Context;
import android.text.TextUtils;

import com.baichang.android.common.BCApplication;
import com.baichang.android.common.Configuration;
import com.baichang.android.common.ConfigurationImpl;
import com.baichang.library.test.R;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.socialize.PlatformConfig;


/**
 * Created by iCong on 2016/11/25.
 */

public class App extends BCApplication implements Configuration {

    private static App instance;
    //token
    private static String TOKEN = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //配置URL TOKEN
        ConfigurationImpl.init(this);
        //友盟分享
        initShare();
        LeakCanary.install(this);
    }

    private void initShare() {
        //微信
        PlatformConfig.setWeixin("wx1c368b574b528feb", "97a57cf3088035a6ae84a97e5613b1e6");
        //qq空间
        PlatformConfig.setQQZone("1105849494", "vdo9HdxqPdEbGNz6");
    }

    public static void setToken(String Token) {
        TOKEN = Token;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public String getApiDefaultHost() {
        return APIConstants.API_DEFAULT_HOST;
    }

    @Override
    public String getApiWebView() {
        return APIConstants.API_WEB_VIEW;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public String getApiUploadImage() {
        return APIConstants.API_UPLOAD_IMAGE;
    }

    @Override
    public String getApiLoadImage() {
        return APIConstants.API_LOAD_IMAGE;
    }

    @Override
    public String getToken() {
        return TextUtils.isEmpty(TOKEN) ? AppDiskCache.getToken() : TOKEN;
    }

    @Override
    public String getApiDownload() {
        return APIConstants.API_DOWNLOAD;
    }

    @Override
    public String getApiUpload() {
        return APIConstants.API_UPLOAD_IMAGE;
    }

    @Override
    public void refreshToken() {
        //TODO 刷新token
        setToken("refresh token");
    }

    @Override
    public int getAppBarColor() {
        return R.color.app_btn_color;
    }
}
