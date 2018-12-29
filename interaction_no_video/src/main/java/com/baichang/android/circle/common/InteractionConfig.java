package com.baichang.android.circle.common;

import android.app.Activity;
import com.baichang.android.circle.entity.InteractionUserData;

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
    private static int mBusinessBrandRes = -1;
    private static int mTitleColor = -1;
    private static int mPublishTitleColor = -1;
    private static boolean isNeedBusinessStore = false;
    private static boolean isNeedSetTitleHeight = false;
    private static boolean isNeedShowBusinessBrand = true;
    private static boolean isNeedShare = true;
    private static boolean isNeedShowCircleTitle = true;
    private static String mTitleText = null;
    private static InteractionUserData mUser;
    private static InteractionListener listener = null;
    private static String mInteractionUrl;
    private static int mDisplayWidth;
    private static boolean isNeedWeChatCircleDisplayMax6 = false;
    private static boolean isPublishNoTitle = false;

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

    @Override public int getTextFontColor() {
        return mTextFontColor;
    }

    @Override public int getTopBarColor() {
        return mTopBarColor;
    }

    @Override public int getButtonDrawableRes() {
        return mButtonDrawableRes;
    }

    @Override public int getShareDrawableRes() {
        return mShareDrawableRes;
    }

    @Override public int getCommentDrawableRes() {
        return mCommentDrawableRes;
    }

    @Override public int getPraiseDrawableRes() {
        return mPraiseDrawableRes;
    }

    @Override public int getCollectDrawableRes() {
        return mCollectDrawableRes;
    }

    @Override public int getBusinessDrawableRes() {
        return mBusinessDrawableRes;
    }

    @Override public int getBackDrawableRes() {
        return mBackDrawableResRes;
    }

    @Override public int getBusinessBrandRes() {
        return mBusinessBrandRes;
    }

    @Override public int getTitleColor() {
        return mTitleColor;
    }

    @Override public int getPublishTitleColor() {
        return mPublishTitleColor;
    }

    @Override public InteractionListener getListener() {
        return listener;
    }

    @Override public String getTitleText() {
        return mTitleText;
    }

    @Override public boolean isNeedBusinessStore() {
        return isNeedBusinessStore;
    }

    @Override public boolean isNeedSetTitleHeight() {
        return isNeedSetTitleHeight;
    }

    @Override public boolean isNeedShowBusinessBrand() {
        return isNeedShowBusinessBrand;
    }

    @Override public boolean isNeedShare() {
        return isNeedShare;
    }

    @Override public boolean isNeedShowCircleTitle() {
        return isNeedShowCircleTitle;
    }

    @Override public boolean isNeedWeChatCircleDisplayMax6() {
        return isNeedWeChatCircleDisplayMax6;
    }

    @Override public boolean isPublishNoTitle() {
        return isPublishNoTitle;
    }

    @Override public void share(Activity activity, String title, String summary, String url) {
        if (listener != null) {
            listener.share(activity, title, summary, url);
        }
    }

    @Override public void businessStore(String id) {
        if (listener != null) {
            listener.businessClick(id);
        }
    }

    @Override public InteractionUserData getUser() {
        return mUser;
    }

    @Override public String getInteractionUrl() {
        return mInteractionUrl;
    }

    @Override public int getDisplayDisplay() {
        return mDisplayWidth;
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

    public InteractionConfig setBackDrawableRes(int mBackDrawableResRes) {
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

    public InteractionConfig setPublishTitileColor(int color) {
        InteractionConfig.mPublishTitleColor = color;
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

    public InteractionConfig setIsPublishNoTitle(boolean isPublishNoTitle) {
        InteractionConfig.isPublishNoTitle = isNeedBusinessStore;
        return this;
    }

    public InteractionConfig setTitleColor(int titleColorRes) {
        InteractionConfig.mTitleColor = titleColorRes;
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

    public InteractionConfig setIsNeedSetTitleHeight(boolean isNeedSetTitleHeight) {
        InteractionConfig.isNeedSetTitleHeight = isNeedSetTitleHeight;
        return this;
    }

    public InteractionConfig setBusinessBrandRes(int businessBrandRes) {
        InteractionConfig.mBusinessBrandRes = businessBrandRes;
        return this;
    }

    public InteractionConfig setIsNeedShare(boolean isNeedShare) {
        InteractionConfig.isNeedShare = isNeedShare;
        return this;
    }

    public InteractionConfig setIsNeedShowCircleTitle(boolean isNeedShow) {
        InteractionConfig.isNeedShowCircleTitle = isNeedShow;
        return this;
    }

    public InteractionConfig setIsNeedWeChatCircleDisplayMax6(boolean isNeedShow) {
        InteractionConfig.isNeedWeChatCircleDisplayMax6 = isNeedShow;
        return this;
    }

    public InteractionConfig setInteractionUrl(String url) {
        InteractionConfig.mInteractionUrl = url;
        return this;
    }

    public InteractionConfig setIsNeedShowBusinessBrand(boolean isNeedShowBusinessBrand) {
        InteractionConfig.isNeedShowBusinessBrand = isNeedShowBusinessBrand;
        return this;
    }

    public InteractionConfig setDisplayWidth(int width) {
        InteractionConfig.mDisplayWidth = width;
        return this;
    }

    public InteractionConfig setUser(InteractionUserData userData) {
        InteractionConfig.mUser = userData;
        return this;
    }

    public interface InteractionListener {

        void share(Activity activity, String title, String summary, String url);

        void businessClick(String id);
    }
}
