package com.baichang.android.circle.present.Impl;

import android.support.v7.widget.RecyclerView;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import com.baichang.android.circle.adapter.InteractionInfoReportAdapter;
import com.baichang.android.circle.adapter.InteractionInfoReportAdapter.OnItemContentClickListener;
import com.baichang.android.circle.entity.InteractionOtherReportData;
import com.baichang.android.circle.InteractInteraction;
import com.baichang.android.circle.present.InteractionInfoReportPresent;
import com.baichang.android.circle.view.InteractionOtherReportView;
import java.util.List;

/**
 * Created by iCong on 2017/3/28.
 */

public class InteractionInfoReportPresentImpl implements InteractionInfoReportPresent,
    BaseListener<List<InteractionOtherReportData>>, OnItemContentClickListener {

  private InteractionOtherReportView mView;
  private InteractInteraction mInteraction;
  private InteractionInfoReportAdapter mAdapter;

  public InteractionInfoReportPresentImpl(InteractionOtherReportView view) {
    mView = view;
    mInteraction = new InteractInteractionImpl();
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {
    mView.showProgressBar();
    mInteraction.getMeInteraction(1, this);
  }

  @Override
  public void attachView(RecyclerView recyclerView) {
    recyclerView.setAdapter(mAdapter);
  }

  @Override
  public void refresh() {
    mView.showProgressBar();
    mInteraction.getMeInteraction(1, this);
  }

  @Override
  public void setIsOneSelf(boolean isOneSelf) {
    mAdapter = new InteractionInfoReportAdapter(this);
    mAdapter.setOneself(isOneSelf);
  }

  @Override
  public void success(List<InteractionOtherReportData> list) {
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
