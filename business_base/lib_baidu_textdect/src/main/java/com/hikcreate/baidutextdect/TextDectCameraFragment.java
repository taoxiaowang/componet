package com.hikcreate.baidutextdect;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.idcardquality.IDcardQualityProcess;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraThreadPool;
import com.baidu.ocr.ui.camera.CameraView;
import com.baidu.ocr.ui.camera.ICameraControl;
import com.baidu.ocr.ui.camera.MaskView;
import com.baidu.ocr.ui.camera.OCRCameraLayout;
import com.baidu.ocr.ui.camera.PermissionCallback;
import com.baidu.ocr.ui.crop.CropView;
import com.baidu.ocr.ui.crop.FrameOverlayView;
import com.hikcreate.baidutextdect.util.TextDecFileUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文本识别fragment
 *
 * @author yslei
 * @date 2019/6/17
 * @email leiyongsheng@hikcreate.com
 */
public class TextDectCameraFragment extends Fragment {

    public static final String CONTENT_TYPE_GENERAL = "general";
    public static final String CONTENT_TYPE_ID_CARD_FRONT = "IDCardFront";
    public static final String CONTENT_TYPE_ID_CARD_BACK = "IDCardBack";
    public static final String CONTENT_TYPE_BANK_CARD = "bankCard";
    public static final String CONTENT_TYPE_PASSPORT = "passport";

    private static final String KEY_OUTPUT_FILE_PATH = "outputFilePath";
    private static final String KEY_CONTENT_TYPE = "contentType";
    private static final String KEY_NATIVE_TOKEN = "nativeToken";
    private static final String KEY_NATIVE_ENABLE = "nativeEnable";
    private static final String KEY_NATIVE_MANUAL = "nativeEnableManual";
    private static final String KEY_CONTENT_TITLE = "contentTitle";

    private static final int PERMISSIONS_REQUEST_CAMERA = 800;
    private static final int PERMISSIONS_EXTERNAL_STORAGE = 801;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    private TextDectCallback mDectCallback;
    private File outputFile;
    private TextDectEnum contentType;
    private Handler handler = new Handler();

    private boolean isNativeEnable;
    private boolean isNativeManual;
    private boolean isGoToGallery;

    private OCRCameraLayout takePictureContainer;
    private OCRCameraLayout cropContainer;
    private OCRCameraLayout confirmResultContainer;
    private ImageView lightButton;
    private CameraView cameraView;
    private ImageView displayImageView;
    private CropView cropView;
    private FrameOverlayView overlayView;
    private MaskView cropMaskView;
    private ImageView takePhotoBtn;

