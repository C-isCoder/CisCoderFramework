package com.baichang.android.common;

import android.app.Application;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.baichang.android.common.cache.ACache;


public class BCApplication extends Application {

  private static BCApplication instance;
  //本地缓存
  public static ACache aCache;
  // 屏幕宽度
  public static int screenWidth;
  // 屏幕高度
  public static int screenHeight;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    init();
    initACache();
    //崩溃日志
    BCCrashHandler.getInstance().init(getApplicationContext());
  }

  private void init() {
    DisplayMetrics display = getDisplayMetrics();
    screenWidth = display.widthPixels;
    screenHeight = display.heightPixels;
  }

  private void initACache() {
    aCache = ACache.get(BCFolderUtil.getDiskFile(getApplicationContext(), "acache"));
  }

  public static BCApplication getInstance() {
    return instance;
  }

  /**
   * 获取屏幕尺寸与密度.
   *
   * @param context the context
   * @return mDisplayMetrics
   */
  public DisplayMetrics getDisplayMetrics() {
    Resources mResources;
    mResources = this.getResources();
    //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
    //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
    DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
    return mDisplayMetrics;
  }
}
