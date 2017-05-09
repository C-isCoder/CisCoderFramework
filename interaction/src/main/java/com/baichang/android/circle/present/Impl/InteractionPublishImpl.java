package com.baichang.android.circle.present.Impl;

import static android.R.style.Theme_Material_Light_Dialog_Alert;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import com.baichang.android.circle.adapter.InteractionDialogAdapter.OnDialogItemClickListener;
import com.baichang.android.circle.adapter.InteractionPublishAdapter;
import com.baichang.android.circle.adapter.InteractionPublishAdapter.SelectPhotoClickListener;
import com.baichang.android.circle.adapter.InteractionPublishAdapter9;
import com.baichang.android.circle.adapter.InteractionPublishAdapter9.SelectPhotoClickListener9;
import com.baichang.android.circle.dialog.InteractionDialogFragment;
import com.baichang.android.circle.entity.InteractionTypeData;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.circle.model.InteractInteraction;
import com.baichang.android.circle.present.InteractionPublishPresent;
import com.baichang.android.circle.view.InteractionPublishView;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import com.baichang.android.utils.photo.BCPhotoUtil;
import com.baichang.android.widget.photoSelectDialog.PhotoSelectDialog;
import com.baichang.android.widget.photoSelectDialog.PhotoSelectDialog.OnResultListener;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by iCong on 2017/3/24.
 */

public class InteractionPublishImpl implements InteractionPublishPresent,
    BaseListener<Boolean>, SelectPhotoClickListener,
    SelectPhotoClickListener9, OnDialogItemClickListener {

  private static final int DEFAULT_MAX_NUMBER = 9;
  private InteractionPublishView mView;
  private InteractInteraction mInteraction;
  private InteractionPublishAdapter9 mAdapter9;
  private String typeId;

  public InteractionPublishImpl(InteractionPublishView view) {
    mView = view;
    mInteraction = new InteractInteractionImpl();
    mAdapter9 = new InteractionPublishAdapter9(this);
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {

  }

  @Override
  public void attachView(RecyclerView recyclerView) {
    recyclerView.addItemDecoration(new SpacesItemDecoration(8));
    recyclerView.setAdapter(mAdapter9);
  }

  @Override
  public void publish(String title, String content) {
    if (TextUtils.isEmpty(title)) {
      mView.showMsg("请输入标题");
    } else if (TextUtils.isEmpty(content)) {
      mView.showMsg("请说点互动内容");
    } else if (TextUtils.isEmpty(typeId)) {
      mView.showMsg("请选择板块");
    } else {
      mView.showProgressBar();
      List<String> images = mAdapter9.getPathList();
      if (images.isEmpty()) {
        mInteraction.publishNoImage(title, content, typeId, this);
      } else {
        mInteraction.publishImage(mView.getActivity().getApplication(),
            title, content, typeId, images, this);
      }
    }
  }

  @Override
  public void onBindImages(ArrayList<BaseMedia> list) {
    if (list == null || list.isEmpty()) {
      return;
    }
    mAdapter9.setData(list);
  }

  @Override
  public void onBindImages(BaseMedia media) {
    if (media == null) {
      return;
    }
    mAdapter9.addData(media);
  }

  @Override
  public void selectModel() {
    InteractionDialogFragment dialog = InteractionDialogFragment.Instance(this);
    dialog.show(mView.getManager(), "tag");
  }

  @Override
  public void success(Boolean aBoolean) {
    mView.hideProgressBar();
    mView.showMsg("发布成功");
    mView.close(typeId);
  }

  @Override
  public void error(String error) {
    mView.hideProgressBar();
    mView.showMsg(error);
  }

//  @Override
//  public void onResult(Bitmap bitmap, String path) {
//    mAdapter.addData(bitmap, path);
//  }

  @Override
  public void select() {
    if (mAdapter9.getPathList().size() >= DEFAULT_MAX_NUMBER) {
      mView.showMsg("最多只能添加9张图片");
      return;
    }
    PhotoSelectDialog dialog = new PhotoSelectDialog();
    dialog.setResultListener(new OnResultListener() {
      @Override
      public void onResult(int i) {
        if (i == 0) {
          int max = DEFAULT_MAX_NUMBER - mAdapter9.getPathList().size();
          mView.selectImages(max);
        } else {
          BCPhotoUtil.choose(mView.getActivity(), i);
        }
      }
    });
    dialog.show(mView.getManager());
  }

  @Override
  public void onItemClick(InteractionTypeData data) {
    mView.setTypeName(data.name);
    typeId = String.valueOf(data.id);
  }
}
