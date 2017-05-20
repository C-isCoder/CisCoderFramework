package com.baichang.android.circle.present.Impl;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import com.baichang.android.circle.R;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.common.InteractionFlag.Event;
import com.baichang.android.circle.entity.InteractionUserData;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.circle.model.InteractInteraction;
import com.baichang.android.circle.present.InteractionInfoPresent;
import com.baichang.android.common.BaseEventData;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import com.baichang.android.circle.common.InteractionAPIConstants;
import com.baichang.android.circle.adapter.InteractionContentAdapter;
import com.baichang.android.circle.adapter.InteractionContentAdapter.InteractionClickListener;
import com.baichang.android.circle.adapter.InteractionContentAdapter.ItemOnClickListener;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.present.InteractionContentPresent;
import com.baichang.android.circle.view.InteractionContentView;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.widget.recycleView.RecyclerViewUtils;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

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
  private int typeId;
  private int modelType;

  public InteractionContentPresentImpl(InteractionContentView view, int mModelType) {
    mView = view;
    mInteraction = new InteractInteractionImpl();
    typeId = view.getTypeId();
    mAdapter = new InteractionContentAdapter(mModelType);
    mAdapter.setItemOnClickListener(this);
    mAdapter.setInteractionClickListener(this);
    modelType = mModelType;
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {
    mView.showProgressBar();
    if (typeId != -1) {
      mInteraction.getInteractionList(typeId, 1, this);
    } else {
      switch (modelType) {
        // 动态
        case InteractionInfoPresent.NORMAL:
        case InteractionInfoPresent.DYNAMIC:
          mInteraction.getDynamics(mView.getUserId(), 1, this);
          break;
        case InteractionInfoPresent.COLLECT: // 收藏的动态
          mInteraction.getCollect(1, this);
          break;
      }
    }
  }

  @Override
  public void refresh() {
    isRefresh = true;
    nowPage = 1;
    if (typeId != -1) {
      mInteraction.getInteractionList(typeId, nowPage, this);
      EventBus.getDefault().post(new BaseEventData(Event.INTERACTION_TYPE_REFRESH));
    } else {
      switch (modelType) {
        // 动态
        case InteractionInfoPresent.NORMAL:
        case InteractionInfoPresent.DYNAMIC:
          mInteraction.getDynamics(mView.getUserId(), nowPage, this);
          break;
        case InteractionInfoPresent.COLLECT: // 收藏的动态
          mInteraction.getCollect(nowPage, this);
          break;
      }
    }
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
          if (typeId != -1) {
            mInteraction.getInteractionList(typeId, nowPage, InteractionContentPresentImpl.this);
          } else {
            switch (modelType) {
              // 动态
              case InteractionInfoPresent.NORMAL:
              case InteractionInfoPresent.DYNAMIC:
                mInteraction.getDynamics(mView.getUserId(), nowPage, InteractionContentPresentImpl.this);
                break;
              case InteractionInfoPresent.COLLECT: // 收藏的动态
                mInteraction.getCollect(nowPage, InteractionContentPresentImpl.this);
                break;
            }
          }
        }
      }
    });
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
    mView.gotoDetail(data);
  }

  @Override
  public void praise(InteractionListData data) {
    // 点赞
  }

  @Override
  public void avatar(InteractionListData data) {
    InteractionUserData user = InteractionConfig.getInstance().getUser();
    if (user == null) {
      return;
    }
    mView.gotoInfo(TextUtils.equals(data.hostUserId, user.id), data.hostUserId);
  }

  @Override
  public void delete(final InteractionListData data) {
    int colorRes = InteractionConfig.getInstance().getTextFontColor();
    BCDialogUtil.showDialog(mView.getContext(), colorRes == -1 ? R.color.interaction_text_font : colorRes,
        "删除", "确定删除吗？",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            mAdapter.remove(data);
            mInteraction.delete(data.id, deleteListener);
          }
        }, null);
  }

  @Override
  public void cancel(final InteractionListData data) {
    int colorRes = InteractionConfig.getInstance().getTextFontColor();
    BCDialogUtil.showDialog(mView.getContext(), colorRes == -1 ? R.color.interaction_text_font : colorRes,
        "取消收藏", "确定取消收藏吗？",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            mAdapter.remove(data);
            mInteraction.collect(data.id, cancelCollectListener);
          }
        }, null);
  }

  private BaseListener<Boolean> deleteListener = new BaseListener<Boolean>() {
    @Override
    public void success(Boolean aBoolean) {
      mView.showMsg("删除成功");
      EventBus.getDefault().post(new BaseEventData(Event.INTERACTION_LIST_DELETE));
    }

    @Override
    public void error(String error) {
      mView.showMsg(error);
    }
  };
  private BaseListener<Boolean> cancelCollectListener = new BaseListener<Boolean>() {
    @Override
    public void success(Boolean aBoolean) {
      mView.showMsg("取消成功");
      EventBus.getDefault().post(new BaseEventData(Event.INTERACTION_LIST_CANCEL_COLLECT));
    }

    @Override
    public void error(String error) {
      mView.showMsg(error);
    }
  };
}
