package com.baichang.android.circle.present;

import android.support.v4.view.ViewPager;
import com.baichang.android.common.IBasePresent;
import com.baichang.android.widget.circleImageView.CircleImageView;
import com.baichang.android.widget.magicIndicator.MagicIndicator;

/**
 * Created by iCong on 2017/3/28.
 */

public interface InteractionInfoPresent extends IBasePresent {

  void attachView(ViewPager viewPager, MagicIndicator indicator, CircleImageView imageView);

  void setIsOneself(boolean isOneself);
}
