package com.baichang.android.widget.cityPop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.baichang.android.config.ConfigurationImpl;
import com.baichang.android.widget.R;

public class BCCitySelectPop extends PopupWindow {

    private CityPicker mCityPicker;

    private static int mColor;
    private static String mTitleText;
    private static OnSelectResultListener mListener;
    private static int mSize;

    public BCCitySelectPop(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.city_select_popup_layout, null),
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCityPicker = getContentView().findViewById(R.id.city_picker);
        TextView tvConfirm = getContentView().findViewById(R.id.city_select_tv_confirm);
        TextView tvTitle = getContentView().findViewById(R.id.city_select_tv_title);
        View line = getContentView().findViewById(R.id.city_select_line_view);

        int color = context.getResources()
            .getColor(mColor == 0 ? ConfigurationImpl.get().getAppBarColor() : mColor);
        tvTitle.setTextColor(color);
        tvConfirm.setTextColor(color);
        tvConfirm.setTextColor(color);
        line.setBackgroundColor(color);
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
        //动画
        setAnimationStyle(R.style.anim_pop_up_show);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        //设置LTSelectPopupWindow弹出窗体的背景  
        setBackgroundDrawable(dw);
        setOutsideTouchable(true);
    }

    public void setListener(OnSelectResultListener listener) {
        mListener = listener;
    }

    public interface OnSelectResultListener {

        void onResult(String address);
    }

    public void show(View view) {
        showAtLocation(view, Gravity.CENTER, 0, 0);
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
