package com.baichang.android.circle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baichang.android.circle.common.InteractionCommonFragment;
import com.baichang.android.circle.present.Impl.InteractionPresentImpl;
import com.baichang.android.circle.present.InteractionPresent;
import com.baichang.android.circle.view.InteractionView;

/**
 * A simple {@link Fragment} subclass.
 */
public class InteractionFragment extends InteractionCommonFragment
    implements InteractionView {

  View mView;
  InteractionPresent mPresent;

  public InteractionFragment() {
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mView == null) {
      initView(inflater, container);
    } else {
      container.removeView(mView);
    }
    return mView;
  }

  private void initView(LayoutInflater inflater, ViewGroup container) {
    mView = inflater.inflate(R.layout.interaction_fragment, container, false);
    mPresent = new InteractionPresentImpl(this);
    mPresent.attachView(mView);
  }

  @Override
  public void showProgressBar() {

  }

  @Override
  public void hideProgressBar() {

  }

  @Override
  public void showMsg(String msg) {
    showMessage(msg);
  }

  @Override
  public void onStart() {
    super.onStart();
    mPresent.onStart();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPresent.onDestroy();
  }
}
