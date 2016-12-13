package com.baichang.library.test.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.baichang.library.test.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PhotoSelectDialog extends DialogFragment {

    public PhotoSelectDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_select_dialog, container, false);
        ButterKnife.bind(this, view);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @OnClick({R.id.photo_select_btn_cancel, R.id.photo_select_btn_image, R.id.photo_select_btn_take})
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.photo_select_btn_cancel:          //取消
                dismiss();
                break;
            case R.id.photo_select_btn_image:           //相册
                if (listener != null){
                    listener.onResult(0);
                }
                dismiss();
                break;
            case R.id.photo_select_btn_take:            //拍照
                if (listener != null){
                    listener.onResult(1);
                }
                dismiss();
                break;
        }
    }

    private OnResultListener listener;

    public void setResultListener(OnResultListener listener) {
        this.listener = listener;
    }

    public interface OnResultListener {
        void onResult(int result);
    }
}
