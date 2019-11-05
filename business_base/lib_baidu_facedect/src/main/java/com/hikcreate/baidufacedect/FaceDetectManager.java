package com.hikcreate.baidufacedect;

import android.content.Context;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaceDetectManager {
    private static FaceDetectManager sInstance = null;
    public static final LivenessTypeEnum[] sLivenessTypeArray = {
            LivenessTypeEnum.Eye,
//            LivenessTypeEnum.Mouth,
//            LivenessTypeEnum.HeadUp,
//            LivenessTypeEnum.HeadDown,
//            LivenessTypeEnum.HeadLeft,
//            LivenessTypeEnum.HeadRight,
//            LivenessTypeEnum.HeadLeftOrRight
    };

    private List<LivenessTypeEnum> mLivenessList = new ArrayList<LivenessTypeEnum>();
    private boolean isLivenessRandom = true;
    private int livenessRandomCount = 2;
    private float blurness = FaceEnvironment.VALUE_BLURNESS;
    private float brightness = FaceEnvironment.VALUE_BRIGHTNESS;
    private int cropFaceSize = FaceEnvironment.VALUE_CROP_FACE_SIZE;
    private int minFaceSize = FaceEnvironment.VALUE_MIN_FACE_SIZE;
    private int headPitch = FaceEnvironment.VALUE_HEAD_PITCH;
    private int headRoll = FaceEnvironment.VALUE_HEAD_ROLL;
    private int headYaw = FaceEnvironment.VALUE_HEAD_YAW;
    private float noFaceThreshold = FaceEnvironment.VALUE_NOT_FACE_THRESHOLD;
    private float occlusion = FaceEnvironment.VALUE_OCCLUSION;
    private boolean checkFaceQuality = true;
    private int faceDecodeNumberOfThreads = 2;

    public static synchronized FaceDetectManager getInstance() {
        if (sInstance == null) {
            sInstance = new FaceDetectManager();
        }
        return sInstance;
    }

    public void init(Context context, String licenseID, String licenseFileName) {
        FaceSDKManager.getInstance().initialize(context, licenseID, licenseFileName);
        if (mLivenessList == null || mLivenessList.size() < 1) {
            mLivenessList = Arrays.asList(sLivenessTypeArray);
        }
        setConfig();
    }

    private void setConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(mLivenessList);
        config.setLivenessRandom(isLivenessRandom);
        config.setBlurnessValue(blurness);
        config.setBrightnessValue(brightness);
        config.setCropFaceValue(cropFaceSize);
        config.setHeadPitchValue(headPitch);
        config.setHeadRollValue(headRoll);
        config.setHeadYawValue(headYaw);
        config.setMinFaceSize(minFaceSize);
        config.setNotFaceValue(noFaceThreshold);
        config.setOcclusionValue(occlusion);
        config.setCheckFaceQuality(checkFaceQuality);
        config.setLivenessRandomCount(livenessRandomCount);
        config.setFaceDecodeNumberOfThreads(faceDecodeNumberOfThreads);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    public FaceDetectManager setsLivenessList(List<LivenessTypeEnum> sLivenessList) {
        mLivenessList = sLivenessList;
        return this;
    }

    public FaceDetectManager setLivenessRandom(boolean livenessRandom) {
        isLivenessRandom = livenessRandom;
        return this;
    }

    public FaceDetectManager setLivenessRandomCount(int livenessRandomCount) {
        this.livenessRandomCount = livenessRandomCount;
        return this;
    }

    public FaceDetectManager setBlurness(float blurness) {
        this.blurness = blurness;
        return this;
    }

    public FaceDetectManager setBrightness(float brightness) {
        this.brightness = brightness;
        return this;
    }

    public FaceDetectManager setCropFaceSize(int cropFaceSize) {
        this.cropFaceSize = cropFaceSize;
        return this;
    }

    public FaceDetectManager setMinFaceSize(int minFaceSize) {
        this.minFaceSize = minFaceSize;
        return this;
    }

    public FaceDetectManager setHeadPitch(int headPitch) {
        this.headPitch = headPitch;
        return this;
    }

    public FaceDetectManager setHeadRoll(int headRoll) {
        this.headRoll = headRoll;
        return this;
    }

    public FaceDetectManager setHeadYaw(int headYaw) {
        this.headYaw = headYaw;
        return this;
    }

    public FaceDetectManager setNoFaceThreshold(float noFaceThreshold) {
        this.noFaceThreshold = noFaceThreshold;
        return this;
    }

    public FaceDetectManager setOcclusion(float occlusion) {
        this.occlusion = occlusion;
        return this;
    }

    public FaceDetectManager setCheckFaceQuality(boolean checkFaceQuality) {
        this.checkFaceQuality = checkFaceQuality;
        return this;
    }

    public FaceDetectManager setFaceDecodeNumberOfThreads(int faceDecodeNumberOfThreads) {
        this.faceDecodeNumberOfThreads = faceDecodeNumberOfThreads;
        return this;
    }
}
