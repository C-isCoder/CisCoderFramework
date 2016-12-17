package com.baichang.android.utils.bitmap;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by iscod.
 * Time:2016/12/15-18:04.
 */

public class BCDrawableUtil {

    /**
     * 获取按下的Drawable
     *
     * @param pressedDraw 按下的Drawable
     * @param normalDraw  正常的Drawable
     * @return 创建好的Drawable
     */
    public static StateListDrawable getPressedDrawable(Drawable normalDraw, Drawable pressedDraw) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDraw);
        stateListDrawable.addState(new int[]{}, normalDraw);
        return stateListDrawable;
    }

    /**
     * 获取按下的Drawable
     *
     * @param pressColor  按下的颜色
     * @param normalColor 正常的颜色
     * @param radius      圆角
     * @return 创建好的Drawable
     */
    public static StateListDrawable getPressedDrawable(int pressColor, int normalColor, float radius) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, getShapeDrawable(pressColor, radius));
        stateListDrawable.addState(new int[]{}, getShapeDrawable(normalColor, radius));
        return stateListDrawable;
    }

    /**
     * 获取选择的Drawable
     *
     * @param select 选中的Drawable
     * @param normal 正常的Drawable
     * @return 创建好的Drawable
     */
    public static StateListDrawable getSelectorDrawable(Drawable normal, Drawable select) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, select);
        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }

    /**
     * 获取选择的Drawable
     *
     * @param pressColor  选中的颜色
     * @param normalColor 正常的颜色
     * @param radius      圆角
     * @return 创建好的Drawable
     */
    public static StateListDrawable getSelectorDrawable(int pressColor, int normalColor, float radius) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, getShapeDrawable(pressColor, radius));
        stateListDrawable.addState(new int[]{}, getShapeDrawable(normalColor, radius));
        return stateListDrawable;
    }

    /**
     * 设置shape
     *
     * @param radius      圆角
     * @param fillColor   填充颜色
     * @param width       边框宽度
     * @param strokeColor 边框颜色
     * @return shape
     */
    public static GradientDrawable getShapeDrawable(int fillColor, float radius, int width, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setStroke(width, strokeColor);
        return gradientDrawable;
    }

    /**
     * 设置shape
     *
     * @param radius    圆角
     * @param fillColor 填充颜色
     * @return shape
     */
    public static GradientDrawable getShapeDrawable(int fillColor, float radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(fillColor);
        return gradientDrawable;
    }
}
