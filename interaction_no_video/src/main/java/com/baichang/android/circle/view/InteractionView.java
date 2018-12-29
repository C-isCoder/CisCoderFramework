package com.baichang.android.circle.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.baichang.android.common.IBaseView;

/**
 * Created by iCong on 2017/3/20.
 */

public interface InteractionView extends IBaseView {

  Context getContext();

  FragmentManager getFragmentManager();
}
