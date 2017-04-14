package com.baichang.android.circle.common;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.baichang.android.circle.common.InteractionConfig.InteractionListener;
import com.baichang.android.common.BCApplication;
import com.baichang.android.config.Configuration;
import com.baichang.android.config.ConfigurationImpl;
import com.baichang.android.circle.BuildConfig;
import com.baichang.android.circle.R;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;


/**
 * Created by iCong. Time:2016/11/25-14:48.
 */

public class InteractionApplication extends BCApplication implements Configuration {

  //token
  private static String TOKEN = "";

  @Override
  public void onCreate() {
    super.onCreate();
    //配置URL TOKEN
    ConfigurationImpl.init(this);
    //互动 配置
    InteractionConfig.getInstance()
        .setTextFontColor(R.color.cm_btn_orange_yellow)
        .setTopBarColor(R.color.cm_btn_orange_n)
        .setTitleText("互动")
        .setIsNeedBusinessStore(true)
        .setListener(new InteractionListener() {
          @Override
          public void share(String title, String summary, String url) {
            Toast.makeText(getInstance(), "分享", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void businessClick() {
            Toast.makeText(getInstance(), "商家详情", Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void initLogger() {
    Logger.init("BC-LOG")                 // default PRETTYLOGGER or use just init()
        .methodCount(0)                 // default 2
        .hideThreadInfo()               // default shown
        .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);// default LogLevel.FULL
  }

  public static void setToken(String Token) {
    TOKEN = Token;
  }

  @Override
  public String getApiDefaultHost() {
    return InteractionAPIConstants.API_DEFAULT_HOST;
  }

  @Override
  public String getApiWebView() {
    return InteractionAPIConstants.API_WEB_VIEW;
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
    return InteractionAPIConstants.API_DOWNLOAD;
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
