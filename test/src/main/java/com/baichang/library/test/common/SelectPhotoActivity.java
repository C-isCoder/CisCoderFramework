package com.baichang.library.test.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.baichang.android.utils.photo.BCPhotoUtil;
import com.baichang.android.widget.photoSelectDialog.PhotoSelectDialog;
import com.baichang.library.test.R;
import com.baichang.library.test.base.CommonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPhotoActivity extends CommonActivity implements PhotoSelectDialog.OnResultListener,
        PhotoSelectDialog.PhotoSelectCallback {
    @BindView(R.id.image_view)
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    void select() {
//        PhotoSelectDialog dialog = new PhotoSelectDialog();
//        dialog.setResultListener(result -> {
//            BCPhotoUtil.choose(getAty(), result);
//        });
//        dialog.setPhotoSelectCallBack((bitmap, path) -> {
//            ivImage.setImageBitmap(bitmap);
//            showMessage(path);
//        });
//        dialog.show(getSupportFragmentManager(), "photo");
        new PhotoSelectDialog.Builder()
                //.setResultListener(this)
                .setSelectCallback(this)
                .setPressColor(R.color.no_activate)
                .setNormalColor(R.color.app_btn_color)
                .setImageText("图库")
                .create()
                .show(getSupportFragmentManager(), "photo");
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (BCPhotoUtil.IsCancel() && requestCode != 100) {
//            BCPhotoUtil.cleanActivity();
//            return;
//        }
//        if (requestCode == 100 && data != null) {
//            //相册选择返回
//            BCPhotoUtil.photoZoomFree(data.getData());
//        } else if (requestCode == 101) {
//            //拍照返回 调用裁剪
//            BCPhotoUtil.photoZoomFree(null);
//        } else if (requestCode == 102 && resultCode != 0) {
//            if (TextUtils.isEmpty(BCPhotoUtil.getPhotoPath())) return;
//            ivImage.setImageBitmap(BCPhotoUtil.gePhotoBitmap());
//        }
//    }

    @Override
    public void onResult(int i) {
        BCPhotoUtil.choose(getAty(), i);
    }

    @Override
    public void onResult(Bitmap bitmap, String path) {
        showMessage(path);
        ivImage.setImageBitmap(bitmap);
    }
}
