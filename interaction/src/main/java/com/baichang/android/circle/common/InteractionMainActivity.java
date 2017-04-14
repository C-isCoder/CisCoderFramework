package com.baichang.android.circle.common;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.baichang.android.circle.R;
import com.baichang.android.circle.InteractionFragment;

public class InteractionMainActivity extends InteractionCommonActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    FragmentManager manager = getSupportFragmentManager();
    manager.beginTransaction().add(R.id.content_layout,new InteractionFragment()).commit();
  }
}
