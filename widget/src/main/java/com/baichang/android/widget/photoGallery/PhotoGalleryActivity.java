package com.baichang.android.widget.photoGallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baichang.android.widget.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotoGalleryActivity extends FragmentActivity {

  private PhotoGalleryViewPager mViewPager;//ViewPager
  private TextView tvCount;//页数
  private List<String> mImageList = new ArrayList<>();
  private PhotoGalleryData photoGalleryData;
  public static final String IMAGE_DATA = "Data";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    //全屏
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.photo_gallery_image_banner);
    initData(getIntent().getParcelableExtra(IMAGE_DATA));
    initView();
  }

  private void initData(Object intentData) {
    if (intentData == null) {
      return;
    }
    photoGalleryData = (PhotoGalleryData) intentData;
    if (photoGalleryData.imageList == null || photoGalleryData.imageList.isEmpty()) {
      if (photoGalleryData.images == null) {
        return;
      }
      mImageList = Arrays.asList(photoGalleryData.images);
    } else {
      mImageList = photoGalleryData.imageList;
    }
  }

  private void initView() {
    mViewPager = (PhotoGalleryViewPager) findViewById(R.id.photo_gallery_image_banner_pager);
    tvCount = (TextView) findViewById(R.id.photo_gallery_image_banner_tv_count);
    mViewPager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager()));
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        tvCount.setText(position + 1 + "/" + mImageList.size());
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
    mViewPager.setCurrentItem(photoGalleryData.index);
    tvCount.setText(photoGalleryData.index + 1 + "/" + mImageList.size());
  }

  private class ImageViewPagerAdapter extends FragmentPagerAdapter {

    ImageViewPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      String path = mImageList.get(position);
      return PhotoGalleryFragment.newInstance(path, photoGalleryData.isLocal);
    }

    @Override public int getCount() {
      return mImageList.size();
    }
  }
}
