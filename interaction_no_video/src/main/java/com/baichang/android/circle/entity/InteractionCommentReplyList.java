package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by iCong on 2017/3/21.
 */

public class InteractionCommentReplyList {

  @Expose
  public String id;

  @Expose
  public String commentId;// 评论id

  @Expose
  public String trendsId; // 互动id

  @Expose
  public String commentUserId; // 评论用户id

  @Expose
  public String replayUserId; // 回复用户id

  @Expose
  public String created;

  @Expose
  public String commentName;// 被回复人

  @Expose
  public String replayName;// 回复人

  @Expose
  public String replayContent;

  @Expose
  public int commentType;// 是否是楼主

  @Expose
  public int replayType;// 是否是楼主

}
