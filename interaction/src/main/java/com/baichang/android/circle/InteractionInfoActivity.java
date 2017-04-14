package com.baichang.android.circle;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.baichang.android.circle.common.InteractionCommonActivity;
import com.baichang.android.circle.common.InteractionConfig.InteractionListener;
import com.baichang.android.circle.common.InteractionFlag;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.present.Impl.InteractionInfoPresentImpl;
import com.baichang.android.circle.present.InteractionInfoPresent;
import com.baichang.android.circle.view.InteractionMeView;
import com.baichang.android.common.BaseActivity;
import com.baichang.android.widget.circleImageView.CircleImageView;
import com.baichang.android.widget.magicIndicator.MagicIndicator;

public class InteractionInfoActivity extends BaseActivity
    implements InteractionMeView, OnClickListener {

  CircleImageView ivAvatar;
  AppBarLayout mBlur;
  MagicIndicator mIndicator;
  ViewPager mViewPager;
  ContentLoadingProgressBar mProgress;
  TextView tvBusiness;

  private InteractionInfoPresent mPresent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setSystemBarColor(R.color.cm_transparent);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.interaction_activity_info);
    ivAvatar = (CircleImageView) findViewById(R.id.interaction_info_iv_avatar);
    mBlur = (AppBarLayout) findViewById(R.id.interaction_info_barLayout);
    mIndicator = (MagicIndicator) findViewById(R.id.interaction_info_indicator);
    mViewPager = (ViewPager) findViewById(R.id.interaction_info_viewPager);
    mProgress = (ContentLoadingProgressBar) findViewById(R.id.interaction_info_progress);
    initConfig();
    init();
  }

  @Override
  public void back(View view) {
    onBackPressed();
  }

  private void initConfig() {
    if (InteractionConfig.getInstance().isNeedBusinessStore()) {
      tvBusiness = (TextView) findViewById(R.id.interaction_other_tv_business);
      tvBusiness.setOnClickListener(this);
      tvBusiness.setVisibility(View.VISIBLE);

      int textColor = InteractionConfig.getInstance().getTextFontColor();
      if (textColor != -1) {
        tvBusiness.setTextColor(textColor);
        int businessDrawableRes = InteractionConfig.getInstance().getBusinessDrawableRes();
        if (businessDrawableRes != -1) {
          tvBusiness.setBackgroundResource(businessDrawableRes);
        }
      }
    }
  }

  private void init() {
    boolean isOneself = getIntent().getBooleanExtra(
        InteractionFlag.ACTION_INTERACTION_IS_ONESELF, false);
    mPresent = new InteractionInfoPresentImpl(this);
    mPresent.setIsOneself(isOneself);
    mPresent.attachView(mViewPager, mIndicator, ivAvatar);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mPresent.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresent.onDestroy();
  }

  @Override
  public void showProgressBar() {
    mProgress.show();
  }

  @Override
  public void hideProgressBar() {
    mProgress.hide();
  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  public FragmentManager getManager() {
    return getSupportFragmentManager();
  }

  @Override
  public void setBackground(BitmapDrawable drawable) {
    mBlur.setBackground(drawable);
  }

  @Override
  public Context getContext() {
    return this;
  }

  @Override
  public void onClick(View view) {
    //TODO 跳转商城-商家详情
    InteractionListener listener = InteractionConfig.getInstance().getListener();
    if (listener != null) {
      listener.businessClick();
    }
  }
}
