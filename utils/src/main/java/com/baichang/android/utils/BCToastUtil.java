package com.baichang.android.utils;

import android.content.Context;
import android.widget.Toast;


public class BCToastUtil {
    /**
     * 普通Toast
     *
     * @param context
     * @param obj
     */
    public static void showMessage(Context context, Object obj) {
        if (context == null || obj == null) return;
        if (obj instanceof String) {
            Toast.makeText(context, (CharSequence) obj, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString((Integer) obj), Toast.LENGTH_SHORT).show();
        }
    }

}
