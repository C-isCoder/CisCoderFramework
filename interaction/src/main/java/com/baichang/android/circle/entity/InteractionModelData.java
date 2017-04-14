package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by iCong on 2017/4/1.
 */

public class InteractionModelData {

  @Expose
  public String id;
  @Expose
  public String name;

  public InteractionModelData(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public InteractionModelData() {
  }
}