    private PermissionCallback permissionCallback = () -> {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSIONS_REQUEST_CAMERA);
        return false;
    };

    /**
     * 用于初始化身份证正面识别实例
     *
     * @param useNative 用于判断是否使用native校验
     * @return
     */
    public static TextDectCameraFragment newIdCardFrontInstance(boolean useNative) {
        return newIdCardInstance(TextDecFileUtil.getImageCacheFilePath(), useNative, useNative, TextDectEnum.ID_CARD_DETECT_FRONT);
    }

    /**
     * 用于初始化身份证背面识别实例
     *
     * @param useNative 用于判断是否使用native校验
     * @return
     */
    public static TextDectCameraFragment newIdCardBackInstance(boolean useNative) {
        return newIdCardInstance(TextDecFileUtil.getImageCacheFilePath(), useNative, useNative, TextDectEnum.ID_CARD_DETECT_BACK);
    }

    /**
     * 用于初始化身份证识别实例
     *
     * @param filePath       图片保存位置
     * @param isNativeEnable native 识别
     * @param isNativeManual native 释放
     * @param textDectEnum   识别具体类型（正反面）
     * @return
     */
    private static TextDectCameraFragment newIdCardInstance(String filePath, boolean isNativeEnable, boolean isNativeManual, TextDectEnum textDectEnum) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_OUTPUT_FILE_PATH, filePath);
        bundle.putBoolean(KEY_NATIVE_ENABLE, isNativeEnable);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        bundle.putBoolean(KEY_NATIVE_MANUAL, isNativeManual);
        bundle.putSerializable(KEY_CONTENT_TYPE, textDectEnum);
        return newInstance(bundle);
    }

    /**
     * 图像识别通用实例接口
     *
     * @param textDectEnum 识别具体类型
     * @return
     */
    public static TextDectCameraFragment newGeneralInstance(TextDectEnum textDectEnum) {
        return newGeneralInstance(TextDecFileUtil.getImageCacheFilePath(), textDectEnum);
    }

    /**
     * 图像识别通用实例接口
     *
     * @param filePath     图片保存位置
     * @param textDectEnum 识别具体类型
     * @return
     */
    public static TextDectCameraFragment newGeneralInstance(String filePath, TextDectEnum textDectEnum) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_OUTPUT_FILE_PATH, filePath);
        bundle.putSerializable(KEY_CONTENT_TYPE, textDectEnum);
        return newInstance(bundle);
    }

    private static TextDectCameraFragment newInstance(Bundle bundle) {
        TextDectCameraFragment instance = new TextDectCameraFragment();
        instance.setArguments(bundle);
        return instance;
    }

    private View.OnClickListener albumButtonOnClickListener = v -> {

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_EXTERNAL_STORAGE);
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        isGoToGallery = true;
    };

    private View.OnClickListener lightButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cameraView.getCameraControl().getFlashMode() == ICameraControl.FLASH_MODE_OFF) {
                cameraView.getCameraControl().setFlashMode(ICameraControl.FLASH_MODE_TORCH);
            } else {
                cameraView.getCameraControl().setFlashMode(ICameraControl.FLASH_MODE_OFF);
            }
            updateFlashMode();
        }
    };

    private View.OnClickListener takeButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraView.takePicture(outputFile, takePictureCallback);
        }
    };

    private CameraView.OnTakePictureCallback autoTakePictureCallback = new CameraView.OnTakePictureCallback() {
        @Override
        public void onPictureTaken(final Bitmap bitmap) {
            CameraThreadPool.execute(() -> {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    bitmap.recycle();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startToDect(contentType);
            });
        }
    };

    private CameraView.OnTakePictureCallback takePictureCallback = new CameraView.OnTakePictureCallback() {
        @Override
        public void onPictureTaken(final Bitmap bitmap) {
            handler.post(() -> {
                takePictureContainer.setVisibility(View.INVISIBLE);
                if (cropMaskView.getMaskType() == MaskView.MASK_TYPE_NONE) {
                    cropView.setFilePath(outputFile.getAbsolutePath());
                    showCrop();
                } else if (cropMaskView.getMaskType() == MaskView.MASK_TYPE_BANK_CARD) {
                    cropView.setFilePath(outputFile.getAbsolutePath());
                    cropMaskView.setVisibility(View.INVISIBLE);
                    overlayView.setVisibility(View.VISIBLE);
                    overlayView.setTypeWide();
                    showCrop();
                } else {
                    displayImageView.setImageBitmap(bitmap);
                    showResultConfirm();
                }
            });
        }
    };

    private View.OnClickListener cropCancelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 释放 cropView中的bitmap;
            cropView.setFilePath(null);
            showTakePicture();
        }
    };

    private View.OnClickListener cropConfirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int maskType = cropMaskView.getMaskType();
            Rect rect;
            switch (maskType) {
                case MaskView.MASK_TYPE_BANK_CARD:
                case MaskView.MASK_TYPE_ID_CARD_BACK:
                case MaskView.MASK_TYPE_ID_CARD_FRONT:
                    rect = cropMaskView.getFrameRect();
                    break;
                case MaskView.MASK_TYPE_NONE:
                default:
                    rect = overlayView.getFrameRect();
                    break;
            }
            Bitmap cropped = cropView.crop(rect);
            displayImageView.setImageBitmap(cropped);
            cropAndConfirm();
        }
    };

    private View.OnClickListener confirmButtonOnClickListener = v -> doConfirmResult();

    private View.OnClickListener confirmCancelButtonOnClickListener = v -> resetShowTakePicture();

    private View.OnClickListener rotateButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cropView.rotate(90);
        }
    };

    public void setDectCallback(TextDectCallback dectCallback) {
        mDectCallback = dectCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.baidu_bd_ocr_fragment_camera, null, false);
        initView(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isGoToGallery) {
            isGoToGallery = false;
        } else {
            cameraView.stop();
        }
    }

    private void initView(View root) {
        takePictureContainer = root.findViewById(R.id.take_picture_container);
        confirmResultContainer = root.findViewById(R.id.confirm_result_container);

        cameraView = root.findViewById(R.id.camera_view);
        cameraView.getCameraControl().setPermissionCallback(permissionCallback);
        lightButton = root.findViewById(R.id.light_button);
        lightButton.setOnClickListener(lightButtonOnClickListener);
        takePhotoBtn = root.findViewById(R.id.take_photo_button);
        root.findViewById(R.id.album_button).setOnClickListener(albumButtonOnClickListener);
        takePhotoBtn.setOnClickListener(takeButtonOnClickListener);

        // confirm result;
        displayImageView = root.findViewById(R.id.display_image_view);
        confirmResultContainer.findViewById(R.id.confirm_button).setOnClickListener(confirmButtonOnClickListener);
        confirmResultContainer.findViewById(R.id.cancel_button).setOnClickListener(confirmCancelButtonOnClickListener);
        root.findViewById(R.id.rotate_button).setOnClickListener(rotateButtonOnClickListener);

        cropView = root.findViewById(R.id.crop_view);
        cropContainer = root.findViewById(R.id.crop_container);
        overlayView = root.findViewById(R.id.overlay_view);
        root.findViewById(R.id.confirm_button).setOnClickListener(cropConfirmButtonListener);
        cropMaskView = cropContainer.findViewById(R.id.crop_mask_view);
        root.findViewById(R.id.cancel_button).setOnClickListener(cropCancelButtonListener);

        cameraView.setAutoPictureCallback(autoTakePictureCallback);
    }

    private void initData() {
        setOrientation(getResources().getConfiguration());
        Bundle bundle = getArguments();
        initParams(bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                cropView.setFilePath(getRealPathFromURI(uri));
                showCrop();
                cropView.rotate(90);
            } else {
                cameraView.getCameraControl().resume();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doClear();
    }

    private void initParams(Bundle bundle) {
        String outputPath = bundle.getString(KEY_OUTPUT_FILE_PATH);
        final String token = bundle.getString(KEY_NATIVE_TOKEN);
        isNativeEnable = bundle.getBoolean(KEY_NATIVE_ENABLE, true);
        isNativeManual = bundle.getBoolean(KEY_NATIVE_MANUAL, false);
        isGoToGallery = false;

        if (token == null && !isNativeManual) {
            isNativeEnable = false;
        }

        if (outputPath != null) {
            outputFile = new File(outputPath);
        }

        contentType = (TextDectEnum) bundle.getSerializable(KEY_CONTENT_TYPE);
        if (contentType == null) {
            contentType = TextDectEnum.BASIC_TEXT_DETECT;
        }
        int maskType;
        switch (contentType) {
            case ID_CARD_DETECT_FRONT:
                maskType = MaskView.MASK_TYPE_ID_CARD_FRONT;
                overlayView.setVisibility(View.INVISIBLE);
                if (isNativeEnable) {
                    takePhotoBtn.setVisibility(View.INVISIBLE);
                }
                break;
            case ID_CARD_DETECT_BACK:
                maskType = MaskView.MASK_TYPE_ID_CARD_BACK;
                overlayView.setVisibility(View.INVISIBLE);
                if (isNativeEnable) {
                    takePhotoBtn.setVisibility(View.INVISIBLE);
                }
                break;
            case BANK_CARD_DETECT:
                maskType = MaskView.MASK_TYPE_BANK_CARD;
                overlayView.setVisibility(View.INVISIBLE);
                break;
            case PASSPORT_CARD_DETECT:
                maskType = MaskView.MASK_TYPE_PASSPORT;
                overlayView.setVisibility(View.INVISIBLE);
                break;
            case BASIC_TEXT_DETECT:
            default:
                maskType = MaskView.MASK_TYPE_NONE;
                cropMaskView.setVisibility(View.INVISIBLE);
                break;
        }

        // 身份证本地能力初始化
        if (maskType == MaskView.MASK_TYPE_ID_CARD_FRONT || maskType == MaskView.MASK_TYPE_ID_CARD_BACK) {
            if (isNativeEnable && !isNativeManual) {
                initNative(token);
            }
        }
        // 特别备注，解决限制竖屏裁剪框的宽高
        overlayView.setTypeWide();
        cameraView.setEnableScan(isNativeEnable);
        cameraView.setMaskType(maskType, getContext());
        cropMaskView.setMaskType(maskType);
    }

    private void initNative(final String token) {
        CameraNativeHelper.init(getContext(), token,
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        cameraView.setInitNativeStatus(errorCode);
                    }

                    @Override
                    public void onSuccess() {

                    }
                });
    }

    private void cropAndConfirm() {
        cameraView.getCameraControl().pause();
        updateFlashMode();
        doConfirmResult();
    }

    private void doConfirmResult() {
        CameraThreadPool.execute(() -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                Bitmap bitmap = ((BitmapDrawable) displayImageView.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            startToDect(contentType);
        });
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            } else {
                result = contentURI.getPath();
            }
        }
        return result;
    }

    private void setOrientation(Configuration newConfig) {
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int orientation;
        int cameraViewOrientation = CameraView.ORIENTATION_PORTRAIT;
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                cameraViewOrientation = CameraView.ORIENTATION_PORTRAIT;
                orientation = OCRCameraLayout.ORIENTATION_PORTRAIT;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                orientation = OCRCameraLayout.ORIENTATION_HORIZONTAL;
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                    cameraViewOrientation = CameraView.ORIENTATION_HORIZONTAL;
                } else {
                    cameraViewOrientation = CameraView.ORIENTATION_INVERT;
                }
                break;
            default:
                orientation = OCRCameraLayout.ORIENTATION_PORTRAIT;
                cameraView.setOrientation(CameraView.ORIENTATION_PORTRAIT);
                break;
        }
        takePictureContainer.setOrientation(orientation);
        cameraView.setOrientation(cameraViewOrientation);
        cropContainer.setOrientation(orientation);
        confirmResultContainer.setOrientation(orientation);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setOrientation(newConfig);
    }

    // take photo;
    private void updateFlashMode() {
        int flashMode = cameraView.getCameraControl().getFlashMode();
        if (flashMode == ICameraControl.FLASH_MODE_TORCH) {
            lightButton.setImageResource(R.drawable.baidu_bd_ocr_light_on);
        } else {
            lightButton.setImageResource(R.drawable.baidu_bd_ocr_light_off);
        }
    }

    public void resetShowTakePicture() {
        displayImageView.setImageBitmap(null);
        showTakePicture();
        cameraView.getCameraControl().resumeScan();
    }

    private void showTakePicture() {
        cameraView.getCameraControl().resume();
        updateFlashMode();
        takePictureContainer.setVisibility(View.VISIBLE);
        confirmResultContainer.setVisibility(View.INVISIBLE);
        cropContainer.setVisibility(View.INVISIBLE);
    }

    private void showCrop() {
        cameraView.getCameraControl().pause();
        updateFlashMode();
        takePictureContainer.setVisibility(View.INVISIBLE);
        confirmResultContainer.setVisibility(View.INVISIBLE);
        cropContainer.setVisibility(View.VISIBLE);
    }

    private void showResultConfirm() {
        cameraView.getCameraControl().pause();
        updateFlashMode();
        takePictureContainer.setVisibility(View.INVISIBLE);
        confirmResultContainer.setVisibility(View.VISIBLE);
        cropContainer.setVisibility(View.INVISIBLE);
    }

    private void startToDect(TextDectEnum contentType) {
        if (mDectCallback != null) {
            getActivity().runOnUiThread(() -> mDectCallback.onTextDect(contentType));
        }
    }

    /**
     * 做一些收尾工作
     */
    public void doClear() {
        cameraView.stop();
        CameraThreadPool.cancelAutoFocusTimer();
        if (isNativeEnable && !isNativeManual) {
            IDcardQualityProcess.getInstance().releaseModel();
        }
    }

    public interface TextDectCallback {
        void onTextDect(TextDectEnum contentType);
    }
}
