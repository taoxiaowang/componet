package com.hikcreate.baidufacedect.ui;

import android.graphics.Rect;

import com.baidu.aip.face.stat.Ast;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ILivenessStrategy;
import com.baidu.idl.face.platform.ILivenessStrategyCallback;

import java.util.HashMap;

/**
 * 人脸检测fragment
 *
 * @author yslei
 * @data 2019/4/3
 * @email leiyongsheng@hikcreate.com
 */
public class BaseLiveDetectFragment extends BaseDetectFragment implements ILivenessStrategyCallback {

    protected ILivenessStrategy mILivenessStrategy;

    public void openAudioPlay(boolean open) {
        if (mILivenessStrategy != null) {
            mILivenessStrategy.setLivenessStrategySoundEnable(open);
            mIsEnableSound = open;
        }
    }

    @Override
    public void resetStrategyDetect() {
        if (mILivenessStrategy != null) {
            mILivenessStrategy.reset();
        }
    }

    @Override
    public void resetStrategyDegree(int degree) {
        if (mILivenessStrategy != null) {
            mILivenessStrategy.setPreviewDegree(degree);
        }
    }

    @Override
    public void clearStrategyDetect() {
        if (mILivenessStrategy != null) {
            mILivenessStrategy = null;
        }
    }

    @Override
    public void dataDetectStrategy(byte[] data) {
        if (mILivenessStrategy == null) {
            mILivenessStrategy = FaceSDKManager.getInstance().getLivenessStrategyModule();
            mILivenessStrategy.setPreviewDegree(mPreviewDegree);
            mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);

            Rect detectRect = com.baidu.idl.face.platform.ui.widget.FaceDetectRoundView.getPreviewDetectRect(
                    mDisplayWidth, mPreviewHight, mPreviewWidth);
            mILivenessStrategy.setLivenessStrategyConfig(
                    mFaceConfig.getLivenessTypeList(), mPreviewRect, detectRect, this);
        }
        mILivenessStrategy.livenessStrategy(data);
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message,
                                     HashMap<String, String> base64ImageMap) {
        if (mIsCompletion) {
            return;
        }

        onRefreshView(status, message);

        if (status == FaceStatusEnum.OK) {
            mIsCompletion = true;
            saveImage(base64ImageMap);
        }
        Ast.getInstance().faceHit("liveness");
    }
}
