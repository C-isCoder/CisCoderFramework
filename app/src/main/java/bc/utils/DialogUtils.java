package bc.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.jnbc.myapplication.R;

import bc.widget.dialog.LoadingDialog;
import com.baichang.android.library.utils.MLStrUtil;
import com.baichang.android.library.utils.ToolsUtil;

/**
 * Created by iscod.
 * Time:2016/9/23-10:57.
 */

public class DialogUtils {
    protected static ProgressDialog progressDialog = null;
    private static AlertDialog builder = null;
    private static LoadingDialog loadingDialog = null;

    /**
     * 显示加载 ProgressDialog
     *
     * @param context
     */
    public static void showProgressDialog(Context context) {
        if (context == null) return;
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.getInstance(context);
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        if (!((Activity) context).isFinishing()) {
            //show dialog
            try {
                loadingDialog.show();
            } catch (Exception e) {
                //Log.d("Dialog", "error: " + e.getMessage());
            }
        }
    }

    /**
     * 显示加载 ProgressDialog
     *
     * @param context
     * @param message
     */
    public static void showProgressDialog(Context context, Object message) {
        if (context == null) return;

        String content = ToolsUtil
                .getResourceString(R.string.loading_message);
        if (message != null && message instanceof Integer) {
            content = ToolsUtil.getResourceString((Integer) message);
        } else if (message != null && message instanceof String && !MLStrUtil.isEmpty((String) message)) {
            content = (String) message;
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setCanceledOnTouchOutside(false);
            //progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(content);
        }
        progressDialog.setMessage(content);

        try {
            progressDialog.show();
        } catch (Exception e) {
            Log.d("Dialog", "error: " + e.getMessage());
        }

    }

    /**
     * /**
     * dismiss ProgressDialog
     */
    public static void dismissProgressDialog() {
        try {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog.repair();
                loadingDialog = null;
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }

        } catch (Exception e) {
            Log.d("Dialog", "dismissError: " + e.getMessage());
        }

    }

}
