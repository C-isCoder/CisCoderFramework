package com.baichang.android.circle.present.Impl;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import com.baichang.android.circle.common.InteractionAPIConstants;
import com.baichang.android.circle.adapter.InteractionContentAdapter;
import com.baichang.android.circle.adapter.InteractionContentAdapter.InteractionClickListener;
import com.baichang.android.circle.adapter.InteractionContentAdapter.ItemOnClickListener;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.InteractInteraction;
import com.baichang.android.circle.present.InteractionContentPresent;
import com.baichang.android.circle.view.InteractionContentView;
import com.baichang.android.widget.recycleView.RecyclerViewUtils;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractionContentPresentImpl implements InteractionContentPresent,
    BaseListener<List<InteractionListData>>, ItemOnClickListener, InteractionClickListener {

  private InteractionContentView mView;
  private InteractInteraction mInteraction;
  private InteractionContentAdapter mAdapter;
  private int nowPage = 1;
  private boolean isRefresh = true;

  public InteractionContentPresentImpl(InteractionContentView view) {
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
    mInteraction.getInteractionList(1, this);
  }

  @Override
  public void refresh() {
    mView.showProgressBar();
    isRefresh = true;
    nowPage = 1;
    mInteraction.getInteractionList(1, this);
  }

  @Override
  public void attachRecyclerView(RecyclerView recyclerView) {
    recyclerView.setAdapter(mAdapter);
    recyclerView.addOnScrollListener(new OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (RecyclerViewUtils.isScrollBottom(recyclerView) &&
            mAdapter.getItemCount() % InteractionAPIConstants.PAGE_SIZE == 0) {
          isRefresh = false;
          nowPage++;
          mInteraction.getInteractionList(nowPage, InteractionContentPresentImpl.this);
        }
      }
    });
  }

  @Override
  public void setType(int type) {
    mAdapter = new InteractionContentAdapter(type);
    mAdapter.setItemOnClickListener(this);
    mAdapter.setInteractionClickListener(this);
  }

  @Override
  public void success(List<InteractionListData> list) {
    mView.hideProgressBar();
    if (isRefresh) {
      mAdapter.setData(list);
    } else {
      mAdapter.addData(list);
    }
  }

  @Override
  public void error(String error) {
    mView.hideProgressBar();
    mView.showMsg(error);
  }

  @Override
  public void onItemClick(InteractionListData data) {
    mView.showMsg(String.valueOf(data.id));
    mView.gotoDetail(data);
  }

  @Override
  public void praise(InteractionListData data) {
    mView.showMsg(String.valueOf(data.id));
  }

  @Override
  public void avatar(InteractionListData data) {
    mView.gotoInfo(data.id == 1);
  }

  @Override
  public void delete(InteractionListData data) {
    mView.showMsg("删除动态");
  }

  @Override
  public void cancel(InteractionListData data) {
    mView.showMsg("取消收藏");
  }
}
