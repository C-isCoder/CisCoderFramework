package com.baichang.android.circle.adapter;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.baichang.android.circle.R;
import com.baichang.android.circle.adapter.InteractionPublishAdapter9.Holder;
import com.baichang.android.circle.ijkplayer.VideoActivity;
import com.baichang.android.widget.photoGallery.PhotoGalleryActivity;
import com.baichang.android.widget.photoGallery.PhotoGalleryData;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iCong on 2017/3/24.
 */

public class InteractionPublishAdapter9 extends RecyclerView.Adapter<Holder> {

  private static final int NORMAL_VIEW = 0;
  private static final int FOOT_VIEW = 1;
  public static final int PHOTO_MODEL = 10;
  public static final int VIDEO_MODEL = 2;
  private static int DEFAULT_MAX_NUMBER = 9;

  private ArrayList<BaseMedia> mList;
  private int currentModel = -1;

  public InteractionPublishAdapter9(SelectPhotoClickListener9 listener) {
    mList = new ArrayList<>();
    setSelectPhotoClickListener9(listener);
  }

  @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    int type;
    if (viewType == NORMAL_VIEW) {
      view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.interaction_item_publish_image_9_layout, parent, false);
      type = NORMAL_VIEW;
    } else {
      view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.interaction_item_publish_foot_view, parent, false);
      type = FOOT_VIEW;
    }
    int height = parent.getMeasuredHeight() / 4;
    view.setMinimumHeight(height);
    return new Holder(view, type);
  }

  @Override public void onBindViewHolder(Holder holder, int position) {
    if (getItemViewType(position) == FOOT_VIEW) {
      if (getItemCount() == getCurrentModel()) { // 1/9张之后不显示添加按钮
        holder.itemView.setVisibility(View.GONE);
      } else {
        holder.itemView.setVisibility(View.VISIBLE);
      }
      return;
    }
    if (currentModel == PHOTO_MODEL) {
      holder.ivPlay.setVisibility(View.GONE);
    } else {
      holder.ivPlay.setVisibility(View.VISIBLE);
    }
    BaseMedia media = mList.get(position);
    String path = ((ImageMedia) media).getThumbnailPath();
    BoxingMediaLoader.getInstance().displayThumbnail(holder.ivImage, path, 400, 200);
    holder.itemView.setTag(position);
  }

  @Override public int getItemViewType(int position) {
    if (position == getItemCount() - 1) {
      return FOOT_VIEW;
    } else {
      return NORMAL_VIEW;
    }
  }

  public BaseMedia getVideo() {
    if (currentModel == PHOTO_MODEL) {
      return null;
    }
    return mList.isEmpty() ? null : mList.get(0);
  }

  public int getCurrentModel() {
    return currentModel;
  }

  public void setCurrentModel(int currentModel) {
    this.currentModel = currentModel;
    if (currentModel == PHOTO_MODEL) {
      DEFAULT_MAX_NUMBER = 9;
    } else if (currentModel == VIDEO_MODEL) {
      DEFAULT_MAX_NUMBER = 1;
    }
  }

  public boolean checkNumber() {
    return getPathList().size() >= DEFAULT_MAX_NUMBER;
  }

  public int getMax() {
    return DEFAULT_MAX_NUMBER - getPathList().size();
  }

  @Override public int getItemCount() {
    return mList.size() + 1;
  }

  public void setData(ArrayList<BaseMedia> list) {
    if (list == null) {
      return;
    }
    mList.addAll(list);
    notifyDataSetChanged();
  }

  public void addData(BaseMedia media) {
    if (media == null) {
      return;
    }
    mList.add(media);
    notifyItemChanged(getItemCount() - 1);
    notifyDataSetChanged();
  }

  public ArrayList<BaseMedia> getList() {
    return mList;
  }

  public List<String> getPathList() {
    List<String> path = new ArrayList<>();
    for (BaseMedia media : mList) {
      path.add(((ImageMedia) media).getThumbnailPath());
    }
    return path;
  }

  private void removeData(int position) {
    mList.remove(position);
    notifyItemRemoved(position);
    notifyDataSetChanged();
  }

  private SelectPhotoClickListener9 selectPhotoClickListener;

  public void setSelectPhotoClickListener9(SelectPhotoClickListener9 listener) {
    selectPhotoClickListener = listener;
  }

  public interface SelectPhotoClickListener9 {

    void select();
  }

  class Holder extends ViewHolder implements OnClickListener {

    ImageButton btnDelete;
    ImageView ivImage;
    ImageView ivPlay;

    Holder(View itemView, int type) {
      super(itemView);
      if (type == NORMAL_VIEW) {
        btnDelete = (ImageButton) itemView.findViewById(R.id.item_interaction_publish_btn_delete);
        ivImage = (ImageView) itemView.findViewById(R.id.item_interaction_publish_iv_image);
        ivPlay = (ImageView) itemView.findViewById(R.id.item_interaction_publish_btn_play);
        btnDelete.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
      } else {
        itemView.findViewById(R.id.item_interaction_publish_btn_add).setOnClickListener(this);
      }
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
      int i = v.getId();
      if (i
          == R.id.item_interaction_publish_btn_add) {// getInstance the center for the clipping circle
        int cx = (v.getLeft() + v.getRight()) / 2;
        int cy = (v.getTop() + v.getBottom()) / 2;
        // getInstance the final radius for the clipping circle
        int finalRadius = Math.max(v.getWidth(), v.getHeight());
        // create the animator for this view (the start radius is zero)
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
          anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
          // make the view visible and start the animation
          v.setVisibility(View.VISIBLE);
          anim.start();
        }
        if (selectPhotoClickListener != null) {
          selectPhotoClickListener.select();
        }
      } else if (i == R.id.item_interaction_publish_btn_delete) {
        removeData(getLayoutPosition());
      } else if (i == ivPlay.getId()) {
        if (currentModel == PHOTO_MODEL) {
          BaseMedia media = mList.get(getLayoutPosition());
          if (media == null) return;
          List<String> images = new ArrayList<>();
          for (BaseMedia baseMedia : mList) {
            images.add(baseMedia.getPath());
          }
          PhotoGalleryData data = new PhotoGalleryData(true, getLayoutPosition(), images);
          Intent intent = new Intent(v.getContext(), PhotoGalleryActivity.class);
          intent.putExtra(PhotoGalleryActivity.IMAGE_DATA, data);
          v.getContext().startActivity(intent);
        } else {
          BaseMedia media = mList.get(getLayoutPosition());
          VideoActivity.intentTo(v.getContext(), media.getId());
        }
      }
    }
  }
}
