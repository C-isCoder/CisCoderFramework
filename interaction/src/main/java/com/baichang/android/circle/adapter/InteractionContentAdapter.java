package com.baichang.android.circle.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.baichang.android.circle.R;
import com.baichang.android.circle.adapter.InteractionContentAdapter.ViewHolder;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.present.Impl.InteractionInfoPresentImpl;
import com.baichang.android.circle.utils.AnimatorUtil;
import com.baichang.android.circle.utils.ColorUtil;
import com.baichang.android.circle.widget.ForceClickImageView;
import com.baichang.android.circle.widget.photocontents.PhotoContents;
import com.baichang.android.circle.widget.photocontents.PhotoContents.OnItemClickListener;
import com.baichang.android.circle.widget.photocontents.adapter.PhotoContentsBaseAdapter;
import com.baichang.android.circle.widget.photopreview.PhotoGalleryActivity;
import com.baichang.android.circle.widget.photopreview.PhotoGalleryData;
import com.baichang.android.imageloader.ImageLoader;
import com.baichang.android.widget.circleImageView.CircleImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractionContentAdapter extends Adapter<ViewHolder> {

  private static final String TAG = "CID";
  private List<InteractionListData> mList = new ArrayList<>();
  private InteractionPhotoAdapter mAdapter;
  private int mType;

  public InteractionContentAdapter(int type) {
    mType = type;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.interaction_item_content_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    InteractionListData data = mList.get(position);
    //TODO 默认图
    ImageLoader.loadImageError(holder.itemView.getContext(),
        data.avatar, R.mipmap.interaction_icon_default, holder.ivAvatar);
    holder.tvName.setText(data.name);
    holder.tvTime.setText(data.time);
    holder.tvTitle.setText(data.title);
    holder.tvContent.setText(data.content);
    holder.tvPraise.setText(String.valueOf(data.praiseCount));
    holder.tvComment.setText(String.valueOf(data.commentCount));

    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      ColorStateList stateList = ColorUtil.createdPressColorList(
          holder.tvButton.getContext(), R.color.cm_tv_black2, textColor);
      holder.tvButton.setTextColor(stateList);
    }
    if (mType == InteractionInfoPresentImpl.COLLECT) {
      holder.tvButton.setText("取消收藏");
      holder.tvButton.setVisibility(View.VISIBLE);
    } else if (mType == InteractionInfoPresentImpl.DYNAMIC) {
      holder.tvButton.setText("删除");
      holder.tvButton.setVisibility(View.VISIBLE);
    } else {
      holder.tvButton.setVisibility(View.GONE);
    }
    if (mAdapter == null) {
      mAdapter = new InteractionPhotoAdapter(data.images);
    } else {
      mAdapter.updateData(data.images);
    }
    holder.mPhotos.setAdapter(mAdapter);
  }

  public void setData(List<InteractionListData> list) {
    if (list != null) {
      mList = list;
      notifyDataSetChanged();
    }
  }

  public void addData(List<InteractionListData> list) {
    if (list != null) {
      mList.addAll(list);
      notifyDataSetChanged();
    }
  }

  private InteractionClickListener interactionClickListener;

  public void setInteractionClickListener(InteractionClickListener listener) {
    interactionClickListener = listener;
  }

  public interface InteractionClickListener {

    void praise(InteractionListData data);

    void avatar(InteractionListData data);

    void delete(InteractionListData data);

    void cancel(InteractionListData data);
  }

  @Override
  public int getItemCount() {
    return mList == null ? 0 : mList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder
      implements OnItemClickListener, OnTouchListener, OnClickListener {

    private long pressTime = 0;

    TextView tvTitle;
    TextView tvName;
    TextView tvTime;
    TextView tvContent;
    TextView tvComment;
    TextView tvPraise;
    PhotoContents mPhotos;
    CircleImageView ivAvatar;
    TextView tvButton;

    ViewHolder(View itemView) {
      super(itemView);
      tvTitle = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_title);
      tvName = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_name);
      tvTime = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_time);
      tvContent = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_content);
      tvComment = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_comment);
      tvPraise = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_praise);
      mPhotos = (PhotoContents) itemView.findViewById(R.id.item_interaction_content_photo_content);
      ivAvatar = (CircleImageView) itemView.findViewById(R.id.item_interaction_content_iv_avatar);
      tvButton = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_button);
      initConfig();
      itemView.setOnTouchListener(this);
      mPhotos.setmOnItemClickListener(this);
      // 列表不能点赞
      //tvPraise.setOnClickListener(this);
      ivAvatar.setOnClickListener(this);
      tvButton.setOnClickListener(this);
    }

    private void initConfig() {
      int praiseDrawableRes = InteractionConfig.getInstance().getPraiseDrawableRes();
      if (praiseDrawableRes != -1) {
        Drawable praiseDrawable = ContextCompat.getDrawable(tvPraise.getContext(), praiseDrawableRes);
        praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(), praiseDrawable.getMinimumHeight());
        tvPraise.setCompoundDrawables(praiseDrawable, null, null, null);
      }
      int commentDrawableRes = InteractionConfig.getInstance().getCommentDrawableRes();
      if (commentDrawableRes != -1) {
        Drawable commentDrawable = ContextCompat.getDrawable(tvComment.getContext(), commentDrawableRes);
        commentDrawable.setBounds(0, 0, commentDrawable.getMinimumWidth(), commentDrawable.getMinimumHeight());
        tvComment.setCompoundDrawables(commentDrawable, null, null, null);
      }
    }

    @Override
    public void onItemClick(ImageView imageView, int position) {
      InteractionListData data = mList.get(getAdapterPosition());
      PhotoGalleryData galleryData = new PhotoGalleryData(position, data.images);
      Intent intent = new Intent(imageView.getContext(), PhotoGalleryActivity.class);
      intent.putExtra(PhotoGalleryActivity.IMAGE_DATA, galleryData);
      ActivityOptionsCompat compat = ActivityOptionsCompat
          .makeSceneTransitionAnimation((Activity) imageView.getContext(), imageView, "photo");
      ActivityCompat.startActivity(imageView.getContext(), intent, compat.toBundle());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
      long upTime;
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          pressTime = System.currentTimeMillis();
          break;
        case MotionEvent.ACTION_UP:
          upTime = System.currentTimeMillis();
          if ((upTime - pressTime) < 200) {
            if (listener != null) {
              listener.onItemClick(mList.get(getAdapterPosition()));
            }
          }
          break;
      }
      return true;
    }

    @Override
    public void onClick(View v) {
      int i = v.getId();
      if (i == tvPraise.getId()) {
        if (interactionClickListener != null) {
          AnimatorUtil.scale(v);
          tvPraise.setSelected(!tvPraise.isSelected());
          interactionClickListener.praise(mList.get(getAdapterPosition()));
        }
      } else if (i == ivAvatar.getId()) {
        if (interactionClickListener != null) {
          interactionClickListener.avatar(mList.get(getAdapterPosition()));
        }
      } else if (i == tvButton.getId()) {
        if (mType == InteractionInfoPresentImpl.COLLECT) {
          interactionClickListener.cancel(mList.get(getAdapterPosition()));
        } else if (mType == InteractionInfoPresentImpl.DYNAMIC) {
          interactionClickListener.delete(mList.get(getAdapterPosition()));
        }
      }
    }

  }

  private ItemOnClickListener listener;

  public void setItemOnClickListener(ItemOnClickListener listener) {
    this.listener = listener;
  }

  public interface ItemOnClickListener {

    void onItemClick(InteractionListData data);
  }

  private class InteractionPhotoAdapter extends PhotoContentsBaseAdapter {

    private List<String> mImageList;

    InteractionPhotoAdapter(List<String> list) {
      this.mImageList = new ArrayList<>();
      this.mImageList.addAll(list);
    }

    @Override
    public ImageView onCreateView(ImageView convertView, ViewGroup parent, int position) {
      if (convertView == null) {
        convertView = new ForceClickImageView(parent.getContext());
        convertView.setScaleType(ScaleType.CENTER_CROP);
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
          convertView.setTransitionName("photo");
        }
      }
      return convertView;
    }

    @Override
    public void onBindData(int position, @NonNull ImageView convertView) {
      //TODO 默认图
      ImageLoader.loadImageError(convertView.getContext(),
          mImageList.get(position), R.mipmap.interaction_icon_default, convertView);
    }

    @Override
    public int getCount() {
      return mImageList.size();
    }

    void updateData(List<String> list) {
      this.mImageList.clear();
      this.mImageList.addAll(list);
      notifyDataChanged();
    }
  }

}
