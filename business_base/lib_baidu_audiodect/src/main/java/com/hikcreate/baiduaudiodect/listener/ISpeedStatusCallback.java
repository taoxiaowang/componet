package com.hikcreate.baiduaudiodect.listener;

/**
 * @author yslei
 * @date 2019/6/17
 * @email leiyongsheng@hikcreate.com
 */
public interface ISpeedStatusCallback {
    void onError(int code, String des);

    void onStart(String utteranceId);

    void onFinish(String utteranceId);
}
