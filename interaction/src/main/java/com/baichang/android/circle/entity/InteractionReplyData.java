package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;
import java.util.List;

/**
 * Created by iCong on 2017/3/28.
 */

public class InteractionReplyData {

  @Expose
  public int trendsId;

  @Expose
  public String hostName;

  @Expose
  public String trendsContent;

  @Expose
  public String trendsTitle;

  @Expose
  public String replyName;

  @Expose
  public List<String> trendsImages;

  @Expose
  public String replyIcon;

  @Expose
  public String replyTime;

  @Expose
  public String replyContent;

  @Expose
  public String commentId;

  @Expose
  public String replyId;
}
