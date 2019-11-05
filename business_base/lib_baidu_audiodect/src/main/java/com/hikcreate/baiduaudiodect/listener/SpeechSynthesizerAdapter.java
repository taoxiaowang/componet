package com.hikcreate.baiduaudiodect.listener;

/**
 * @author yslei
 * @date 2019/6/14
 * @email leiyongsheng@hikcreate.com
 */
public class SpeechSynthesizerAdapter implements ISpeechSynthesizerCallback {

    @Override
    public void onSynthesizeStart(String utteranceId) {

    }

    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] bytes, int progress) {

    }

    @Override
    public void onSynthesizeFinish(String utteranceId) {

    }

    @Override
    public void onSpeechStart(String utteranceId) {

    }

    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {

    }

    @Override
    public void onSpeechFinish(String utteranceId) {

    }

    @Override
    public void onError(String utteranceId, String des, int code) {

    }
}
