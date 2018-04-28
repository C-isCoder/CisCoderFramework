package com.baichang.android.circle.widget.photopreview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.baichang.android.circle.R;
import com.baichang.android.circle.common.InteractionAPIConstants;
import com.baichang.android.config.Configuration;
import com.baichang.android.config.ConfigurationImpl;
import com.baichang.android.imageloader.ImageLoader;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.widget.photoView.PhotoView;
import com.baichang.android.widget.photoView.PhotoViewAttacher.OnViewTapListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/3/21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImagePreviewAdapter extends PagerAdapter implements OnViewTapListener, OnLongClickListener {

  private static int ERROR_IMAGE = R.mipmap.interaction_place_image;
  private List<ImageInfo> imageInfo;
  private Context context;
  private View currentView;

  public ImagePreviewAdapter(Context context, @NonNull List<ImageInfo> imageInfo) {
    super();
    this.imageInfo = imageInfo;
    this.context = context;
  }

  @Override
  public int getCount() {
    return imageInfo.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public void setPrimaryItem(ViewGroup container, int position, Object object) {
    super.setPrimaryItem(container, position, object);
    currentView = (View) object;
  }

  public View getPrimaryItem() {
    return currentView;
  }

  public ImageView getPrimaryImageView() {
    return (ImageView) currentView.findViewById(R.id.image_photo_view);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    View view = LayoutInflater.from(context).inflate(R.layout.interaction_item_photo_view, container, false);
    final ProgressBar pb = (ProgressBar) view.findViewById(R.id.image_progress);
    final PhotoView imageView = (PhotoView) view.findViewById(R.id.image_photo_view);

    ImageInfo info = this.imageInfo.get(position);
    imageView.setOnViewTapListener(this);
    imageView.setOnLongClickListener(this);
    showExcessPic(info, imageView);

    //如果需要加载的loading,需要自己改写,不能使用这个方法
    // 转成Drawable 加载，不然下面长按保存会报错。
    Glide.with(context).load(ConfigurationImpl.get().getApiLoadImage() + info.bigImageUrl).asBitmap()
        .error(ERROR_IMAGE)
        .into(new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            imageView.setImageBitmap(resource);
          }
        });

    container.addView(view);
    return view;
  }

  /**
   * 展示过度图片
   */
  private void showExcessPic(ImageInfo imageInfo, PhotoView imageView) {
    imageView.setImageResource(R.mipmap.interaction_place_image);
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  /**
   * 单击屏幕关闭
   */
  @Override
  public void onViewTap(View view, float x, float y) {
    ((ImagePreviewActivity) context).finishActivityAnim();
  }

  @Override
  public boolean onLongClick(final View v) {
    BCDialogUtil.showDialog(context, R.color.interaction_text_font,
        "保存", "保存图片", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            BitmapDrawable drawable = (BitmapDrawable) ((ImageView) v).getDrawable();
            if (drawable != null) {
              saveBitmap(drawable.getBitmap());
            }
          }
        }, null);
    return true;
  }

  private void saveBitmap(Bitmap bitmap) {
    String path = saveImageToGallery(context, bitmap, context.getString(R.string.app_name));
    if (TextUtils.isEmpty(path)) {
      Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(context, "保存成功,请到相册查看 " + path, Toast.LENGTH_SHORT).show();
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