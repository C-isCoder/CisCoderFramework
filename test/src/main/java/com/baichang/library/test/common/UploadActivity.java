package com.baichang.library.test.common;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.baichang.android.imageloader.ImageLoader;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.UploadSubscriber;
import com.baichang.android.request.UploadUtils;
import com.baichang.android.widget.photoGallery.PhotoGalleryActivity;
import com.baichang.android.widget.photoGallery.PhotoGalleryData;
import com.baichang.android.widget.recycleView.RecyclerViewAdapter;
import com.baichang.android.widget.recycleView.ViewHolder;
import com.baichang.library.test.R;
import com.baichang.library.test.base.CommonActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends CommonActivity {

  private static final String TAG = "CID";

  @BindView(R.id.recycler_view)
  RecyclerView rvList;
  @BindView(R.id.image_view)
  ImageView ivImage;

  private RecyclerViewAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upload);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    mAdapter = new RecyclerViewAdapter<String>(this, R.layout.item_revcycle_view) {
      @Override
      protected void setItemData(ViewHolder viewHolder, String s, int i) {
        viewHolder.setImageView(R.id.image_view, s);
      }
    }.setOnItemClickListener(position -> {
      PhotoGalleryData data = new PhotoGalleryData();
      data.imageList = mAdapter.getList();
      data.index = position;
      startAct(getAty(), PhotoGalleryActivity.class, data);
    });
    rvList.setLayoutManager(new GridLayoutManager(this, 4));
    rvList.setAdapter(mAdapter);
  }

  @OnClick({R.id.button, R.id.button1})
  void OnClick(View v) {
    switch (v.getId()) {
      case R.id.button:
        upload();
        break;
      case R.id.button1:
        uploads();
        break;
    }
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
    api().upload(UploadUtils.getMultipartBody(file))
        .compose(HttpSubscriber.downSchedulers())
        .subscribe(new UploadSubscriber<List<String>>(this) {
          @Override
          public void onNext(List<String> list) {
            Log.d(TAG, "ImagePath：" + list.get(0));
            showMessage("上传成功");
            rvList.setVisibility(View.GONE);
            ImageLoader.loadImage(getApplicationContext(), list.get(0), ivImage);
            ivImage.setVisibility(View.VISIBLE);
          }
        });
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
    api().uploads(UploadUtils.getMultipartBodysForFile(files))
        .compose(HttpSubscriber.downSchedulers())
        .subscribe(new UploadSubscriber<List<String>>(this) {
          @Override
          public void onNext(List<String> list) {
            showMessage("上传成功");
            for (String s : list) {
              Log.d(TAG, "ImagePath：" + s);
            }
            ivImage.setVisibility(View.GONE);
            mAdapter.setData(list);
            rvList.setVisibility(View.VISIBLE);
          }
        });
    //路径
    List<String> paths = new ArrayList<>();
    paths.add(path);
    paths.add(path1);
    paths.add(path2);
    paths.add(path3);
    paths.add(path4);
    api().uploads(UploadUtils.getMultipartBodysForPath(paths))
        .compose(HttpSubscriber.downSchedulers())
        .subscribe(new UploadSubscriber<List<String>>(this) {
          @Override
          public void onNext(List<String> list) {
            for (String s : list) {
              Log.d(TAG, "ImagePath：" + s);
            }
            ImageLoader.loadImage(getAty(), list.get(0), ivImage);
          }
        });
  }
}
