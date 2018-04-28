package com.baichang.android.circle.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;

/**
 * Created by iCong on 2017/4/7.
 */

public class ColorUtil {

  public static ColorStateList createdPressColorList(Context context, int pressColorRes, int normalColorRes) {
    int[] colors = new int[]{ContextCompat.getColor(context, pressColorRes),
        ContextCompat.getColor(context, normalColorRes)};
    int[][] states = new int[2][];
    states[0] = new int[]{android.R.attr.state_pressed};
    states[1] = new int[]{android.R.attr.state_enabled};
    return new ColorStateList(states, colors);
  }

  public static ColorStateList createdSelectColorList(Context context, int selectColorRes, int normalColorRes) {
    int[] colors = new int[]{ContextCompat.getColor(context, selectColorRes),
        ContextCompat.getColor(context, normalColorRes)};
    int[][] states = new int[2][];
    states[0] = new int[]{android.R.attr.state_selected};
    states[1] = new int[]{android.R.attr.state_enabled};
    return new ColorStateList(states, colors);
  }
}
