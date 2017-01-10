package com.baichang.library.test.base;


import com.baichang.android.common.BaseFragment;

/**
 * Created by iscod.
 * Time:2016/12/13-18:02.
 */

public class CommentFragment extends BaseFragment {
    private Api instance;

    public Api api() {
        if (instance == null) {
            instance = new ApiWrapper();
        }
        return instance;
    }
}
