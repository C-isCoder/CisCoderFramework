package com.baichang.android.library.widget.photoGallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.TextView;


import com.baichang.android.library.widget.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageBanner extends FragmentActivity {
    private PhotoViewPager mViewPager;//ViewPager
    private TextView tvCount;//页数
    private List<String> mImageList = new ArrayList<>();
    private PassData passData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.photo_gallery_image_banner);
        initData(getIntent().getSerializableExtra("Data"));
        initView();
    }

    private void initData(Object intentData) {
        if (intentData == null) return;
        passData = (PassData) intentData;
        if (passData.images == null) return;
        mImageList = Arrays.asList(passData.images);
    }

    private void initView() {
        mViewPager = (PhotoViewPager) findViewById(R.id.photo_gallery_image_banner_pager);
        tvCount = (TextView) findViewById(R.id.photo_gallery_image_banner_tv_count);
        mViewPager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvCount.setText(position + 1 + "/" + mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(passData.index);
        tvCount.setText(passData.index + 1 + "/" + mImageList.size());

    }

    class ImageViewPagerAdapter extends FragmentPagerAdapter {
        public ImageViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageBannerFragment.newInstance(mImageList.get(position));
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }
    }
}
