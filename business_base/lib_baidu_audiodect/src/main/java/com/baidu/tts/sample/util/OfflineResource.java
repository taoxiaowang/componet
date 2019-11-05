package com.baidu.tts.sample.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.hikcreate.baiduaudiodect.model.OfflineVoiceType;

import java.io.IOException;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


/**
 * Created by fujiayi on 2017/5/19.
 */

public class OfflineResource {

    private static final String SAMPLE_DIR = "baiduTTS";

    private AssetManager assets;
    private String destPath;

    private String textFilename;
    private String modelFilename;

    private static HashMap<String, Boolean> mapInitied = new HashMap<String, Boolean>();

    public OfflineResource(Context context, OfflineVoiceType offlineVoiceType) throws IOException {
        context = context.getApplicationContext();
        this.assets = context.getApplicationContext().getAssets();
        this.destPath = FileUtil.createTmpDir(context);
        setOfflineVoiceType(offlineVoiceType);
    }

    public String getModelFilename() {
        return modelFilename;
    }

    public String getTextFilename() {
        return textFilename;
    }

    public void setOfflineVoiceType(OfflineVoiceType offlineVoiceType) throws IOException {
        String text = "bd_etts_text.dat";
        String model;
        switch (offlineVoiceType){
            case COMMON_MALE_OFFLINE_INFORMANT:
                model = "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat";
                break;
            case COMMON_FEMALE_OFFLINE_INFORMANT:
                model = "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat";
                break;
            case EMOTION_DXY_OFFLINE_INFORMANT:
                model = "bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat";
                break;
            case EMOTION_DYY_OFFLINE_INFORMANT:
                model = "bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat";
                break;
            default:
                throw new RuntimeException("voice type is not in list");
        }

        textFilename = copyAssetsFile(text);
        modelFilename = copyAssetsFile(model);

    }


    private String copyAssetsFile(String sourceFilename) throws IOException {
        String destFilename = destPath + "/" + sourceFilename;
        boolean recover = false;
        Boolean existed = mapInitied.get(sourceFilename); // 启动时完全覆盖一次
        if (existed == null || !existed) {
            recover = true;
        }
        FileUtil.copyFromAssets(assets, sourceFilename, destFilename, recover);
        Log.i(TAG, "文件复制成功：" + destFilename);
        return destFilename;
    }


}
