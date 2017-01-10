package com.baichang.library.test.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.baichang.android.request.DownloadUtils;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.utils.BCAppUpdateManager;
import com.baichang.android.utils.BCUmUtil;
import com.baichang.android.widget.cityPop.BCCitySelectPop;
import com.baichang.android.widget.photoGallery.PhotoGalleryActivity;
import com.baichang.android.widget.photoGallery.PhotoGalleryData;
import com.baichang.library.test.R;
import com.baichang.library.test.base.AppDiskCache;
import com.baichang.library.test.base.CommonActivity;
import com.baichang.library.test.common.Indicator.example.ExampleMainActivity;
import com.baichang.library.test.common.qrcode.QrCodeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;


public class MainActivity extends CommonActivity {
    private static final String DOWNLOAD_TEST = "https://sm.wdjcdn.com/release/files/jupiter/5.24.1.12069/wandoujia-web_seo_baidu_homepage.apk";
    private static final String SHARE_URL_TEST = "http://www.baidu.com";
    private static final String[] IMAGES = new String[]{
            "group1/M00/00/8A/cxydmlhSINOAdClaAAIOEBpej0w098.png",
            "group1/M00/00/8A/cxydmlhSINOAP-a2AAGcq0WFRI4721.png",
            "group1/M00/00/8A/cxydmlhSINOATJP7AAp3kep8MyQ367.png",
            "group1/M00/00/8A/cxydmlhSINOAUlMiAAk0bpeRed4096.png",
            "group1/M00/00/8A/cxydmlhSINSAXuN3AAGHObcCVh0892.png",
            "group1/M00/00/8A/cxydmlhSINSAMBeCAABqTWWG-yY171.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    //初始化
    private void initData() {
        AppDiskCache.setToken("set token");
        showMessage(AppDiskCache.getToken());
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9, R.id.button0, R.id.button11, R.id.button12,
            R.id.button13, R.id.button14, R.id.button15, R.id.button16, R.id.button17})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                startAct(getAty(), RequestActivity.class);
                break;
            case R.id.button2:
                startAct(getAty(), BannerActivity.class, IMAGES);
                break;
            case R.id.button3:
                PhotoGalleryData data = new PhotoGalleryData(0, IMAGES);
                startAct(getAty(), PhotoGalleryActivity.class, data);
                break;
            case R.id.button4:
                startAct(getAty(), SelectPhotoActivity.class);
                break;
            case R.id.button5:
                download();
                break;
            case R.id.button6:
                BCUmUtil.share(getAty(), "标题", "内容", SHARE_URL_TEST, R.mipmap.ic_launcher, null, true);
                break;
            case R.id.button7:
                startAct(getAty(), SideViewActivity.class);
                break;
            case R.id.button8:
                startAct(getAty(), UploadActivity.class);
                break;
            case R.id.button9:
                startAct(getAty(), QrCodeActivity.class);
                break;
            case R.id.button0:
                startAct(getAty(), DialogActivity.class);
                break;
            case R.id.button11:
                startAct(getAty(), MarqueeViewActivity.class);
                break;
            case R.id.button12:
                startAct(getAty(), ExampleMainActivity.class);
                break;
            case R.id.button13:
                startAct(getAty(), HttpsWebViewActivity.class);
                break;
            case R.id.button14:
//                BCCitySelectPop.Builder builder = new BCCitySelectPop.Builder();
//                builder.setLineColor(R.color.cm_btn_orange_f)
//                        .setListener(this::showMessage)
//                        .setTextSize(16)
//                        .setTitleText("选择地址")
//                        .create(getAty())
//                        .show(getWindow().getDecorView());
                BCCitySelectPop pop = new BCCitySelectPop(getAty());
                pop.setListener(this::showMessage);
                pop.show(getWindow().getDecorView());
                break;
            case R.id.button15:
                BCAppUpdateManager manager = new BCAppUpdateManager(getAty(), DOWNLOAD_TEST, "修复Bug");
                manager.checkUpdateInfo();
                break;
            case R.id.button16:
                startAct(getAty(), CircleRoundActivity.class);
                break;
            case R.id.button17:
                //startAct(getAty(), FunActivity.class);
                startAct(getAty(), Main2Activity.class);
                break;
        }
    }

    /**
     * 下载
     */
    private void download() {
        Observable download = api()
                .download("wandoujia-web_seo_baidu_homepage.apk")
                .compose(HttpSubscriber.downSchedulers());
        DownloadUtils.down(this, download, file -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
        });
    }
}
