package com.baichang.android.circle.present.Impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.baichang.android.circle.InteractionContentFragment;
import com.baichang.android.circle.InteractionInfoActivity;
import com.baichang.android.circle.InteractionPublishActivity;
import com.baichang.android.circle.R;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.common.InteractionFlag;
import com.baichang.android.circle.common.InteractionFlag.Event;
import com.baichang.android.circle.entity.InteractionTypeData;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.circle.model.InteractInteraction;
import com.baichang.android.circle.present.InteractionInfoPresent;
import com.baichang.android.circle.present.InteractionPresent;
import com.baichang.android.circle.utils.AnimatorUtil;
import com.baichang.android.circle.utils.ColorUtil;
import com.baichang.android.circle.view.InteractionView;
import com.baichang.android.common.BaseEventData;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import com.baichang.android.utils.BCDensityUtil;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractionPresentImpl
        implements InteractionPresent, OnClickListener, BaseListener<List<InteractionTypeData>> {

    private InteractionView mView;
    private FragmentPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private TextView tvMe;
    private FloatingActionButton mFloating;
    private InteractInteraction mInteraction;
    private ViewPager mViewPager;
    public static List<InteractionTypeData> mTypeList = new ArrayList<>();

    public InteractionPresentImpl(InteractionView view) {
        mView = view;
        mAdapter = new InteractionAdapter(mView.getFragmentManager());
        mInteraction = new InteractInteractionImpl();
        EventBus.getDefault().register(this);
    }

    @Override public void onDestroy() {
        mView = null;
        EventBus.getDefault().unregister(this);
    }

    @Override public void onStart() {
        if (mTypeList.isEmpty()) {
            mInteraction.getInteractionTypeList(this);
        }
        if (mTypeList.size() > 4) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    @Override public void attachView(View contentView) {
        mViewPager = (ViewPager) contentView.findViewById(R.id.interaction_view_pager);
        mTabLayout = (TabLayout) contentView.findViewById(R.id.interaction_tab_layout);
        tvMe = (TextView) contentView.findViewById(R.id.interaction_tv_me);
        mFloating = (FloatingActionButton) contentView.findViewById(
                R.id.interaction_floating_btn_publish);
        tvMe.setOnClickListener(this);
        mFloating.setOnClickListener(this);

        initColor(contentView);

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override public void jumpMe(Context context) {
        Intent infoIntent = new Intent(context, InteractionInfoActivity.class);
        infoIntent.putExtra(InteractionFlag.ACTION_INTERACTION_USER_ID,
                InteractionConfig.getInstance().getUser().id);
        infoIntent.putExtra(InteractionFlag.ACTION_INTERACTION_IS_ONESELF, true);
        context.startActivity(infoIntent);
    }

    @Override public void onClick(View v) {
        int i = v.getId();
        if (i == mFloating.getId()) {
            Intent publishIntent = new Intent(v.getContext(), InteractionPublishActivity.class);
            v.getContext().startActivity(publishIntent);
        } else if (i == tvMe.getId()) {
            AnimatorUtil.scale(v);
            jumpMe(v.getContext());
        }
    }

    @Override public void success(List<InteractionTypeData> list) {
        if (list.isEmpty()) {
            mTabLayout.setVisibility(View.GONE);
            return;
        }
        if (!mTypeList.isEmpty()) {
            mTypeList.clear();
        }
        mTypeList.addAll(list);
        if (mTypeList.size() > 4) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override public void error(String error) {
        mView.showMsg(error);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(BaseEventData<Integer, Integer> event) {
        if (event.key == Event.INTERACTION_JUMP_PAGE) {
            int typeId = event.value;
            int index = -1;
            for (InteractionTypeData typeData : mTypeList) {
                if (typeData.id == typeId) {
                    index = mTypeList.indexOf(typeData);
                }
            }
            if (index != -1) {
                mViewPager.setCurrentItem(index);
            }
        } else if (event.key == Event.INTERACTION_TYPE_REFRESH) {
            mInteraction.getInteractionTypeList(this);
        }
    }

    private class InteractionAdapter extends FragmentPagerAdapter {

        InteractionAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            // 类别未空的时候默认传0
            return InteractionContentFragment.newInstance(
                    mTypeList.isEmpty() ? 0 : mTypeList.get(position).id,
                    InteractionInfoPresent.NORMAL);
        }

        @Override public int getCount() {
            return mTypeList.isEmpty() ? 1 : mTypeList.size();
        }

        @Override public CharSequence getPageTitle(int position) {
            return mTypeList.isEmpty() ? "" : mTypeList.get(position).name;
        }
    }

    public abstract class FragmentPagerAdapter extends PagerAdapter {
        private static final String TAG = "FragmentPagerAdapter";
        private static final boolean DEBUG = false;

        private final FragmentManager mFragmentManager;
        private FragmentTransaction mCurTransaction = null;
        private Fragment mCurrentPrimaryItem = null;

        public FragmentPagerAdapter(FragmentManager fm) {
            mFragmentManager = fm;
        }

        public abstract Fragment getItem(int position);

        @Override public void startUpdate(ViewGroup container) {
            if (container.getId() == View.NO_ID) {
                throw new IllegalStateException(
                        "ViewPager with adapter " + this + " requires a view id");
            }
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }

            final long itemId = getItemId(position);

            // Do we already have this fragment?
            String name = makeFragmentName(container.getId(), itemId);
            Fragment fragment = mFragmentManager.findFragmentByTag(name);
            if (fragment != null && !fragment.isAdded()) {
                if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
                mCurTransaction.attach(fragment);
            } else {
                fragment = getItem(position);
                if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
                mCurTransaction.add(container.getId(), fragment,
                        makeFragmentName(container.getId(), itemId));
            }
            if (fragment != mCurrentPrimaryItem) {
                fragment.setMenuVisibility(false);
                fragment.setUserVisibleHint(false);
            }

            return fragment;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            if (DEBUG) {
                Log.v(TAG, "Detaching item #"
                        + getItemId(position)
                        + ": f="
                        + object
                        + " v="
                        + ((Fragment) object).getView());
            }
            mCurTransaction.detach((Fragment) object);
        }

        @Override public void setPrimaryItem(ViewGroup container, int position, Object object) {
            Fragment fragment = (Fragment) object;
            if (fragment != mCurrentPrimaryItem) {
                if (mCurrentPrimaryItem != null) {
                    mCurrentPrimaryItem.setMenuVisibility(false);
                    mCurrentPrimaryItem.setUserVisibleHint(false);
                }
                if (fragment != null) {
                    fragment.setMenuVisibility(true);
                    fragment.setUserVisibleHint(true);
                }
                mCurrentPrimaryItem = fragment;
            }
        }

        @Override public void finishUpdate(ViewGroup container) {
            if (mCurTransaction != null) {
                mCurTransaction.commitNowAllowingStateLoss();
                mCurTransaction = null;
            }
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return ((Fragment) object).getView() == view;
        }

        @Override public Parcelable saveState() {
            return null;
        }

        @Override public void restoreState(Parcelable state, ClassLoader loader) {
        }

        public long getItemId(int position) {
            return position;
        }

        private String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }
    }

    private void initColor(View contentView) {
        boolean isNeedShowCircleTitle = InteractionConfig.getInstance().isNeedShowCircleTitle();
        RelativeLayout title = (RelativeLayout) contentView.findViewById(R.id.title);
        if (isNeedShowCircleTitle) {
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }

        int textColor = InteractionConfig.getInstance().getTextFontColor();
        if (textColor != -1) {
            mFloating.setBackgroundTintList(
                    ColorUtil.createdPressColorList(contentView.getContext(), textColor,
                            textColor));
            mTabLayout.setSelectedTabIndicatorColor(
                    ContextCompat.getColor(contentView.getContext(), textColor));
            mTabLayout.setTabTextColors(
                    ColorUtil.createdSelectColorList(contentView.getContext(), textColor,
                            R.color.cm_tv_black1));
        }

        int topColor = InteractionConfig.getInstance().getTopBarColor();
        if (topColor != -1) {
            title.setBackgroundResource(topColor);
        }
        TextView tvTitle = (TextView) contentView.findViewById(R.id.interaction_tv_title);
        int color = InteractionConfig.getInstance().getTitleColor();
        if (color != -1) {
            tvTitle.setTextColor(ContextCompat.getColor(mView.getContext(), color));
            tvMe.setTextColor(ContextCompat.getColor(mView.getContext(), color));
        } else {
            tvTitle.setTextColor(Color.WHITE);
            tvMe.setTextColor(Color.WHITE);
        }
        String titleText = InteractionConfig.getInstance().getTitleText();
        if (!TextUtils.isEmpty(titleText)) {
            tvTitle.setText(titleText);
        }

        boolean isNeedSetTitleHeight = InteractionConfig.getInstance().isNeedSetTitleHeight();
        if (isNeedSetTitleHeight) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    BCDensityUtil.dip2px(title.getContext(), 72));
            title.setLayoutParams(params);
            title.setPadding(0, BCDensityUtil.dip2px(title.getContext(), 24), 0, 0);
        }
    }
}
