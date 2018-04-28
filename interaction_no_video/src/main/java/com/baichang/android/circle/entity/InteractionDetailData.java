package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by iCong on 2017/4/17.
 */

public class InteractionDetailData {

  @Expose
  @SerializedName("trendsTitle")
  public String title;

  @Expose
  @SerializedName("trendsId")
  public int id;

  @Expose
  @SerializedName("hostName")
  public String name;

  @Expose
  @SerializedName("hostIcon")
  public String avatar;

  @Expose
  @SerializedName("releaseTime")
  public String time;

  @Expose
  @SerializedName("trendsContent")
  public String content;

  @Expose
  @SerializedName("trendsImages")
  public List<String> images;

  @Expose
  @SerializedName("praiseNum")
  public int praiseCount;

  @Expose
  @SerializedName("commentNum")
  public int commentCount;

  @Expose
  public int isPraise;
  @Expose
  public int isCollection;

  @Expose
  public String hostUserId;
  @Expose
  public List<InteractionCommentList> commentList;
}
