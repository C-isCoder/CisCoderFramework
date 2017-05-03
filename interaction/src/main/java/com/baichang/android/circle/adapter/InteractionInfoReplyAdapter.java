package com.baichang.android.circle.adapter;

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baichang.android.circle.R;
import com.baichang.android.circle.adapter.InteractionInfoReplyAdapter.Holder;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.entity.InteractionReplyData;
import com.baichang.android.imageloader.ImageLoader;
import com.baichang.android.widget.circleImageView.CircleImageView;
import java.util.List;

/**
 * Created by iCong on 2017/3/28.
 */

public class InteractionInfoReplyAdapter extends RecyclerView.Adapter<Holder> {

  private List<InteractionReplyData> mList;

  public InteractionInfoReplyAdapter(OnItemContentClickListener listener) {
    setOnItemContentClickListener(listener);
  }

  @Override
  public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new Holder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.interaction_item_reply_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(Holder holder, int position) {
    InteractionReplyData data = mList.get(position);
    if (data.trendsImages != null && !data.trendsImages.isEmpty()) {
      ImageLoader.loadImageError(holder.ivImage.getContext(), data.trendsImages.get(0),
          R.mipmap.interaction_icon_default_image, holder.ivImage);
    }
    ImageLoader.loadImageError(holder.ivAvatar.getContext(), data.replyIcon,
        R.mipmap.interaction_icon_default, holder.ivAvatar);
    holder.tvName.setText(data.replyName);
    holder.tvContent.setText(data.replyContent);
    holder.tvTime.setText(data.replyTime);
    holder.tvSummary.setText(Html.fromHtml(
        String.format("<font color=\"#67c4fe\">%s</font>,<font color=\"#666666\">%s</font>",
            data.hostName, data.trendsContent)));
    // 去掉不要了。
    //holder.tvMe.setVisibility(isOneself ? View.VISIBLE : View.GONE);
    //holder.tvMe.setText(isOneself ? "我：jxxx" : "");
  }

  @Override
  public int getItemCount() {
    return mList == null ? 0 : mList.size();
  }

  public void setData(List<InteractionReplyData> list) {
    mList = list;
    notifyDataSetChanged();
  }

  public void addData(List<InteractionReplyData> list) {
    mList.addAll(list);
    notifyDataSetChanged();
  }

  private OnItemContentClickListener listener;

  public void setOnItemContentClickListener(OnItemContentClickListener listener) {
    this.listener = listener;
  }

  public interface OnItemContentClickListener {

    void onContentClick(int id);
  }

  class Holder extends ViewHolder implements OnClickListener {

    CircleImageView ivAvatar;
    ImageView ivImage;
    TextView tvContent;
    TextView tvName;
    TextView tvSummary;
    TextView tvTime;
    LinearLayout mContentLayout;
    TextView tvMe;
    TextView tvReply;

    Holder(View itemView) {
      super(itemView);
      ivAvatar = (CircleImageView) itemView.findViewById(R.id.item_interaction_reply_iv_avatar);
      ivImage = (ImageView) itemView.findViewById(R.id.item_interaction_reply_iv_image);
      tvContent = (TextView) itemView.findViewById(R.id.item_interaction_reply_tv_content);
      tvName = (TextView) itemView.findViewById(R.id.item_interaction_reply_tv_name);
      tvSummary = (TextView) itemView.findViewById(R.id.item_interaction_reply_tv_summary);
      tvTime = (TextView) itemView.findViewById(R.id.item_interaction_reply_tv_time);
      mContentLayout = (LinearLayout) itemView.findViewById(R.id.item_interaction_reply_content_layout);
      tvReply = (TextView) itemView.findViewById(R.id.item_interaction_reply_tv_reply);
      mContentLayout.setOnClickListener(this);
      initConfig();
    }

    private void initConfig() {
      int deleteDrawableRes = InteractionConfig.getInstance().getButtonDrawableRes();
      if (deleteDrawableRes != -1) {
        ColorStateList stateList = new ColorStateList(
            new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{0}},
            new int[]{deleteDrawableRes, R.color.cm_tv_black1});
        tvReply.setTextColor(stateList);
      }
    }

    @Override
    public void onClick(View view) {
      if (listener != null) {
        listener.onContentClick(mList.get(getLayoutPosition()).trendsId);
      }
    }
  }
}
