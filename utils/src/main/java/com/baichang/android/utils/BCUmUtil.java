package com.baichang.android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;


/**
 * Created by lingyun on 2016/7/19.
 */
public class BCUmUtil {

    /**
     * 使用方法 isCustom 使用个别还是列表
     * MLUMUtils.share(LoginAty.this, "分享内容", "标题", "http://www.baidu.com", umShareListener, true);
     *  // 分享监听
     umShareListener = new UMShareListener() {
    @Override public void onResult(SHARE_MEDIA platform) {
    Toast.makeText(LoginAty.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
    }

    @Override public void onError(SHARE_MEDIA platform, Throwable t) {
    Log.d("分享失败", platform + "分享失败");
    Toast.makeText(LoginAty.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
    }

    @Override public void onCancel(SHARE_MEDIA platform) {
    Toast.makeText(LoginAty.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
    }
    };
     /** */
    /**
     * * 分享最好写上
     * <p>
     * //     * @param requestCode
     * //     * @param resultCode
     * //     * @param data
     *//*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
     */
    public static void share(Context context, String title, String content, String url, int imageRes, UMShareListener umShareListener, boolean isCustom) {
        if (isCustom) {
            shareCustom(context, content, title, url, imageRes, umShareListener);
        } else {
            shareAll(context, content, title, url, imageRes, umShareListener);
        }
    }

    /**
     * 列表
     *
     * @param context
     * @param content
     * @param title
     * @param url
     * @param umShareListener
     */
    private static void shareAll(Context context, String content, String title, String url, int imageRes, UMShareListener umShareListener) {
        SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_FAVORITE};
        UMImage image = new UMImage(context, BitmapFactory.decodeResource(context.getResources(), imageRes));
        new ShareAction((Activity) context).setDisplayList(displayList)
                .withText(content)
                .withTitle(title)
                .withTargetUrl(url)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
    }

    /**
     * 定制
     *
     * @param context
     * @param content
     * @param title
     * @param url
     * @param umShareListener
     */
    private static void shareCustom(final Context context, String content, String title, String url, int imageRes, UMShareListener umShareListener) {
        SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE};
        UMImage image = new UMImage(context, BitmapFactory.decodeResource(context.getResources(), imageRes));
        new ShareAction((Activity) context)
                .setDisplayList(displayList)
                .withText(content)
                .withTitle(title)
                .withTargetUrl(url)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
    }
}
