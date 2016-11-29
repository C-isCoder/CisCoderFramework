package bc.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import java.util.Locale;

import com.baichang.android.library.utils.MLAppManager;
import com.baichang.android.library.utils.MLDialogUtils;
import com.baichang.android.library.utils.MLToastUtils;

/**
 * ViewPager 懒加载
 */
public abstract class BaseFragmentLazyLoad extends Fragment {

    // ==========HTTP Request请求==================


    // ==========加载数据对话框ProgressDialog==================
    public void showProgressDialog(Context context) {
        showProgressDialog(context, null);
    }

    public void showProgressDialog(Context context, String message) {
        MLDialogUtils.showProgressDialog(context, message);
    }

    public void dismissProgressDialog() {
        MLDialogUtils.dismissProgressDialog();
    }

    // ==========Toast 提示=======================
    public void showMessage(Context context, Object obj) {
        MLToastUtils.showMessage(context, obj);
    }


    // ==========跳转页面=======================
    protected void startAct(Fragment act, Class cls) {
        startAct(act, cls, null, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj) {
        startAct(act, cls, obj, -1);
    }

    protected void startAct(Fragment act, Class cls, Object obj, int requestCode) {
        MLAppManager.startActivityForFrg(act, cls, obj, requestCode);
    }

    public Context getContext() {
        return getActivity();
    }

    protected Fragment getFragment() {
        return this;
    }

    protected void switchLanguage(String language) {
        // 本地语言设置
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        //保存设置语言的类型
//        PreferenceUtil.commitString("language", language);
    }

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
