package com.baichang.android.utils;

import android.content.Context;
import android.widget.Toast;


public class BCToastUtil {
    /**
     * 普通Toast
     *
     * @param context 上下文
     * @param content 内容
     */
    public static void showMessage(Context context, Object content) {
        if (context == null || content == null) return;
        if (content instanceof String) {
            Toast.makeText(context, (CharSequence) content, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString((Integer) content), Toast.LENGTH_SHORT).show();
        }
    }

}
