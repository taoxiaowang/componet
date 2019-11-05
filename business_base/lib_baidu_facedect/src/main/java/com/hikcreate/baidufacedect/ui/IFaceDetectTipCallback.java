package com.hikcreate.baidufacedect.ui;

import com.baidu.idl.face.platform.FaceStatusEnum;

/**
 * fragment 人脸检测回调
 *
 * @author yslei
 * @data 2019/4/3
 * @email leiyongsheng@hikcreate.com
 */
public interface IFaceDetectTipCallback {
    void onFaceDetectTip(FaceStatusEnum status, String message);
}
