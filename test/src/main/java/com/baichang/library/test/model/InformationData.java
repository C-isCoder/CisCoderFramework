package com.baichang.library.test.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by iscod.
 * Time:2016/9/26-17:57.
 */
public class InformationData implements Serializable {
    @Expose
    public String titile;
    @Expose
    public String created;
    @Expose
    public String abstractTest;
    @Expose
    public String cityId;
    @Expose
    public String picture;
    @Expose
    public String id;
    @Expose
    public String content;
    @Expose
    public int type;

}
