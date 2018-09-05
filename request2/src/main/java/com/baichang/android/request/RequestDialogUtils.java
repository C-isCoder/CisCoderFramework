package com.baichang.android.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import com.baichang.android.config.ConfigurationImpl;
import com.baichang.android.widget.requestDialog.ProgressWheel;
import java.lang.ref.WeakReference;

/**
 * Created by iCong. Time:2016/12/5-11:36.
 */

public class RequestDialogUtils {

    private static ProgressDialog sDialog = null;
    private static int sColor = ConfigurationImpl.get().getAppBarColor();

    public static void show(Context context) {
        WeakReference<Context> wc = new WeakReference<Context>(context);
        if (sDialog == null) {
            sDialog = new ProgressDialog(wc.get(), R.style.request_dialog_style);
            try {
                sDialog.show();
            } catch (Exception ignored) {
            }
            ProgressWheel progress = (ProgressWheel) LayoutInflater.from(wc.get())
                .inflate(R.layout.request_dialog_layout, null);
            progress.setBarColor(wc.get().getResources().getColor(sColor));
            sDialog.setContentView(progress);
            sDialog.setCanceledOnTouchOutside(false);
            sDialog.setCancelable(false);
        } else {
            try {
                sDialog.show();
            } catch (Exception ignored) {
            }
        }
    }

    public static void show(Context context, int colorRes) {
        WeakReference<Context> wc = new WeakReference<Context>(context);
        if (sDialog == null) {
            sDialog = new ProgressDialog(wc.get(), R.style.request_dialog_style);
            try {
                sDialog.show();
            } catch (Exception ignored) {
            }
            ProgressWheel progress = (ProgressWheel) LayoutInflater.from(wc.get())
                .inflate(R.layout.request_dialog_layout, null);
            progress.setBarColor(wc.get().getResources().getColor(colorRes));
            sDialog.setContentView(progress);
            sDialog.setCanceledOnTouchOutside(false);
            sDialog.setCancelable(false);
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
