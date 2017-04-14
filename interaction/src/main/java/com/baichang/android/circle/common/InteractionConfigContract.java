package com.baichang.android.circle.common;

import com.baichang.android.circle.common.InteractionConfig.InteractionListener;

/**
 * Created by iCong on 2017/4/1.
 */

public interface InteractionConfigContract {

  int getTextFontColor();

  int getTopBarColor();

  int getButtonDrawableRes();

  int getShareDrawableRes();

  int getCommentDrawableRes();

  int getPraiseDrawableRes();

  int getCollectDrawableRes();

  int getBusinessDrawableRes();

  int getBackDrawableRes();

  InteractionListener getListener();

  String getTitleText();

  boolean isNeedBusinessStore();

  void share(String title, String summary, String url);

  void businessStore();

}
