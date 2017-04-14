package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by iCong on 2017/3/28.
 */

public class InteractionOtherReportData {

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
  public String image;
  @Expose
  public String businessName;
  @Expose
  public String summary;

  public InteractionOtherReportData() {
    this.id = 1;
    this.name = "名字";
    this.avatar = "";
    this.time = "2017-03-29";
    this.content = "核弹翻新多少钱";
    this.image = "";
    this.businessName = "徐老二汽配";
    this.summary = "专业维修核潜艇，核弹头翻新，保养，租赁，改装，抛光，喷漆，内饰";
  }
}
