package com.baichang.android.circle.present;

import android.support.v7.widget.RecyclerView;
import com.baichang.android.common.IBasePresent;
import com.bilibili.boxing.model.entity.BaseMedia;
import java.util.ArrayList;

/**
 * Created by iCong on 2017/3/24.
 */

public interface InteractionPublishPresent extends IBasePresent {

  void attachView(RecyclerView recyclerView);

  void publish(String title, String content, String modelId);

  void onBindImages(ArrayList<BaseMedia> list);

  void onBindImages(BaseMedia media);

  void selectModel();
}
