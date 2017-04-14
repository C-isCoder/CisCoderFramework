package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;
import java.util.List;

/**
 * Created by iCong on 2017/3/21.
 */

public class InteractionCommentData {

  @Expose
  public int id;
  @Expose
  public String avatar;
  @Expose
  public String name;
  @Expose
  public String time;
  @Expose
  public String content;
  @Expose
  public List<InteractionCommentListData> comments;

  public boolean isNullComment() {
    return comments == null || comments.isEmpty();
  }

  public boolean isShowMore() {
    return comments != null && comments.size() > 2;
  }
}
