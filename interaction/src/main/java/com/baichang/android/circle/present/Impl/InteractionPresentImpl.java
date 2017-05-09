package com.baichang.android.circle.present.Impl;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractionPresentImpl implements InteractionPresent,
    OnClickListener, BaseListener<List<InteractionTypeData>> {

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

  @Override
  public void onDestroy() {
    mView = null;
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onStart() {
    if (mTypeList.isEmpty()) {
      mView.showProgressBar();
      mInteraction.getInteractionTypeList(this);
    }
  }

  @Override
  public void attachView(View contentView) {
    mViewPager = (ViewPager) contentView.findViewById(R.id.interaction_view_pager);
    mTabLayout = (TabLayout) contentView.findViewById(R.id.interaction_tab_layout);
    tvMe = (TextView) contentView.findViewById(R.id.interaction_tv_me);
    mFloating = (FloatingActionButton) contentView.findViewById(R.id.interaction_floating_btn_publish);
    tvMe.setOnClickListener(this);
    mFloating.setOnClickListener(this);

    initColor(contentView);

    mViewPager.setAdapter(mAdapter);
    mTabLayout.setupWithViewPager(mViewPager);
  }

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == mFloating.getId()) {
      Intent publishIntent = new Intent(v.getContext(),
          InteractionPublishActivity.class);
      v.getContext().startActivity(publishIntent);
    } else if (i == tvMe.getId()) {
      AnimatorUtil.scale(v);
      Intent infoIntent = new Intent(v.getContext(),
          InteractionInfoActivity.class);
      infoIntent.putExtra(InteractionFlag.ACTION_INTERACTION_USER_ID,
          InteractionConfig.getInstance().getUser().id);
      infoIntent.putExtra(InteractionFlag.ACTION_INTERACTION_IS_ONESELF, true);
      v.getContext().startActivity(infoIntent);
    }
  }

  @Override
  public void success(List<InteractionTypeData> list) {
    mView.hideProgressBar();
    mTypeList.addAll(list);
    if (mTypeList.size() > 4) {
      mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    } else {
      mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
    mAdapter.notifyDataSetChanged();
  }

  @Override
  public void error(String error) {
    mView.hideProgressBar();
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
    }
  }

  private class InteractionAdapter extends FragmentPagerAdapter {

    InteractionAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return InteractionContentFragment.newInstance(
          mTypeList.get(position).id,
          InteractionInfoPresent.NORMAL);
    }

    @Override
    public int getCount() {
      return mTypeList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mTypeList.get(position).name;
    }
  }

  private void initColor(View contentView) {

    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      tvMe.setTextColor(textColor);
      mFloating.setBackgroundTintList(ColorUtil.createdPressColorList(
          contentView.getContext(), textColor, textColor));
      mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(
          contentView.getContext(), textColor));
      mTabLayout.setTabTextColors(ColorUtil.createdSelectColorList(
          contentView.getContext(), textColor, R.color.cm_tv_black1));
    }

    int topColor = InteractionConfig.getInstance().getTopBarColor();
    if (topColor != -1) {
      RelativeLayout title = (RelativeLayout) contentView.findViewById(R.id.title);
      title.setBackgroundResource(topColor);
      TextView tvTitle = (TextView) contentView.findViewById(R.id.interaction_tv_title);
      tvTitle.setTextColor(Color.WHITE);

      String titleText = InteractionConfig.getInstance().getTitleText();
      if (!TextUtils.isEmpty(titleText)) {
        tvTitle.setText(titleText);
      }
    }
  }
}
