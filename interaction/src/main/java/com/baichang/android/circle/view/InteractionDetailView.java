package com.baichang.android.circle.view;

import android.content.Context;
import com.baichang.android.common.IBaseView;

/**
 * Created by iCong on 2017/3/21.
 */

public interface InteractionDetailView extends IBaseView {

  Context getContext();

  void setReportHint(String tips);

  void showInputKeyBord();

  void hideInputKeyBord();

  void scrollToPosition(int position);

  void setCollectState(boolean isCollect);

  void setPraiseState(boolean isPraise);
}