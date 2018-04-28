package com.baichang.android.circle.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by iCong on 2017/4/7.
 */
public class AnimatorUtil {

  public static AnimatorSet scale(View view) {
    ValueAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.2f, 1f);
    ValueAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.2f, 1f);
    AnimatorSet set = new AnimatorSet();
    set.setDuration(500).play(animatorX).with(animatorY);
    set.start();
    return set;
  }
}
