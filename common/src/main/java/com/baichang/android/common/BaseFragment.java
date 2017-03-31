package com.baichang.android.common;

import android.content.Context;
import android.support.v4.app.Fragment;

import android.widget.Toast;


public class BaseFragment extends Fragment {
    public static final int RESULT_OK = -1;

    /*==========Toast 提示===========*/
    public void showMessage(Object content) {
        if (content instanceof String) {
            Toast.makeText(getActivity(), (CharSequence) content, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), this.getString((Integer) content), Toast.LENGTH_SHORT).show();
        }
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
