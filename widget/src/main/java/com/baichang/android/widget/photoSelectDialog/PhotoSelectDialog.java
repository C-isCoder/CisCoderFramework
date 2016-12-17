package com.baichang.android.widget.photoSelectDialog;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.baichang.android.common.ConfigurationImpl;
import com.baichang.android.utils.BCViewUtil;
import com.baichang.android.utils.bitmap.BCDrawableUtil;
import com.baichang.android.utils.photo.BCPhotoFragUtil;
import com.baichang.android.widget.R;


public class PhotoSelectDialog extends DialogFragment implements View.OnClickListener {
    /**
     * 拍照
     */
    private Button btnTake;
    /**
     * 相册
     */
    private Button btnImage;
    /**
     * 取消
     */
    private Button btnCancel;
    /**
     * 按下的颜色
     */
    private static int sPressColor;
    /**
     * 正常颜色
     */
    private static int sNormalColor;
    /**
     * 取消按钮的边框宽度
     */
    private static int sCancelStroke;
    /**
     * 按钮文字
     */
    private static String sTakeText, sImageText, sCancelText;
    /**
     * 拍照按钮背景
     */
    private static Drawable sTakeDrawable;
    /**
     * 相册按钮背景
     */
    private static Drawable sImageDrawable;
    /**
     * 取消按钮背景
     */
    private static Drawable sCancelDrawable;
    /**
     * 按钮圆角
     */
    private static float sRadios;

