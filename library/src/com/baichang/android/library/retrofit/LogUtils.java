package com.baichang.android.library.retrofit;

import android.util.Log;

import com.baichang.android.library.Configuration;
import com.baichang.android.library.utils.MLToastUtils;

/**
 * Created by iscod.
 * Time:2016/9/23-11:14.
 */

public class LogUtils {

    public static void logError(Throwable e) {
        MLToastUtils.showMessage(Configuration.getAppContext(), e.getMessage());
        Log.e("Request", "请求异常: " + e.toString());
    }
}
