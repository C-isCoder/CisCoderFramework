package com.baichang.android.permission;

import android.Manifest;
import android.app.Activity;
import android.text.TextUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import rx.Subscriber;


/**
 * Created by iCong on 2016/9/17.
 */
public class PermissionUtils {

  public static final String CAMERA = Manifest.permission.CAMERA;
  public static final String CALL = Manifest.permission.CALL_PHONE;
  public static final String WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
  public static final String READ = Manifest.permission.READ_EXTERNAL_STORAGE;
  public static final String CONTACTS = Manifest.permission.READ_CONTACTS;
  private static RxPermissions rxPermission;

  public PermissionUtils(Activity activity) {
    if (activity == null) {
      throw new NullPointerException("Activity not null");
    }
    if (rxPermission == null) {
      rxPermission = new RxPermissions(activity);
    }
  }

  public static PermissionUtils getInstance(Activity activity) {
    return new PermissionUtils(activity);
  }

  public void setPermission(String permission, final Listener listener) {
    if (TextUtils.isEmpty(permission)) {
      throw new NullPointerException("permission not null");
    }
    if (listener == null) {
      throw new NullPointerException("PermissionListener not null");
    }
    if (TextUtils.equals(permission, CAMERA)) {
      rxPermission.request(CAMERA).subscribe(new RxSubscribe(listener));
    } else if (TextUtils.equals(permission, CALL)) {
      rxPermission.request(CALL).subscribe(new RxSubscribe(listener));
    } else if (TextUtils.equals(permission, WRITE)) {
      rxPermission.request(WRITE).subscribe(new RxSubscribe(listener));
    } else if (TextUtils.equals(permission, READ)) {
      rxPermission.request(READ).subscribe(new RxSubscribe(listener));
    } else if (TextUtils.equals(permission, CONTACTS)) {
      rxPermission.request(CONTACTS).subscribe(new RxSubscribe(listener));
    } else {
      throw new IllegalArgumentException("Please enter the correct permissions");
    }
  }

  public interface Listener {

    void isPermission(boolean grant);
  }

  private class RxSubscribe extends Subscriber<Boolean> {

    Listener listener;

    RxSubscribe(Listener listener) {
      this.listener = listener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Boolean aBoolean) {
      if (listener != null) {
        listener.isPermission(aBoolean);
      }
    }
  }
}
