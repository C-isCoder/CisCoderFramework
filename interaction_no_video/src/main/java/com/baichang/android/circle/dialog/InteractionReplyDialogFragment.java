package com.baichang.android.circle.dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import com.baichang.android.circle.R;
import com.baichang.android.utils.BCDensityUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class InteractionReplyDialogFragment extends DialogFragment
    implements OnClickListener {

  private TextView tvCancel;
  private TextView tvSend;
  private EditText etContent;
  private String name;

  public InteractionReplyDialogFragment() {
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
  public void onResume() {
    super.onResume();
    Dialog dialog = getDialog();
    Window window = dialog.getWindow();
    if (window != null) {
      window.setLayout(BCDensityUtil.dip2px(getContext(), 320),
          BCDensityUtil.dip2px(getContext(), 120));
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.interaction_fragment_reply_dailog, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    tvCancel = (TextView) view.findViewById(R.id.interaction_reply_tv_cancel);
    tvSend = (TextView) view.findViewById(R.id.interaction_reply_tv_send);
    etContent = (EditText) view.findViewById(R.id.interaction_reply_et_report);
    tvCancel.setOnClickListener(this);
    tvSend.setOnClickListener(this);
    etContent.setHint(" 回复 " + name);
  }

  private OnInputContentListener listener;

  public void setOnInputContentListener(OnInputContentListener listener) {
    this.listener = listener;
  }

  public interface OnInputContentListener {

    void result(String content);
  }

  public void setEtContentHint(String name) {
    this.name = name;
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == tvCancel.getId()) {
      dismiss();
    } else if (id == tvSend.getId()
        && listener != null) {
      listener.result(etContent.getText().toString());
      dismiss();
    }
  }

}
