package com.baichang.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class BCDensityUtil {

  // 根据手机的分辨率将dp的单位转成px(像素)
  public static int dip2px(Context context, float dpValue) {
    final float scale = getDisplayMetrics(context).density;
    return (int) (dpValue * scale + 0.5f);
  }

  // 根据手机的分辨率将px(像素)的单位转成dp
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  // 将px值转换为sp值
  public static int px2sp(Context context, float pxValue) {
    final float fontScale = getDisplayMetrics(context).scaledDensity;
    return (int) (pxValue / fontScale + 0.5f);
  }

  // 将sp值转换为px值
  public static int sp2px(Context context, float spValue) {
    final float fontScale = getDisplayMetrics(context).scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }

  /**
   * TypedValue官方源码中的算法，任意单位转换为PX单位
   *
   * @param unit TypedValue.COMPLEX_UNIT_DIP
   * @param value 对应单位的值
   * @param metrics 密度
   * @return px值
   */
  public static float applyDimension(int unit, float value,
      DisplayMetrics metrics) {
    switch (unit) {
      case TypedValue.COMPLEX_UNIT_PX:
        return value;
      case TypedValue.COMPLEX_UNIT_DIP:
        return value * metrics.density;
      case TypedValue.COMPLEX_UNIT_SP:
        return value * metrics.scaledDensity;
      case TypedValue.COMPLEX_UNIT_PT:
        return value * metrics.xdpi * (1.0f / 72);
      case TypedValue.COMPLEX_UNIT_IN:
        return value * metrics.xdpi;
      case TypedValue.COMPLEX_UNIT_MM:
        return value * metrics.xdpi * (1.0f / 25.4f);
    }
    return 0;
  }

  // 屏幕宽度（像素）
  public static int getWindowWidth(Activity context) {
    DisplayMetrics metric = new DisplayMetrics();
    context.getWindowManager().getDefaultDisplay().getMetrics(metric);
    return metric.widthPixels;
  }

  // 屏幕高度（像素）
  public static int getWindowHeight(Activity activity) {
    DisplayMetrics metric = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
    return metric.heightPixels;
  }

  /**
   * 获取屏幕尺寸与密度.
   *
   * @param context the context
   * @return mDisplayMetrics
   */
  public static DisplayMetrics getDisplayMetrics(Context context) {
    Resources mResources;
    if (context == null) {
      mResources = Resources.getSystem();
    } else {
      mResources = context.getResources();
    }
    //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
    //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
    DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
    return mDisplayMetrics;
  }
}
