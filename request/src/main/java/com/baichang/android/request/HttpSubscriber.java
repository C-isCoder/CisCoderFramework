package com.baichang.android.request;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;
import com.baichang.android.config.ConfigurationImpl;
import com.orhanobut.logger.Logger;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by iscod. Time:2016/9/21-14:03.
 */

public class HttpSubscriber<T> extends Subscriber<T> {

  private static final String TAG = "Request";
  private static Context sContext = null;
  private static ProgressDialog sDialog = null;
  private static SwipeRefreshLayout sRefresh = null;

  private HttpSuccessListener<T> mSuccessListener;
  private HttpErrorListener mErrorListener;

  public HttpSubscriber() {

  }

  public HttpSubscriber(HttpSuccessListener<T> successListener) {
    if (successListener == null) {
      throw new NullPointerException("HttpSuccessListener not null");
    }
    mSuccessListener = successListener;
  }

  public HttpSubscriber(HttpSuccessListener<T> successListener, HttpErrorListener errorListener) {
    if (successListener == null) {
      throw new NullPointerException("HttpSuccessListener not null");
    }
    if (errorListener == null) {
      throw new NullPointerException("HttpErrorListener not null");
    }
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
        Logger.e(e, "Exception-Info", e.getMessage());
      }
    }
  }

  @Override
  public void onNext(T t) {
    mSuccessListener.success(t);
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
  public static <T> Observable.Transformer<T, T> applySchedulers(Context context,
      final int colorRes) {
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
        return ((Observable) observable).subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io());
      }
    };
  }

  @Deprecated
  public HttpSubscriber(Context context) {
    if (context == null) {
      throw new NullPointerException("Context not null");
    }
    WeakReference<Context> wc = new WeakReference<Context>(context);
    sContext = wc.get();
  }

  @Deprecated
  public HttpSubscriber(ProgressDialog dialog) {
    if (dialog == null) {
      throw new NullPointerException("Progress not null");
    }
    sDialog = dialog;
  }

  @Deprecated
  public HttpSubscriber(SwipeRefreshLayout refreshLayout) {
    if (refreshLayout == null) {
      throw new NullPointerException("SwipeRefreshLayout not null");
    }
    sRefresh = refreshLayout;
  }

  /**
   * @param Callback
   * @return
   */
  @Deprecated
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
        if (sRefresh != null) {
          sRefresh.setRefreshing(true);
        }
        if (sDialog != null) {
          sDialog.show();
        }
        if (sContext != null) {
          RequestDialogUtils.show(sContext);
        }
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
        if (e instanceof SocketTimeoutException) {
          Toast.makeText(ConfigurationImpl.get().getAppContext(),
              R.string.net_request_time_out, Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
          Toast.makeText(ConfigurationImpl.get().getAppContext(),
              R.string.net_error_tips, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(ConfigurationImpl.get().getAppContext(),
              e.getMessage(), Toast.LENGTH_SHORT).show();
          Logger.e(e, "Exception-Info", e.getMessage());
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
   * @return 已作废
   */
  @Deprecated
  public Subscriber<T> get(final HttpSuccessListener<T> Callback,
      final HttpErrorListener errorBack) {
    return new Subscriber<T>() {
      @Override
      public void onStart() {
        super.onStart();
        if (!NetWorkStateUtils.isNetworkConnected()) {
          Toast.makeText(ConfigurationImpl.get().getAppContext(),
              R.string.net_error_tips, Toast.LENGTH_SHORT).show();
          onCompleted();
        }
        if (sRefresh != null) {
          sRefresh.setRefreshing(true);
        }
        if (sDialog != null) {
          sDialog.show();
        }
        if (sContext != null) {
          RequestDialogUtils.show(sContext);
        }
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
