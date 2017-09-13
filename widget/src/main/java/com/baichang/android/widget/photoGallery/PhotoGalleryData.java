package com.baichang.android.widget.photoGallery;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iscod.
 * Time:2016/9/22-18:18.
 */

public class PhotoGalleryData implements Parcelable {
  public String name;
  public int index;
  public String url;
  public String[] images;
  public List<String> imageList;
  public boolean isLocal;

  public PhotoGalleryData(int index, String... images) {
    this.index = index;
    this.images = images;
    this.isLocal = false;
  }

  public PhotoGalleryData(boolean isLocal, int index, String... images) {
    this.index = index;
    this.images = images;
    this.isLocal = isLocal;
  }

  public PhotoGalleryData(int index, List<String> list) {
    imageList = new ArrayList<>();
    this.index = index;
    imageList.addAll(list);
    this.isLocal = false;
  }

  public PhotoGalleryData(boolean isLocal, int index, List<String> list) {
    imageList = new ArrayList<>();
    this.index = index;
    imageList.addAll(list);
    this.isLocal = isLocal;
  }

  public PhotoGalleryData() {
  }

  private PhotoGalleryData(Parcel in) {
    name = in.readString();
    index = in.readInt();
    url = in.readString();
    images = in.createStringArray();
    imageList = in.createStringArrayList();
    isLocal = in.readByte() != 0;
  }

  public static final Creator<PhotoGalleryData> CREATOR = new Creator<PhotoGalleryData>() {
    @Override public PhotoGalleryData createFromParcel(Parcel in) {
      return new PhotoGalleryData(in);
    }

    @Override public PhotoGalleryData[] newArray(int size) {
      return new PhotoGalleryData[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeInt(index);
    dest.writeString(url);
    dest.writeStringArray(images);
    dest.writeStringList(imageList);
    dest.writeByte((byte) (isLocal ? 1 : 0));
  }
}
