package com.baichang.android.circle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import com.baichang.android.common.IBaseView;

/**
 * Created by iCong on 2017/3/28.
 */

public interface InteractionMeView extends IBaseView {

  FragmentManager getManager();

  void setBackground(BitmapDrawable drawable);

  Context getContext();

  void setUserName(String name);

  void setAvatar(Drawable drawable);
}
