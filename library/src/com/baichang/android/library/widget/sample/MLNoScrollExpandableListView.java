package com.baichang.android.library.widget.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 解决ScrollView ListView 冲突
 *
 * @author Marcello
 */
public class MLNoScrollExpandableListView extends ExpandableListView {

    public MLNoScrollExpandableListView(Context context) {
        super(context);
    }

    public MLNoScrollExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MLNoScrollExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
