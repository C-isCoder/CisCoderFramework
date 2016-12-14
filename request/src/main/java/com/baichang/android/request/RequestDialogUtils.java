package com.baichang.android.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;


import java.lang.ref.WeakReference;

/**
 * Created by iscod.
 * Time:2016/12/5-11:36.
 */

public class RequestDialogUtils {
    private static ProgressDialog sDialog = null;

    public static void show(Context context) {
        WeakReference<Context> wc = new WeakReference<Context>(context);
        if (sDialog == null) {
            sDialog = new ProgressDialog(wc.get(), R.style.request_dialog_style);
            try {
                sDialog.show();
            } catch (Exception e) {
                Log.e("CID", "Dialog show error", e);
            }
            sDialog.setContentView(R.layout.request_dialog_layout);
            sDialog.setCanceledOnTouchOutside(false);
            sDialog.setCancelable(false);
        } else {
            try {
                sDialog.show();
            } catch (Exception e) {
                Log.e("CID", "Dialog show error", e);
            }
        }

    }

    public static void dismiss() {
        if (sDialog != null) {
            try {
                sDialog.dismiss();
                sDialog = null;
            } catch (Exception e) {
                Log.e("CID", "Dialog dismiss error", e);
            }
        }
    }
}
