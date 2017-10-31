package com.baichang.android.circle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baichang.android.circle.common.InteractionCommonActivity;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.common.InteractionFlag;
import com.baichang.android.circle.present.Impl.InteractionDetailPresentImpl;
import com.baichang.android.circle.present.InteractionDetailPresent;
import com.baichang.android.circle.utils.AnimatorUtil;
import com.baichang.android.circle.view.InteractionDetailView;
import com.baichang.android.utils.BCToolsUtil;

public class InteractionDetailActivity extends InteractionCommonActivity
    implements InteractionDetailView, OnRefreshListener, OnClickListener, OnTouchListener,
    OnLayoutChangeListener {

  SwipeRefreshLayout mRefresh;
  RecyclerView rvList;
  TextView tvSend;
  EditText etReport;
  ImageButton btnShare;
  ImageButton btnCollect;
  ImageButton mBack;
  ImageButton btnPraise;
  LinearLayout mCommentLayout;
  private InteractionDetailPresent mPresent;
  private int[] locations = new int[2];
  private int initHeight;
  private boolean isFirst = true;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.interaction_activity_detail);
    mRefresh = (SwipeRefreshLayout) findViewById(R.id.interaction_detail_refresh);
    rvList = (RecyclerView) findViewById(R.id.interaction_detail_rv_list);
    tvSend = (TextView) findViewById(R.id.interaction_me_reply_tv_send);
    etReport = (EditText) findViewById(R.id.interaction_me_reply_et_report);
    btnShare = (ImageButton) findViewById(R.id.interaction_detail_btn_share);
    btnCollect = (ImageButton) findViewById(R.id.interaction_detail_btn_collect);
    btnPraise = (ImageButton) findViewById(R.id.interaction_detail_iv_praise);
    mBack = (ImageButton) findViewById(R.id.back);
    mCommentLayout = (LinearLayout) findViewById(R.id.interaction_detail_comment_layout);

    initLayoutChange();
    btnShare.setOnClickListener(this);
    btnCollect.setOnClickListener(this);
    btnPraise.setOnClickListener(this);
    init();
  }

  private void initLayoutChange() {
    getWindow().getDecorView().addOnLayoutChangeListener(this);
  }

  private void init() {
    View mHeaderView =
        getLayoutInflater().inflate(R.layout.interaction_activity_detail_header_layout, null);
    int id = getIntent().getIntExtra(InteractionFlag.ACTION_INTERACTION_ID, -1);
    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      mRefresh.setColorSchemeResources(textColor);
    } else {
      mRefresh.setColorSchemeResources(R.color.interaction_text_font);
    }
    mRefresh.setOnRefreshListener(this);
    tvSend.setOnClickListener(this);
    rvList.setOnTouchListener(this);

    setConfig();
    mPresent = new InteractionDetailPresentImpl(id, this);
    mPresent.attachView(rvList, mHeaderView);
  }

  private void setConfig() {
    btnShare.setVisibility(
        InteractionConfig.getInstance().isNeedShare() ? View.VISIBLE : View.GONE);
    int topColor = InteractionConfig.getInstance().getTopBarColor();
    if (topColor != -1) {
      RelativeLayout title = (RelativeLayout) findViewById(R.id.title);
      title.setBackgroundResource(topColor);
      TextView tvTitle = (TextView) findViewById(R.id.interaction_top_tv_title);
      if (topColor != Color.WHITE) {
        tvTitle.setTextColor(Color.WHITE);
      }
    }
    int praiseDrawableRes = InteractionConfig.getInstance().getPraiseDrawableRes();
    if (praiseDrawableRes != -1) {
      btnPraise.setImageResource(praiseDrawableRes);
    }
    int collectDrawableRes = InteractionConfig.getInstance().getCollectDrawableRes();
    if (collectDrawableRes != -1) {
      btnCollect.setImageResource(collectDrawableRes);
    }
    int shareDrawableRes = InteractionConfig.getInstance().getShareDrawableRes();
    if (shareDrawableRes != -1) {
      btnShare.setImageResource(shareDrawableRes);
    }
    int backDrawableRes = InteractionConfig.getInstance().getBackDrawableRes();
    if (backDrawableRes != -1) {
      mBack.setImageResource(backDrawableRes);
    }
  }

  @Override public void showProgressBar() {
    mRefresh.setRefreshing(true);
  }

  @Override public void hideProgressBar() {
    mRefresh.setRefreshing(false);
  }

  @Override public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override protected void onStart() {
    super.onStart();
    mPresent.onStart();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mPresent.onDestroy();
  }

  @Override public Context getContext() {
    return this;
  }

  @Override public Activity getActivity() {
    return this;
  }

  @Override public void setReportHint(String tips) {
    etReport.setText("");
    etReport.setHint(tips);
  }

  @Override public void showInputKeyBord() {
    etReport.requestFocus();
    BCToolsUtil.openKeybord(etReport, this);
  }

  @Override public void hideInputKeyBord() {
    etReport.clearFocus();
    BCToolsUtil.closeKeybord(etReport, this);
  }

  @Override public void scrollToPosition(int position) {
    rvList.smoothScrollToPosition(position);
  }

  @Override public void setCollectState(boolean isCollect) {
    btnCollect.setSelected(isCollect);
  }

  @Override public void setPraiseState(boolean isPraise) {
    btnPraise.setSelected(isPraise);
  }

  @Override public void onRefresh() {
    mPresent.refresh();
  }

  @Override public void onClick(View v) {
    int id = v.getId();
    if (id == btnCollect.getId()) {
      AnimatorUtil.scale(v).addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationEnd(Animator animation) {
          btnCollect.setSelected(!btnCollect.isSelected());
        }
      });
      mPresent.collect();
    } else if (id == btnShare.getId()) {
      AnimatorUtil.scale(v);
      mPresent.share();
    } else if (id == tvSend.getId()) {
      mPresent.send(etReport.getText().toString());
    } else if (id == btnPraise.getId()) {
      AnimatorUtil.scale(v);
      btnPraise.setSelected(!btnPraise.isSelected());
      mPresent.praise();
    }
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_MOVE:
        hideInputKeyBord();
        etReport.clearFocus();
        break;
    }
    return false;
  }

  @Override public void back(View view) {
    onBackPressed();
  }

  @Override
  public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
      int oldTop, int oldRight, int oldBottom) {
    if (isFirst) {
      mCommentLayout.getLocationOnScreen(locations);
      initHeight = locations[1];
    }
    isFirst = false;
    int[] currentLocation = new int[2];
    mCommentLayout.getLocationOnScreen(currentLocation);
    int currentX = currentLocation[1];
    //Log.d("CID", "init:" + locations[0] + "-" + locations[1]);
    //Log.d("CID", "current:" + currentLocation[0] + "-" + currentLocation[1]);
    if (initHeight > currentX) {
      //Log.d("CID", "Open");
      tvSend.setVisibility(View.VISIBLE);
      btnPraise.setVisibility(View.GONE);
    } else {
      //Log.d("CId", "Close");
      tvSend.setVisibility(View.GONE);
      btnPraise.setVisibility(View.VISIBLE);
    }
  }
}
