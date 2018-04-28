package com.baichang.android.circle.common;

import android.os.Bundle;
import android.view.View;
import com.baichang.android.common.BaseActivity;
import com.baichang.android.circle.R;

/**
 * Created by iCong on 2017/2/28.
 *
 * C is a Coder
 */

public class InteractionCommonActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    int topColor = InteractionConfig.getInstance().getTopBarColor();
    if (topColor != -1) {
      setSystemBarColor(topColor);
    } else {
      setSystemBarColor(R.color.cm_transparent);
    }
    super.onCreate(savedInstanceState);
  }

  @Override
  public void back(View view) {
    onBackPressed();
  }
}
