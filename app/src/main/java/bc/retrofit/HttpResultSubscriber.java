package bc.retrofit;


import android.widget.Toast;


import java.net.SocketTimeoutException;

import bc.base.APP;
import bc.utils.DialogUtils;
import com.baichang.android.library.utils.MLToastUtils;
import com.baichang.android.library.utils.ToolsUtil;
import rx.Subscriber;

/**
 * Created by iscod.
 * Time:2016/9/21-14:03.
 */

public class HttpResultSubscriber<T> {
    public synchronized Subscriber<T> get(final ResultSuccessListener<T> Callback) {
        return new Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                if (!ToolsUtil.isNetworkConnected()) {
                    MLToastUtils.showMessage(APP.getInstance(), "网络未连接，稍后重试");
                    onCompleted();
                    return;
                }
            }

            @Override
            public void onCompleted() {
                DialogUtils.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                DialogUtils.dismissProgressDialog();
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(APP.getInstance(), "请求超时", Toast.LENGTH_SHORT).show();
                } else {
                    LogUtils.logError(e);
                }
            }

            @Override
            public void onNext(T t) {
                if (Callback != null) {
                    Callback.success(t);
                }
            }
        };
    }

    public synchronized Subscriber<T> get(final ResultSuccessListener<T> Callback, final ResultErrorListener errorBack) {
        return new Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                if (!ToolsUtil.isNetworkConnected()) {
                    MLToastUtils.showMessage(APP.getInstance(), "网络未连接，稍后重试");
                    onCompleted();
                    return;
                }
            }


            @Override
            public void onCompleted() {
                DialogUtils.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                DialogUtils.dismissProgressDialog();
                if (errorBack != null) {
                    errorBack.error(e);
                }
            }

            @Override
            public void onNext(T t) {
                if (Callback != null) {
                    Callback.success(t);
                }
            }
        };
    }
}
