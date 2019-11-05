package com.hikcreate.baidufacedect.ui;

import android.graphics.Rect;

import com.baidu.aip.face.stat.Ast;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.IDetectStrategy;
import com.baidu.idl.face.platform.IDetectStrategyCallback;
import com.hikcreate.baidufacedect.ui.widget.FaceDetectRoundView;

import java.util.HashMap;

/**
 * to do
 *
 * @author yslei
 * @data 2019/4/3
 * @email leiyongsheng@hikcreate.com
 */
public class BaseFaceDetectFragment extends BaseDetectFragment implements IDetectStrategyCallback {

    protected IDetectStrategy mIDetectStrategy;

    public void openAudioPlay(boolean open) {
        if (mIDetectStrategy != null) {
            mIDetectStrategy.setDetectStrategySoundEnable(open);
            mIsEnableSound = open;
        }
    }

    @Override
    public void resetStrategyDetect() {
        if (mIDetectStrategy != null) {
            mIDetectStrategy.reset();
        }
    }

    @Override
    public void resetStrategyDegree(int degree) {
        if (mIDetectStrategy != null) {
            mIDetectStrategy.setPreviewDegree(degree);
        }
    }

    @Override
    public void clearStrategyDetect() {
        if (mIDetectStrategy != null) {
            mIDetectStrategy = null;
        }
    }

    @Override
    public void dataDetectStrategy(byte[] data) {
        if (mIDetectStrategy == null && mFaceDetectRoundView != null && mFaceDetectRoundView.getRound() > 0) {
            mIDetectStrategy = FaceSDKManager.getInstance().getDetectStrategyModule();
            mIDetectStrategy.setPreviewDegree(mPreviewDegree);
            mIDetectStrategy.setDetectStrategySoundEnable(mIsEnableSound);

            Rect detectRect = FaceDetectRoundView.getPreviewDetectRect(mDisplayWidth, mPreviewHight, mPreviewWidth);
            mIDetectStrategy.setDetectStrategyConfig(mPreviewRect, detectRect, this);
        }
        if (mIDetectStrategy != null) {
            mIDetectStrategy.detectStrategy(data);
        }
    }

    @Override
    public void onDetectCompletion(FaceStatusEnum status, String message,
                                   HashMap<String, String> base64ImageMap) {
        if (mIsCompletion) {
            return;
        }

        onRefreshView(status, message);

        if (status == FaceStatusEnum.OK) {
            mIsCompletion = true;
            saveImage(base64ImageMap);
        }
        Ast.getInstance().faceHit("detect");
    }
}
