package com.baichang.library.test.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

import com.baichang.library.test.R;
import com.baichang.library.test.base.CommonActivity;
import com.baichang.library.test.camera.CameraPreview2;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunActivity extends CommonActivity {
    @BindView(R.id.surface_1)
    CameraPreview2 surface1;
    @BindView(R.id.surface_2)
    CameraPreview2 surface2;

    private Camera mCamera1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);
        ButterKnife.bind(this);
        mCamera1 = getCamera();
        try {
            mCamera1.setPreviewDisplay(surface1.getHolder());
            mCamera1.addCallbackBuffer(new byte[1024]);
            mCamera1.setPreviewCallbackWithBuffer((data, camera) -> {
                Log.d("FUN", "byte:" + data.length);
                surfaceCreated(data, surface1.getHolder());
                //surfaceCreated(data, surface2.getHolder());
            });
            mCamera1.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    Log.d("FUN", "byte:" + data.length);
                    surfaceCreated(data, surface1.getHolder());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FUN", e.getMessage());
        }

    }

    public void surfaceCreated(byte[] bytes, SurfaceHolder holder) {
        // getting byte array
        Bitmap bmp;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(bmp, 0, 0, null);
        }
        holder.unlockCanvasAndPost(canvas); //finalize
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    /**
     * 初始化相机
     *
     * @return camera
     */
    private Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    /**
     * 初始化相机
     *
     * @return camera
     */
    private Camera getCameraFont() {
        Camera camera;
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera1 != null) {
            mCamera1.setPreviewCallback(null);
            mCamera1.stopPreview();
            mCamera1.release();
            mCamera1 = null;
        }
//        if (mCamera2 != null) {
//            mCamera2.setPreviewCallback(null);
//            mCamera2.stopPreview();
//            mCamera2.release();
//            mCamera2 = null;
//        }
    }

}
