package com.baichang.android.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.baichang.android.common.ConfigurationImpl;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BCDialogUtil {

    private static ProgressDialog sProgressDialog = null;
    private static AlertDialog sAlertDialog = null;
    private static final int DEFAULT_COLOR = ConfigurationImpl.get().getAppBarColor();

    /**
     * 显示加载 ProgressDialog
     *
     * @param context 上下文
     * @param message
     */
    public static void showProgressDialog(Context context, Object message) {
        if (context == null) return;
        WeakReference<Context> wf = new WeakReference<Context>(context);
        String content = "正在加载...";
        if (message != null && message instanceof Integer) {
            content = wf.get().getString((Integer) message);
        } else if (message != null && message instanceof String
                && !BCStringUtil.isEmpty((String) message)) {
            content = (String) message;
        }

        if (sProgressDialog == null) {
            sProgressDialog = new ProgressDialog(wf.get(),
                    ProgressDialog.THEME_HOLO_LIGHT);
            sProgressDialog.setCanceledOnTouchOutside(false);
            sProgressDialog.setCancelable(false);
            sProgressDialog.setMessage(content);
        }
        sProgressDialog.setMessage(content);

        try {
            sProgressDialog.show();
        } catch (Exception ignored) {
        }

    }

    /**
     * dismiss ProgressDialog
     */
    public static void dismissProgressDialog() {
        try {
            if (sProgressDialog != null) {
                sProgressDialog.dismiss();
                sProgressDialog = null;
            }
        } catch (Exception ignored) {
        }

    }


    /**
     * 返回AlertDialogBuilder
     *
     * @param context 上下文
     * @return AlertDialog
     */

    private static AlertDialog.Builder getAlertDialog(Context context) {
        WeakReference<Context> wf = new WeakReference<Context>(context);
        if (Build.VERSION.SDK_INT >= 11) {
            return new AlertDialog.Builder(wf.get(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            return new AlertDialog.Builder(wf.get());
        }

    }

    /**
     * 获取带输入框的Dialog
     *
     * @param context  上下文
     * @param colorRes 颜色
     * @param view     输入框View
     * @param title    标题
     * @param confirm  确认监听
     * @param cancel   取消监听
     */
    public static void getInputDialog(Context context, int colorRes, View view, String title,
                                      DialogInterface.OnClickListener confirm,
                                      DialogInterface.OnClickListener cancel) {
        AlertDialog builder = getAlertDialog(context)
                .setView(view)
                .setTitle(title)
                .setPositiveButton("确定", confirm)
                .setNegativeButton("取消", cancel)
                .create();
        builder.show();
        setDialogTitleColor(builder, colorRes);
    }

    /**
     * 获取带输入框的Dialog
     *
     * @param context 上下文
     * @param view    输入框View
     * @param title   标题
     * @param confirm 确认监听
     * @param cancel  取消监听
     */
    public static void getInputDialog(Context context, View view, String title,
                                      DialogInterface.OnClickListener confirm,
                                      DialogInterface.OnClickListener cancel) {
        AlertDialog builder = getAlertDialog(context)
                .setView(view)
                .setTitle(title)
                .setPositiveButton("确定", confirm)
                .setNegativeButton("取消", cancel)
                .create();
        builder.show();
        setDialogTitleColor(builder, DEFAULT_COLOR);
    }

    /**
     * 选择日期的Dialog
     *
     * @param context         上下文
     * @param colorRes        颜色
     * @param date            当前日期
     * @param dateSetListener 回调
     */
    public static void choiceTime(Context context, int colorRes, String date,
                                  DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy-MM-dd", date));
        }
        DatePickerDialog dialog = new DatePickerDialog(
                context, DatePickerDialog.THEME_HOLO_LIGHT,
                dateSetListener,
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.show();
        setDialogTitleColor(dialog, colorRes);
        setPickerDividerColor(dialog.getDatePicker(), colorRes);
    }

    /**
     * 选择日期的Dialog
     *
     * @param context         上下文
     * @param date            当前日期
     * @param dateSetListener 回调
     */
    public static void choiceTime(Context context, String date,
                                  DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy-MM-dd", date));
        }
        DatePickerDialog dialog = new DatePickerDialog(
                context, DatePickerDialog.THEME_HOLO_LIGHT,
                dateSetListener,
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.show();
        setDialogTitleColor(dialog, DEFAULT_COLOR);
        setPickerDividerColor(dialog.getDatePicker(), DEFAULT_COLOR);
    }

    /**
     * 日期选择 自己设置 确认 取消的监听
     *
     * @param context  上下文
     * @param colorRes 颜色
     * @param date     当前日期
     * @param confirm  确认
     * @param cancel   取消
     */
    public static void choiceTime(Context context, int colorRes, String date,
                                  DialogInterface.OnClickListener confirm,
                                  DialogInterface.OnClickListener cancel) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy-MM-dd", date));
        }
        DatePickerDialog dialog = new DatePickerDialog(
                context, DatePickerDialog.THEME_HOLO_LIGHT, null,
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        //手动设置按钮
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", confirm);
        //取消按钮，如果不需要直接不设置即可
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", cancel);
        dialog.show();

        setDialogTitleColor(dialog, colorRes);
        setPickerDividerColor(dialog.getDatePicker(), colorRes);
    }

    /**
     * 日期选择 自己设置 确认 取消的监听
     *
     * @param context 上下文
     * @param date    当前日期
     * @param confirm 确认
     * @param cancel  取消
     */
    public static void choiceTime(Context context, String date,
                                  DialogInterface.OnClickListener confirm,
                                  DialogInterface.OnClickListener cancel) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy-MM-dd", date));
        }
        DatePickerDialog dialog = new DatePickerDialog(
                context, DatePickerDialog.THEME_HOLO_LIGHT, null,
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        //手动设置按钮
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", confirm);
        //取消按钮，如果不需要直接不设置即可
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", cancel);
        dialog.show();

        setDialogTitleColor(dialog, DEFAULT_COLOR);
        setPickerDividerColor(dialog.getDatePicker(), DEFAULT_COLOR);
    }

    /**
     * 选择年份的Dialog
     *
     * @param context         上下文
     * @param colorRes        颜色
     * @param date            当前日期
     * @param dateSetListener 监听
     */
    public static void choiceYear(Context context, int colorRes, String date,
                                  DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy", date));
        }
        DatePickerDialog dialog = new DatePickerDialog(
                context, DatePickerDialog.THEME_HOLO_LIGHT,
                dateSetListener,
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.show();

        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            ((ViewGroup) (((ViewGroup) dp.getChildAt(0)).getChildAt(0)))
                    .getChildAt(1).setVisibility(View.GONE);
        }
        setDialogTitleColor(dialog, colorRes);
        setPickerDividerColor(dialog.getDatePicker(), colorRes);
    }

    /**
     * 选择年份的Dialog
     *
     * @param context         上下文
     * @param date            当前日期
     * @param dateSetListener 监听
     */
    public static void choiceYear(Context context, String date,
                                  DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(date)) {
            c.setTime(strToDate("yyyy", date));
        }
        DatePickerDialog dialog = new DatePickerDialog(
                context, DatePickerDialog.THEME_HOLO_LIGHT,
                dateSetListener,
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.show();

        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            ((ViewGroup) (((ViewGroup) dp.getChildAt(0)).getChildAt(0)))
                    .getChildAt(1).setVisibility(View.GONE);
        }
        setDialogTitleColor(dialog, DEFAULT_COLOR);
        setPickerDividerColor(dialog.getDatePicker(), DEFAULT_COLOR);
    }

    /**
     * 获取items Dialog
     *
     * @param colorRes 颜色
     * @param context  上下文
     * @param title    标题
     * @param items    items
     * @param listener 回调
     */
    public static void getDialogItem(Context context, int colorRes, String title, String[] items,
                                     final DialogInterface.OnClickListener listener) {
        AlertDialog builder = getAlertDialog(context)
                .setItems(items, listener)
                .setTitle(title)
                .create();
        builder.show();
        setDialogTitleColor(builder, colorRes);
    }

    /**
     * 获取items Dialog
     *
     * @param context  上下文
     * @param title    标题
     * @param items    items
     * @param listener 回调
     */
    public static void getDialogItem(Context context, String title, String[] items,
                                     final DialogInterface.OnClickListener listener) {
        AlertDialog builder = getAlertDialog(context)
                .setItems(items, listener)
                .setTitle(title)
                .create();
        builder.show();
        setDialogTitleColor(builder, DEFAULT_COLOR);
    }

    /**
     * 获取普通的 警告框
     *
     * @param context         上下文
     * @param colorRes        颜色
     * @param title           标题
     * @param content         内容
     * @param confirmListener 确定监听
     * @param cancelListener  取消监听
     * @return AlertDialog
     */
    public static void showDialog(Context context, int colorRes, String title, String content,
                                  DialogInterface.OnClickListener confirmListener,
                                  DialogInterface.OnClickListener cancelListener) {
        sAlertDialog = getAlertDialog(context)
                .setNegativeButton("取消", cancelListener)
                .setPositiveButton("确认", confirmListener)
                .setTitle(title)
                .setMessage(content)
                .create();
        sAlertDialog.show();
        setDialogTitleColor(sAlertDialog, colorRes);
    }

    /**
     * 获取普通的 警告框
     *
     * @param context         上下文
     * @param title           标题
     * @param content         内容
     * @param confirmListener 确定监听
     * @param cancelListener  取消监听
     * @return AlertDialog
     */
    public static void showDialog(Context context, String title, String content,
                                  DialogInterface.OnClickListener confirmListener,
                                  DialogInterface.OnClickListener cancelListener) {
        sAlertDialog = getAlertDialog(context)
                .setNegativeButton("取消", cancelListener)
                .setPositiveButton("确认", confirmListener)
                .setTitle(title)
                .setMessage(content)
                .create();
        sAlertDialog.show();
        setDialogTitleColor(sAlertDialog, DEFAULT_COLOR);
    }

    /**
     * 弹出警告框默认
     *
     * @param context  上下文
     * @param colorRes 颜色
     * @param title    标题
     * @param content  内容
     */
    public static void showDialog(Context context, int colorRes, String title, String content) {
        sAlertDialog = getAlertDialog(context)
                .setTitle(title)
                .setMessage(content)
                .create();
        sAlertDialog.show();
        setDialogTitleColor(sAlertDialog, colorRes);
    }

    /**
     * 弹出警告框默认
     *
     * @param context 上下文
     * @param title   标题
     * @param content 内容
     */
    public static void showDialog(Context context, String title, String content) {
        sAlertDialog = getAlertDialog(context)
                .setTitle(title)
                .setMessage(content)
                .create();
        sAlertDialog.show();
        setDialogTitleColor(sAlertDialog, DEFAULT_COLOR);
    }

    /************************************************************************************************************************/
    /**
     * 设置选择器线的颜色
     *
     * @param datePicker DatePicker
     * @param color      线的颜色
     */
    private static void setPickerDividerColor(DatePicker datePicker, int color) {

        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(datePicker.getContext().getResources().getColor(color)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 日期转换
     *
     * @param style 样式
     * @param date  日期
     * @return Date
     */
    private static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 获取ViewGroup 的选择器
     *
     * @param group ViewGroup
     * @return DatePicker
     */
    private static DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    /**
     * 设置Dialog的颜色
     *
     * @param dialog Dialog
     * @param color  颜色
     */
    private static void setDialogTitleColor(Dialog dialog, int color) {
        try {
            Context context = dialog.getContext();
            int dividerId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = dialog.findViewById(dividerId);
            divider.setBackgroundColor(context.getResources().getColor(color));

            int alertTitleId = context.getResources().getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) dialog.findViewById(alertTitleId);
            alertTitle.setTextColor(context.getResources().getColor(color));
        } catch (Exception ignored) {

        }

    }
}
