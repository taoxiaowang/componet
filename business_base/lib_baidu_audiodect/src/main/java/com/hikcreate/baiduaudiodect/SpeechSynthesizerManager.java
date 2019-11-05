package com.hikcreate.baiduaudiodect;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Pair;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.sample.util.OfflineResource;
import com.hikcreate.baiduaudiodect.listener.ASpeechSynthesizerManager;
import com.hikcreate.baiduaudiodect.listener.IInitCallback;
import com.hikcreate.baiduaudiodect.listener.ISpeedFocusCallback;
import com.hikcreate.baiduaudiodect.listener.ISpeedInitStatusCallback;
import com.hikcreate.baiduaudiodect.listener.ISpeedStatusCallback;
import com.hikcreate.baiduaudiodect.listener.SpeechSynthesizerAdapter;
import com.hikcreate.baiduaudiodect.model.Config;
import com.hikcreate.baiduaudiodect.model.Informant;
import com.hikcreate.baiduaudiodect.model.MixMode;
import com.hikcreate.baiduaudiodect.model.OfflineVoiceType;
import com.hikcreate.baiduaudiodect.model.TtsMode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yslei
 * @date 2019/6/14
 * @email leiyongsheng@hikcreate.com
 */
public class SpeechSynthesizerManager extends ASpeechSynthesizerManager {

    private static SpeechSynthesizerManager sInstance = null;

    public static synchronized SpeechSynthesizerManager getInstance() {
        if (sInstance == null) {
            sInstance = new SpeechSynthesizerManager();
        }
        return sInstance;
    }

    private Context mApplicationContext;
    private AudioManager mAudioManager;
    private AudioFocuseChangeCallback mAudioFocusChangeListener;
    private MainHandler mMainHandler;
    private SpeechSynthesizer mSpeechSynthesizer;
    private boolean mIsInitSuccess;
    private Informant mInformant = Informant.COMMON_FEMALE_INFORMANT;//发音人
    private OfflineVoiceType mOfflineVoiceType = OfflineVoiceType.COMMON_FEMALE_OFFLINE_INFORMANT;
    private int mSynthesizerVolume = 9;//合成的音量，0-9
    private int mSynthesizerSpeed = 5;//合成的语速，0-9
    private int mSynthesizerPitch = 5;//设置合成的语调，0-9
    private MixMode mSynthesizerMode = MixMode.MIX_MODE_DEFAULT;//网络合成模式
    private TtsMode mTtsMode = TtsMode.MODE_MIX;//合成模式
    private String mTextFilename;//离线文件
    private String mModelFilename;//离线model文件
    private SpeechSynthesizerAdapter mSpeechSynthesizerAdapter;
    private Map<String, ISpeedStatusCallback> mStatusCallbackMap;

    public SpeechSynthesizerManager setInformant(Informant informant) {
        mInformant = informant;
        return this;
    }

    public SpeechSynthesizerManager setSynthesizerVolume(int synthesizerVolume) {
        mSynthesizerVolume = synthesizerVolume;
        return this;
    }

    public SpeechSynthesizerManager setSynthesizerSpeed(int synthesizerSpeed) {
        mSynthesizerSpeed = synthesizerSpeed;
        return this;
    }

    public SpeechSynthesizerManager setSynthesizerPitch(int synthesizerPitch) {
        mSynthesizerPitch = synthesizerPitch;
        return this;
    }

    public SpeechSynthesizerManager setSynthesizerMode(MixMode synthesizerMode) {
        mSynthesizerMode = synthesizerMode;
        return this;
    }

    public SpeechSynthesizerManager setTtsMode(TtsMode ttsMode) {
        mTtsMode = ttsMode;
        return this;
    }

    public void setOfflineVoiceType(OfflineVoiceType offlineVoiceType) {
        mOfflineVoiceType = offlineVoiceType;
    }

    public void setSpeechSynthesizerAdapter(SpeechSynthesizerAdapter speechSynthesizerAdapter) {
        mSpeechSynthesizerAdapter = speechSynthesizerAdapter;
    }

