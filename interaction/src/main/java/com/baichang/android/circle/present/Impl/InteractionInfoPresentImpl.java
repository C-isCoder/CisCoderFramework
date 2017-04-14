package com.baichang.android.circle.present.Impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.baichang.android.circle.R;
import com.baichang.android.circle.InteractionContentFragment;
import com.baichang.android.circle.InteractionReportFragment;
import com.baichang.android.circle.InteractInteraction;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.circle.present.InteractionInfoPresent;
import com.baichang.android.circle.utils.UIUtil;
import com.baichang.android.circle.view.InteractionMeView;
import com.baichang.android.widget.circleImageView.CircleImageView;
import com.baichang.android.widget.magicIndicator.MagicIndicator;
import com.baichang.android.widget.magicIndicator.ViewPagerHelper;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.CommonNavigator;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import com.baichang.android.widget.magicIndicator.buildins.commonnavigator.titles.badge.BadgeRule;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by iCong on 2017/3/28.
 */

public class InteractionInfoPresentImpl implements InteractionInfoPresent {

  public static final int DYNAMIC = 0;
  public static final int REPORT = 1;
  public static final int COLLECT = 2;
  public static final int NORMAL = -1;

  private InteractionMeView mView;
  private InfoFragmentAdapter mAdapter;
  private InteractInteraction mInteraction;
  private boolean isOneself;

  public InteractionInfoPresentImpl(InteractionMeView view) {
    mView = view;
    mInteraction = new InteractInteractionImpl();
  }


  @Override
  public void onStart() {
    Glide.with(mView.getContext()).load(R.mipmap.interaction_icon_default).asBitmap()
        .transform(new BlurTransformation(mView.getContext()))
        .into(new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            mView.setBackground(new BitmapDrawable(mView.getContext().getResources(), resource));
          }
        });

  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void attachView(final ViewPager viewPager, MagicIndicator indicator,
      CircleImageView circleImageView) {
    viewPager.setAdapter(mAdapter);
    CommonNavigator commonNavigator = new CommonNavigator(viewPager.getContext());
    commonNavigator.setAdjustMode(true);
    commonNavigator.setAdapter(new NavigatorAdapter(viewPager));
    indicator.setNavigator(commonNavigator);
    ViewPagerHelper.bind(indicator, viewPager);
    circleImageView.setImageResource(R.mipmap.interaction_icon_default);
  }

  @Override
  public void setIsOneself(boolean isOneself) {
    this.isOneself = isOneself;
    mAdapter = new InfoFragmentAdapter(mView.getManager());
  }


  private class InfoFragmentAdapter extends FragmentPagerAdapter {

    InfoFragmentAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case DYNAMIC:
          if (isOneself) {
            return InteractionContentFragment.newInstance(DYNAMIC);
          } else {
            return InteractionContentFragment.newInstance(NORMAL);
          }
        case REPORT:
          return InteractionReportFragment.newInstance(REPORT, isOneself);
        case COLLECT:
          return InteractionContentFragment.newInstance(COLLECT);
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      if (isOneself) {
        return 3;
      } else {
        return 2;
      }
    }
  }

  private class NavigatorAdapter extends CommonNavigatorAdapter {

    ViewPager viewPager;
    Context context;

    NavigatorAdapter(ViewPager viewPager) {
      this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
      if (isOneself) {
        return 3;
      } else {
        return 2;
      }
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
      this.context = context;
      BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
      SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
      simplePagerTitleView.setNormalColor(Color.GRAY);

      int textColor = InteractionConfig.getInstance().getTextFontColor();
      if (textColor != -1) {
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, textColor));
      } else {
        simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.interaction_text_font));
      }
      switch (index) {
        case DYNAMIC:
          simplePagerTitleView.setText(getString(R.string.str_interaction_info_dynamic, "231"));
          break;
        case REPORT:
          simplePagerTitleView.setText(getString(R.string.str_interaction_info_report, "231"));
          if (isOneself) {
            ImageView badge = new ImageView(context);
            badge.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.interaction_ic_red_dot));
            badgePagerTitleView.setBadgeView(badge);
            badgePagerTitleView.setAutoCancelBadge(true);
            badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
            badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
          }
          break;
        case COLLECT:
          simplePagerTitleView.setText(getString(R.string.str_interaction_info_collect, "231"));
          break;
      }
      simplePagerTitleView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          viewPager.setCurrentItem(index);
        }
      });
      simplePagerTitleView.setTextSize(14);
      badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
      return badgePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
      LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
      linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
      linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 36));
      linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 1));

      int textColor = InteractionConfig.getInstance().getTextFontColor();
      if (textColor != -1) {
        linePagerIndicator.setColors(ContextCompat.getColor(context, textColor));
      } else {
        linePagerIndicator.setColors(ContextCompat.getColor(context, R.color.interaction_text_font));
      }
      return linePagerIndicator;
    }

    private String getString(int resId, Object... args) {
      return context.getString(resId, args);
    }
  }

}
