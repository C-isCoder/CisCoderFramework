package com.baichang.android.circle.adapter;

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
import com.baichang.android.circle.adapter.InteractionInfoReportAdapter.Holder;
import com.baichang.android.circle.entity.InteractionOtherReportData;
import com.baichang.android.widget.circleImageView.CircleImageView;
import java.util.List;

/**
 * Created by iCong on 2017/3/28.
 */

public class InteractionInfoReportAdapter extends RecyclerView.Adapter<Holder> {

  private List<InteractionOtherReportData> mList;
  private boolean isOneself;

  public InteractionInfoReportAdapter(OnItemContentClickListener listener) {
    setOnItemContentClickListener(listener);
  }

  @Override
  public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new Holder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.interaction_item_report_layout, parent, false));
  }

  @Override
  public void onBindViewHolder(Holder holder, int position) {
    InteractionOtherReportData data = mList.get(position);
    //TODO 替换图片
    holder.ivImage.setImageResource(R.mipmap.interaction_icon_default);
    holder.ivAvatar.setImageResource(R.mipmap.interaction_icon_default);
    holder.tvName.setText(data.name);
    holder.tvContent.setText(data.content);
    holder.tvTime.setText(data.time);
    holder.tvSummary.setText(Html.fromHtml(
        String.format("<font color=\"#67c4fe\">%s</font>,<font color=\"#666666\">%s</font>",
            data.businessName, data.summary)));
    holder.tvMe.setVisibility(isOneself ? View.VISIBLE : View.GONE);
    holder.tvMe.setText(isOneself ? "我：jxxx" : "");
  }

  @Override
  public int getItemCount() {
    return mList == null ? 0 : mList.size();
  }

  public void setData(List<InteractionOtherReportData> list) {
    mList = list;
    notifyDataSetChanged();
  }

  public void addData(List<InteractionOtherReportData> list) {
    mList.addAll(list);
    notifyDataSetChanged();
  }

  public void setOneself(boolean isOneself) {
    this.isOneself = isOneself;
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

    Holder(View itemView) {
      super(itemView);
      ivAvatar = (CircleImageView) itemView.findViewById(R.id.item_interaction_report_iv_avatar);
      ivImage = (ImageView) itemView.findViewById(R.id.item_interaction_report_iv_image);
      tvContent = (TextView) itemView.findViewById(R.id.item_interaction_report_tv_content);
      tvName = (TextView) itemView.findViewById(R.id.item_interaction_report_tv_name);
      tvSummary = (TextView) itemView.findViewById(R.id.item_interaction_report_tv_summary);
      tvTime = (TextView) itemView.findViewById(R.id.item_interaction_report_tv_time);
      mContentLayout = (LinearLayout) itemView.findViewById(R.id.item_interaction_report_content_layout);
      tvMe = (TextView) itemView.findViewById(R.id.item_interaction_report_tv_me);
      mContentLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      if (listener != null) {
        listener.onContentClick(mList.get(getLayoutPosition()).id);
      }
    }
  }
}
