package com.baichang.library.test.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.baichang.android.imageloader.ImageLoader;
import com.baichang.android.request.DownloadUtils;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.UploadSubscriber;
import com.baichang.android.request.UploadUtils;
import com.baichang.android.utils.photo.BCPhotoUtil;
import com.baichang.android.widget.BCHttpsWebView;
import com.baichang.android.widget.photoGallery.PhotoGalleryActivity;
import com.baichang.android.widget.photoGallery.PhotoGalleryData;
import com.baichang.android.widget.recycleView.RecyclerViewAdapter;
import com.baichang.android.widget.recycleView.RecyclerViewUtils;
import com.baichang.android.widget.recycleView.ViewHolder;
import com.baichang.library.test.R;
import com.baichang.library.test.base.AppDiskCache;
import com.baichang.library.test.base.CommentActivity;
import com.baichang.library.test.data.InformationData;
import com.baichang.library.test.dialog.PhotoSelectDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends CommentActivity {
    @BindView(R.id.recycler_view)
    RecyclerView rvList;
    @BindView(R.id.web_view)
    BCHttpsWebView webView;
    @BindView(R.id.image_view)
    ImageView ivImage;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefresh;

    private RecyclerViewAdapter mAdapter;
    private List<InformationData> mList = new ArrayList<>();
    private List<String> mIamgeList = new ArrayList<>();
    private static final String TAG = "CID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData(getIntentData());
    }

    private void initData(Object intentData) {
        AppDiskCache.setToken("set token");
        showMessage(AppDiskCache.getToken());
    }

    private void initView() {
        mRefresh.setColorSchemeColors(getResources().getColor(R.color.app_btn_color));
        mRefresh.setOnRefreshListener(() -> {
            normalTest();
        });
        webView.loadUrl("https://www.baidu.com");
        mAdapter = new RecyclerViewAdapter<InformationData>(this, R.layout.item_information_list) {
            @Override
            protected void setItemData(ViewHolder holder, InformationData informationData, int i) {
                holder.setImageView(R.id.item_information_iv_image, informationData.picture);
                holder.setTextView(R.id.item_information_tv_title, informationData.titile);
                holder.setTextView(R.id.item_information_tv_date, informationData.created);
                holder.setTextView(R.id.item_information_tv_content, informationData.abstractTest);
            }
        }.setOnItemClickListener(position -> {
            InformationData data = (InformationData) mAdapter.getItemData(position);
            Toast.makeText(this, data.content, Toast.LENGTH_SHORT).show();
        });
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(mAdapter);
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (RecyclerViewUtils.isScrollBottom(recyclerView)) {
                    showMessage("滑到底部啦");
                    normalTest();
                }
            }
        });
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                uploads();
                //upload();
                break;
            case R.id.button2:
                PhotoSelectDialog dialog = new PhotoSelectDialog();
                dialog.setResultListener(result -> {
                    BCPhotoUtil.choose(getAty(), result);
                });
                dialog.show(getSupportFragmentManager(), "photo");
                break;
            case R.id.button3:
                if (mIamgeList.isEmpty()) break;
                PhotoGalleryData data = new PhotoGalleryData();
                data.imageList = mIamgeList;
                startAct(getAty(), PhotoGalleryActivity.class, data);
                break;
            case R.id.button4:
                normalTest();
                //sslTest();
                break;
            case R.id.button5:
                download();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BCPhotoUtil.IsCancel() && requestCode != 100) {
            BCPhotoUtil.cleanActivity();
            return;
        }
        if (requestCode == 100 && data != null) {
            //相册选择返回
            BCPhotoUtil.photoZoomFree(data.getData());
        } else if (requestCode == 101) {
            //拍照返回 调用裁剪
            BCPhotoUtil.photoZoomFree(null);
        } else if (requestCode == 102 && resultCode != 0) {
            if (TextUtils.isEmpty(BCPhotoUtil.getPhotoPath())) return;
            ivImage.setImageBitmap(BCPhotoUtil.gePhotoBitmap());
            ivImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 多张上传
     */
    private void uploads() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test.png";
        String path1 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test1.png";
        String path2 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test2.png";
        String path3 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test3.png";
        String path4 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test4.png";
        String path5 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test5.jpg";
        Log.d(TAG, "测试图片路径：" + path);
        File file = new File(path);
        File file1 = new File(path1);
        File file2 = new File(path2);
        File file3 = new File(path3);
        File file4 = new File(path4);
        File file5 = new File(path5);
        Log.d(TAG, "File：" + file.getAbsolutePath());
        List<File> files = new ArrayList<>();
        files.add(file);
        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        request().uploads(UploadUtils.getMultipartBodysForFile(files))
                .subscribe(new UploadSubscriber<List<String>>(this) {
                    @Override
                    public void onNext(List<String> list) {
                        for (String s : list) {
                            Log.d(TAG, "ImagePath：" + s);
                        }
                        mIamgeList.addAll(list);
                        ImageLoader.loadImage(getApplicationContext(), list.get(0), ivImage);
                    }
                });
//        List<String> paths = new ArrayList<>();
//        paths.add(path);
//        paths.add(path1);
//        paths.add(path2);
//        paths.add(path3);
//        paths.add(path4);
//        request().uploads(UploadUtils.getMultipartBodysForPath(paths))
//                .subscribe(new HttpSubscriber<List<String>>(this).get(idList -> {
//                    for (String s : idList) {
//                        Log.d(TAG, "ImagePath：" + s);
//                    }
//                    ImageLoader.loadImage(this, idList.get(0), ivImage);
//                }));
    }

    /**
     * 单张上传
     */
    private void upload() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test.png";
        Log.d(TAG, "测试图片路径：" + path);
        File file = new File(path);
        Log.d(TAG, "File：" + file.getAbsolutePath());
        request().upload(UploadUtils.getMultipartBody(file)).subscribe(new UploadSubscriber<List<String>>(this) {
            @Override
            public void onNext(List<String> list) {
                Log.d(TAG, "ImagePath：" + list.get(0));
                ImageLoader.loadImage(getApplicationContext(), list.get(0), ivImage);
            }
        });
    }

    /**
     * 下载
     */
    private void download() {
        DownloadUtils.down(this, request().download("wandoujia-web_seo_baidu_homepage.apk"), file -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
        });
    }

    /**
     * 普通列表请求
     */
    private void normalTest() {
        Map<String, String> map = new HashMap<>();
        map.put("cityId", "1");
        request().getInformationList(map)
                .subscribe(new HttpSubscriber<List<InformationData>>(mRefresh).get(list -> {
                    list.addAll(list);
                    list.addAll(list);
                    list.addAll(list);
                    mList.addAll(list);
                    mAdapter.setData(mList);
                }));
    }

    /**
     * https请求
     */
    private void sslTest() {
    }
}
