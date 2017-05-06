package com.baichang.android.circle;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baichang.android.circle.adapter.VerticalDividerDecoration;
import com.baichang.android.circle.common.InteractionCommonFragment;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.common.InteractionFlag;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.present.Impl.InteractionContentPresentImpl;
import com.baichang.android.circle.present.InteractionContentPresent;
import com.baichang.android.circle.view.InteractionContentView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InteractionContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InteractionContentFragment extends InteractionCommonFragment
    implements OnRefreshListener, InteractionContentView {

  SwipeRefreshLayout mRefreshLayout;
  RecyclerView rvList;

  private static final String ARG_PARAM_MODEL = "arg_param_model";
  private static final String ARG_PARAM_TYPE = "arg_param_type";
  private static final String ARG_PARAM_USER_ID = "arg_param_user_id";

  private int mModelType;
  private int mTypeId = -1;
  private InteractionContentPresent mPresent;
  private String mUserId;

  public InteractionContentFragment() {
    // Required empty public constructor
  }

  public static InteractionContentFragment newInstance(String userId, int modeType) {
    InteractionContentFragment fragment = new InteractionContentFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PARAM_MODEL, modeType);
    args.putString(ARG_PARAM_USER_ID, userId);
    fragment.setArguments(args);
    return fragment;
  }

  public static InteractionContentFragment newInstance(int typeId, int modeType) {
    InteractionContentFragment fragment = new InteractionContentFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PARAM_TYPE, typeId);
    args.putInt(ARG_PARAM_MODEL, modeType);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mModelType = getArguments().getInt(ARG_PARAM_MODEL);
      mTypeId = getArguments().getInt(ARG_PARAM_TYPE, -1);
      mUserId = getArguments().getString(ARG_PARAM_USER_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.interaction_fragment_content, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.interaction_content_refresh);
    rvList = (RecyclerView) view.findViewById(R.id.interaction_content_rv_list);

    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      mRefreshLayout.setColorSchemeResources(textColor);
    } else {
      mRefreshLayout.setColorSchemeResources(R.color.interaction_text_font);
    }
    mRefreshLayout.setOnRefreshListener(this);
    rvList.addItemDecoration(
        new VerticalDividerDecoration(ContextCompat.getColor(getContext(),
            R.color.interaction_layout_background)));
    mPresent = new InteractionContentPresentImpl(this, mModelType);
    mPresent.attachRecyclerView(rvList);
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
  public void showProgressBar() {
    mRefreshLayout.setRefreshing(true);
  }

  @Override
  public void hideProgressBar() {
    mRefreshLayout.setRefreshing(false);
  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  public void gotoDetail(InteractionListData data) {
    Intent intent = new Intent(getActivity(), InteractionDetailActivity.class);
    intent.putExtra(InteractionFlag.ACTION_INTERACTION_ID, data.id);
    startActivity(intent);
  }

  @Override
  public void gotoInfo(boolean isOneself, String userId) {
    if (TextUtils.isEmpty(userId)) {
      return;
    }
    Intent intent = new Intent(getActivity(), InteractionInfoActivity.class);
    intent.putExtra(InteractionFlag.ACTION_INTERACTION_IS_ONESELF, isOneself);
    intent.putExtra(InteractionFlag.ACTION_INTERACTION_USER_ID, userId);
    startActivity(intent);
  }

  @Override
  public int getTypeId() {
    return mTypeId;
  }

  @Override
  public String getUserId() {
    return mUserId;
  }

}
