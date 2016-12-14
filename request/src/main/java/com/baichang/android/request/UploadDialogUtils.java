package com.baichang.android.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;


/**
 * Created by iscod.
 * Time:2016/12/12-14:12.
 */

public class UploadDialogUtils {
    private static ProgressDialog sDialog;
    private static final String TIPS = "正在上传...";

    public static void show(Context context) {
        WeakReference<Context> wc = new WeakReference<Context>(context);
        if (sDialog == null) {
            sDialog = new ProgressDialog(wc.get(), ProgressDialog.THEME_HOLO_LIGHT);
            sDialog.setCanceledOnTouchOutside(false);
            sDialog.setCancelable(false);
            sDialog.setMessage(TIPS);
            try {
                sDialog.show();
            } catch (Exception e) {
                Log.e("CID", "Dialog show error", e);
            }
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
