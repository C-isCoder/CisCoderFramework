package com.baichang.android.request;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;


import com.baichang.android.common.ConfigurationImpl;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by iscod.
 * Time:2016/9/21-14:03.
 */

public class HttpSubscriber<T> {
    private static final String TAG = "Request";
    private Context mContext = null;
    private ProgressDialog mDialog = null;
    private SwipeRefreshLayout mRefresh = null;

    public HttpSubscriber(Context mContext) {
        if (mContext == null) throw new NullPointerException("Context not null");
        WeakReference<Context> wc = new WeakReference<Context>(mContext);
        this.mContext = wc.get();
    }

    public HttpSubscriber(ProgressDialog dialog) {
        if (dialog == null) throw new NullPointerException("Progress not null");
        mDialog = dialog;
    }

    public HttpSubscriber(SwipeRefreshLayout refreshLayout) {
        if (refreshLayout == null) throw new NullPointerException("SwipeRefreshLayout not null");
        mRefresh = refreshLayout;
    }

    public HttpSubscriber() {

    }

    /**
     *
     * @param Callback
     * @return
     */
    public Subscriber<T> get(final HttpSuccessListener<T> Callback) {
        return new Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetWorkStateUtils.isNetworkConnected()) {
                    Toast.makeText(ConfigurationImpl.get().getAppContext(),
                            R.string.net_error_tips, Toast.LENGTH_SHORT).show();
                    onCompleted();
                }
                if (mRefresh != null) {
                    mRefresh.setRefreshing(true);
                } else if (mDialog != null) {
                    mDialog.show();
                } else if (mContext != null) {
                    RequestDialogUtils.show(mContext);
                }
            }

            @Override
            public void onCompleted() {
                if (mRefresh != null) {
                    mRefresh.setRefreshing(false);
                } else if (mDialog != null) {
                    mDialog.dismiss();
                } else if (mContext != null) {
                    RequestDialogUtils.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mRefresh != null) {
                    mRefresh.setRefreshing(false);
                } else if (mDialog != null) {
                    mDialog.dismiss();
                } else if (mContext != null) {
                    RequestDialogUtils.dismiss();
                }
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(ConfigurationImpl.get().getAppContext(),
                            R.string.net_request_time_out, Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(ConfigurationImpl.get().getAppContext(),
                            R.string.net_error_tips, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConfigurationImpl.get().getAppContext(),
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, e.getMessage(), e);
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

    /**
     * 有加载框的请求-抛出错误
     *
     * @param Callback
     * @param errorBack
     * @return
     */
    public Subscriber<T> get(final HttpSuccessListener<T> Callback, final HttpErrorListener errorBack) {
        return new Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                if (!NetWorkStateUtils.isNetworkConnected()) {
                    Toast.makeText(ConfigurationImpl.get().getAppContext(),
                            R.string.net_error_tips, Toast.LENGTH_SHORT).show();
                    onCompleted();
                }
                if (mRefresh != null) {
                    mRefresh.setRefreshing(true);
                } else if (mDialog != null) {
                    mDialog.show();
                } else if (mContext != null) {
                    RequestDialogUtils.show(mContext);
                }
            }

            @Override
            public void onCompleted() {
                if (mRefresh != null) {
                    mRefresh.setRefreshing(false);
                } else if (mDialog != null) {
                    mDialog.dismiss();
                } else if (mContext != null) {
                    RequestDialogUtils.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (mRefresh != null) {
                    mRefresh.setRefreshing(false);
                } else if (mDialog != null) {
                    mDialog.dismiss();
                } else if (mContext != null) {
                    RequestDialogUtils.dismiss();
                }
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
