package com.hikcreate.baiduaudiodect.listener;

import android.content.Context;
import android.util.Pair;

import java.util.List;

/**
 * @author yslei
 * @date 2019/6/17
 * @email leiyongsheng@hikcreate.com
 */
public abstract class ASpeechSynthesizerManager implements ISpeechSynthesizerManager {

    public void speak(Context context, String text) {
        speak(context, null, text);
    }

    public void speakWithId(Context context, String text, String utteranceId) {
        speakWithId(context, null, text, utteranceId);
    }

    public void synthesize(Context context, String text) {
        synthesize(context, null, text);
    }

    public void synthesizeWithId(Context context, String text, String utteranceId) {
        synthesizeWithId(context, null, text, utteranceId);
    }

    public void batchSpeak(Context context, List<Pair<String, String>> texts) {
        batchSpeak(context, null, texts);
    }

    public void pause() {
        pause(null);
    }

    public void resume() {
        release(null);
    }

    public void stop() {
        stop(null);
    }

    public void loadModel(Context context, String modelFilename, String textFilename) {
        loadModel(context, null, modelFilename, textFilename);
    }

    public void release() {
        release(null);
    }
}
