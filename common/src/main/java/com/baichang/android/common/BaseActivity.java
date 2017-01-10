package com.baichang.android.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.baichang.android.utils.BCAppManager;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCToastUtil;
import com.baichang.android.utils.BCToolsUtil;
import com.baichang.android.utils.SystemBarTintManager;


public abstract class BaseActivity extends FragmentActivity {
    private static final String TAG_INTENT_DATA = "Data";
    protected static Context mBaseContext;

    private Object mIntentData;

    private boolean isSystemBar = true;
    private int color = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        mBaseContext = this;
        initIntentData();
        BCAppManager.getAppManager().addActivity(this);
        //沉浸式状态栏
        if (isSystemBar) {
            initSystemBar();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBaseContext != null) {
            BCToolsUtil.closeSoftInput(mBaseContext);
        }
        BCDialogUtil.dismissProgressDialog();
    }

    /**
     * 设置是否启用全透明系统栏
     * 默认开启
     *
     * @param isVisible true 启用
     */
    public void setSystemBar(boolean isVisible) {
        isSystemBar = isVisible;
    }

    /**
     * 设置之全透明蓝的颜色
     * 默认topBar的颜色即app主色调
     *
     * @param color
     */
    public void setSystemBarColor(int color) {
        this.color = color;
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            // 状态栏透明 需要在创建SystemBarTintManager 之前调用。
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // 使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
            if (color != -1) {
                tintManager.setStatusBarTintResource(color);
            } else {
                tintManager.setStatusBarTintResource(ConfigurationImpl.get().getAppBarColor());//app主色调
            }
            // 设置状态栏的文字颜色
            tintManager.setStatusBarDarkMode(false, this);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.setBackgroundDrawable(null);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//是否全透明，5.0以上默认半透明
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (color != -1) {
                window.setStatusBarColor(getResources().getColor(color));
            } else {
                int defColor = getResources().getColor(ConfigurationImpl.get().getAppBarColor());
                window.setStatusBarColor(defColor);//app主色调
            }
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //滑动关闭输入框
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mBaseContext != null) {
                BCToolsUtil.closeSoftInput(mBaseContext);
            }
        }
        return super.onTouchEvent(event);
    }

    //返回
    public abstract void back(View view);

    @Override
    protected void onResume() {
        super.onResume();
        if (mBaseContext != null) {
            BCToolsUtil.closeSoftInput(mBaseContext);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseContext = null;
        BCAppManager.getAppManager().finishActivity(this);
    }

    // 获取传递值
    private void initIntentData() {
        mIntentData = getIntent().getSerializableExtra(TAG_INTENT_DATA);
    }

    public Object getIntentData() {
        return mIntentData;
    }


    /*==========Toast 提示===========*/
    public void showMessage(Object obj) {
        BCToastUtil.showMessage(getAty(), obj);
    }

    /*==============跳转页面==============*/
    protected void startAct(Activity act, Class cls) {
        startAct(act, cls, null, -1);
    }

    protected void startAct(Activity act, Class cls, Object obj) {
        startAct(act, cls, obj, -1);
    }

    protected void startAct(Activity act, Class cls, Object obj, int requestCode) {
        BCAppManager.startActivity(act, cls, obj, requestCode);
    }

    protected void startActNoFlags(Activity act, Class cls) {
        startAct2(act, cls, null, -1);
    }

    protected void startActNoFlags(Activity act, Class cls, Object obj) {
        startAct2(act, cls, obj, -1);
    }

    protected void startAct2(Activity act, Class cls, Object obj, int requestCode) {
        BCAppManager.startActivity2(act, cls, obj, requestCode);
    }

    protected Activity getAty() {
        return this;
    }


}
