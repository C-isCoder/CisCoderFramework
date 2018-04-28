package com.baichang.android.circle.entity;

import android.graphics.Bitmap;

/**
 * Created by iCong on 2017/3/24.
 */

public class InteractionPublishImageData {

  public String path;
  public Bitmap bitmap;

  public InteractionPublishImageData() {
  }

  public InteractionPublishImageData(String path, Bitmap bitmap) {
    this.path = path;
    this.bitmap = bitmap;
  }
}
