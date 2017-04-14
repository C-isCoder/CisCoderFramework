package com.baichang.android.circle.widget.photopreview;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.baichang.android.imageloader.ImageLoader;
import com.baichang.android.circle.R;
import com.baichang.android.widget.photoView.PhotoView;
import com.baichang.android.widget.photoView.PhotoViewAttacher.OnViewTapListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoGalleryFragment extends Fragment
    implements OnViewTapListener, OnLongClickListener {

  private static final String ARG_PARAM = "param";
  private static int ERROR_IMAGE = R.mipmap.interaction_icon_default;
  private String imageUrl;

  private PhotoView mPhoto;

  public PhotoGalleryFragment() {
  }

  public static PhotoGalleryFragment newInstance(String param) {
    PhotoGalleryFragment fragment = new PhotoGalleryFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM, param);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      imageUrl = getArguments().getString(ARG_PARAM);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return createView(inflater, container);
  }

  private View createView(LayoutInflater inflater, ViewGroup container) {
    View view = inflater.inflate(R.layout.interaction_fragment_image_banner, container, false);
    mPhoto = (PhotoView) view.findViewById(R.id.fragment_photo_gallery_image_banner_image);
    mPhoto.setOnViewTapListener(this);
    mPhoto.setOnLongClickListener(this);
    ImageLoader.loadImageError(getActivity().getApplicationContext(), imageUrl, ERROR_IMAGE, mPhoto);
    mPhoto.setImageResource(R.mipmap.interaction_icon_default);
    return view;
  }

  public void setErrorIamge(int resId) {
    ERROR_IMAGE = resId;
  }

  @Override
  public void onViewTap(View view, float v, float v1) {
    getActivity().overridePendingTransition(0, 0);
    getActivity().onBackPressed();
  }

  @Override
  public boolean onLongClick(final View v) {
    new Builder(getActivity())
        .setMessage("保存图片")
        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            BitmapDrawable drawable = (BitmapDrawable) ((ImageView) v).getDrawable();
            if (drawable != null) {
              saveBitmap(drawable.getBitmap());
            }
          }
        })
        .setNegativeButton("取消", null)
        .create()
        .show();
    return true;
  }

  private void saveBitmap(Bitmap bitmap) {
    String path = saveImageToGallery(getActivity(), bitmap, getString(R.string.app_name));
    if (TextUtils.isEmpty(path)) {
      Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(getActivity(), "保存成功,请到相册查看 " + path, Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 保存图片到指定目录，并且更新图库
   *
   * @param context 上下文
   * @param bmp 要保存的Bitmap
   * @param dirName 文件夹名字
   * @return 文件路径
   */
  private String saveImageToGallery(Context context, Bitmap bmp, String dirName) {
    // 首先保存图片
    File appDir = new File(Environment.getExternalStorageDirectory(), dirName);
    if (!appDir.exists()) {
      appDir.mkdir();
    }
    String fileName = System.currentTimeMillis() + ".jpg";
    File file = new File(appDir, fileName);
    try {
      FileOutputStream fos = new FileOutputStream(file);
      bmp.compress(CompressFormat.JPEG, 100, fos);
      fos.flush();
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 其次把文件插入到系统图库
    try {
      MediaStore.Images.Media.insertImage(context.getContentResolver(),
          file.getAbsolutePath(), fileName, null);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    // 最后通知图库更新
    String path = file.getAbsolutePath();
    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    return path;
  }
}
