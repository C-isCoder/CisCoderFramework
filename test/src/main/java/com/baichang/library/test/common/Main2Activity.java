package com.baichang.library.test.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.baichang.library.test.R;
import com.baichang.library.test.base.CommonActivity;
import com.baichang.library.test.camera.CameraConfigurationManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends CommonActivity implements SurfaceHolder.Callback {
    @BindView(R.id.surface_1)
    SurfaceView surface1;
    @BindView(R.id.surface_2)
    SurfaceView surface2;
    private Camera mCamera;// Camera对象
    private SurfaceHolder holder1;// SurfaceView的控制器
    private SurfaceHolder holder2;// SurfaceView的控制器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        holder1 = surface1.getHolder();
        holder2 = surface2.getHolder();
        holder1.addCallback(this);
        holder2.addCallback(new towCall());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = null;
            try {
                mCamera = Camera.open();//打开相机；在低版本里，只有open（）方法；高级版本加入此方法的意义是具有打开多个
                //摄像机的能力，其中输入参数为摄像机的编号
                //在manifest中设定的最小版本会影响这里方法的调用，如果最小版本设定有误（版本过低），在ide里将不允许调用有参的
                //open方法;
                //如果模拟器版本较高的话，无参的open方法将会获得null值!所以尽量使用通用版本的模拟器和API；
            } catch (Exception e) {
                Log.e("============", "摄像头被占用");
            }
            if (mCamera == null) {
                Log.e("============", "摄像机为空");
                System.exit(0);
            }
            mCamera.setPreviewDisplay(holder);//设置显示面板控制器
            previewCallBack pre = new previewCallBack();//建立预览回调对象
            mCamera.setPreviewCallback(pre); //设置预览回调对象
            //mCamera.getParameters().setPreviewFormat(ImageFormat.JPEG);
            CameraConfigurationManager manager = new CameraConfigurationManager(this);
            manager.initFromCameraParameters(mCamera);
            manager.setDesiredCameraParameters(mCamera);
            mCamera.startPreview();//开始预览，这步操作很重要
        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
 /* 相机初始化 */
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
        mCamera.release();
        mCamera = null;
    }

    /* 相机初始化的method */
    private void initCamera() {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                /*
                 * 设定相片大小为1024*768， 格式为JPG
                 */
                // parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPictureSize(1024, 768);
                mCamera.setParameters(parameters);
                /* 打开预览画面 */
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 停止相机的method */
    private void stopCamera() {
        if (mCamera != null) {
            try {
                /* 停止预览 */
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 每次cam采集到新图像时调用的回调方法，前提是必须开启预览
    class previewCallBack implements Camera.PreviewCallback {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.w("wwwwwwwww", data[5] + "");
            Log.w("支持格式", mCamera.getParameters().getPreviewFormat()+"");
            decodeToBitMap(data, camera);

        }
    }

    public void decodeToBitMap(byte[] data, Camera _camera) {
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
                    size.height, null);
            Log.w("wwwwwwwww", size.width + " " + size.height);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                Log.w("wwwwwwwww", bmp.getWidth() + " " + bmp.getHeight());
                Log.w("wwwwwwwww", (bmp.getPixel(100, 100) & 0xff) + "  " + ((bmp.getPixel(100, 100) >> 8) & 0xff) + "  " + ((bmp.getPixel(100, 100) >> 16) & 0xff));
                stream.close();
                Canvas canvas = holder2.lockCanvas();
                holder2.unlockCanvasAndPost(canvas);
            }
        } catch (Exception ex) {
            Log.e("Sys", "Error:" + ex.getMessage());
        }
    }

    public class towCall implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            holder2 = holder;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
}
