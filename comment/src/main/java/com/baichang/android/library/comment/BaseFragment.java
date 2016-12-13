package com.baichang.android.library.comment;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;


import com.baichang.android.utils.BCAppManager;
import com.baichang.android.utils.BCToastUtil;

import java.util.Locale;


public class BaseFragment extends Fragment {
    public static final int RESULT_OK = -1;

    /*==========Toast 提示===========*/
    public void showMessage(Object obj) {
        BCToastUtil.showMessage(getActivity(), obj);
    }

    /*==================跳转页面=======================*/
    protected void startAct(Fragment act, Class cls) {
        startAct(act, cls, null, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj) {
        startAct(act, cls, obj, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj, int requestCode) {
        BCAppManager.startActivityForFrg(act, cls, obj, requestCode);
    }

    public Context getContext() {
        return getActivity();
    }

    protected Fragment getFragment() {
        return this;
    }

}
