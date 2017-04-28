package com.baichang.android.circle.model;

import android.app.Application;
import com.baichang.android.circle.entity.InteractionCommentList;
import com.baichang.android.circle.entity.InteractionCommentReplyList;
import com.baichang.android.circle.entity.InteractionDetailData;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.entity.InteractionNumberData;
import com.baichang.android.circle.entity.InteractionReplyData;
import com.baichang.android.circle.entity.InteractionShareData;
import com.baichang.android.circle.entity.InteractionTypeData;
import com.baichang.android.circle.entity.InteractionUserInfo;
import com.baichang.android.common.IBaseInteraction;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public interface InteractInteraction extends IBaseInteraction {

  void getInteractionList(int typeId, int nowPage, BaseListener<List<InteractionListData>> listener);

  void getInteractionDetail(int id, BaseListener<InteractionDetailData> listener);

  void publishImage(Application application, String title, String content, String modelId, List<String> paths,
      BaseListener<Boolean> listener);

  void publishNoImage(String title, String content, String modelId, BaseListener<Boolean> listener);

  void getInteractionTypeList(BaseListener<List<InteractionTypeData>> listener);

  void praise(int id, BaseListener<Boolean> listener);

  void collect(int id, BaseListener<Boolean> listener);

  void getDynamics(String userId, int nowPage, BaseListener<List<InteractionListData>> listener);

  void getCollect(int nowPage, BaseListener<List<InteractionListData>> listener);

  void getReplay(int nowPage, String userId, BaseListener<List<InteractionReplyData>> listener);

  void delete(int id, BaseListener<Boolean> listener);

  void getNumbers(String userId, BaseListener<InteractionNumberData> listener);

  void report(int id, BaseListener<Boolean> listener);

  void comment(int id, InteractionCommentList commentData, BaseListener<Boolean> listener);

  void reply(InteractionCommentReplyList replyData, BaseListener<Boolean> listener);

  void getUserInfo(String userId, BaseListener<InteractionUserInfo> listener);

  void getShareLink(String id, BaseListener<String> listener);
}
