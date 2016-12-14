package com.baichang.android.common;

import android.content.Context;

/**
 * Created by iscod.
 * Time:2016/12/7-15:40.
 */

public interface Configuration {

    String getApiDefaultHost();

    String getApiWebView();

    Context getAppContext();

    String getApiUploadImage();

    String getApiLoadImage();

    String getToken();

    String getApiDownload();

    String getApiUpload();

    void refreshToken();

    int getAppBarColor();
}
