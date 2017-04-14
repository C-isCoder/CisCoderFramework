package com.baichang.android.circle.present;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.baichang.android.common.IBasePresent;
import com.baichang.android.circle.entity.InteractionListData;

/**
 * Created by iCong on 2017/3/21.
 */

public interface InteractionDetailPresent extends IBasePresent {

  void attachView(RecyclerView recyclerView, View header);

  void bindData(InteractionListData data);

  void refresh();

  void send(String content);

  void share();

  void collect();

  void praise();
}
