package com.baichang.library.test.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by iscod.
 * Time:2016/11/14-11:22.
 */
public class CityData implements Serializable, Comparable<CityData> {
    @Expose
    public String name;

    public String index;
    @Expose
    public String id;

    public CityData(String id, String name, String index) {
        this.id = id;
        this.name = name;
        this.index = index;
    }

    public CityData() {

    }

    @Override
    public int compareTo(CityData another) {
        return this.index.compareTo(another.index);
    }
}
