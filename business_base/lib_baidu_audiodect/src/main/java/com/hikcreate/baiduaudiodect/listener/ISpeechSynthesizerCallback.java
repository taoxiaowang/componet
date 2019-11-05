package com.hikcreate.baiduaudiodect.listener;

/**
 * @author yslei
 * @date 2019/6/14
 * @email leiyongsheng@hikcreate.com
 */
public interface ISpeechSynthesizerCallback {

    void onSynthesizeStart(String utteranceId);

    void onSynthesizeDataArrived(String utteranceId, byte[] bytes, int progress);

    void onSynthesizeFinish(String utteranceId);

    void onSpeechStart(String utteranceId);

    void onSpeechProgressChanged(String utteranceId, int progress);

    void onSpeechFinish(String utteranceId);

    void onError(String utteranceId, String des, int code);
}
