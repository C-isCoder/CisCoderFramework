package com.baichang.android.circle.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.baichang.android.circle.BuildConfig;
import com.baichang.android.circle.R;
import com.baichang.android.circle.entity.InteractionUserData;
import com.baichang.android.common.BCApplication;
import com.baichang.android.config.Configuration;
import com.baichang.android.config.ConfigurationImpl;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by iCong. Time:2016/11/25-14:48.
 */

public class InteractionApplication extends BCApplication implements Configuration {

  //token
  private static String TOKEN = "";
  //user
  private static InteractionUserData USER = null;

  @Override
  public void onCreate() {
    super.onCreate();
    //配置URL TOKEN
    ConfigurationImpl.init(this);
    initLogger();
    //互动 配置
//    InteractionConfig.getInstance()
//        .setTextFontColor(R.color.cm_btn_orange_yellow)
//        .setTopBarColor(R.color.cm_btn_orange_n)
//        .setTitleText("互动")
//        .setIsNeedBusinessStore(true)
//        .setListener(new InteractionListener() {
//          @Override
//          public void share(String title, String summary, String url) {
//            Toast.makeText(getInstance(), "分享", Toast.LENGTH_SHORT).show();
//          }
//
//          @Override
//          public void businessClick() {
//            Toast.makeText(getInstance(), "商家详情", Toast.LENGTH_SHORT).show();
//          }
//        });
  }

  private void initLogger() {
    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .tag("REQUEST")
            .build();
    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
      @Override public boolean isLoggable(int priority, @Nullable String tag) {
        return BuildConfig.DEBUG;
      }
    });
  }

  public static void setToken(String Token) {
    TOKEN = Token;
  }

  public static void setUser(InteractionUserData user) {
    USER = user;
  }

  public InteractionUserData getUser() {
    if (USER == null) {
      USER = InteractionDiskCache.getUser();
    }
    return USER;
  }

  @Override
  public String getApiDefaultHost() {
    return InteractionAPIConstants.API_DEFAULT_HOST;
  }

  @Override
  public String getApiWebView() {
    return "";
  }

  @Override
  public Context getAppContext() {
    return getApplicationContext();
  }

  @Override
  public String getApiUploadImage() {
    return InteractionAPIConstants.API_UPLOAD_IMAGE;
  }

  @Override
  public String getApiLoadImage() {
    return InteractionAPIConstants.API_LOAD_IMAGE;
  }

  @Override
  public String getToken() {
    return TextUtils.isEmpty(TOKEN) ? InteractionDiskCache.getToken() : TOKEN;
  }

  @Override
  public String getApiDownload() {
    return "";
  }

  @Override
  public String getApiUpload() {
    return InteractionAPIConstants.API_UPLOAD_IMAGE;
  }

  @Override
  public void refreshToken() {

  }

  @Override
  public int getAppBarColor() {
    return InteractionConfig.getInstance().getTextFontColor() == -1 ?
        R.color.interaction_text_font : InteractionConfig.getInstance().getTextFontColor();
  }

}
