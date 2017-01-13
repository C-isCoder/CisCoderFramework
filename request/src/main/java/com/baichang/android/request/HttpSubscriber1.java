package com.baichang.android.request;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baichang.android.common.ConfigurationImpl;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by iscod.
 * Time:2016/9/21-14:03.
 */

public class HttpSubscriber1<T> extends Subscriber<T> {
    private static final String TAG = "Request";
    private static Context sContext = null;
    private static ProgressDialog sDialog = null;
    private static SwipeRefreshLayout sRefresh = null;

    private HttpSuccessListener<T> mSuccessListener;
    private HttpErrorListener mErrorListener;

    //error
    private static final String SERVICE_ERROR = "请求服务器异常";
    private static final String DATA_ERROR = "请求数据异常";

    public HttpSubscriber1() {

    }

    public HttpSubscriber1(HttpSuccessListener<T> successListener) {
        if (successListener == null)
            throw new NullPointerException("HttpSuccessListener not null");
        mSuccessListener = successListener;
    }

    public HttpSubscriber1(HttpSuccessListener<T> successListener, HttpErrorListener errorListener) {
        if (successListener == null)
            throw new NullPointerException("HttpSuccessListener not null");
        if (errorListener == null)
            throw new NullPointerException("HttpErrorListener not null");
        mSuccessListener = successListener;
        mErrorListener = errorListener;
    }

    @Override
    public void onCompleted() {
        if (sRefresh != null) {
            sRefresh.setRefreshing(false);
        }
        if (sDialog != null) {
            sDialog.dismiss();
        }
        if (sContext != null) {
            RequestDialogUtils.dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (sRefresh != null) {
            sRefresh.setRefreshing(false);
        }
        if (sDialog != null) {
            sDialog.dismiss();
        }
        if (sContext != null) {
            RequestDialogUtils.dismiss();
        }
        if (mErrorListener != null) {
            mErrorListener.error(e);
        } else {
            if (e instanceof SocketTimeoutException) {
                Toast.makeText(ConfigurationImpl.get().getAppContext(),
                        R.string.net_request_time_out, Toast.LENGTH_SHORT).show();
            } else if (e instanceof ConnectException) {
                Toast.makeText(ConfigurationImpl.get().getAppContext(),
                        R.string.net_error_tips, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ConfigurationImpl.get().getAppContext(),
                        e.getMessage(), Toast.LENGTH_SHORT).show();
                Logger.e(e, e.getMessage());
            }
        }
    }

    @Override
    public void onNext(T t) {
        HttpResponse response = (HttpResponse) t;
        int service_state = response.getState();
        if (service_state != 1) {
            // 服务器异常
            throw new HttpException(response.getMsg());
        }
        // 接口状态
        int ret_state = response.getRes().getCode();
        if (ret_state == 40000) {
            if (response.getRes().getData() == null) {
                throw new HttpException(SERVICE_ERROR);
            }
            mSuccessListener.success(t);
        } else if (ret_state == 30000) {
            ConfigurationImpl.get().refreshToken();
            throw new HttpException(response.getRes().getMsg());
        } else {
            // 接口异常
            throw new HttpException(response.getRes().getMsg());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers(Context context) {
        WeakReference<Context> wk = new WeakReference<Context>(context);
        sContext = wk.get();
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (!NetWorkStateUtils.isNetworkConnected()) {
                                    Toast.makeText(ConfigurationImpl.get().getAppContext(),
                                            R.string.net_error_tips, Toast.LENGTH_SHORT).show();
                                } else if (sContext != null) {
                                    RequestDialogUtils.show(sContext);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers(ProgressDialog dialog) {
        WeakReference<ProgressDialog> wk = new WeakReference<ProgressDialog>(dialog);
        sDialog = wk.get();
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (!NetWorkStateUtils.isNetworkConnected()) {
                                    Toast.makeText(ConfigurationImpl.get().getAppContext(),
                                            R.string.net_error_tips, Toast.LENGTH_SHORT).show();
                                } else if (sDialog != null) {
                                    sDialog.show();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers(SwipeRefreshLayout refresh) {
        WeakReference<SwipeRefreshLayout> wk = new WeakReference<SwipeRefreshLayout>(refresh);
        sRefresh = wk.get();
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable)
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (!NetWorkStateUtils.isNetworkConnected()) {
                                    Toast.makeText(ConfigurationImpl.get().getAppContext(),
                                            R.string.net_error_tips, Toast.LENGTH_SHORT).show();
                                } else if (sRefresh != null) {
                                    sRefresh.setRefreshing(true);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> downSchedulers() {
        return new Observable.Transformer() {
            public Object call(Object observable) {
                return ((Observable) observable).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io());
            }
        };
    }
}
