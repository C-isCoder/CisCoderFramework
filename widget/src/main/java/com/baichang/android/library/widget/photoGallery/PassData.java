package com.baichang.android.library.widget.photoGallery;

import java.io.Serializable;

/**
 * Created by iscod.
 * Time:2016/9/22-18:18.
 */

public class PassData implements Serializable {
    public String name;
    public int index;
    public String url;
    public String[] images;

    public PassData(int index, String... images) {
        this.index = index;
        this.images = images;
    }

    public PassData() {
    }
}
