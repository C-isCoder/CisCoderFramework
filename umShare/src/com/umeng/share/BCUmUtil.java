package com.umeng.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;

import android.util.Log;
import android.widget.Toast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;


/**
 * Created by lingyun on 2016/7/19.
 */
public class BCUmUtil {

  /**
   * 使用方法 isCustom 使用个别还是列表
   * MLUMUtils.share(LoginAty.this, "分享内容", "标题", "http://www.baidu.com", umShareListener, true);
   * // 分享监听
   * umShareListener = new UMShareListener() {
   *
   * @Override public void onResult(SHARE_MEDIA platform) { Toast.makeText(LoginAty.this, platform + " 分享成功啦",
   * Toast.LENGTH_SHORT).show(); }
   * @Override public void onError(SHARE_MEDIA platform, Throwable t) { Log.d("分享失败", platform + "分享失败");
   * Toast.makeText(LoginAty.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show(); }
   * @Override public void onCancel(SHARE_MEDIA platform) { Toast.makeText(LoginAty.this, platform + " 分享取消了",
   * Toast.LENGTH_SHORT).show(); } };
   */

  /**
   * 友盟分享
   *
   * @param activity Activity
   * @param content 内容
   * @param title 标题
   * @param url 连接
   * @param icon 图标
   */
  public static void share(final Activity activity, String content, String title, String url, int icon) {
    final ProgressDialog dialog = new ProgressDialog(activity);
    UMImage image = new UMImage(activity, icon);
    UMWeb web = new UMWeb(url);
    web.setThumb(image);
    web.setTitle(title);
    web.setDescription(content);
    dialog.setTitle("分享");
    dialog.setMessage("正在分享...");
    dialog.setCanceledOnTouchOutside(false);
    dialog.setCancelable(false);
    dialog.setProgressStyle(AlertDialog.THEME_HOLO_LIGHT);
    new ShareAction(activity)
        .setDisplayList(DisplayList)
        .withMedia(image)
        .withMedia(web)
        .withSubject(title)
        .withText(content)
        .setCallback(new UMShareListener() {
          @Override
          public void onStart(SHARE_MEDIA share_media) {
            // start
            SocializeUtils.safeShowDialog(dialog);
          }

          @Override
          public void onResult(SHARE_MEDIA share_media) {
            // result
            SocializeUtils.safeCloseDialog(dialog);
          }

          @Override
          public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(activity, "分享失败：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("UMShare", "友盟分享Error: " + throwable.toString());
          }

          @Override
          public void onCancel(SHARE_MEDIA share_media) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
          }
        }).open();
  }

  /**
   * 友盟分享
   *
   * @param activity Activity
   * @param content 内容
   * @param title 标题
   * @param url 连接
   * @param icon 图标
   * @param imageUrl 缩略图
   */
  public static void share(final Activity activity, String content, String title, String url, int icon,
      String imageUrl) {
    final ProgressDialog dialog = new ProgressDialog(activity);
    UMImage image = new UMImage(activity, icon);
    UMWeb web = new UMWeb(url);
    web.setThumb(new UMImage(activity, imageUrl));
    web.setTitle(title);
    web.setDescription(content);
    dialog.setTitle("分享");
    dialog.setMessage("正在分享...");
    dialog.setCanceledOnTouchOutside(false);
    dialog.setCancelable(false);
    dialog.setProgressStyle(AlertDialog.THEME_HOLO_LIGHT);
    new ShareAction(activity)
        .setDisplayList(DisplayList)
        .withMedia(image)
        .withMedia(web)
        .withSubject(title)
        .withText(content)
        .setCallback(new UMShareListener() {
          @Override
          public void onStart(SHARE_MEDIA share_media) {
            // start
            SocializeUtils.safeShowDialog(dialog);
          }

          @Override
          public void onResult(SHARE_MEDIA share_media) {
            // result
            SocializeUtils.safeCloseDialog(dialog);
          }

          @Override
          public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(activity, "分享失败：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("UMShare", "友盟分享Error: " + throwable.toString());
          }

          @Override
          public void onCancel(SHARE_MEDIA share_media) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
          }
        }).open();
  }

  private static SHARE_MEDIA[] DisplayList = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
      SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_FAVORITE};

  public static void setShareMedia(SHARE_MEDIA[] displayList) {
    DisplayList = displayList;
  }

}
