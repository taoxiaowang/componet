package com.hikcreate.baidufacedect.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceSDKResSettings;
import com.baidu.idl.face.platform.ui.utils.CameraUtils;
import com.baidu.idl.face.platform.ui.utils.VolumeUtils;
import com.baidu.idl.face.platform.utils.APIUtils;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.baidu.idl.face.platform.utils.CameraPreviewUtils;
import com.hikcreate.baidufacedect.R;
import com.hikcreate.baidufacedect.ui.widget.FaceDetectRoundView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * to do
 *
 * @author yslei
 * @data 2019/4/3
 * @email leiyongsheng@hikcreate.com
 */
public abstract class BaseDetectFragment extends Fragment implements
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        Camera.ErrorCallback,
        VolumeUtils.VolumeCallback {

    public static final String TAG = BaseDetectFragment.class.getSimpleName();
    // View
    protected View mRootView;
    protected FrameLayout mFrameLayout;
    protected SurfaceView mSurfaceView;
    protected SurfaceHolder mSurfaceHolder;
    protected FaceDetectRoundView mFaceDetectRoundView;
    protected LinearLayout mImageLayout;
    // 人脸信息
    protected FaceConfig mFaceConfig;
    // 显示Size
    protected Rect mPreviewRect = new Rect();
    protected int mDisplayWidth = 0;
    protected int mDisplayHeight = 0;
    protected int mSurfaceWidth = 0;
    protected int mSurfaceHeight = 0;
    protected Drawable mTipsIcon;
    // 状态标识
    protected volatile boolean mIsEnableSound = true;
    protected volatile int mOldVolume = -1;
    protected HashMap<String, String> mBase64ImageMap = new HashMap<String, String>();
    protected boolean mIsCreateSurface = false;
    protected volatile boolean mIsCompletion = false;
    protected boolean mShowDetectImageList = false;
    // 相机
    protected Camera mCamera;
    protected Camera.Parameters mCameraParam;
    protected int mCameraId;
    protected int mPreviewWidth;
    protected int mPreviewHight;
    protected int mPreviewDegree;
    private ViewGroup contentView;
    // 监听系统音量广播
    protected BroadcastReceiver mVolumeReceiver;
    protected IFaceDetectTipCallback mDetectTipCallback;
    private IVolumeChangeCallback mVolumeChangeCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      contentView = (ViewGroup) inflater.inflate(R.layout.baidu_fragment_face_detect_version_v3100,container,false);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        mDisplayWidth = dm.widthPixels;
        mDisplayHeight = dm.heightPixels;

        FaceSDKResSettings.initializeResId();
        mFaceConfig = FaceSDKManager.getInstance().getFaceConfig();

        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int vol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        mIsEnableSound = vol > 0 ? mFaceConfig.isSound : false;

        mRootView = contentView;
        mFrameLayout = contentView.findViewById(R.id.detect_surface_layout);

        mSurfaceView = new SurfaceView(getContext());
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setSizeFromLayout();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        int w = mDisplayWidth;
        int h = mDisplayHeight;

        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
                (int) (w * FaceDetectRoundView.SURFACE_RATIO), (int) (h * FaceDetectRoundView.SURFACE_RATIO),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        mSurfaceView.setLayoutParams(cameraFL);
        mFrameLayout.addView(mSurfaceView);

        mFaceDetectRoundView = contentView.findViewById(R.id.detect_face_round);
        mImageLayout = contentView.findViewById(R.id.detect_result_image_layout);
        if (mBase64ImageMap != null) {
            mBase64ImageMap.clear();
        }
    }

    public void setDetectTipCallback(IFaceDetectTipCallback detectTipCallback) {
        mDetectTipCallback = detectTipCallback;
    }

    public void setVolumeChangeCallback(IVolumeChangeCallback volumeChangeCallback) {
        mVolumeChangeCallback = volumeChangeCallback;
    }

    public abstract void openAudioPlay(boolean open);

    public boolean isAudioPlayOpen() {
        return mIsEnableSound;
    }

    public void startDetect() {
        clearSurfaceDraw();
        if (mIsCompletion) {
            reStartDetect();
        } else {
            if (mDetectTipCallback != null) {
                mDetectTipCallback.onFaceDetectTip(FaceStatusEnum.OK, getString(R.string.baidu_detect_face_in));
            }
            startPreview();
        }
        mIsCompletion = false;
        mFaceDetectRoundView.startCycleAnimator();
    }

    public void clearSurfaceDraw() {
        mSurfaceView.setVisibility(View.GONE);
//        Canvas canvas = null;
//        if (mSurfaceHolder != null) {
//            try {
//                canvas = mSurfaceHolder.lockCanvas(null);
//                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            } catch (Exception e) {
//            } finally {
//                if (canvas != null) {
//                    mSurfaceHolder.unlockCanvasAndPost(canvas);
//                }
//            }
//        }
    }

    private void showSurfaceDraw(){
        mSurfaceView.setVisibility(View.VISIBLE);
    }

    public void stopDetect(boolean success) {
        if (success) {
            mIsCompletion = success;
        }
        if (mIsCompletion) {
            mFaceDetectRoundView.stopCycleAnimator(true);
        } else {
            mFaceDetectRoundView.stopCycleAnimator(success);
        }
        stopPreview();
    }

    private void reStartDetect() {
        if (mDetectTipCallback != null) {
            mDetectTipCallback.onFaceDetectTip(FaceStatusEnum.OK, getString(R.string.baidu_detect_face_in));
        }
        resetStrategyDetect();
        stopPreview();
        startPreview();
    }

    public abstract void resetStrategyDetect();

    public abstract void resetStrategyDegree(int degree);

    public abstract void clearStrategyDetect();

    public abstract void dataDetectStrategy(byte[] data);

    public void hideDetectImageList() {
        mShowDetectImageList = false;
    }

    public void showDetectImageList() {
        mShowDetectImageList = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mVolumeReceiver = VolumeUtils.registerVolumeReceiver(getContext(), this);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopDetect(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        VolumeUtils.unRegisterVolumeReceiver(getContext(), mVolumeReceiver);
        mVolumeReceiver = null;
        resetStrategyDetect();
        stopPreview();
    }

    @Override
    public void volumeChanged() {
        try {
            AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                int cv = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                mIsEnableSound = cv > 0;
                openAudioPlay(mIsEnableSound);
                if (mVolumeChangeCallback != null) {
                    mVolumeChangeCallback.onVolumeChange(cv, mOldVolume, mIsEnableSound);
                }
                mOldVolume = cv;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Camera open() {
        Camera camera;
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            return null;
        }

        int index = 0;
        while (index < numCameras) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(index, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                break;
            }
            index++;
        }

        if (index < numCameras) {
            camera = Camera.open(index);
            mCameraId = index;
        } else {
            camera = Camera.open(0);
            mCameraId = 0;
        }
        return camera;
    }

    protected void startPreview() {
        if (mSurfaceView != null && mSurfaceView.getHolder() != null) {
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
        }

        if (mCamera == null) {
            try {
                mCamera = open();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mCamera == null) {
            return;
        }
        if (mCameraParam == null) {
            mCameraParam = mCamera.getParameters();
        }

        mCameraParam.setPictureFormat(PixelFormat.JPEG);
        int degree = displayOrientation(getContext());
        mCamera.setDisplayOrientation(degree);
        // 设置后无效，camera.setDisplayOrientation方法有效
        mCameraParam.set("rotation", degree);
        mPreviewDegree = degree;
        resetStrategyDegree(degree);

        Point point = CameraPreviewUtils.getBestPreview(mCameraParam,
                new Point(mDisplayWidth, mDisplayHeight));
        mPreviewWidth = point.x;
        mPreviewHight = point.y;
        // Preview 768,432
        mPreviewRect.set(0, 0, mPreviewHight, mPreviewWidth);

        mCameraParam.setPreviewSize(mPreviewWidth, mPreviewHight);
        mCamera.setParameters(mCameraParam);
        showSurfaceDraw();

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.stopPreview();
            mCamera.setErrorCallback(this);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (RuntimeException e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        }

    }

    protected void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.setErrorCallback(null);
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CameraUtils.releaseCamera(mCamera);
                mCamera = null;
            }
        }
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(this);
        }
        clearStrategyDetect();
    }

    private int displayOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }
        int result = (0 - degrees + 360) % 360;
        if (APIUtils.hasGingerbread()) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else {
                result = (info.orientation - degrees + 360) % 360;
            }
        }
        return result;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsCreateSurface = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format,
                               int width,
                               int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        if (holder.getSurface() == null) {
            return;
        }
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsCreateSurface = false;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (mIsCompletion) {
            return;
        }

        dataDetectStrategy(data);
    }

    @Override
    public void onError(int error, Camera camera) {
    }

    protected void onRefreshView(FaceStatusEnum status, String message) {
        switch (status) {
            case OK:
                onRefreshTipsView(status, false, message);
                // mFaceDetectRoundView.processDrawState(false);
                break;
            case Detect_PitchOutOfUpMaxRange:
            case Detect_PitchOutOfDownMaxRange:
            case Detect_PitchOutOfLeftMaxRange:
            case Detect_PitchOutOfRightMaxRange:
                onRefreshTipsView(status, true, message);
                // bottom tip
                // mDetectTipCallback.onFaceDetectTip(status, message);
                // mFaceDetectRoundView.processDrawState(true);
                break;
            default:
                onRefreshTipsView(status, false, message);
                //mFaceDetectRoundView.processDrawState(true);
        }
    }

    private void onRefreshTipsView(FaceStatusEnum status, boolean isAlert, String message) {
        if (isAlert) {
            if (mDetectTipCallback != null) {
                mDetectTipCallback.onFaceDetectTip(status, getString(R.string.baidu_detect_standard));
            }
        } else {
            if (!TextUtils.isEmpty(message)) {
                if (mDetectTipCallback != null) {
                    mDetectTipCallback.onFaceDetectTip(status, message);
                }
            }
        }
    }

    protected void saveImage(HashMap<String, String> imageMap) {
        Set<Map.Entry<String, String>> sets = imageMap.entrySet();
        Bitmap bmp = null;
        mImageLayout.removeAllViews();
        if (mShowDetectImageList) {
            mImageLayout.setVisibility(View.VISIBLE);
        } else {
            mImageLayout.setVisibility(View.GONE);
        }
        for (Map.Entry<String, String> entry : sets) {
            bmp = base64ToBitmap(entry.getValue());
            ImageView iv = new ImageView(getContext());
            iv.setImageBitmap(bmp);
            mImageLayout.addView(iv, new LinearLayout.LayoutParams(300, 300));
        }
    }

    private static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
