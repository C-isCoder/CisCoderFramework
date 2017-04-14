package com.baichang.android.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baichang.android.circle.R;
import com.baichang.android.circle.adapter.InteractionDetailAdapter.Holder;
import com.baichang.android.circle.entity.InteractionCommentData;
import com.baichang.android.circle.entity.InteractionCommentListData;
import com.baichang.android.circle.utils.SimpleObjectPool;
import com.baichang.android.circle.widget.CommentTextView;
import com.baichang.android.circle.widget.CommentTextView.CommentOnClickListener;
import com.baichang.android.widget.circleImageView.CircleImageView;
import java.util.List;

/**
 * Created by iCong on 2017/3/21.
 */

public class InteractionDetailAdapter extends Adapter<Holder> {

  private static final int HEADER_VIEW = 0;
  private static final int NORMAL_VIEW = 1;
  private static final int MAX_CHILD_COUNT = 2;
  private static final String OPEN_SEE_MORE_TEXT = "查看更多回复";
  private static final String CLOSE_SEE_MORE_TEXT = "收起更多回复";
  private static final SimpleObjectPool<CommentTextView> CommentViewPool = new SimpleObjectPool<>(50);
  private List<InteractionCommentData> mList;
  private View mHeaderView;
  private int currentPosition = -1;

  public InteractionDetailAdapter(CommentOnClickListener listener) {
    setCommentOnClickListener(listener);
  }

  @Override
  public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == HEADER_VIEW) {
      return new Holder(mHeaderView);
    } else {
      return new Holder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.interaction_item_detail_comments_layout, parent, false));
    }
  }

  public InteractionCommentData getItemData(int position) {
    return mList.get(position);
  }

  public void setData(List<InteractionCommentData> list) {
    if (list != null) {
      mList = list;
      notifyDataSetChanged();
    }
  }

  public int addChildComment(InteractionCommentListData data) {
    if (currentPosition != -1) {
      mList.get(currentPosition - 1).comments.add(0, data);
      notifyItemChanged(currentPosition);
      return currentPosition;
    }
    return 0;
  }

  public void addComment(InteractionCommentData data) {
    mList.add(0, data);
    notifyItemInserted(1);
  }

  public List<InteractionCommentData> getList() {
    return mList;
  }

  @Override
  public void onBindViewHolder(Holder holder, int position) {
    if (getItemViewType(position) == HEADER_VIEW) {
      return;
    }
    int finalPos = holder.getLayoutPosition() - 1;
    InteractionCommentData data = getItemData(finalPos);
    holder.onBindData(data);
    holder.onBindCommentData(data, false);
  }

  @Override
  public int getItemCount() {
    return mList == null ? 0 : mList.size() + 1;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return HEADER_VIEW;
    } else {
      return NORMAL_VIEW;
    }
  }

  public void addHeaderView(View view) {
    if (view == null) {
      return;
    }
    mHeaderView = view;
  }

  private CommentOnClickListener listener;

  private void setCommentOnClickListener(CommentOnClickListener listener) {
    this.listener = listener;
  }

  class Holder extends RecyclerView.ViewHolder
      implements ViewGroup.OnHierarchyChangeListener, CommentOnClickListener {

    TextView tvName;
    TextView tvTime;
    TextView tvContent;
    CircleImageView ivAvatar;
    LinearLayout commentLayout;
    TextView tvMore;

    Holder(View itemView) {
      super(itemView);
      if (itemView != mHeaderView) {
        tvName = (TextView) itemView.findViewById(R.id.item_interaction_detail_tv_name);
        tvTime = (TextView) itemView.findViewById(R.id.item_interaction_detail_tv_time);
        tvContent = (TextView) itemView.findViewById(R.id.item_interaction_detail_tv_content);
        ivAvatar = (CircleImageView) itemView.findViewById(R.id.item_interaction_detail_iv_avatar);
        commentLayout = (LinearLayout) itemView.findViewById(R.id.item_interaction_detail_comment_layout);
        tvMore = (TextView) itemView.findViewById(R.id.interaction_detail_tv_more);
      }
    }

    public Context getContext() {
      return itemView.getContext();
    }

    void onBindData(InteractionCommentData data) {
      commentLayout.setOnHierarchyChangeListener(this);
      tvName.setText(data.name);
      tvContent.setText(data.content);
      tvTime.setText(data.time);
      ivAvatar.setImageResource(R.mipmap.interaction_icon_default);
      commentLayout.setVisibility(data.isNullComment() ? View.GONE : View.VISIBLE);
      tvMore.setVisibility(data.isShowMore() ? View.VISIBLE : View.GONE);
      tvMore.setOnClickListener(new CommentOnClick(data));
      tvContent.setOnClickListener(new CommentOnClick(data));
      tvMore.setText(OPEN_SEE_MORE_TEXT);
    }

    void onBindCommentData(InteractionCommentData data, boolean isMore) {
      if (data.comments == null || data.comments.isEmpty()) {
        return;
      }
      int childCount = commentLayout.getChildCount();
      int commentSize;
      if (isMore) {
        commentSize = data.comments.size();
      } else if (data.isShowMore()) {
        commentSize = MAX_CHILD_COUNT;
      } else {
        commentSize = data.comments.size();
      }
      if (childCount < commentSize) {
        int subCount = commentSize - childCount;
        for (int i = 0; i < subCount; i++) {
          CommentTextView commentView = CommentViewPool.get();
          if (commentView == null) {
            commentView = new CommentTextView(getContext());
          }
          commentView.setCommentOnClickListener(this);
          commentLayout.addView(commentView);
        }
      } else if (childCount > commentSize) {
        commentLayout.removeViews(commentSize, childCount - commentSize);
      }
      for (int i = 0; i < commentSize; i++) {
        CommentTextView commentView = (CommentTextView) commentLayout.getChildAt(i);
        if (commentView == null) {
          return;
        }
        InteractionCommentListData comment = data.comments.get(i);
        commentView.setText(comment);
      }
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
      if (child instanceof CommentTextView) {
        CommentViewPool.put((CommentTextView) child);
      }
    }

    @Override
    public void ownerOnClick(InteractionCommentListData data) {
      if (listener != null) {
        listener.ownerOnClick(data);
        currentPosition = getLayoutPosition();
      }
    }

    @Override
    public void reportOnClick(InteractionCommentListData data) {
      if (listener != null) {
        listener.reportOnClick(data);
        currentPosition = getLayoutPosition();
      }
    }

    @Override
    public void contentOnClick(InteractionCommentListData data) {
      if (listener != null) {
        listener.contentOnClick(data);
        currentPosition = getLayoutPosition();
      }
    }

    class CommentOnClick implements OnClickListener {

      InteractionCommentData data;

      CommentOnClick(InteractionCommentData data) {
        this.data = data;
      }

      @Override
      public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.item_interaction_detail_tv_content) {
          if (listener != null) {
            InteractionCommentListData commentListData = new InteractionCommentListData();
            commentListData.owner = data.name;
            listener.contentOnClick(commentListData);
            currentPosition = getLayoutPosition();
          }
        } else {
          String text = ((TextView) v).getText().toString();
          if (TextUtils.equals(OPEN_SEE_MORE_TEXT, text)) {
            tvMore.setText(CLOSE_SEE_MORE_TEXT);
            onBindCommentData(data, true);
          } else {
            tvMore.setText(OPEN_SEE_MORE_TEXT);
            onBindCommentData(data, false);
          }

        }
      }
    }
  }

}
