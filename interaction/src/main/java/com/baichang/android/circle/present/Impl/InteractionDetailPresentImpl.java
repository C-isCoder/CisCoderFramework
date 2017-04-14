package com.baichang.android.circle.present.Impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.baichang.android.circle.InteractInteraction;
import com.baichang.android.circle.R;
import com.baichang.android.circle.adapter.InteractionDetailAdapter;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.common.InteractionConfig.InteractionListener;
import com.baichang.android.circle.entity.InteractionCommentData;
import com.baichang.android.circle.entity.InteractionCommentListData;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.circle.present.InteractionDetailPresent;
import com.baichang.android.circle.utils.AnimatorUtil;
import com.baichang.android.circle.view.InteractionDetailView;
import com.baichang.android.circle.widget.CommentTextView.CommentOnClickListener;
import com.baichang.android.circle.widget.ForceClickImageView;
import com.baichang.android.circle.widget.photocontents.PhotoContents;
import com.baichang.android.circle.widget.photocontents.adapter.PhotoContentsBaseAdapter;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import com.baichang.android.imageloader.ImageLoader;
import com.baichang.android.widget.circleImageView.CircleImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iCong on 2017/3/21.
 */

public class InteractionDetailPresentImpl implements InteractionDetailPresent,
    BaseListener<List<InteractionCommentData>>, OnClickListener, CommentOnClickListener {

  private static final int REPORT_TYPE = 132;
  private static final int OWNER_TYPE = 641;
  private static final int THEM_TYPE = 643;
  private InteractionDetailView mView;
  private InteractInteraction mInteraction;

  private TextView tvName;
  private TextView tvTime;
  private TextView tvTitle;
  private TextView tvContent;
  private ImageView ivComment;
  private TextView tvCount;
  private CircleImageView ivAvatar;
  private PhotoContentsBaseAdapter mPhotoAdapter;
  private List<String> mPhotoList = new ArrayList<>();
  private InteractionDetailAdapter mAdapter;
  private InteractionCommentListData mCommentListData;
  private int currentType = THEM_TYPE;

  public InteractionDetailPresentImpl(InteractionDetailView view) {
    mView = view;
    mInteraction = new InteractInteractionImpl();
    mPhotoAdapter = new PhotoContentsAdapter(mView.getContext());
    mAdapter = new InteractionDetailAdapter(this);
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {
    mView.showProgressBar();
    mInteraction.getInteractionDetail(1, this);
  }

  @Override
  public void attachView(RecyclerView recyclerView, View header) {
    mAdapter.addHeaderView(header);
    recyclerView.setAdapter(mAdapter);
    tvName = (TextView) header.findViewById(R.id.interaction_detail_tv_name);
    tvTime = (TextView) header.findViewById(R.id.interaction_detail_tv_time);
    tvTitle = (TextView) header.findViewById(R.id.interaction_detail_tv_title);
    ivComment = (ImageView) header.findViewById(R.id.interaction_detail_iv_comment);
    tvContent = (TextView) header.findViewById(R.id.interaction_detail_tv_content);
    tvCount = (TextView) header.findViewById(R.id.interaction_detail_tv_count);
    ivAvatar = (CircleImageView) header.findViewById(R.id.interaction_detail_iv_avatar);

    ivComment.setOnClickListener(this);
    header.findViewById(R.id.interaction_detail_tv_report).setOnClickListener(this);
    initConfig();
    PhotoContents mPhotos = (PhotoContents) header.findViewById(R.id.interaction_detail_photo_content);
    mPhotos.setAdapter(mPhotoAdapter);
  }

  private void initConfig() {
    int commentDrawableRes = InteractionConfig.getInstance().getCommentDrawableRes();
    if (commentDrawableRes != -1) {
      ivComment.setImageResource(commentDrawableRes);
    }
  }

  @Override
  public void bindData(InteractionListData data) {
    if (data == null) {
      return;
    }
    mPhotoList.addAll(data.images);
    mPhotoAdapter.notifyDataChanged();
    tvName.setText(data.name);
    tvTime.setText(data.time);
    tvTitle.setText(data.title);
    tvContent.setText(data.content);
    //TODO 默认图片
    ImageLoader.loadImageError(mView.getContext(), data.avatar, R.mipmap.interaction_icon_default, ivAvatar);
  }

  @Override
  public void refresh() {
    mView.showProgressBar();
    mInteraction.getInteractionDetail(1, this);
  }

  @Override
  public void send(String content) {
    if (TextUtils.isEmpty(content)) {
      mView.showMsg("请输入回复内容");
      return;
    }
    if (mCommentListData != null) {
      switch (currentType) {
        case REPORT_TYPE:
          InteractionCommentListData reportData = new InteractionCommentListData();
          reportData.owner = mCommentListData.reportName;
          reportData.reportName = "user_name";
          reportData.content = content;
          addCommentChild(reportData);
          break;
        case OWNER_TYPE:
          InteractionCommentListData ownerData = new InteractionCommentListData();
          ownerData.owner = mCommentListData.owner;
          ownerData.reportName = "user_name";
          ownerData.content = content;
          addCommentChild(ownerData);
          break;
      }
    } else {
      InteractionCommentData data = new InteractionCommentData();
      data.content = content;
      data.avatar = "user_avatar";
      data.name = "user_name";
      data.time = "2017-03-23";
      addComment(data);
    }
  }

  @Override
  public void share() {
    //TODO share
    InteractionListener listener = InteractionConfig.getInstance().getListener();
    if (listener != null) {
      listener.share("123", "jf", "ifjj");
    }
  }

  @Override
  public void collect() {
    //TODO collect
  }

  @Override
  public void praise() {
    //TODO praise
    mView.showMsg("赞一个");
  }

  // 回复某人的评论
  private void addCommentChild(InteractionCommentListData data) {
    int index = mAdapter.addChildComment(data);
    mView.scrollToPosition(index);
    mView.hideInputKeyBord();
    mView.showMsg("回复成功");
    mView.setReportHint("回复");
    mCommentListData = null;
  }

  // 评论该帖子
  private void addComment(InteractionCommentData data) {
    mAdapter.addComment(data);
    mView.hideInputKeyBord();
    mView.showMsg("回复成功");
    mView.setReportHint("回复");
    mView.scrollToPosition(1);// 滚动到顶部
  }

  @Override
  public void success(List<InteractionCommentData> list) {
    mView.hideProgressBar();
    mAdapter.setData(list);
    tvCount.setText("评论（" + list.size() + "）");
  }

  @Override
  public void error(String error) {
    mView.hideProgressBar();
    mView.showMsg(error);
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.interaction_detail_tv_report) {
      mView.showMsg("举报");
    } else if (id == ivComment.getId()) {
      AnimatorUtil.scale(v);
      mView.setReportHint(" 回复 ");
      mView.showInputKeyBord();
      currentType = THEM_TYPE;
    }
  }

  @Override
  public void ownerOnClick(InteractionCommentListData data) {
    mView.setReportHint(" 回复 " + data.owner);
    mView.showInputKeyBord();
    mCommentListData = data;
    currentType = OWNER_TYPE;
  }

  @Override
  public void reportOnClick(InteractionCommentListData data) {
    mView.setReportHint(" 回复 " + data.reportName);
    mView.showInputKeyBord();
    mCommentListData = data;
    currentType = REPORT_TYPE;
  }

  @Override
  public void contentOnClick(InteractionCommentListData data) {
    mView.setReportHint(" 回复 " + data.owner);
    mView.showInputKeyBord();
    mCommentListData = data;
    currentType = OWNER_TYPE;
  }


  private class PhotoContentsAdapter extends PhotoContentsBaseAdapter {

    private Context mContext;

    PhotoContentsAdapter(Context context) {
      mContext = context;
    }

    @Override
    public ImageView onCreateView(ImageView imageView, ViewGroup viewGroup, int i) {
      if (imageView == null) {
        imageView = new ForceClickImageView(viewGroup.getContext());
        imageView.setScaleType(ScaleType.CENTER_CROP);
      }
      return imageView;
    }

    @Override
    public void onBindData(int i, @NonNull ImageView imageView) {
      ImageLoader.loadImageError(mContext, mPhotoList.get(i), R.mipmap.interaction_icon_default, imageView);
    }

    @Override
    public int getCount() {
      return mPhotoList.size();
    }
  }
}
