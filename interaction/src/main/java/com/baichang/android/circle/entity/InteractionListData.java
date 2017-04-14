package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractionListData implements Serializable{

  @Expose
  public String title;
  @Expose
  public int id;
  @Expose
  public String name;
  @Expose
  public String avatar;
  @Expose
  public String time;
  @Expose
  public String content;
  @Expose
  public List<String> images;
  @Expose
  public int praiseCount;
  @Expose
  public int commentCount;
  @Expose
  public boolean isPraise;
}
