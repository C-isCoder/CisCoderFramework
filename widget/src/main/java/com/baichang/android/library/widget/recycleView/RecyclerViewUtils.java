package com.baichang.android.library.widget.recycleView;

import android.support.v7.widget.RecyclerView;

/**
 * Created by iscod.
 * Time:2016/12/12-17:35.
 */

public class RecyclerViewUtils {
    /**
     * 判断RecyclerView是否已经滚动到底部
     *
     * @param recyclerView
     * @return
     */
    public static boolean isScrollBottom(RecyclerView recyclerView) {
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
    }
}
