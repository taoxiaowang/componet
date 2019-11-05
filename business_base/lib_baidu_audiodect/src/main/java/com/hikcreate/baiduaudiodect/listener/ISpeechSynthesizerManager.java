package com.hikcreate.baiduaudiodect.listener;

import android.content.Context;
import android.util.Pair;

import java.util.List;

/**
 * @author yslei
 * @date 2019/6/17
 * @email leiyongsheng@hikcreate.com
 */
public interface ISpeechSynthesizerManager {

    /**
     * 合成并播放
     *
     * @param text 小于1024 GBK字节，即512个汉字或者字母数字
     * @return
     */
    void speak(Context context, ISpeedStatusCallback callback, String text);

    /**
     * 合成并播放
     *
     * @param text        小于1024 GBK字节，即512个汉字或者字母数字
     * @param utteranceId 用于listener的回调，默认"0"
     * @return
     */
    void speakWithId(Context context, ISpeedStatusCallback callback, String text, String utteranceId);

    /**
     * 只合成不播放
     *
     * @param text
     * @return
     */
    void synthesize(Context context, ISpeedStatusCallback callback, String text);

    /**
     * 只合成不播放
     *
     * @param text
     * @return
     */
    void synthesizeWithId(Context context, ISpeedStatusCallback callback, String text, String utteranceId);

    /**
     * 批量合成并播放
     *
     * @param texts
     * @return
     */
    void batchSpeak(Context context, ISpeedStatusCallback callback, List<Pair<String, String>> texts);

    /**
     * 暂停
     *
     * @return
     */
    void pause(ISpeedStatusCallback callback);

    /**
     * 重置播放
     *
     * @return
     */
    void resume(ISpeedStatusCallback callback);

    /**
     * 停止退出
     *
     * @return
     */
    void stop(ISpeedStatusCallback callback);

    /**
     * 设置播放音量，默认已经是最大声音
     * 0.0f为最小音量，1.0f为最大音量
     *
     * @param leftVolume  [0-1] 默认1.0f
     * @param rightVolume [0-1] 默认1.0f
     */
    void setStereoVolume(float leftVolume, float rightVolume);

    /**
     * 引擎在合成时该方法不能调用！！！
     * 注意 只有 TtsMode.MIX 才可以切换离线发音
     *
     * @return
     */
    void loadModel(Context context, ISpeedStatusCallback callback, String modelFilename, String textFilename);

    /**
     * 释放
     *
     * @return
     */
    void release(ISpeedStatusCallback callback);
}
