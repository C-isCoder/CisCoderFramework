package com.baichang.android.circle.dialog;

import android.Manifest.permission;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.CamcorderProfile;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.baichang.android.circle.R;
import com.baichang.android.circle.video.ERecorderActivityImpl;
import com.baichang.android.circle.video.VideoConfig;
import com.baichang.android.circle.video.exception.NullProfileException;
import com.baichang.android.circle.video.exception.NullRecordTimeException;
import com.baichang.android.circle.video.model.VideoInfo;
import com.baichang.android.config.ConfigurationImpl;
import com.baichang.android.utils.bitmap.BCDrawableUtil;
import com.baichang.android.utils.photo.BCPhotoFragUtil;
import com.baichang.android.widget.uiutils.UIUtil;

public class InteractionPhotoSelectDialog extends DialogFragment implements View.OnClickListener {

  private static final int REQUEST_PERMISSION_CODE = 369;
  /**
   * 拍照
   */
  private Button btnTake;
  /**
   * 相册
   */
  private Button btnImage;
  /**
   * 取消
   */
  private Button btnCancel;
  /**
   * 小视频
   */
  private Button btnVideo;
  /**
   * 按下的颜色
   */
  private static int sPressColor;
  /**
   * 正常颜色
   */
  private static int sNormalColor;
  /**
   * 取消按钮的边框宽度
   */
  private static int sCancelStroke;
  /**
   * 按钮文字
   */
  private static String sTakeText, sImageText, sCancelText, sVideoText;
  /**
   * 拍照按钮背景
   */
  private static Drawable sTakeDrawable;
  /**
   * 相册按钮背景
   */
  private static Drawable sImageDrawable;
  /**
   * 取消按钮背景
   */
  private static Drawable sCancelDrawable;
  /**
   * 小视频按钮背景
   */
  private static Drawable sVideoDrawable;
  /**
   * 按钮圆角
   */
  private static float sRadios;
  /**
   * 是否自由裁剪
   */
  private static boolean isFree = false;

  private VideoConfig mVideoConfig;

