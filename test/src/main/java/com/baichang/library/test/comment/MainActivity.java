package com.baichang.library.test.comment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baichang.android.library.imageloader.ImageLoader;
import com.baichang.android.library.request.retrofit.DownloadUtils;
import com.baichang.android.library.request.retrofit.HttpSubscriber;
import com.baichang.android.library.request.retrofit.UploadSubscriber;
import com.baichang.android.library.request.retrofit.UploadUtils;
import com.baichang.android.library.widget.BCHttpsWebView;
import com.baichang.android.library.widget.recycleView.RecyclerViewAdapter;
import com.baichang.android.library.widget.recycleView.RecyclerViewUtils;
import com.baichang.android.library.widget.recycleView.ViewHolder;
import com.baichang.library.test.R;
import com.baichang.library.test.base.AppDiskCache;
import com.baichang.library.test.base.CommentActivity;
import com.baichang.library.test.data.InformationData;

import java.io.File;
import java.util.ArrayList;
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
    @BindView(R.id.text_view)
    TextView tvText;
    @BindView(R.id.button)
    Button btnButton;
    @BindView(R.id.image_view)
    ImageView ivImage;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefresh;

    private RecyclerViewAdapter mAdapter;
    private List<InformationData> mList = new ArrayList<>();
    private static final String TAG = "CID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        normalTest();
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

    @OnClick(R.id.button)
    void onClick(View v) {
        normalTest();
        //sslTest();
        //download();
        //uploads();
        //upload();
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
        Log.d(TAG, "测试图片路径：" + path);
        File file = new File(path);
        File file1 = new File(path1);
        File file2 = new File(path2);
        File file3 = new File(path3);
        File file4 = new File(path4);
        Log.d(TAG, "File：" + file.getAbsolutePath());
        List<File> files = new ArrayList<>();
        files.add(file);
        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
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
                        ImageLoader.loadImage(getApplicationContext(), list.get(0), ivImage);
                    }
                });
        List<String> paths = new ArrayList<>();
        paths.add(path);
        paths.add(path1);
        paths.add(path2);
        paths.add(path3);
        paths.add(path4);
        request().uploads(UploadUtils.getMultipartBodysForPath(paths))
                .subscribe(new HttpSubscriber<List<String>>(this).get(idList -> {
                    for (String s : idList) {
                        Log.d(TAG, "ImagePath：" + s);
                    }
                    ImageLoader.loadImage(this, idList.get(0), ivImage);
                }));
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