    public int init(Context context, IInitCallback callback) {
        mApplicationContext = context;
        mStatusCallbackMap = new HashMap<>();
        mMainHandler = new MainHandler(context.getMainLooper());

        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(mApplicationContext);
        mSpeechSynthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerEvent());

        // 请替换为语音开发者平台上注册应用得到的App ID ,AppKey ，Secret Key ，填写在SynthActivity的开始位置
        mSpeechSynthesizer.setAppId(Config.appId);
        mSpeechSynthesizer.setApiKey(Config.appKey, Config.secretKey);

        callback.onTtsInit();

        // 初始化tts
        int result = mSpeechSynthesizer.initTts(mTtsMode.mode);
        mIsInitSuccess = result == 0;

        return result;
    }

    private void startTts(Context context, ISpeedInitStatusCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            callback.onError(-1, "暂无权限");
            return;
        }
        if (!mIsInitSuccess) {
            Observable.create((ObservableOnSubscribe<Pair<Integer, String>>) observableEmitter -> {
                int result = init(context.getApplicationContext(), () -> {
                    boolean isMix = mTtsMode.equals(TtsMode.MODE_MIX);
                    if (isMix) {
                        if (!checkOfflineResources()) {
                            initOfflineResource(mOfflineVoiceType);
                        }
                    }
                    initParams(mSpeechSynthesizer);
                    if (isMix) {
                        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。选择纯在线可以不必调用auth方法。
                        AuthInfo authInfo = mSpeechSynthesizer.auth(mTtsMode.mode);
                        if (!authInfo.isSuccess()) {
                            // 离线授权需要网站上的应用填写包名。本demo的包名是com.baidu.tts.sample，定义在build.gradle中
                            String errorMsg = authInfo.getTtsError().getDetailMessage();
                            observableEmitter.onNext(new Pair<>(authInfo.getTtsError().getCode(), errorMsg));
                        }
                    }
                });
                if (result == 0) {
                    observableEmitter.onNext(new Pair<>(result, null));
                } else {
                    observableEmitter.onNext(new Pair<>(result, "语音合成初始化失败"));
                }
                observableEmitter.onComplete();
            }).compose(applySchedulers())
                    .subscribe(integerStringPair -> {
                        if (callback != null && integerStringPair != null) {
                            if (integerStringPair.first == 0) {
                                callback.onSuccess();
                            } else {
                                callback.onError(integerStringPair.first, integerStringPair.second);
                            }
                        }
                    });
        } else {
            callback.onSuccess();
        }
    }

    public  <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    private boolean checkOfflineResources() {
        String[] filenames = {mTextFilename, mModelFilename};
        for (String path : filenames) {
            if (TextUtils.isEmpty(mTextFilename)) return false;
            File f = new File(path);
            if (f.exists() && f.canRead()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    private void initOfflineResource(OfflineVoiceType offlineVoice) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(mApplicationContext, offlineVoice);
            mTextFilename = offlineResource.getTextFilename();
            mModelFilename = offlineResource.getModelFilename();
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
        }
    }

    private void initParams(SpeechSynthesizer speechSynthesizer) {
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, String.valueOf(mInformant.ordinal()));
        // 设置合成的音量，0-9 ，默认 9
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, String.valueOf(mSynthesizerVolume));
        // 设置合成的语速，0-9 ，默认 5
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, String.valueOf(mSynthesizerSpeed));
        // 设置合成的语调，0-9 ，默认 5
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, String.valueOf(mSynthesizerPitch));
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, mSynthesizerMode.modeName);

        if (!TextUtils.isEmpty(mTextFilename)) {
            speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mTextFilename);
        }
        if (!TextUtils.isEmpty(mModelFilename)) {
            speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                    mModelFilename);
        }
    }

    private String generatorRandomUtteranceId() {
        return "Ss_" + Math.random() * 1000;
    }

    private void resetAudioFocus(Context context, ISpeedFocusCallback callback) {
        if (mAudioFocusChangeListener == null) {
            mAudioFocusChangeListener = new AudioFocuseChangeCallback();
        }
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        if (mAudioManager != null) {
            int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,// Use the music stream.
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);// Request permanent focus.
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                callback.onFocusGain(true);
            } else {
                callback.onFocusGain(false);
            }
        } else {
            callback.onFocusGain(false);
        }
    }

    private void releaseAudioFocus() {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }

    @Override
    public void speak(Context context, ISpeedStatusCallback callback, String text) {
        speakWithId(context, callback, text, generatorRandomUtteranceId());
    }

    @Override
    public void speakWithId(Context context, ISpeedStatusCallback callback, String text, String utteranceId) {
        resetAudioFocus(context, gain -> startTts(context, new ISpeedInitStatusCallback() {

            @Override
            public void onError(int code, String des) {
                if (callback != null) callback.onError(code, des);
            }

            @Override
            public void onSuccess() {
                if (callback != null) mStatusCallbackMap.put(utteranceId, callback);
                int result = mSpeechSynthesizer.speak(text, utteranceId);
                if (result != 0 && callback != null) {
                    callback.onError(result, "合成播放失败");
                }
            }
        }));
    }

    @Override
    public void synthesize(Context context, ISpeedStatusCallback callback, String text) {
        synthesizeWithId(context, callback, text, generatorRandomUtteranceId());
    }

    @Override
    public void synthesizeWithId(Context context, ISpeedStatusCallback callback, String text, String utteranceId) {
        startTts(context, new ISpeedInitStatusCallback() {

            @Override
            public void onError(int code, String des) {
                if (callback != null) callback.onError(code, des);
            }

            @Override
            public void onSuccess() {
                if (callback != null) mStatusCallbackMap.put(utteranceId, callback);
                int result = mSpeechSynthesizer.synthesize(text, utteranceId);
                if (result != 0 && callback != null) {
                    callback.onError(result, "合成失败");
                }
            }
        });
    }

    @Override
    public void batchSpeak(Context context, ISpeedStatusCallback callback, List<Pair<String, String>> texts) {
        resetAudioFocus(context, gain -> startTts(context, new ISpeedInitStatusCallback() {

            @Override
            public void onError(int code, String des) {
                if (callback != null) callback.onError(code, des);
            }

            @Override
            public void onSuccess() {
                List<SpeechSynthesizeBag> bags = new ArrayList<SpeechSynthesizeBag>();
                for (Pair<String, String> pair : texts) {
                    SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
                    speechSynthesizeBag.setText(pair.first);
                    if (pair.second != null) {
                        speechSynthesizeBag.setUtteranceId(pair.second);
                    }
                    bags.add(speechSynthesizeBag);

                }
                int result = mSpeechSynthesizer.batchSpeak(bags);
                if (result != 0 && callback != null) {
                    callback.onError(result, "批量合成播放失败");
                }
            }
        }));
    }

    @Override
    public void pause(ISpeedStatusCallback callback) {
        if (mSpeechSynthesizer != null) {
            int result = mSpeechSynthesizer.pause();
            if (result != 0 && callback != null) {
                callback.onError(result, "暂停合成播放失败");
            }
        } else {
            if (callback != null) {
                callback.onError(-1, "未初始化");
            }
        }
    }

    @Override
    public void resume(ISpeedStatusCallback callback) {
        if (mSpeechSynthesizer != null) {
            int result = mSpeechSynthesizer.resume();
            if (result != 0 && callback != null) {
                callback.onError(result, "重启合成播放失败");
            }
        } else {
            if (callback != null) {
                callback.onError(-1, "未初始化");
            }
        }
    }

    @Override
    public void stop(ISpeedStatusCallback callback) {
        if (mSpeechSynthesizer != null) {
            int result = mSpeechSynthesizer.stop();
            if (result != 0 && callback != null) {
                callback.onError(result, "取消合成播放失败");
            }
        } else {
            if (callback != null) {
                callback.onError(-1, "未初始化");
            }
        }
    }

    @Override
    public void setStereoVolume(float leftVolume, float rightVolume) {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.setStereoVolume(leftVolume, rightVolume);
        }
    }

    @Override
    public void loadModel(Context context, ISpeedStatusCallback callback, String modelFilename, String textFilename) {
        startTts(context, new ISpeedInitStatusCallback() {

            @Override
            public void onError(int code, String des) {
                if (callback != null) callback.onError(code, des);
            }

            @Override
            public void onSuccess() {
                int result = mSpeechSynthesizer.loadModel(modelFilename, textFilename);
                if (result != 0 && callback != null) {
                    callback.onError(result, "加载离线失败");
                }
            }
        });
    }

    @Override
    public void release(ISpeedStatusCallback callback) {
        if (mSpeechSynthesizer != null) {
            int result = mSpeechSynthesizer.release();
            if (result != 0 && callback != null) {
                callback.onError(result, "释放合成播放失败");
            }
        } else {
            if (callback != null) {
                callback.onError(-1, "未初始化");
            }
        }
    }


    public class SpeechSynthesizerEvent implements SpeechSynthesizerListener {

        private void start(String utteranceId) {
            ISpeedStatusCallback callback = mStatusCallbackMap.get(utteranceId);
            if (callback != null) {
                mMainHandler.post(() -> callback.onStart(utteranceId));
            }
        }

        private void finish(String utteranceId) {
            ISpeedStatusCallback callback = mStatusCallbackMap.get(utteranceId);
            if (callback != null) {
                mMainHandler.post(() -> callback.onFinish(utteranceId));
                mStatusCallbackMap.remove(utteranceId);
            }
        }

        private void error(int code, String utteranceId) {
            ISpeedStatusCallback callback = mStatusCallbackMap.get(utteranceId);
            if (callback != null) {
                mMainHandler.post(() -> callback.onError(code, utteranceId));
                mStatusCallbackMap.remove(utteranceId);
            }
        }

        @Override
        public void onSynthesizeStart(String s) {
            start(s);
            if (mSpeechSynthesizerAdapter != null) {
                mMainHandler.post(() -> mSpeechSynthesizerAdapter.onSynthesizeStart(s));
            }
        }

        @Override
        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
            if (mSpeechSynthesizerAdapter != null) {
                mMainHandler.post(() -> mSpeechSynthesizerAdapter.onSynthesizeDataArrived(s, bytes, i));
            }
        }

        @Override
        public void onSynthesizeFinish(String s) {
            if (mSpeechSynthesizerAdapter != null) {
                mMainHandler.post(() -> mSpeechSynthesizerAdapter.onSynthesizeFinish(s));
            }
        }

        @Override
        public void onSpeechStart(String s) {
            if (mSpeechSynthesizerAdapter != null) {
                mMainHandler.post(() -> mSpeechSynthesizerAdapter.onSpeechStart(s));
            }
        }

        @Override
        public void onSpeechProgressChanged(String s, int i) {
            if (mSpeechSynthesizerAdapter != null) {
                mMainHandler.post(() -> mSpeechSynthesizerAdapter.onSpeechProgressChanged(s, i));
            }
        }

        @Override
        public void onSpeechFinish(String s) {
            finish(s);
            if (mSpeechSynthesizerAdapter != null) {
                mMainHandler.post(() -> mSpeechSynthesizerAdapter.onSpeechFinish(s));
            }
            releaseAudioFocus();
        }

        @Override
        public void onError(String s, SpeechError speechError) {
            error(speechError.code, speechError.description);
            if (mSpeechSynthesizerAdapter != null) {
                mMainHandler.post(() -> mSpeechSynthesizerAdapter.onError(s, speechError.description, speechError.code));
            }
        }
    }

    public class AudioFocuseChangeCallback implements AudioManager.OnAudioFocusChangeListener {

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //mAudioManager.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
                mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
                // Stop playback
            }
        }
    }

    public static class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }
    }
}
