package com.baichang.android.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;
import com.baichang.android.config.ConfigurationImpl;
import com.orhanobut.logger.Logger;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by iCong.
 *
 * Date:2017年7月6日
 */

public class HttpObserver<T> extends DisposableObserver<T> {

  private static final String TAG = "Request";
  private static Context sContext = null;
  private static ProgressDialog sDialog = null;
  private static SwipeRefreshLayout sRefresh = null;

  private HttpSuccessListener<T> mSuccessListener;
  private HttpErrorListener mErrorListener;

  public HttpObserver() {

  }

  public HttpObserver(HttpSuccessListener<T> successListener) {
    if (successListener == null) {
      throw new NullPointerException("HttpSuccessListener not null");
    }
    mSuccessListener = successListener;
  }

  public HttpObserver(HttpSuccessListener<T> successListener, HttpErrorListener errorListener) {
    if (successListener == null) {
      throw new NullPointerException("HttpSuccessListener not null");
    }
    if (errorListener == null) {
      throw new NullPointerException("HttpErrorListener not null");
    }
    mSuccessListener = successListener;
    mErrorListener = errorListener;
  }

  @Override public void onError(Throwable e) {
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
        Toast.makeText(ConfigurationImpl.get().getAppContext(), R.string.net_request_time_out,
            Toast.LENGTH_SHORT).show();
      } else if (e instanceof ConnectException) {
        Toast.makeText(ConfigurationImpl.get().getAppContext(), R.string.net_error_tips,
            Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(ConfigurationImpl.get().getAppContext(), e.getMessage(), Toast.LENGTH_SHORT)
            .show();
        Logger.e(e, "Exception-Info", e.getMessage());
      }
    }
  }

  @Override public void onComplete() {
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

  @Override public void onNext(T t) {
    mSuccessListener.success(t);
  }

  @SuppressWarnings("unchecked")
  public static <T> ObservableTransformer<T, T> schedulers(Context context) {
    WeakReference<Context> wk = new WeakReference<Context>(context);
    sContext = wk.get();
    return new ObservableTransformer<T, T>() {
      @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.newThread())
            .unsubscribeOn(Schedulers.newThread())
            .doOnSubscribe(new Consumer<Disposable>() {
              @Override public void accept(Disposable disposable) throws Exception {
                if (!NetWorkStateUtils.isNetworkConnected()) {
                  Toast.makeText(ConfigurationImpl.get().getAppContext(), R.string.net_error_tips,
                      Toast.LENGTH_SHORT).show();
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
  public static <T> ObservableTransformer<T, T> schedulers(Context context, final int colorRes) {
    WeakReference<Context> wk = new WeakReference<Context>(context);
    sContext = wk.get();
    return new ObservableTransformer<T, T>() {
      @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.newThread())
            .unsubscribeOn(Schedulers.newThread())
            .doOnSubscribe(new Consumer<Disposable>() {
              @Override public void accept(Disposable disposable) throws Exception {
                if (!NetWorkStateUtils.isNetworkConnected()) {
                  Toast.makeText(ConfigurationImpl.get().getAppContext(), R.string.net_error_tips,
                      Toast.LENGTH_SHORT).show();
                } else if (sContext != null) {
                  RequestDialogUtils.show(sContext, colorRes);
                }
              }
            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread());
      }
    };
  }

  @SuppressWarnings("unchecked")
  public static <T> ObservableTransformer<T, T> schedulers(ProgressDialog dialog) {
    WeakReference<ProgressDialog> wk = new WeakReference<ProgressDialog>(dialog);
    sDialog = wk.get();
    return new ObservableTransformer<T, T>() {
      @Override public ObservableSource<T> apply(Observable upstream) {
        return upstream.subscribeOn(Schedulers.newThread())
            .unsubscribeOn(Schedulers.newThread())
            .doOnSubscribe(new Consumer<Disposable>() {
              @Override public void accept(Disposable disposable) throws Exception {
                if (!NetWorkStateUtils.isNetworkConnected()) {
                  Toast.makeText(ConfigurationImpl.get().getAppContext(), R.string.net_error_tips,
                      Toast.LENGTH_SHORT).show();
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
  public static <T> ObservableTransformer<T, T> schedulers(SwipeRefreshLayout refresh) {
    WeakReference<SwipeRefreshLayout> wk = new WeakReference<SwipeRefreshLayout>(refresh);
    sRefresh = wk.get();
    return new ObservableTransformer<T, T>() {
      @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.newThread())
            .unsubscribeOn(Schedulers.newThread())
            .doOnSubscribe(new Consumer<Disposable>() {
              @Override public void accept(Disposable disposable) throws Exception {
                if (!NetWorkStateUtils.isNetworkConnected()) {
                  Toast.makeText(ConfigurationImpl.get().getAppContext(), R.string.net_error_tips,
                      Toast.LENGTH_SHORT).show();
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

  @SuppressWarnings("unchecked") public static <T> ObservableTransformer<T, T> schedulers() {
    return new ObservableTransformer<T, T>() {
      @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
      }
    };
  }

  @SuppressWarnings("unchecked") public static <T> ObservableTransformer<T, T> downSchedulers() {
    return new ObservableTransformer<T, T>() {
      public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread());
        //((Observable) observable).subscribeOn(Schedulers.newThread())
        //.observeOn(Schedulers.io());
      }
    };
  }

  @Deprecated public HttpObserver(Context context) {
    if (context == null) {
      throw new NullPointerException("Context not null");
    }
    WeakReference<Context> wc = new WeakReference<Context>(context);
    sContext = wc.get();
  }

  @Deprecated public HttpObserver(ProgressDialog dialog) {
    if (dialog == null) {
      throw new NullPointerException("Progress not null");
    }
    sDialog = dialog;
  }

  @Deprecated public HttpObserver(SwipeRefreshLayout refreshLayout) {
    if (refreshLayout == null) {
      throw new NullPointerException("SwipeRefreshLayout not null");
    }
    sRefresh = refreshLayout;
  }
}
