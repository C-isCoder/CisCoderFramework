package com.baichang.android.widget.cityPop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baichang.android.common.ConfigurationImpl;
import com.baichang.android.widget.R;


public class BCCitySelectPop extends PopupWindow {

    private CityPicker mCityPicker;

    private TextView tvConfirm;
    private TextView tvTitle;
    private View mLineView;
    private static int mColor;
    private static String mTitleText;
    private static OnSelectResultListener mListener;
    private static int mSize;

    public BCCitySelectPop(Context context) {
        super(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.city_select_popup_layout, null);
        mCityPicker = (CityPicker) view.findViewById(R.id.city_picker);
        tvConfirm = (TextView) view.findViewById(R.id.city_select_tv_confirm);
        tvTitle = (TextView) view.findViewById(R.id.city_select_tv_title);
        mLineView = view.findViewById(R.id.city_select_line_view);

        int color = context.getResources().getColor(mColor == 0 ? ConfigurationImpl.get().getAppBarColor() : mColor);
        tvTitle.setTextColor(color);
        tvConfirm.setTextColor(color);
        tvConfirm.setTextColor(color);
        mLineView.setBackgroundColor(color);
        mCityPicker.setLineColor(mColor == 0 ? ConfigurationImpl.get().getAppBarColor() : mColor);
        if (!TextUtils.isEmpty(mTitleText)) {
            tvTitle.setText(mTitleText);
        }
        if (mSize != 0) {
            tvTitle.setTextSize(mSize);
            tvConfirm.setTextSize(mSize);
        }
        tvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String cityString = mCityPicker.getCity_string();
                if (!TextUtils.isEmpty(cityString)) {
                    if (mListener == null) return;
                    mListener.onResult(cityString);
                }
                dismiss();
            }
        });
        //设置LTSelectPopupWindow的View
        this.setContentView(view);
        //设置LTSelectPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置LTSelectPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置LTSelectPopupWindow弹出窗体可点击  
        this.setFocusable(true);
        //动画
        this.setAnimationStyle(R.style.anim_pop_up_show);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        //设置LTSelectPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.city_select_ly_status).getTop();
                int bottom = view.findViewById(R.id.city_select_ly_status).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height || y > bottom) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void setListener(OnSelectResultListener listener) {
        mListener = listener;
    }

    public interface OnSelectResultListener {

        void onResult(String address);
    }

    public void show(View view) {
        this.showAtLocation(((ViewGroup) (view.findViewById(android.R.id.content)))
                .getChildAt(0), Gravity.CENTER, 0, 0);
    }

    public static class Builder {
        public Builder setLineColor(int ColorRes) {
            mColor = ColorRes;
            return this;
        }

        public Builder setTitleText(String text) {
            mTitleText = text;
            return this;
        }

        public Builder setListener(OnSelectResultListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setTextSize(int size) {
            mSize = size;
            return this;
        }

        public BCCitySelectPop create(Context context) {
            return new BCCitySelectPop(context);
        }
    }
}
