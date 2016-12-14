package com.baichang.library.test.base;

import android.os.Bundle;

import com.baichang.android.common.BaseActivity;


/**
 * Created by iscod.
 * Time:2016/12/13-17:00.
 */

public class CommentActivity extends BaseActivity {
    private Api instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Api request() {
        if (instance == null) {
            instance = new ApiWrapper();
        }
        return instance;
    }
}
