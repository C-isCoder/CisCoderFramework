package com.baichang.android.circle.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
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
import com.baichang.android.circle.present.InteractionInfoPresent;
import com.baichang.android.circle.utils.AnimatorUtil;
import com.baichang.android.circle.utils.ColorUtil;
import com.baichang.android.circle.widget.ForceClickImageView;
import com.baichang.android.circle.widget.photocontents.PhotoContents;
import com.baichang.android.circle.widget.photocontents.PhotoContents.OnItemClickListener;
import com.baichang.android.circle.widget.photocontents.adapter.PhotoContentsBaseAdapter;
import com.baichang.android.circle.widget.photopreview.ImageInfo;
import com.baichang.android.circle.widget.photopreview.ImagePreviewActivity;
import com.baichang.android.imageloader.ImageLoader;
import com.baichang.android.widget.circleImageView.CircleImageView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractionContentAdapter extends Adapter<ViewHolder> {

    private static final String TAG = "CID";
    //private List<InteractionListData> mList = InteractionDiskCache.getCache();
    private List<InteractionListData> mList = new ArrayList<>();
    private int mType;

    public InteractionContentAdapter(int type) {
        mType = type;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.interaction_item_content_layout, parent, false));
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
        final InteractionListData data = mList.get(position);
        ImageLoader.loadImageError(holder.itemView.getContext(), data.avatar,
            R.mipmap.interaction_icon_default, holder.ivAvatar);
        holder.tvName.setText(data.name);
        holder.tvTime.setText(data.time);
        holder.tvTitle.setText(data.title);
        if (InteractionConfig.getInstance().isPublishNoTitle()) {
            holder.tvTitle.setVisibility(View.GONE);
        } else {
            holder.tvTitle.setVisibility(View.VISIBLE);
        }

        if (InteractionConfig.getInstance().isNeedWeChatCircleDisplayMax6()) {
            if (data.isMax6()) {
                holder.tvAll.setVisibility(View.VISIBLE);
                holder.tvContent.setText(
                    data.isShowAll ? data.content.substring(0, data.getDisplayNumber())
                        : data.content);
                holder.tvAll.setText(data.isShowAll ? "全部" : "收起");
            } else {
                holder.tvContent.setText(data.content);
                holder.tvAll.setVisibility(View.GONE);
            }
            holder.tvAll.setOnClickListener(new OnClickListener() {
                @Override public void onClick(View v) {
                    data.isShowAll = !data.isShowAll;
                    notifyItemChanged(position);
                }
            });
        } else {
            holder.tvContent.setText(data.content);
        }

        holder.tvPraise.setText(String.valueOf(data.praiseCount));
        holder.tvComment.setText(String.valueOf(data.commentCount));

        int textColor = InteractionConfig.getInstance().getTextFontColor();
        if (textColor != -1) {
            ColorStateList stateList = ColorUtil.createdPressColorList(holder.tvButton.getContext(),
                R.color.cm_tv_black2, textColor);
            holder.tvButton.setTextColor(stateList);
        }
        int businessBrandRes = InteractionConfig.getInstance().getBusinessBrandRes();
        if (businessBrandRes != -1) {
            holder.ivBusinessBrand.setImageResource(businessBrandRes);
        }
        if (!TextUtils.isEmpty(data.type)) {
            if (!"1".equals(data.type) && InteractionConfig.
                getInstance().isNeedShowBusinessBrand()) {
                holder.ivBusinessBrand.setVisibility(View.VISIBLE);
            } else {
                holder.ivBusinessBrand.setVisibility(View.GONE);
            }
        } else {
            holder.ivBusinessBrand.setVisibility(View.GONE);
        }
        if (mType == InteractionInfoPresent.COLLECT) {
            holder.tvButton.setText(R.string.button_cancel_collect);
            holder.tvButton.setVisibility(View.VISIBLE);
        } else if (mType == InteractionInfoPresent.DYNAMIC) {
            holder.tvButton.setText(R.string.button_delete);
            holder.tvButton.setVisibility(View.VISIBLE);
        } else {
            holder.tvButton.setVisibility(View.GONE);
        }
        holder.tvPraise.setSelected(data.isPraise == 1);
        if (data.images != null && !data.images.isEmpty()) {
            InteractionPhotoAdapter mAdapter = new InteractionPhotoAdapter(data.images);
            holder.mPhotos.setAdapter(mAdapter);
            holder.mPhotos.setVisibility(View.VISIBLE);
        } else {
            holder.mPhotos.setVisibility(View.GONE);
        }
    }

    public void setData(List<InteractionListData> list) {
        // FIXME: 2017/5/11 缓存处理不完美,点赞返回数据不刷新。
        if (list != null) {
            mList = list;
            notifyDataSetChanged();
        }
        //    if (list == null) {
        //      return;
        //    }
        //    if (mList == null) {
        //      mList = list;
        //      notifyDataSetChanged();
        //    } else if (list.size() > getItemCount()) {
        //      setDiffData(list, mList);
        //    }
    }

    //  private void setDiffData(List<InteractionListData> first,
    //      List<InteractionListData> two) {
    //    List<InteractionListData> list = new ArrayList<>();
    //    for (InteractionListData firstData : first) {
    //      if (two.contains(firstData)) {
    //        list.add(firstData);
    //      }
    //    }
    //    first.removeAll(list);
    //    for (InteractionListData data : first) {
    //      mList.add(0, data);
    //      notifyItemChanged(0);
    //    }
    //  }

    public void addData(List<InteractionListData> list) {
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    // 更新点赞状态
    public void updatePraise(int id, boolean isSuccess) {
        for (InteractionListData data : mList) {
            if (data.id == id && isSuccess) {
                if (data.isPraise == 1) {
                    data.isPraise = 0;
                    data.praiseCount--;
                } else {
                    data.isPraise = 1;
                    data.praiseCount++;
                }
                notifyItemChanged(mList.indexOf(data));
            }
        }
    }

    private int getIndex(InteractionListData data) {
        return mList.indexOf(data);
    }

    public void remove(InteractionListData data) {
        int index = getIndex(data);
        mList.remove(index);
        notifyItemRemoved(index);
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

    @Override public int getItemCount() {
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
        ImageView ivBusinessBrand;
        TextView tvAll;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_title);
            tvAll = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_all);
            tvName = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_time);
            tvContent = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_content);
            tvComment = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_comment);
            tvPraise = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_praise);
            mPhotos = (PhotoContents) itemView.findViewById(
                R.id.item_interaction_content_photo_content);
            ivAvatar = (CircleImageView) itemView.findViewById(
                R.id.item_interaction_content_iv_avatar);
            tvButton = (TextView) itemView.findViewById(R.id.item_interaction_content_tv_button);
            ivBusinessBrand =
                (ImageView) itemView.findViewById(R.id.item_interaction_content_iv_brand);
            initConfig();
            itemView.setOnTouchListener(this);
            mPhotos.setOnItemClickListener(this);
            // 列表不能点赞
            tvPraise.setOnClickListener(this);
            ivAvatar.setOnClickListener(this);
            tvButton.setOnClickListener(this);
        }

        private void initConfig() {
            int praiseDrawableRes = InteractionConfig.getInstance().getPraiseDrawableRes();
            if (praiseDrawableRes != -1) {
                Drawable praiseDrawable =
                    ContextCompat.getDrawable(tvPraise.getContext(), praiseDrawableRes);
                praiseDrawable.setBounds(0, 0, praiseDrawable.getMinimumWidth(),
                    praiseDrawable.getMinimumHeight());
                tvPraise.setCompoundDrawables(praiseDrawable, null, null, null);
            }
            int commentDrawableRes = InteractionConfig.getInstance().getCommentDrawableRes();
            if (commentDrawableRes != -1) {
                Drawable commentDrawable =
                    ContextCompat.getDrawable(tvComment.getContext(), commentDrawableRes);
                commentDrawable.setBounds(0, 0, commentDrawable.getMinimumWidth(),
                    commentDrawable.getMinimumHeight());
                tvComment.setCompoundDrawables(commentDrawable, null, null, null);
            }
            int deleteDrawableRes = InteractionConfig.getInstance().getButtonDrawableRes();
            if (deleteDrawableRes != -1) {
                ColorStateList stateList = new ColorStateList(
                    new int[][] { new int[] { android.R.attr.state_pressed }, new int[] { 0 } },
                    new int[] { deleteDrawableRes, R.color.cm_tv_black1 });
                tvButton.setTextColor(stateList);
            }
        }

        @Override public void onItemClick(PhotoContents photoContents, int position) {
            InteractionListData data = mList.get(getAdapterPosition());
            List<ImageInfo> imageInfoList = new ArrayList<>();
            for (int i = 0; i < data.images.size(); i++) {
                ImageInfo info = new ImageInfo();
                View imageView = photoContents.getChildAt(i);
                info.bigImageUrl = data.images.get(i);
                info.imageViewWidth = imageView.getWidth();
                info.imageViewHeight = imageView.getHeight();
                int[] points = new int[2];
                imageView.getLocationInWindow(points);
                info.imageViewX = points[0];
                info.imageViewY = points[1];
                imageInfoList.add(info);
            }
            Intent intent = new Intent(photoContents.getContext(), ImagePreviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfoList);
            bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);
            intent.putExtras(bundle);
            photoContents.getContext().startActivity(intent);
            ((Activity) photoContents.getContext()).overridePendingTransition(0, 0);
        }

        @Override public boolean onTouch(View v, MotionEvent event) {
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

        @Override public void onClick(View v) {
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
                if (mType == InteractionInfoPresent.COLLECT) {
                    interactionClickListener.cancel(mList.get(getAdapterPosition()));
                } else if (mType == InteractionInfoPresent.DYNAMIC) {
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

        @Override public void onBindData(int position, @NonNull ImageView convertView) {
            ImageLoader.loadImageError(convertView.getContext(), mImageList.get(position),
                R.mipmap.interaction_place_image, convertView);
        }

        @Override public int getCount() {
            return mImageList.size();
        }

        // FIXME 列表中更新数据 乱序
        void updateData(List<String> list) {
            this.mImageList.clear();
            this.mImageList.addAll(list);
            notifyDataChanged();
        }
    }
}
