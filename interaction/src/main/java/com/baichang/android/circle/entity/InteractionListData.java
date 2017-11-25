package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractionListData implements Serializable {

  @Expose @SerializedName("trendsTitle") public String title;

  @Expose @SerializedName("trendsId") public int id;

  @Expose @SerializedName("hostName") public String name;

  @Expose @SerializedName("hostIcon") public String avatar;

  @Expose @SerializedName("releaseTime") public String time;

  @Expose @SerializedName("trendsContent") public String content;

  @Expose @SerializedName("trendsImages") public List<String> images;

  @Expose @SerializedName("praiseNum") public int praiseCount;

  @Expose @SerializedName("commentNum") public int commentCount;

  @Expose @SerializedName("trendsVideo") public String video;

  @Expose public String videoPic;
  @Expose public int isPraise;
  @Expose public int isCollect;

  @Expose public String hostUserId;

  @Expose public String type;
}
