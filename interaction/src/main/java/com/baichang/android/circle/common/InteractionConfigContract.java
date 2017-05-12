package com.baichang.android.circle.common;

import android.app.Activity;
import android.content.Context;
import com.baichang.android.circle.common.InteractionConfig.InteractionListener;
import com.baichang.android.circle.entity.InteractionUserData;

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

  boolean isNeedSetTitleHeight();

  void share(Activity activity, String title, String summary, String url);

  void businessStore(String id);

  InteractionUserData getUser();
}
