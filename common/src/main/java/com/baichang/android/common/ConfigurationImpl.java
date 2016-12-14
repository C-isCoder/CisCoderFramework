package com.baichang.android.common;

import android.content.Context;

/**
 * Created by iscod.
 * Time:2016/12/7-15:03.
 */

public class ConfigurationImpl implements Configuration {
    private static Configuration sConfig;
    private static ConfigurationImpl INSTANCE;

    private ConfigurationImpl() {
    }

    public static ConfigurationImpl get() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigurationImpl();
        }
        return INSTANCE;
    }

    public static void init(Configuration config) {
        sConfig = config;
    }


    @Override
    public String getApiDefaultHost() {
        return sConfig.getApiDefaultHost();
    }

    @Override
    public String getApiWebView() {
        return sConfig.getApiWebView();
    }

    @Override
    public Context getAppContext() {
        return sConfig.getAppContext();
    }

    @Override
    public String getApiUploadImage() {
        return sConfig.getApiUploadImage();
    }

    @Override
    public String getApiLoadImage() {
        return sConfig.getApiLoadImage();
    }

    @Override
    public String getToken() {
        return sConfig.getToken();
    }

    @Override
    public String getApiDownload() {
        return sConfig.getApiDownload();
    }

    @Override
    public String getApiUpload() {
        return sConfig.getApiUpload();
    }

    @Override
    public void refreshToken() {
        sConfig.refreshToken();
    }

    @Override
    public int getAppBarColor() {
        return sConfig.getAppBarColor();
    }

}
