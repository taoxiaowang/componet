package com.hikcreate.baidufacedect.ui;

import android.content.Intent;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.hikcreate.baidufacedect.widget.DefaultDialog;

import java.util.HashMap;

/**
 * 人脸检测fragment
 *
 * @author yslei
 * @data 2019/4/3
 * @email leiyongsheng@hikcreate.com
 */
public class FaceDetectFragment extends BaseFaceDetectFragment {
    private DefaultDialog mDefaultDialog;
    private IFaceDetectFragmentCallback mDetectFragmentCallback;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setDetectFragmentCallback(IFaceDetectFragmentCallback detectFragmentCallback) {
        mDetectFragmentCallback = detectFragmentCallback;
    }

    @Override
    public void onDetectCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onDetectCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            if (mDetectFragmentCallback != null) {
                mDetectFragmentCallback.onFaceDetected(message, base64ImageMap);
            }
            //showMessageDialog("人脸图像采集", "采集成功",true);
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            //showMessageDialog("人脸图像采集", "采集超时",false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //startDetectAnimator();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showMessageDialog(String title, String message,boolean success) {
        stopDetect(success);
        if (mDefaultDialog == null) {
            DefaultDialog.Builder builder = new DefaultDialog.Builder(getContext());
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("确认",
                            (dialog, which) -> {
                                mDefaultDialog.dismiss();
                                startDetect();
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();
    }
}
