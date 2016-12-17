package com.baichang.library.test.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by iscod.
 * Time:2016/12/17-11:14.
 */

public class UserData implements Serializable {
    @Expose
    public String token;
    @Expose
    public String stationId;
}
