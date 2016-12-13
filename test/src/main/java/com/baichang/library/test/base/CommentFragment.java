package com.baichang.library.test.base;

import com.baichang.android.library.comment.BaseFragment;

/**
 * Created by iscod.
 * Time:2016/12/13-18:02.
 */

public class CommentFragment extends BaseFragment {
    private Api instance;

    public Api request() {
        if (instance == null) {
            instance = new ApiWrapper();
        }
        return instance;
    }
}
