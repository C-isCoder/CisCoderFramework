package com.baichang.android.circle.present;

import android.support.v7.widget.RecyclerView;
import com.baichang.android.common.IBasePresent;

/**
 * Created by iCong on 2017/3/28.
 */

public interface InteractionInfoReportPresent extends IBasePresent {

  void attachView(RecyclerView recyclerView);

  void refresh();

  void setIsOneSelf(boolean isOneSelf);
}
