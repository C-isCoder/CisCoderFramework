package com.baichang.android.circle.present;

import android.support.v7.widget.RecyclerView;
import com.baichang.android.common.IBasePresent;

/**
 * Created by iCong on 2017/3/20.
 */

public interface InteractionContentPresent<View> extends IBasePresent {

  void refresh();

  void attachRecyclerView(RecyclerView recyclerView);

  void setType(int type);
}
