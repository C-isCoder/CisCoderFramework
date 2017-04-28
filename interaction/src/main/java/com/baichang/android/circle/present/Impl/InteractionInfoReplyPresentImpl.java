package com.baichang.android.circle.present.Impl;

import android.support.v7.widget.RecyclerView;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.circle.model.InteractInteraction;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import com.baichang.android.circle.adapter.InteractionInfoReplyAdapter;
import com.baichang.android.circle.adapter.InteractionInfoReplyAdapter.OnItemContentClickListener;
import com.baichang.android.circle.entity.InteractionReplyData;
import com.baichang.android.circle.present.InteractionInfoReportPresent;
import com.baichang.android.circle.view.InteractionOtherReportView;
import java.util.List;

/**
 * Created by iCong on 2017/3/28.
 */

public class InteractionInfoReplyPresentImpl implements InteractionInfoReportPresent,
    BaseListener<List<InteractionReplyData>>, OnItemContentClickListener {

  private InteractionOtherReportView mView;
  private InteractInteraction mInteraction;
  private InteractionInfoReplyAdapter mAdapter;
  private String mUserId;

  public InteractionInfoReplyPresentImpl(String userId, InteractionOtherReportView view) {
    mView = view;
    mUserId = userId;
    mInteraction = new InteractInteractionImpl();
    mAdapter = new InteractionInfoReplyAdapter(this);
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {
    mView.showProgressBar();
    mInteraction.getReplay(1, mUserId, this);
  }

  @Override
  public void attachView(RecyclerView recyclerView) {
    recyclerView.setAdapter(mAdapter);
  }

  @Override
  public void refresh() {
    mView.showProgressBar();
    mInteraction.getReplay(1, mUserId, this);
  }

  @Override
  public void success(List<InteractionReplyData> list) {
    mView.hideProgressBar();
    mAdapter.setData(list);
  }

  @Override
  public void error(String error) {
    mView.hideProgressBar();
    mView.showMsg(error);
  }

  @Override
  public void onContentClick(int id) {
    mView.gotoDetail(id);
  }
}
