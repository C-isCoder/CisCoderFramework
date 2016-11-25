package cn.ml.base.retrofit;

import android.util.Log;

import cn.ml.base.Configuration;
import cn.ml.base.utils.MLToastUtils;

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
