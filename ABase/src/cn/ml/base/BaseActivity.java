package cn.ml.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import bc.retrofit.HttpService;
import bc.retrofit.RetrofitClient;
import bc.utils.DialogUtils;
import cn.ml.base.utils.MLAppManager;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLToastUtils;
import cn.ml.base.utils.ToolsUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BaseActivity extends FragmentActivity {

    protected static Context mBaseContext;

    private Object mIntentData;

    private boolean isSystemBar = true;
    private int color = -1;
    private View loadingView;

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

        MLAppManager.getAppManager().addActivity(this);

        //沉浸式状态栏
        if (isSystemBar) {
            initSystemBar();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mBaseContext != null) {
            ToolsUtil.closeSoftInput(mBaseContext);
        }
        MLDialogUtils.dismissProgressDialog();
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
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.setBackgroundDrawable(null);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//是否全透明，5.0以上默认半透明
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (color != -1) {
                window.setStatusBarColor(getResources().getColor(color));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.top_bar_bg));//app主色调
            }
        }
    }

    //滑动关闭输入框
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mBaseContext != null) {
                ToolsUtil.closeSoftInput(mBaseContext);
            }
        }
        return super.onTouchEvent(event);
    }

    //返回
    public void back(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBaseContext != null) {
            ToolsUtil.closeSoftInput(mBaseContext);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseContext = null;
        MLAppManager.getAppManager().finishActivity(this);
        //APP.IMAGE_CACHE.saveDataToDb(this, APP.TAG_CACHE);
    }

    // 获取传递值
    private void initIntentData() {
        mIntentData = getIntent().getSerializableExtra(
                MLBaseConstants.TAG_INTENT_DATA);
    }

    public Object getIntentData() {
        return mIntentData;
    }

    // ==========HTTP Request请求==================

    /**
     * 请求 默认有加载圈
     *
     * @return
     */
    protected HttpService request() {
        DialogUtils.showProgressDialog(getAty());
        return RetrofitClient.getInstance().create();
    }

    /**
     * 请求 没有加载圈
     *
     * @return
     */
    protected HttpService noLoadingRequest() {
        return RetrofitClient.getInstance().create();
    }

    //变换操作符省略subscribeOnOn和observeOn的是设置
    final Observable.Transformer schedulersTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    @SuppressWarnings("unchecked")
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

    //=======================================================================================================
    // ==========加载数据对话框ProgressDialog===========
    public void showProgressDialog() {
        DialogUtils.showProgressDialog(getAty());
    }

    public void showProgressDialog(String message) {
        DialogUtils.showProgressDialog(getAty(), message);
    }

    public void dismissProgressDialog() {
        DialogUtils.dismissProgressDialog();
    }

    // ==========Toast 提示=========================
    public void showMessage(Context context, Object obj) {
        MLToastUtils.showMessage(context, obj);
    }

    public void showMessage(Object obj) {
        MLToastUtils.showMessage(getAty(), obj);
    }


    // ==========跳转页面=======================
    protected void startAct(Activity act, Class cls) {
        startAct(act, cls, null, -1);
    }

    protected void startAct(Activity act, Class cls, Object obj) {
        startAct(act, cls, obj, -1);
    }

    protected void startAct(Activity act, Class cls, Object obj, int requestCode) {
        MLAppManager.startActivity(act, cls, obj, requestCode);
    }

    protected void startAct2(Activity act, Class cls) {
        startAct2(act, cls, null, -1);
    }

    protected void startAct2(Activity act, Class cls, Object obj) {
        startAct2(act, cls, obj, -1);
    }

    protected void startAct2(Activity act, Class cls, Object obj, int requestCode) {
        MLAppManager.startActivity2(act, cls, obj, requestCode);
    }
    // =======================================

    //打印请求异常
    public void logError(Throwable e) {
        Log.d("Request", "请求异常:" + e.toString());
        showMessage(getAty(), e.getMessage());
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

    protected Activity getAty() {
        return this;
    }
}
