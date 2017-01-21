package com.baichang.library.test.common;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.widget.recycleView.RecyclerViewAdapter;
import com.baichang.android.widget.recycleView.RecyclerViewUtils;
import com.baichang.android.widget.recycleView.ViewHolder;
import com.baichang.library.test.R;
import com.baichang.library.test.base.APIConstants;
import com.baichang.library.test.base.App;
import com.baichang.library.test.base.AppDiskCache;
import com.baichang.library.test.base.CommonActivity;
import com.baichang.library.test.model.InformationData;
import com.baichang.library.test.model.UserData;
import com.bumptech.glide.Glide;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestActivity extends CommonActivity {

  @BindView(R.id.recycler_view)
  RecyclerView rvList;
  @BindView(R.id.refresh_layout)
  SwipeRefreshLayout mRefresh;

  private RecyclerViewAdapter mAdapter;
  private boolean isFirst = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_request);
    ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    mRefresh.setColorSchemeColors(getResources().getColor(R.color.app_btn_color),
        getResources().getColor(R.color.cm_btn_orange_f),
        getResources().getColor(R.color.cm_font_sky_green),
        getResources().getColor(R.color.cm_font_sky_blue));
    mRefresh.setOnRefreshListener(this::normalTest);
    mAdapter = new RecyclerViewAdapter<InformationData>(this, R.layout.item_information_list) {
      @Override
      protected void setItemData(ViewHolder holder, InformationData informationData, int i) {
        //holder.setImageView(R.id.item_information_iv_image, informationData.picture);
        holder.setTextView(R.id.item_information_tv_title, informationData.titile);
        holder.setTextView(R.id.item_information_tv_date, informationData.created);
        holder.setTextView(R.id.item_information_tv_content, informationData.abstractTest);
        Glide.with(mContext)
            .load("http://114.215.88.167:8080/images/file/" + informationData.picture)
            .into((ImageView) holder.getView(R.id.item_information_iv_image));
      }
    }.setOnItemClickListener(position -> {
      InformationData data = (InformationData) mAdapter.getItemData(position);
      Toast.makeText(this, data.content, Toast.LENGTH_SHORT).show();
    });
    rvList.setLayoutManager(new LinearLayoutManager(this));
    rvList.setAdapter(mAdapter);
    rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (RecyclerViewUtils.isScrollBottom(recyclerView)
            && mAdapter.getItemCount() % APIConstants.PAGE_SIZE == 0) {
          showMessage("滑到底部啦");
          normalTest();
        }
      }
    });
  }

  @OnClick({R.id.button, R.id.button1})
  void onClick(View v) {
    switch (v.getId()) {
      case R.id.button:
        normalTest();
        break;
      case R.id.button1:
        https();
        break;
    }
  }

  /**
   * https
   */
  private void https() {
    Map<String, String> map = new HashMap<>();
    map.put("stationAccount", "test");
    map.put("stationPwd", "test");
    //老版本写法
    api().login(map).subscribe(new HttpSubscriber<UserData>(this).get(user -> {
      AppDiskCache.setToken(user.token);
      App.setToken(user.token);
      showMessage(AppDiskCache.getToken());
    }));
    //推荐写法
    api().login(map)
        .compose(HttpSubscriber.applySchedulers(this))
        .subscribe(new HttpSubscriber<>(userData -> {
          AppDiskCache.setToken(userData.token);
          App.setToken(userData.token);
          showMessage(AppDiskCache.getToken());
        }));
  }

  /**
   * 普通列表请求
   */
  private void normalTest() {
    Map<String, String> map = new HashMap<>();
    map.put("cityId", "1");
    //过时写法
    api().getInformationList(map)
        .subscribe(new HttpSubscriber<List<InformationData>>(mRefresh).get(list -> {
          if (isFirst) {
            mAdapter.setData(list);
          } else {
            mAdapter.addData(list);
          }
        }));
    //推荐写法
    api().getInformationList(map)
        .compose(HttpSubscriber.applySchedulers(mRefresh))
        .subscribe(new HttpSubscriber<>(list -> {
          if (isFirst) {
            mAdapter.setData(list);
          } else {
            mAdapter.addData(list);
          }
        }));
  }
}
