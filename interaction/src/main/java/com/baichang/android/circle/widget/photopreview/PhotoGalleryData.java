package com.baichang.android.circle.widget.photopreview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iscod.
 * Time:2016/9/22-18:18.
 */

public class PhotoGalleryData implements Serializable {
    public String name;
    public int index;
    public String url;
    public String[] images;
    public List<String> imageList;

    public PhotoGalleryData(int index, String... images) {
        this.index = index;
        this.images = images;
    }

    public PhotoGalleryData(int index, List<String> list) {
        imageList = new ArrayList<>();
        this.index = index;
        imageList.addAll(list);
    }

    public PhotoGalleryData() {
    }
}
