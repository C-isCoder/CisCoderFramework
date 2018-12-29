package com.baichang.android.circle;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baichang.android.circle.common.InteractionCommonFragment;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.common.InteractionFlag;
import com.baichang.android.circle.common.InteractionFlag.Event;
import com.baichang.android.circle.dialog.InteractionReplyDialogFragment;
import com.baichang.android.circle.dialog.InteractionReplyDialogFragment.OnInputContentListener;
import com.baichang.android.circle.present.Impl.InteractionInfoReplyPresentImpl;
import com.baichang.android.circle.present.InteractionInfoReportPresent;
import com.baichang.android.circle.view.InteractionOtherReportView;
import com.baichang.android.common.BaseEventData;
import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InteractionReplyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionReplyFragment extends InteractionCommonFragment
    implements InteractionOtherReportView, OnRefreshListener {

  SwipeRefreshLayout mRefresh;
  RecyclerView rvList;

  private static final String ARG_PARAM_USER_ID = "arg_param_user_id";
  private String mUserId;
  private InteractionInfoReportPresent mPresent;

  public InteractionReplyFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment InteractionReplyFragment.
   */
  public static InteractionReplyFragment newInstance(String userId) {
    InteractionReplyFragment fragment = new InteractionReplyFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM_USER_ID, userId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mUserId = getArguments().getString(ARG_PARAM_USER_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.interaction_fragment_other_reply, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.interaction_me_reply_refresh);
    rvList = (RecyclerView) view.findViewById(R.id.interaction_me_reply_rv_list);
    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      mRefresh.setColorSchemeResources(textColor);
    } else {
      mRefresh.setColorSchemeResources(R.color.interaction_text_font);
    }
    mRefresh.setOnRefreshListener(this);
    mPresent = new InteractionInfoReplyPresentImpl(mUserId, this);
    mPresent.attachView(rvList);
  }

  @Override
  public void showProgressBar() {
    mRefresh.setRefreshing(true);
  }

  @Override
  public void hideProgressBar() {
    mRefresh.setRefreshing(false);
  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  public void onRefresh() {
    mPresent.refresh();
  }

  @Override
  public void onStart() {
    super.onStart();
    mPresent.onStart();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPresent.onDestroy();
  }

  @Override
  public void gotoDetail(int id) {
    Intent intent = new Intent(getActivity(), InteractionDetailActivity.class);
    intent.putExtra(InteractionFlag.ACTION_INTERACTION_ID, id);
    startActivity(intent);
  }

  @Override
  public void showReplyDialog(String name) {
    InteractionReplyDialogFragment dialog = new InteractionReplyDialogFragment();
    dialog.setEtContentHint(name);
    dialog.setOnInputContentListener(new OnInputContentListener() {
      @Override
      public void result(String content) {
        mPresent.reply(content);
      }
    });
    dialog.show(getFragmentManager(), "tag");
  }
}
