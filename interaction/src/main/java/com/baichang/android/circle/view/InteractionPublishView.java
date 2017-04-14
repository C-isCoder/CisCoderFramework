package com.baichang.android.circle.view;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import com.baichang.android.common.IBaseView;

/**
 * Created by iCong on 2017/3/24.
 */

public interface InteractionPublishView extends IBaseView {

  void close();

  FragmentManager getManager();

  void selectImages(int max);

  Activity getActivity();
}
