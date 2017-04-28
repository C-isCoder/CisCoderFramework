package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by iCong on 2017/4/17.
 */

public class InteractionTypeData {

  @Expose
  @SerializedName("typeName")
  public String name;

  @Expose
  @SerializedName("typeId")
  public int id;

  @Override
  public String toString() {
    return "NAME: " + name + " ID " + id;
  }
}
