package com.baichang.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 解决ScrollView ListView 冲突
 *
 * @author Marcello
 */
public class BCNoScrollExpandableListView extends ExpandableListView {

    public BCNoScrollExpandableListView(Context context) {
        super(context);
    }

    public BCNoScrollExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BCNoScrollExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
