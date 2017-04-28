package com.baichang.android.circle.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baichang.android.circle.R;
import com.baichang.android.circle.entity.InteractionTypeData;
import java.util.ArrayList;
import java.util.List;

public class InteractionDialogAdapter extends
    RecyclerView.Adapter<InteractionDialogAdapter.ViewHolder> {

  private List<InteractionTypeData> mValues = new ArrayList<>();

  public InteractionDialogAdapter(OnDialogItemClickListener listener) {
    setOnDialogItemClickListener(listener);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.interaction_dialog_fragment, parent, false);
    return new ViewHolder(view);
  }

  public void setData(List<InteractionTypeData> list) {
    if (!mValues.isEmpty()) {
      mValues.clear();
    }
    mValues.addAll(list);
    notifyDataSetChanged();
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
    Log.d("CID", holder.mItem.toString());
    Log.d("CID", holder.toString());
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    final View mView;
    final TextView mContentView;
    private InteractionTypeData mItem;

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

    void onItemClick(InteractionTypeData data);
  }
}
