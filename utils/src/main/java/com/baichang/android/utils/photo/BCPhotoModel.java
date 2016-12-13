package com.baichang.android.utils.photo;

import android.graphics.Bitmap;

public class BCPhotoModel {

    public BCPhotoModel() {
    }

    public BCPhotoModel(Bitmap mBitMap, String path, boolean isNet) {
        super();
        this.mBitMap = mBitMap;
        this.path = path;
        this.isNet = isNet;
    }

    public BCPhotoModel(int id, Bitmap mBitMap, String path) {
        super();
        this.id = id;
        this.mBitMap = mBitMap;
        this.path = path;
    }

    public int id;
    public Bitmap mBitMap;
    public String path;
    public boolean isNet;
}
