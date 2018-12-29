package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 * Created by iCong on 2017/4/18.
 */

public class InteractionUserData implements Serializable {

  @Expose
  public String id;
  @Expose
  public String avatar;
  @Expose
  public String name;
  @Expose
  public String address;

  public InteractionUserData() {
  }
}
