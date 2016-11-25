package com.jnbc.myapplication;

import android.app.Activity;
import android.os.Bundle;
import cn.ml.base.utils.BCAppUpdateManager;


public class MainActivity extends Activity {
    private ZoomableListView zoomableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testAppDownload();
        zoomableListView = (ZoomableListView) findViewById(R.id.list_view);
        //testShare();
    }

    private void testAppDownload() {
        BCAppUpdateManager appUpdateManager = new BCAppUpdateManager(this,
                "https://sm.wdjcdn.com/release/files/jupiter/5.24.1.12069/wandoujia-web_seo_baidu_homepage.apk", "更新测试");
        appUpdateManager.checkUpdateInfo();
    }

}
