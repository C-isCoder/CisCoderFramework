package com.baichang.android.circle;

import com.baichang.android.common.IBaseInteraction;
import com.baichang.android.circle.entity.InteractionCommentData;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.entity.InteractionOtherReportData;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public interface InteractInteraction extends IBaseInteraction {

  void getInteractionList(int nowPage, BaseListener<List<InteractionListData>> listener);

  void getInteractionDetail(int id, BaseListener<List<InteractionCommentData>> listener);

  void publish(String title, String content, String modelId, List<String> paths, BaseListener<Boolean> listener);

  void getMeInteraction(int nowPage, BaseListener<List<InteractionOtherReportData>> listener);
}
