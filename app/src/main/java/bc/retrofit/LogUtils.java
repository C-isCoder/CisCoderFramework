package bc.retrofit;

import android.util.Log;

import bc.base.APP;
import com.baichang.android.library.utils.MLToastUtils;

/**
 * Created by iscod.
 * Time:2016/9/23-11:14.
 */

public class LogUtils {

    public static void logError(Throwable e) {
        MLToastUtils.showMessage(APP.getInstance(), e.getMessage());
        Log.e("Request", "请求异常: " + e.toString());
    }
}
