package com.baichang.android.circle.common;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.baichang.android.circle.R;
import com.baichang.android.circle.InteractionFragment;
import com.baichang.android.circle.entity.InteractionUserData;

public class InteractionMainActivity extends InteractionCommonActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    InteractionUserData userData = new InteractionUserData();
    userData.address = "用户地址";
    userData.avatar = "头像";
    userData.id = "1";
    userData.name = "用户名字";
    InteractionDiskCache.setUser(userData);
    FragmentManager manager = getSupportFragmentManager();
    manager.beginTransaction().add(R.id.content_layout, new InteractionFragment()).commit();
  }
}
