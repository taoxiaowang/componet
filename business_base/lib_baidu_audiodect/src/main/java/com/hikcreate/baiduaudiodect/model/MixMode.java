package com.hikcreate.baiduaudiodect.model;

import com.baidu.tts.client.SpeechSynthesizer;

/**
 * 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
 * MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
 * MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
 * MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
 * MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
 *
 * @author yslei
 * @date 2019/6/14
 * @email leiyongsheng@hikcreate.com
 */
public enum MixMode {
    MIX_MODE_DEFAULT(SpeechSynthesizer.MIX_MODE_DEFAULT),
    MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI(SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI),
    MIX_MODE_HIGH_SPEED_NETWORK(SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK),
    MIX_MODE_HIGH_SPEED_SYNTHESIZE(SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE);

    public String modeName;

    MixMode(String modeName) {
        this.modeName = modeName;
    }
}
