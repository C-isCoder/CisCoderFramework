package com.baichang.android.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


/**
 * Created by Marcello on 2015/11/25.
 * Description 上下有横线，背景是白色
 */
public class BCHorizontalRelaLayout extends RelativeLayout {
    private Context mContext;

    public BCHorizontalRelaLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BCHorizontalRelaLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    public BCHorizontalRelaLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        setBackgroundResource(android.R.color.white);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#D3D3D3"));
        paint.setStrokeWidth(1);


        int width = this.getWidth();
        int height = this.getHeight();

        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(0, height, width, height, paint);
        super.onDraw(canvas);
    }

}
