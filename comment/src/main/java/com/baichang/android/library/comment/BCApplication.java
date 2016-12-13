package com.baichang.android.library.comment;

import android.app.Application;
import android.util.DisplayMetrics;

import com.baichang.android.utils.BCAppUtil;
import com.baichang.android.utils.BCCrashHandler;
import com.baichang.android.utils.BCDensityUtil;
import com.baichang.android.utils.BCFolderUtil;
import com.baichang.android.utils.cache.ACache;


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
        DisplayMetrics display = BCDensityUtil.getDisplayMetrics(this);
        screenWidth = display.widthPixels;
        screenHeight = display.heightPixels;
    }

    private void initACache() {
        aCache = ACache.get(BCFolderUtil.getDiskFile(getApplicationContext(), "acache"));
    }

    public static BCApplication getInstance() {
        return instance;
    }
}
