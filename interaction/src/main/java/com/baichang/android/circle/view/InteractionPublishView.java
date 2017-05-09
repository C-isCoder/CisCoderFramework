package com.baichang.android.circle.view;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import com.baichang.android.common.IBaseView;

/**
 * Created by iCong on 2017/3/24.
 */

public interface InteractionPublishView extends IBaseView {

  int REQUEST_CODE_BOXING = 1024;
  int REQUEST_CODE_TAKE = 101;
  int REQUEST_CODE_CROP = 102;

  void close(String typeId);

  FragmentManager getManager();

  void selectImages(int max);

  void setTypeName(String name);

  Activity getActivity();
}
