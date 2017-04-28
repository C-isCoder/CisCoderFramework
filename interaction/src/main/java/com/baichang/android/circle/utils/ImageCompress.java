package com.baichang.android.circle.utils;

import android.app.Application;
import com.zxy.tiny.Tiny;

/**
 * Created by iCong on 2017/4/27.
 */

public class ImageCompress {

  private ImageCompress(Application application) {
    Tiny.getInstance().init(application);
  }
}