    public PhotoSelectDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_select_dialog, container, false);
        Window window = getDialog().getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btnCancel = (Button) view.findViewById(R.id.photo_select_btn_cancel);
        btnTake = (Button) view.findViewById(R.id.photo_select_btn_take);
        btnImage = (Button) view.findViewById(R.id.photo_select_btn_image);
        btnImage.setOnClickListener(this);
        btnTake.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnTake.setText(sTakeText == null ? "拍照" : sTakeText);
        btnImage.setText(sImageText == null ? "相册" : sImageText);
        btnCancel.setText(sCancelText == null ? "取消" : sCancelText);
        int pressColor = sPressColor == 0 ? R.color.no_activate : sPressColor;
        int normalColor = sNormalColor == 0 ? ConfigurationImpl.get().getAppBarColor() : sNormalColor;
        int stoke = sCancelStroke == 0 ? (int) BCViewUtil.dip2px(getActivity(), 1) : sCancelStroke;
        float radios = sRadios == 0.0 ? BCViewUtil.dip2px(getActivity(), 5) : sRadios;
        setTakeButtonBackground(pressColor, normalColor, radios);
        setImageButtonBackground(pressColor, normalColor, radios);
        setCancelButtonBackground(pressColor, normalColor, radios, stoke);
        if (sCancelDrawable != null) {
            btnCancel.setBackground(sCancelDrawable);
        }
        if (sTakeDrawable != null) {
            btnTake.setBackground(sTakeDrawable);
        }
        if (sImageDrawable != null) {
            btnImage.setBackground(sImageDrawable);
        }
    }

    /**
     * 设置相册按钮的背景颜色
     *
     * @param pressResID  按下颜色id
     * @param normalResID 正常颜色id
     * @param radios      圆角
     */
    private void setImageButtonBackground(int pressResID, int normalResID, float radios) {
        setButtonDrawableForColor(btnImage, pressResID, normalResID, radios);
    }

    /**
     * 设置拍照按钮的背景颜色
     *
     * @param pressResID  按下颜色id
     * @param normalResID 正常颜色id
     * @param radios      圆角
     */
    private void setTakeButtonBackground(int pressResID, int normalResID, float radios) {
        setButtonDrawableForColor(btnTake, pressResID, normalResID, radios);
    }

    /**
     * 设置取消按钮的背景颜色
     *
     * @param pressResID  按下颜色id
     * @param normalResID 正常颜色id
     * @param radios      圆角
     */
    private void setCancelButtonBackground(int pressResID, int normalResID, float radios, int stoke) {
        GradientDrawable pressDrawable = BCDrawableUtil.getShapeDrawable(
                getResources().getColor(normalResID), radios);
        GradientDrawable normalDrawable = BCDrawableUtil.getShapeDrawable(
                getResources().getColor(pressResID), radios, stoke, getResources().getColor(normalResID));
        StateListDrawable drawable = BCDrawableUtil.getPressedDrawable(normalDrawable, pressDrawable);
        btnCancel.setBackground(drawable);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.photo_select_btn_cancel) {
            dismiss();
        } else if (id == R.id.photo_select_btn_image) {
            if (callback != null) {
                BCPhotoFragUtil.choose(this, 0);
            } else if (resultListener != null) {
                resultListener.onResult(0);
                dismiss();
            }
        } else if (id == R.id.photo_select_btn_take) {
            if (callback != null) {
                BCPhotoFragUtil.choose(this, 1);
            } else if (resultListener != null) {
                resultListener.onResult(1);
                dismiss();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (BCPhotoFragUtil.IsCancel() && requestCode != 100) {
            BCPhotoFragUtil.cleanActivity();
            return;
        }
        if (requestCode == 100 && data != null) {
            //相册选择返回
            BCPhotoFragUtil.photoZoomFree(data.getData());
        } else if (requestCode == 101) {
            //拍照返回 调用裁剪
            BCPhotoFragUtil.photoZoomFree(null);
        } else if (requestCode == 102 && resultCode != 0) {
            if (callback != null) {
                callback.onResult(BCPhotoFragUtil.gePhotoBitmap(), BCPhotoFragUtil.getPhotoPath());
                dismiss();
            }
            BCPhotoFragUtil.cleanActivity();
        }
    }

    /**
     * 改变按钮已设置的Drawable颜色
     *
     * @param btn      要在设置的按钮
     * @param pressId  按下颜色 id
     * @param normalId 正常颜色 id
     * @param radios   圆角
     */
    private void setButtonDrawableForColor(Button btn, int pressId, int normalId, float radios) {
        btn.setBackground(BCDrawableUtil.getPressedDrawable(getResources().getColor(pressId),
                getResources().getColor(normalId), radios));
    }

    private static OnResultListener resultListener;

    public void setResultListener(OnResultListener listener) {
        resultListener = listener;
    }

    public interface OnResultListener {
        void onResult(int result);
    }

    private static PhotoSelectCallback callback;

    public void setSelectCallBack(PhotoSelectCallback listener) {
        callback = listener;
    }

    public interface PhotoSelectCallback {
        void onResult(Bitmap bitmap, String photoPath);
    }

    public static class Builder {
        public Builder setPressColor(int colorId) {
            sPressColor = colorId;
            return this;
        }

        public Builder setNormalColor(int colorId) {
            sNormalColor = colorId;
            return this;
        }

        public Builder setRadios(float radios) {
            sRadios = radios;
            return this;
        }

        public Builder setCancelButtonStoke(int stoke) {
            sCancelStroke = stoke;
            return this;
        }

        public Builder setTakeText(String text) {
            sTakeText = text;
            return this;
        }

        public Builder setImageText(String text) {
            sImageText = text;
            return this;
        }

        public Builder setCancelText(String text) {
            sCancelText = text;
            return this;
        }

        public Builder setTakeButtonBackground(Drawable drawable) {
            sTakeDrawable = drawable;
            return this;
        }

        public Builder setImageButtonBackground(Drawable drawable) {
            sImageDrawable = drawable;
            return this;
        }

        public Builder setCancelButtonBackground(Drawable drawable) {
            sCancelDrawable = drawable;
            return this;
        }

        public Builder setResultListener(OnResultListener listener) {
            resultListener = listener;
            callback = null;
            return this;
        }

        public Builder setSelectCallback(PhotoSelectCallback call) {
            callback = call;
            resultListener = null;
            return this;
        }

        public PhotoSelectDialog create() {
            return new PhotoSelectDialog();
        }
    }

}
