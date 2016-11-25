package cn.ml.base.retrofit;


import android.text.TextUtils;
import android.widget.Toast;

import java.net.SocketTimeoutException;

import cn.ml.base.Configuration;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLToastUtils;
import cn.ml.base.utils.ToolsUtil;
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
                    MLToastUtils.showMessage(Configuration.getAppContext(), "网络未连接，稍后重试");
                    onCompleted();
                    return;
                }
            }

            @Override
            public void onCompleted() {
                MLDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                MLDialogUtils.dismissProgressDialog();
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(Configuration.getAppContext(), "请求超时", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.equals("该用户没有授权", e.getMessage())) {
//                    Intent i = new Intent(APP.getInstance(), LoginActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    APP.getInstance().startActivity(i);
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
                    MLToastUtils.showMessage(Configuration.getAppContext(), "网络未连接，稍后重试");
                    onCompleted();
                    return;
                }
            }


            @Override
            public void onCompleted() {
                MLDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                MLDialogUtils.dismissProgressDialog();
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