  public InteractionPhotoSelectDialog() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view =
        inflater.inflate(R.layout.interaction_fragment_photo_select_dialog, container, false);
    Window window = getDialog().getWindow();
    assert window != null;
    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    window.requestFeature(Window.FEATURE_NO_TITLE);
    initVideo();
    initView(view);
    return view;
  }

  private void initVideo() {
    try {
      mVideoConfig = VideoConfig.get()
          .setTime(10 * 1000)
          .setProfile(CamcorderProfile.QUALITY_480P)
          .setCompress(true)
          .setCompressMode(VideoConfig.CompressMode.fast)
          .check();
    } catch (NullRecordTimeException e) {
      mVideoConfig.setTime(10 * 1000);
    } catch (NullProfileException e) {
      mVideoConfig.setProfile(CamcorderProfile.QUALITY_480P);
    }
  }

  private void initView(View view) {
    btnCancel = (Button) view.findViewById(R.id.interaction_photo_select_btn_cancel);
    btnTake = (Button) view.findViewById(R.id.interaction_photo_select_btn_take);
    btnImage = (Button) view.findViewById(R.id.interaction_photo_select_btn_image);
    btnVideo = (Button) view.findViewById(R.id.interaction_photo_select_btn_video);

    btnImage.setOnClickListener(this);
    btnTake.setOnClickListener(this);
    btnCancel.setOnClickListener(this);
    btnVideo.setOnClickListener(this);

    btnTake.setText(sTakeText == null ? "拍照" : sTakeText);
    btnImage.setText(sImageText == null ? "相册" : sImageText);
    btnCancel.setText(sCancelText == null ? "取消" : sCancelText);
    btnVideo.setText(sVideoText == null ? "小视频" : sVideoText);

    int pressColor = sPressColor == 0 ? R.color.btn_no_activate : sPressColor;
    int normalColor = sNormalColor == 0 ? ConfigurationImpl.get().getAppBarColor() : sNormalColor;
    int stoke = sCancelStroke == 0 ? UIUtil.dip2px(getActivity(), 1) : sCancelStroke;
    float radios = sRadios == 0.0 ? UIUtil.dip2px(getActivity(), 5) : sRadios;

    setTakeButtonBackground(pressColor, normalColor, radios);
    setImageButtonBackground(pressColor, normalColor, radios);
    setVideoButtonBackground(pressColor, normalColor, radios);
    setCancelButtonBackground(pressColor, normalColor, radios, stoke);

    if (sCancelDrawable != null) {
      btnCancel.setBackground(sCancelDrawable);
    }
    if (sTakeDrawable != null) {
      btnTake.setBackground(sTakeDrawable);
    }
    if (sImageDrawable != null) {
      btnImage.setBackground(sImageDrawable);
    }
    if (sImageDrawable != null) {
      btnImage.setBackground(sImageDrawable);
    }
    if (sVideoDrawable != null) {
      btnVideo.setBackground(sVideoDrawable);
    }
  }

  /**
   * 设置相册按钮的背景颜色
   *
   * @param pressResID 按下颜色id
   * @param normalResID 正常颜色id
   * @param radios 圆角
   */
  private void setImageButtonBackground(int pressResID, int normalResID, float radios) {
    setButtonDrawableForColor(btnImage, pressResID, normalResID, radios);
  }

  /**
   * 设置小视频按钮的背景颜色
   *
   * @param pressResID 按下颜色id
   * @param normalResID 正常颜色id
   * @param radios 圆角
   */
  private void setVideoButtonBackground(int pressResID, int normalResID, float radios) {
    setButtonDrawableForColor(btnVideo, pressResID, normalResID, radios);
  }

  /**
   * 设置拍照按钮的背景颜色
   *
   * @param pressResID 按下颜色id
   * @param normalResID 正常颜色id
   * @param radios 圆角
   */
  private void setTakeButtonBackground(int pressResID, int normalResID, float radios) {
    setButtonDrawableForColor(btnTake, pressResID, normalResID, radios);
  }

  /**
   * 设置取消按钮的背景颜色
   *
   * @param pressResID 按下颜色id
   * @param normalResID 正常颜色id
   * @param radios 圆角
   */
  private void setCancelButtonBackground(int pressResID, int normalResID, float radios, int stoke) {
    GradientDrawable pressDrawable =
        BCDrawableUtil.getShapeDrawable(getResources().getColor(normalResID), radios);
    GradientDrawable normalDrawable =
        BCDrawableUtil.getShapeDrawable(getResources().getColor(pressResID), radios, stoke,
            getResources().getColor(normalResID));
    StateListDrawable drawable = BCDrawableUtil.getPressedDrawable(normalDrawable, pressDrawable);
    btnCancel.setBackground(drawable);
  }

  @Override public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.interaction_photo_select_btn_cancel) {
      dismiss();
    } else if (id == R.id.interaction_photo_select_btn_image) {
      if (callback != null) {
        BCPhotoFragUtil.choose(this, 0);
      } else if (resultListener != null) {
        resultListener.onResult(0);
        dismiss();
      }
    } else if (id == R.id.interaction_photo_select_btn_take) {
      if (callback != null) {
        if (checkPermission()) {
          BCPhotoFragUtil.choose(this, 1);
        } else {
          ActivityCompat.requestPermissions(getActivity(), new String[] { permission.CAMERA },
              REQUEST_PERMISSION_CODE);
        }
      } else if (resultListener != null) {
        resultListener.onResult(1);
        dismiss();
      }
    } else if (id == R.id.interaction_photo_select_btn_video) {
      if (callback != null) {
        if (checkPermission()) {
          if (mVideoConfig != null) {
            mVideoConfig.start(getActivity());
          }
        } else {
          ActivityCompat.requestPermissions(getActivity(), new String[] { permission.CAMERA },
              REQUEST_PERMISSION_CODE);
        }
      } else if (resultListener != null) {
        resultListener.onResult(2);
        dismiss();
      }
    }
  }

  private boolean checkPermission() {
    return VERSION.SDK_INT < VERSION_CODES.M
        || PermissionChecker.checkSelfPermission(getActivity(), permission.CAMERA)
        == PermissionChecker.PERMISSION_GRANTED;
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_PERMISSION_CODE) {
      if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
        BCPhotoFragUtil.choose(this, 1);
        dismiss();
      } else {
        dismiss();
        Toast.makeText(getContext(), "没有相机权限，请到设置-应用程序管理，开通权限", Toast.LENGTH_SHORT).show();
      }
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (BCPhotoFragUtil.IsCancel() && requestCode != 100) {
      BCPhotoFragUtil.cleanActivity();
      return;
    }
    if (requestCode == 100 && data != null) {
      //相册选择返回
      if (isFree) {
        BCPhotoFragUtil.photoZoomFree(data.getData());
      } else {
        BCPhotoFragUtil.photoZoom(data.getData());
      }
    } else if (requestCode == 101) {
      //拍照返回 调用裁剪
      if (isFree) {
        BCPhotoFragUtil.photoZoomFree(null);
      } else {
        BCPhotoFragUtil.photoZoom(null);
      }
    } else if (requestCode == 102 && resultCode != 0) {
      if (callback != null) {
        callback.onResult(BCPhotoFragUtil.gePhotoBitmap(), BCPhotoFragUtil.getPhotoPath());
        dismiss();
      }
      BCPhotoFragUtil.cleanActivity();
    } else if (requestCode == VideoConfig.REQUESR_RECORD_MEDIA) {
      VideoInfo videoInfo = ERecorderActivityImpl.getVedioInfo(data);
      if (videoInfo == null) {
        Toast.makeText(getActivity(), "视频拍摄失败", Toast.LENGTH_SHORT).show();
        return;
      }

      //压缩后视频地址
      String mVideoPath = videoInfo.getVideoPath();
      //视频截图
      String mVideoPicPath = videoInfo.getPicPath();
      //视频原地址
      String mVideoOriginPath = videoInfo.getOriginVideoPath();

      if (TextUtils.isEmpty(mVideoPath) || TextUtils.isEmpty(mVideoOriginPath)) {
        Toast.makeText(getActivity(), "视频拍摄失败", Toast.LENGTH_SHORT).show();
        return;
      }
      if (callback != null) {
        callback.onResult(BitmapFactory.decodeFile(mVideoPicPath), mVideoPath);
        dismiss();
      }
    }
  }

  /**
   * 改变按钮已设置的Drawable颜色
   *
   * @param btn 要在设置的按钮
   * @param pressId 按下颜色 id
   * @param normalId 正常颜色 id
   * @param radios 圆角
   */
  private void setButtonDrawableForColor(Button btn, int pressId, int normalId, float radios) {
    btn.setBackground(BCDrawableUtil.getPressedDrawable(getResources().getColor(pressId),
        getResources().getColor(normalId), radios));
  }

  /**
   * 设置是否 自由裁剪
   *
   * @param isFreeCrop 是否自由裁剪
   */
  public void setIsFreeCrop(boolean isFreeCrop) {
    isFree = isFreeCrop;
  }

  public void show(FragmentManager manager) {
    this.show(manager, "Photo");
  }

  private static OnResultListener resultListener;

  public void setResultListener(OnResultListener listener) {
    resultListener = listener;
  }

  public interface OnResultListener {

    void onResult(int result);
  }

  private static PhotoSelectCallback callback;

  public void setSelectCallBack(PhotoSelectCallback listener) {
    callback = listener;
  }

  public interface PhotoSelectCallback {

    void onResult(Bitmap bitmap, String photoPath);
  }

  public static class Builder {

    public Builder setPressColor(int colorId) {
      sPressColor = colorId;
      return this;
    }

    public Builder setNormalColor(int colorId) {
      sNormalColor = colorId;
      return this;
    }

    public Builder setRadios(float radios) {
      sRadios = radios;
      return this;
    }

    public Builder setCancelButtonStoke(int stoke) {
      sCancelStroke = stoke;
      return this;
    }

    public Builder setTakeText(String text) {
      sTakeText = text;
      return this;
    }

    public Builder setImageText(String text) {
      sImageText = text;
      return this;
    }

    public Builder setCancelText(String text) {
      sCancelText = text;
      return this;
    }

    public Builder setVideoText(String text) {
      sVideoText = text;
      return this;
    }

    public Builder setTakeButtonBackground(Drawable drawable) {
      sTakeDrawable = drawable;
      return this;
    }

    public Builder setImageButtonBackground(Drawable drawable) {
      sImageDrawable = drawable;
      return this;
    }

    public Builder setVideoButtonBackground(Drawable drawable) {
      sVideoDrawable = drawable;
      return this;
    }

    public Builder setCancelButtonBackground(Drawable drawable) {
      sCancelDrawable = drawable;
      return this;
    }

    public Builder setResultListener(OnResultListener listener) {
      resultListener = listener;
      callback = null;
      return this;
    }

    public Builder setSelectCallback(PhotoSelectCallback call) {
      callback = call;
      resultListener = null;
      return this;
    }

    public Builder setIsFreeCrop(boolean isFreeCrop) {
      isFree = isFreeCrop;
      return this;
    }

    public InteractionPhotoSelectDialog create() {
      return new InteractionPhotoSelectDialog();
    }
  }
}
