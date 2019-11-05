package com.hikcreate.baiduaudiodect.model;

/**
 * TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
 *
 * @author yslei
 * @date 2019/6/14
 * @email leiyongsheng@hikcreate.com
 */
public enum TtsMode {
    MODE_MIX(com.baidu.tts.client.TtsMode.MIX),
    MODE_ONLINE(com.baidu.tts.client.TtsMode.ONLINE);

    public com.baidu.tts.client.TtsMode mode;

    TtsMode(com.baidu.tts.client.TtsMode mode) {
        this.mode = mode;
    }
}
