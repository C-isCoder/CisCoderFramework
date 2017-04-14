package com.baichang.android.circle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baichang.android.circle.R;
import com.baichang.android.circle.entity.InteractionModelData;
import java.util.List;

public class InteractionDialogAdapter extends
    RecyclerView.Adapter<InteractionDialogAdapter.ViewHolder> {

  private final List<InteractionModelData> mValues;

  public InteractionDialogAdapter(List<InteractionModelData> items, OnDialogItemClickListener listener) {
    mValues = items;
    setOnDialogItemClickListener(listener);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.interaction_dialog_fragment, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.mItem = mValues.get(position);
    holder.mContentView.setText(holder.mItem.name);
    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          listener.onItemClick(holder.mItem);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    final View mView;
    final TextView mContentView;
    private InteractionModelData mItem;

    ViewHolder(View view) {
      super(view);
      mView = view;
      mContentView = (TextView) view;
    }

    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }

  private OnDialogItemClickListener listener;

  public void setOnDialogItemClickListener(OnDialogItemClickListener listener) {
    this.listener = listener;
  }

  public interface OnDialogItemClickListener {

    void onItemClick(InteractionModelData data);
  }
}
