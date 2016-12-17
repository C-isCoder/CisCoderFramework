package com.baichang.library.test.base;

import android.os.Bundle;
import android.view.Window;

import com.baichang.android.common.BaseActivity;
import com.baichang.library.test.R;


/**
 * Created by iscod.
 * Time:2016/12/13-17:00.
 */

public class CommonActivity extends BaseActivity {
    private Api instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSystemBarColor(R.color.cm_black);
        super.onCreate(savedInstanceState);
    }

    public Api request() {
        if (instance == null) {
            instance = new ApiWrapper();
        }
        return instance;
    }
}
