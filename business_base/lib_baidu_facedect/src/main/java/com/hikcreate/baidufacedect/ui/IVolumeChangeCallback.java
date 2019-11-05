package com.hikcreate.baidufacedect.ui;

/**
 * 剩余变动回调
 *
 * @author yslei
 * @data 2019/4/3
 * @email leiyongsheng@hikcreate.com
 */
public interface IVolumeChangeCallback {
    void onVolumeChange(int currentVolume, int oldVolume, boolean enableAudio);
}
