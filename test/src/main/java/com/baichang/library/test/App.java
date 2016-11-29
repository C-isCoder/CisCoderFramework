package com.baichang.library.test;


import com.baichang.android.library.Configuration;
import com.baichang.android.library.MLApplication;
import com.baichang.android.library.retrofit.RetrofitClient;

/**
 * Created by iCong on 2016/11/25.
 */

public class App extends MLApplication {

    private static App instance;
    private static String TOKEN = "this is token";

    @Override
    public void onCreate() {
        super.onCreate();
        initAPIConstants();
    }

    //配置URL TOKEN
    private void initAPIConstants() {
        Configuration.init(this);
        Configuration.setToken(getToken());
        Configuration.setBaseUrl(getString(R.string.API_DEFAULT_HOST));
        Configuration.setApiLoadImage(getString(R.string.API_LOAD_IMAGE));
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

    public static App getInstance() {
        return instance;
    }

    public static Api request() {
        return RetrofitClient.getInstance().create(Api.class);
    }
}
