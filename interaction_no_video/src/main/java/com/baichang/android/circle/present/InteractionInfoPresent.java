package com.baichang.android.circle.present;

import android.support.v4.view.ViewPager;
import com.baichang.android.common.IBasePresent;
import com.baichang.android.widget.circleImageView.CircleImageView;
import com.baichang.android.widget.magicIndicator.MagicIndicator;

/**
 * Created by iCong on 2017/3/28.
 */

public interface InteractionInfoPresent extends IBasePresent {

  int DYNAMIC = 0;
  int REPORT = 1;
  int COLLECT = 2;
  int NORMAL = -1;

  void attachView(ViewPager viewPager, MagicIndicator indicator);

  void setIsOneself(boolean isOneself, String userId);

  void jumpBusiness();
}
