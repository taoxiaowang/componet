/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui;

import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceStatusEnum;
import com.hikcreate.baidufacedect.R;

/**
 * sdk使用Res资源设置功能
 */
public class FaceSDKResSettings {

    public static void initializeResId() {

        // Sound Res Id
        FaceEnvironment.setSoundId(FaceStatusEnum.Detect_NoFace, R.raw.detect_face_in);
        FaceEnvironment.setSoundId(FaceStatusEnum.Detect_FacePointOut, R.raw.detect_face_in);
        FaceEnvironment.setSoundId(FaceStatusEnum.Liveness_Eye, R.raw.liveness_eye);
        FaceEnvironment.setSoundId(FaceStatusEnum.Liveness_Mouth, R.raw.liveness_mouth);
        FaceEnvironment.setSoundId(FaceStatusEnum.Liveness_HeadUp, R.raw.liveness_head_up);
        FaceEnvironment.setSoundId(FaceStatusEnum.Liveness_HeadDown, R.raw.liveness_head_down);
        FaceEnvironment.setSoundId(FaceStatusEnum.Liveness_HeadLeft, R.raw.liveness_head_left);
        FaceEnvironment.setSoundId(FaceStatusEnum.Liveness_HeadRight, R.raw.liveness_head_right);
        FaceEnvironment.setSoundId(FaceStatusEnum.Liveness_HeadLeftRight, R.raw.liveness_head_left_right);
        FaceEnvironment.setSoundId(FaceStatusEnum.Liveness_OK, R.raw.face_good);
        FaceEnvironment.setSoundId(FaceStatusEnum.OK, R.raw.face_good);

        // Tips Res Id
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_NoFace, R.string.baidu_detect_no_face);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_FacePointOut, R.string.baidu_detect_face_in);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_PoorIllumintion, R.string.baidu_detect_low_light);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_ImageBlured, R.string.baidu_detect_keep);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_OccLeftEye, R.string.baidu_detect_occ_face);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_OccRightEye, R.string.baidu_detect_occ_face);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_OccNose, R.string.baidu_detect_occ_face);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_OccMouth, R.string.baidu_detect_occ_face);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_OccLeftContour, R.string.baidu_detect_occ_face);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_OccRightContour, R.string.baidu_detect_occ_face);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_OccChin, R.string.baidu_detect_occ_face);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_PitchOutOfUpMaxRange, R.string.baidu_detect_head_down);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_PitchOutOfDownMaxRange, R.string.baidu_detect_head_up);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_PitchOutOfLeftMaxRange, R.string.baidu_detect_head_right);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_PitchOutOfRightMaxRange, R.string.baidu_detect_head_left);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_FaceZoomIn, R.string.baidu_detect_zoom_in);
        FaceEnvironment.setTipsId(FaceStatusEnum.Detect_FaceZoomOut, R.string.baidu_detect_zoom_out);

        FaceEnvironment.setTipsId(FaceStatusEnum.Liveness_Eye, R.string.baidu_liveness_eye);
        FaceEnvironment.setTipsId(FaceStatusEnum.Liveness_Mouth, R.string.baidu_liveness_mouth);
        FaceEnvironment.setTipsId(FaceStatusEnum.Liveness_HeadUp, R.string.baidu_liveness_head_up);
        FaceEnvironment.setTipsId(FaceStatusEnum.Liveness_HeadDown, R.string.baidu_liveness_head_down);
        FaceEnvironment.setTipsId(FaceStatusEnum.Liveness_HeadLeft, R.string.baidu_liveness_head_left);
        FaceEnvironment.setTipsId(FaceStatusEnum.Liveness_HeadRight, R.string.baidu_liveness_head_right);
        FaceEnvironment.setTipsId(FaceStatusEnum.Liveness_HeadLeftRight, R.string.baidu_liveness_head_left_right);
        FaceEnvironment.setTipsId(FaceStatusEnum.Liveness_OK, R.string.baidu_liveness_good);
        FaceEnvironment.setTipsId(FaceStatusEnum.OK, R.string.baidu_liveness_good);

        FaceEnvironment.setTipsId(FaceStatusEnum.Error_Timeout, R.string.baidu_detect_timeout);
        FaceEnvironment.setTipsId(FaceStatusEnum.Error_DetectTimeout, R.string.baidu_detect_timeout);
        FaceEnvironment.setTipsId(FaceStatusEnum.Error_LivenessTimeout, R.string.baidu_detect_timeout);
    }
}
