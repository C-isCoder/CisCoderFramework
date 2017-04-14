package com.baichang.android.circle.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baichang.android.circle.R;
import com.baichang.android.circle.adapter.InteractionDialogAdapter;
import com.baichang.android.circle.adapter.InteractionDialogAdapter.OnDialogItemClickListener;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.entity.InteractionModelData;
import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * interface.
 */
public class InteractionDialogFragment extends DialogFragment
    implements OnDialogItemClickListener {

  private List<InteractionModelData> mList;
  private static OnDialogItemClickListener mListener;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public InteractionDialogFragment() {
  }

  public static InteractionDialogFragment Instance(OnDialogItemClickListener listener) {
    mListener = listener;
    return new InteractionDialogFragment();
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //TODO 模拟数据
    mList = new ArrayList<>();
    mList.add(new InteractionModelData("1", "热门互动"));
    mList.add(new InteractionModelData("2", "经典老车"));
    mList.add(new InteractionModelData("3", "海外购车"));
    mList.add(new InteractionModelData("4", "现身说法"));
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.interaction_dialog_fragment_item,
        container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    RecyclerView recycler = (RecyclerView) view.findViewById(
        R.id.interaction_fragment_dialog_rv_list);
    recycler.setAdapter(new InteractionDialogAdapter(mList, this));

    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      View title = view.findViewById(R.id.interaction_dialog_view);
      title.setBackgroundResource(textColor);
    }
  }

  @Override
  public void onItemClick(InteractionModelData data) {
    if (mListener != null) {
      mListener.onItemClick(data);
    }
    dismiss();
  }
}
