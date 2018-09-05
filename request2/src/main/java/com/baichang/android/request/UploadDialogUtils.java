package com.baichang.android.request;

import android.app.ProgressDialog;
import android.content.Context;
import java.lang.ref.WeakReference;

/**
 * Created by iCong.
 * Time:2017年7月6日
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
            } catch (Exception ignored) {
            }
        } else {
            try {
                sDialog.show();
            } catch (Exception ignored) {
            }
        }
    }

    public static void dismiss() {
        if (sDialog != null) {
            try {
                sDialog.dismiss();
                sDialog = null;
            } catch (Exception ignored) {
            }
        }
    }
}
