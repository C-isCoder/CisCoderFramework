package com.baichang.android.library.request.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.baichang.android.library.comment.ConfigurationImpl;

/**
 * Created by iscod.
 * Time:2016/12/5-11:31.
 */

public class NetWorkStateUtils {
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) ConfigurationImpl.get().getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
