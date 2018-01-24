package com.baichang.android.circle.present.Impl;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import com.baichang.android.circle.adapter.InteractionDialogAdapter.OnDialogItemClickListener;
import com.baichang.android.circle.adapter.InteractionPublishAdapter.SelectPhotoClickListener;
import com.baichang.android.circle.adapter.InteractionPublishAdapter9;
import com.baichang.android.circle.adapter.InteractionPublishAdapter9.SelectPhotoClickListener9;
import com.baichang.android.circle.dialog.InteractionDialogFragment;
import com.baichang.android.circle.dialog.InteractionPhotoSelectDialog;
import com.baichang.android.circle.entity.InteractionTypeData;
import com.baichang.android.circle.model.Impl.InteractInteractionImpl;
import com.baichang.android.circle.model.InteractInteraction;
import com.baichang.android.circle.present.InteractionPublishPresent;
import com.baichang.android.circle.view.InteractionPublishView;
import com.baichang.android.common.IBaseInteraction.BaseListener;
import com.baichang.android.utils.photo.BCPhotoUtil;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iCong on 2017/3/24.
 */

public class InteractionPublishImpl
    implements InteractionPublishPresent, BaseListener<Boolean>, SelectPhotoClickListener,
    SelectPhotoClickListener9, OnDialogItemClickListener {

  private InteractionPublishView mView;
  private InteractInteraction mInteraction;
  private InteractionPublishAdapter9 mAdapter9;
  private String typeId;

  public InteractionPublishImpl(InteractionPublishView view) {
    mView = view;
    mInteraction = new InteractInteractionImpl();
    mAdapter9 = new InteractionPublishAdapter9(this);
  }

  @Override public void onDestroy() {
    mView = null;
  }

  @Override public void onStart() {

  }

  @Override public void attachView(RecyclerView recyclerView) {
    recyclerView.addItemDecoration(new SpacesItemDecoration(8));
    recyclerView.setAdapter(mAdapter9);
  }

  @Override public void publish(String title, String content) {
    if (TextUtils.isEmpty(title)) {
      mView.showMsg("请输入标题");
    } else if (TextUtils.isEmpty(content)) {
      mView.showMsg("请说点互动内容");
    } else if (TextUtils.isEmpty(typeId)) {
      mView.showMsg("请选择板块");
    } else {
      mView.showProgressBar();
      int model = mAdapter9.getCurrentModel();
      if (model == InteractionPublishAdapter9.PHOTO_MODEL) {
        List<String> images = mAdapter9.getPathList();
        mInteraction.publishImage(mView.getActivity().getApplication(), title, content, typeId,
            images, this);
      } else if (model == InteractionPublishAdapter9.VIDEO_MODEL) {
        BaseMedia media = mAdapter9.getVideo();
        if (media == null) return;
        mInteraction.publishVideo(mView.getActivity().getApplication(), title, content, typeId,
            media.getId(), media.getPath(), this);
      } else {
        mInteraction.publishNoImage(title, content, typeId, this);
      }
    }
  }

  @Override public void onBindImages(ArrayList<BaseMedia> list) {
    if (list == null || list.isEmpty()) {
      return;
    }
    mAdapter9.setData(list);
  }

  @Override public void onBindImages(BaseMedia media) {
    if (media == null) {
      return;
    }
    mAdapter9.addData(media);
  }

  @Override public void selectModel() {
    if (InteractionPresentImpl.mTypeList.isEmpty()) {
      mView.showMsg("暂无分类");
    } else {
      InteractionDialogFragment dialog = InteractionDialogFragment.Instance(this);
      dialog.show(mView.getManager(), "tag");
    }
  }

  @Override public void success(Boolean aBoolean) {
    mView.hideProgressBar();
    mView.showMsg("发布成功");
    mView.close(typeId);
  }

  @Override public void error(String error) {
    mView.hideProgressBar();
    mView.showMsg(error);
  }

  //  @Override
  //  public void onResult(Bitmap bitmap, String path) {
  //    mAdapter.addData(bitmap, path);
  //  }

  @Override public void select() {
    if (mAdapter9.checkNumber()) {
      mView.showMsg("不能添加更多了");
      return;
    }
    InteractionPhotoSelectDialog photoSelectDialog = new InteractionPhotoSelectDialog();
    photoSelectDialog.setResultListener(new InteractionPhotoSelectDialog.OnResultListener() {
      @Override public void onResult(int result) {
        if (result == 0) {
          mAdapter9.setCurrentModel(InteractionPublishAdapter9.PHOTO_MODEL);
          mView.selectImages(mAdapter9.getMax());
        } else if (result == 1) {
          mAdapter9.setCurrentModel(InteractionPublishAdapter9.PHOTO_MODEL);
          BCPhotoUtil.choose(mView.getActivity(), result);
        } else if (result == 2) {
          if (mAdapter9.getCurrentModel() == InteractionPublishAdapter9.PHOTO_MODEL
              && mAdapter9.getItemCount() > 1) {
            mView.showMsg("只能选择一种素材格式");
          } else {
            mAdapter9.setCurrentModel(InteractionPublishAdapter9.VIDEO_MODEL);
            mView.takeVideo();
          }
        }
      }
    });
    photoSelectDialog.show(mView.getManager());
  }

  @Override public void onItemClick(InteractionTypeData data) {
    mView.setTypeName(data.name);
    typeId = String.valueOf(data.id);
  }
}
