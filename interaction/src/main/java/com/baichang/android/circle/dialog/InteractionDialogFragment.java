package com.baichang.android.circle.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import com.baichang.android.circle.R;
import com.baichang.android.circle.adapter.InteractionDialogAdapter;
import com.baichang.android.circle.adapter.InteractionDialogAdapter.OnDialogItemClickListener;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.entity.InteractionTypeData;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * interface.
 */
public class InteractionDialogFragment extends DialogFragment
    implements OnDialogItemClickListener, BaseListener<List<InteractionTypeData>> {

  private static OnDialogItemClickListener mListener;
  private InteractionDialogAdapter mAdapter;

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

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    Window window = dialog.getWindow();
    if (window != null) {
      window.requestFeature(Window.FEATURE_NO_TITLE);
    }
    return dialog;
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
    mAdapter = new InteractionDialogAdapter(this);
    recycler.setAdapter(mAdapter);
    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      View title = view.findViewById(R.id.interaction_dialog_view);
      title.setBackgroundResource(textColor);
    }
    new InteractInteractionImpl().getInteractionTypeList(this);
  }

  @Override
  public void onItemClick(InteractionTypeData data) {
    if (mListener != null) {
      mListener.onItemClick(data);
    }
    dismiss();
  }

  @Override
  public void success(List<InteractionTypeData> list) {
    mAdapter.setData(list);
  }

  @Override
  public void error(String error) {
    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
  }
}
