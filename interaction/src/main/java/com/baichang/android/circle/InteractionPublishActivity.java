package com.baichang.android.circle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baichang.android.circle.common.InteractionCommonActivity;
import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.circle.present.Impl.InteractionPublishImpl;
import com.baichang.android.circle.present.InteractionPublishPresent;
import com.baichang.android.circle.utils.AnimatorUtil;
import com.baichang.android.circle.utils.BoxingPicassoLoader;
import com.baichang.android.circle.view.InteractionPublishView;
import com.baichang.android.utils.photo.BCPhotoUtil;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingConfig.Mode;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import java.util.ArrayList;

public class InteractionPublishActivity extends InteractionCommonActivity
    implements InteractionPublishView, OnClickListener {

  private static final int REQUEST_CODE_BOXING = 1024;
  private static final int REQUEST_CODE_TAKE = 101;
  private static final int REQUEST_CODE_CROP = 102;

  EditText etTitle;
  EditText etContent;
  RecyclerView rvList;
  TextView tvModel;
  TextView tvPublish;
  ContentLoadingProgressBar mProgress;
  ImageButton mBack;

  private InteractionPublishPresent mPresent;
  private String modelId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.interaction_activity_publish);
    etTitle = (EditText) findViewById(R.id.interaction_publish_et_title);
    etContent = (EditText) findViewById(R.id.interaction_publish_et_content);
    rvList = (RecyclerView) findViewById(R.id.interaction_publish_rv_images);
    tvModel = (TextView) findViewById(R.id.interaction_publish_tv_model);
    mProgress = (ContentLoadingProgressBar) findViewById(R.id.interaction_publish_progress);
    tvPublish = (TextView) findViewById(R.id.interaction_publish_tv_publish);
    mBack = (ImageButton) findViewById(R.id.back);
    mBack.setOnClickListener(this);
    initConfig();
    init();
  }

  private void initConfig() {
    int textColor = InteractionConfig.getInstance().getTextFontColor();
    if (textColor != -1) {
      tvPublish.setTextColor(textColor);
    }

    int drawableRes = InteractionConfig.getInstance().getBackDrawableRes();
    if (drawableRes != -1) {
      mBack.setImageResource(drawableRes);
    }

    int topColor = InteractionConfig.getInstance().getTopBarColor();
    if (topColor != -1) {
      RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.title);
      titleLayout.setBackgroundResource(topColor);
      TextView tvTitle = (TextView) findViewById(R.id.interaction_publish_tv_title);
      tvTitle.setTextColor(Color.WHITE);
    }
  }

  private void init() {
    //Boxing
    IBoxingMediaLoader loader = new BoxingPicassoLoader();
    BoxingMediaLoader.getInstance().init(loader);
    tvModel.setOnClickListener(this);
    findViewById(R.id.interaction_publish_tv_publish).setOnClickListener(this);
    mPresent = new InteractionPublishImpl(this);
    mPresent.attachView(rvList);
  }

  @Override
  public void showProgressBar() {
    mProgress.show();
  }

  @Override
  public void hideProgressBar() {
    mProgress.hide();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mPresent.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresent.onDestroy();
  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  public void close() {
    finish();
  }

  @Override
  public FragmentManager getManager() {
    return getSupportFragmentManager();
  }

  @Override
  public void selectImages(int num) {
    BoxingConfig config = new BoxingConfig(Mode.MULTI_IMG).withMaxCount(num);
    Boxing.of(config).withIntent(this, BoxingActivity.class).start(this, REQUEST_CODE_BOXING);
  }

  @Override
  public Activity getActivity() {
    return this;
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_BOXING && resultCode == RESULT_OK) {
      final ArrayList<BaseMedia> medias = Boxing.getResult(data);
      mPresent.onBindImages(medias);
    } else if (requestCode == REQUEST_CODE_TAKE && resultCode == RESULT_OK) {
      BCPhotoUtil.photoZoomFree(null);
    } else if (requestCode == REQUEST_CODE_CROP && resultCode == RESULT_OK) {
      // 为了兼容Boxing
      BaseMedia media = new ImageMedia(BCPhotoUtil.getPhotoName(), BCPhotoUtil.getPhotoPath());
      mPresent.onBindImages(media);
      BCPhotoUtil.cleanActivity();
    }
  }

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == tvModel.getId()) {
      mPresent.selectModel();
    } else if (i == tvPublish.getId()) {
      AnimatorUtil.scale(v);
      mPresent.publish(etTitle.getText().toString(),
          etContent.getText().toString(), modelId);
    } else if (i == mBack.getId()) {
      AnimatorUtil.scale(v);
      onBackPressed();
    }
  }

}
