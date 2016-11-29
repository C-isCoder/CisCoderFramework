package bc.base;


import com.baichang.android.library.Configuration;
import com.baichang.android.library.MLApplication;
import com.jnbc.myapplication.R;

import bc.utils.MLCrashHandler;

public class APP extends MLApplication {

    private static APP instance;
    private static String TOKEN = "this is token";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //崩溃收集
        MLCrashHandler crashHandler = MLCrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        initAPIConstants();
    }

    //配置URL TOKEN
    private void initAPIConstants() {
        Configuration.init(this);
        Configuration.setToken(getToken());
        Configuration.setApiLoadImage(getString(R.string.API_DEFAULT_HOST));
        Configuration.setApiUploadImage(getString(R.string.API_UPLOAD_IMAGE));
        Configuration.setBase_Download(getString(R.string.BASE_DOWNLOAD));
        Configuration.setAppBarColor(R.color.top_bar_bg);
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void setToken(String Token) {
        TOKEN = Token;
    }

    public static APP getInstance() {
        return instance;
    }
}
