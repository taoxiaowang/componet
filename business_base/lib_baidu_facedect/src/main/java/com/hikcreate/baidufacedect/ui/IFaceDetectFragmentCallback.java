package com.hikcreate.baidufacedect.ui;

import java.util.HashMap;

/**
 * fragment 人脸检测回调
 *
 * @author yslei
 * @data 2019/4/3
 * @email leiyongsheng@hikcreate.com
 */
public interface IFaceDetectFragmentCallback {
    void onFaceDetected(String message, HashMap<String, String> base64ImageMap);
}
