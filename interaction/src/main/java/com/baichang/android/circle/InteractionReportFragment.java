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
import android.view.ViewGroup;
import com.baichang.android.circle.common.InteractionCommonFragment;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.common.InteractionFlag;
import com.baichang.android.circle.present.Impl.InteractionInfoReportPresentImpl;
import com.baichang.android.circle.present.InteractionInfoReportPresent;
import com.baichang.android.circle.view.InteractionOtherReportView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InteractionReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionReportFragment extends InteractionCommonFragment
    implements InteractionOtherReportView, OnRefreshListener {

  SwipeRefreshLayout mRefresh;
  RecyclerView rvList;

  private static final String ARG_PARAM = "param";
  private static final String ARG_PARAM_IS_ONE_SELF = "arg_param_is_one_self";
  private int mType;
  private boolean isOneSelf;
  private InteractionInfoReportPresent mPresent;

  public InteractionReportFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param Parameter 1.
   * @return A new instance of fragment InteractionReportFragment.
   */
  public static InteractionReportFragment newInstance(int param, boolean isOneSelf) {
    InteractionReportFragment fragment = new InteractionReportFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PARAM, param);
    args.putBoolean(ARG_PARAM_IS_ONE_SELF, isOneSelf);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mType = getArguments().getInt(ARG_PARAM);
      isOneSelf = getArguments().getBoolean(ARG_PARAM_IS_ONE_SELF, false);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.interaction_fragment_other_report, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.interaction_me_report_refresh);
    rvList = (RecyclerView) view.findViewById(R.id.interaction_me_report_rv_list);
    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      mRefresh.setColorSchemeResources(textColor);
    } else {
      mRefresh.setColorSchemeResources(R.color.interaction_text_font);
    }
    mRefresh.setOnRefreshListener(this);
    mPresent = new InteractionInfoReportPresentImpl(this);
    mPresent.setIsOneSelf(isOneSelf);
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
    //TODO 传值有问题
    Intent intent = new Intent(getActivity(), InteractionDetailActivity.class);
    intent.putExtra(InteractionFlag.ACTION_INTERACTION_ID, id);
    startActivity(intent);
  }
}
