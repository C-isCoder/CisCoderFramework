package cn.ml.base;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import cn.ml.base.utils.ACache;
import cn.ml.base.utils.MLFolderUtils;

public class MLApplication extends Application {
    private static MLApplication instance;

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
    }

    private void init() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay()
                .getWidth();
        screenHeight = wm.getDefaultDisplay()
                .getHeight();


    }

    private void initACache() {
        aCache = ACache.get(MLFolderUtils.getDiskFile("acache"));
    }

    public static MLApplication getInstance() {
        return instance;
    }
}
