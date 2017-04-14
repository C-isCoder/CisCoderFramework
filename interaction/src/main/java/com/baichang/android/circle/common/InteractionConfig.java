package com.baichang.android.circle.common;

/**
 * Created by iCong on 2017/4/1.
 */

public class InteractionConfig implements InteractionConfigContract {

  private static InteractionConfig INSTANCE;

  private static int mTextFontColor = -1;
  private static int mTopBarColor = -1;
  private static int mLayoutBackground = -1;
  private static int mCommentDrawableRes = -1;
  private static int mShareDrawableRes = -1;
  private static int mPraiseDrawableRes = -1;
  private static int mButtonDrawableRes = -1;
  private static int mCollectDrawableRes = -1;
  private static int mBusinessDrawableRes = -1;
  private static int mBackDrawableResRes = -1;
  private static boolean isNeedBusinessStore = false;
  private static String mTitleText = null;

  private static InteractionListener listener = null;

  private InteractionConfig() {
  }

  public static InteractionConfig getInstance() {
    if (INSTANCE == null) {
      synchronized (InteractionConfig.class) {
        if (INSTANCE == null) {
          INSTANCE = new InteractionConfig();
        }
      }
    }
    return INSTANCE;
  }

  @Override
  public int getTextFontColor() {
    return mTextFontColor;
  }

  @Override
  public int getTopBarColor() {
    return mTopBarColor;
  }

  @Override
  public int getButtonDrawableRes() {
    return mButtonDrawableRes;
  }

  @Override
  public int getShareDrawableRes() {
    return mShareDrawableRes;
  }

  @Override
  public int getCommentDrawableRes() {
    return mCommentDrawableRes;
  }

  @Override
  public int getPraiseDrawableRes() {
    return mPraiseDrawableRes;
  }

  @Override
  public int getCollectDrawableRes() {
    return mCollectDrawableRes;
  }

  @Override
  public int getBusinessDrawableRes() {
    return mBusinessDrawableRes;
  }

  @Override
  public int getBackDrawableRes() {
    return mBackDrawableResRes;
  }

  @Override
  public InteractionListener getListener() {
    return listener;
  }

  @Override
  public String getTitleText() {
    return mTitleText;
  }

  @Override
  public boolean isNeedBusinessStore() {
    return isNeedBusinessStore;
  }

  @Override
  public void share(String title, String summary, String url) {
    if (listener != null) {
      listener.share(title, summary, url);
    }
  }

  @Override
  public void businessStore() {
    if (listener != null) {
      listener.businessClick();
    }
  }

  public InteractionConfig setTextFontColor(int mTextFontColor) {
    InteractionConfig.mTextFontColor = mTextFontColor;
    return this;
  }

  public InteractionConfig setTopBarColor(int mTopBarColor) {
    InteractionConfig.mTopBarColor = mTopBarColor;
    return this;
  }

  public InteractionConfig setButtonDrawableRes(int mButtonDrawableRes) {
    InteractionConfig.mButtonDrawableRes = mButtonDrawableRes;
    return this;
  }

  public InteractionConfig setBackDrawableResRes(int mBackDrawableResRes) {
    InteractionConfig.mBackDrawableResRes = mBackDrawableResRes;
    return this;
  }

  public InteractionConfig setCommentDrawableRes(int mCommentDrawableRes) {
    InteractionConfig.mCommentDrawableRes = mCommentDrawableRes;
    return this;
  }

  public InteractionConfig setShareDrawableRes(int mShareDrawableRes) {
    InteractionConfig.mShareDrawableRes = mShareDrawableRes;
    return this;
  }

  public InteractionConfig setPraiseDrawableRes(int mPraiseDrawableRes) {
    InteractionConfig.mPraiseDrawableRes = mPraiseDrawableRes;
    return this;
  }

  public InteractionConfig setCollectDrawableRes(int mCollectDrawableRes) {
    InteractionConfig.mCollectDrawableRes = mCollectDrawableRes;
    return this;
  }

  public InteractionConfig setLayoutBackground(int mLayoutBackground) {
    InteractionConfig.mLayoutBackground = mLayoutBackground;
    return this;
  }

  public InteractionConfig setIsNeedBusinessStore(boolean isNeedBusinessStore) {
    InteractionConfig.isNeedBusinessStore = isNeedBusinessStore;
    return this;
  }

  public InteractionConfig setBusinessDrawableRes(int mBusinessDrawableRes) {
    InteractionConfig.mBusinessDrawableRes = mBusinessDrawableRes;
    return this;
  }

  public InteractionConfig setTitleText(String titleText) {
    InteractionConfig.mTitleText = titleText;
    return this;
  }

  public InteractionConfig setListener(InteractionListener listener) {
    InteractionConfig.listener = listener;
    return this;
  }

  public interface InteractionListener {

    void share(String title, String summary, String url);

    void businessClick();
  }
}
