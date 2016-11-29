package com.baichang.android.library;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.baichang.android.library.retrofit.HttpService;
import com.baichang.android.library.retrofit.RetrofitClient;
import com.baichang.android.library.utils.MLAppManager;
import com.baichang.android.library.utils.MLDialogUtils;
import com.baichang.android.library.utils.MLToastUtils;

import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BaseFragment extends Fragment {
    public static final int RESULT_OK = -1;

    // ==========HTTP Request请求==================

    /**
     * 请求 默认有加载圈
     *
     * @return
     */
    protected HttpService request() {
        MLDialogUtils.showProgressDialog(getActivity());
        return RetrofitClient.getInstance().create();
    }

    /**
     * 请求 没有加载圈
     *
     * @return
     */
    protected HttpService noLoadingRequest() {
        return RetrofitClient.getInstance().create();
    }

    final Observable.Transformer schedulersTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    @SuppressWarnings("unchecked")
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

    // ==========加载数据对话框ProgressDialog==================
    public void showProgressDialog() {
        MLDialogUtils.showProgressDialog(getActivity());
    }

    public void showProgressDialog(String message) {
        MLDialogUtils.showProgressDialog(getActivity(), message);
    }

    public void dismissProgressDialog() {
        MLDialogUtils.dismissProgressDialog();
    }

    // ==========Toast 提示=======================
    public void showMessage(Context context, Object obj) {
        MLToastUtils.showMessage(context, obj);
    }

    public void showMessage(Object obj) {
        MLToastUtils.showMessage(getActivity(), obj);
    }
    public void logError(Throwable e) {
        showMessage(e.getMessage());
        Log.d("Request", "请求异常：" + e.toString());
    }

    // ==========跳转页面=======================
    protected void startAct(Fragment act, Class cls) {
        startAct(act, cls, null, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj) {
        startAct(act, cls, obj, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj, int requestCode) {
        MLAppManager.startActivityForFrg(act, cls, obj, requestCode);
    }

    public Context getContext() {
        return getActivity();
    }

    protected Fragment getFragment() {
        return this;
    }

    protected void switchLanguage(String language) {
        // 本地语言设置
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        //保存设置语言的类型
//        PreferenceUtil.commitString("language", language);
    }

}
