package com.baichang.library.test.common;

import android.content.Context;
import android.widget.ImageView;

import com.baichang.library.test.R;
import com.baichang.library.test.base.APIConstants;
import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by iscod.
 * Time:2016/12/15-10:19.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(APIConstants.API_LOAD_IMAGE + path).error(R.mipmap.ic_launcher).into(imageView);
    }
}
