package com.hikcreate.baidutextdect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hikcreate.baidutextdect.bean.DriveLicenseMain;
import com.hikcreate.baidutextdect.bean.DriveLicenseSub;
import com.hikcreate.baidutextdect.bean.IDCardEntity;
import com.hikcreate.baidutextdect.bean.LicenseConstant;
import com.hikcreate.baidutextdect.bean.VehicleLicenseMain;
import com.hikcreate.baidutextdect.util.TextDecFileUtil;
import com.hikcreate.baidutextdect.util.TextDecTools;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * to do
 *
 * @author yslei
 * @data 2019/4/2
 * @email leiyongsheng@hikcreate.com
 */
public class TextDectManager implements ITextDectManager {

    private final String TAG = "TextDectManager";

    private volatile static TextDectManager INSTANCE;
    private final String DriveLicenseSubSin = "1e0b8e19305a41bc32160390b540a3ef";

    private Activity mActivity;
    private int mRequestCode;

    public static TextDectManager getInstance() {
        if (INSTANCE == null) {
            synchronized (TextDectManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TextDectManager();
                }
            }
        }
        return INSTANCE;
    }

    public void initTextDect(Context context, List<TextDectEnum> textDectEnumList, TextDectInitCallback callback) {

        TextDecFileUtil.initAppRootFilePath(context);
        BaiDuTextAccessTokenHelper.initAccessTokenLicenseFile(context, new BaiDuTextAccessTokenHelper.ITextAccessTokenInitCallback() {

            @Override
            public void onTextTokenGetSuccess(String token) {
                if (textDectEnumList != null && (textDectEnumList.contains(TextDectEnum.ID_CARD_DETECT_FRONT)
                        || textDectEnumList.contains(TextDectEnum.ID_CARD_DETECT_FRONT))) {
                    enableIDCardQualityModel(callback, token);
                } else {
                    if (callback != null) {
                        callback.onInitSuccess(token);
                    }
                }
            }

            @Override
            public void onTextTokenGetError(String message) {
                Log.e(TAG, "init token error:" + message);
                if (callback != null) {
                    callback.onInitError("网络跑偏了，请重试");
                }
            }
        });
    }

    @Override
    public void initTextDect(Activity activity, TextDectInitCallback callback, TextDectEnum... types) {
        mActivity = activity;
        List<TextDectEnum> textDectEnumList = new ArrayList<>();
        if (types != null && types.length > 0) {
            textDectEnumList.addAll(Arrays.asList(types));
        }
        initTextDect(activity, textDectEnumList, callback);
    }

    @Override
    public void startTextDect(TextDectEnum type, int requestCode) {
        String title = type.getName() + "扫描";
        Intent intent = null;
        mRequestCode = requestCode;
        switch (type) {
            case ID_CARD_DETECT_FRONT:
                intent = new Intent(mActivity, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        TextDecFileUtil.getImageCacheFilePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                        true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TITLE, title);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                        true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                mActivity.startActivityForResult(intent, requestCode);
                break;
            case ID_CARD_DETECT_BACK:
                intent = new Intent(mActivity, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        TextDecFileUtil.getImageCacheFilePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                        true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TITLE, title);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                        true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                mActivity.startActivityForResult(intent, requestCode);
                break;
            case DRIVING_LICENSE_DETECT_SUN:
            case DRIVING_LICENSE_DETECT_MAIN:
            case VEHICLE_LICENSE_DETECT_MAIN:
            case VEHICLE_LICENSE_DETECT_SUB:
                intent = new Intent(mActivity, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        TextDecFileUtil.getImageCacheFilePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                intent.putExtra(CameraActivity.KEY_CONTENT_TITLE, title);
                mActivity.startActivityForResult(intent, requestCode);
                break;
        }
    }

    @Override
    public void textDetect(TextDectEnum type, Intent data, TextDectCallbackAdapter callback) {
        switch (type) {
            case ID_CARD_DETECT_FRONT:
            case ID_CARD_DETECT_BACK:
                String contentType = data != null ? data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE) : CameraActivity.CONTENT_TYPE_ID_CARD_FRONT;
                String filePath = TextDecFileUtil.getImageCacheFilePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCardFront(filePath, callback);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCardBack(filePath, callback);
                    }
                }
                break;
            case DRIVING_LICENSE_DETECT_SUN:
                RecognizeService.recDriveSub(mActivity, DriveLicenseSubSin, TextDecFileUtil.getImageCacheFilePath(),
                        result -> {
                            if (callback.getClazzType().isAssignableFrom(DriveLicenseSub.class)) {
                                callback.onResult(recDriveLicenseSub(result));
                            } else {
                                callback.onResult(result);
                            }
                        });
                break;
            case DRIVING_LICENSE_DETECT_MAIN:
                RecognizeService.recDrivingLicense(mActivity, TextDecFileUtil.getImageCacheFilePath(),
                        result -> {
                            if (callback.getClazzType().isAssignableFrom(DriveLicenseMain.class)) {
                                callback.onResult(recDriveLicenseMain(result));
                            } else {
                                callback.onResult(result);
                            }
                        });
                break;
            case VEHICLE_LICENSE_DETECT_MAIN:
                RecognizeService.recVehicleLicense(mActivity, TextDecFileUtil.getImageCacheFilePath(),
                        result -> {
                            if (callback.getClazzType().isAssignableFrom(VehicleLicenseMain.class)) {
                                callback.onResult(recVehicleLicenseMain(result));
                            } else {
                                callback.onResult(result);
                            }
                        });
                break;
        }
    }

    @Override
    public void releaseTextDect() {
        // 释放本地质量控制模型
        CameraNativeHelper.release();
        // 释放内存资源
        // RecognizeService.release(mActivity);
//        mActivity = null;
    }

    private void enableIDCardQualityModel(TextDectInitCallback callback, String token) {
        //  初始化本地质量控制模型,释放代码在onDestory中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(mActivity, OCR.getInstance(mActivity).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        if (callback != null) {
                            callback.onInitError("本地质量控制初始化错误，错误原因： " + msg);
                        }
                    }

                    @Override
                    public void onSuccess() {
                        if (callback != null) {
                            callback.onInitSuccess(token);
                        }
                    }
                });
    }

    public void recIDCardFront(TextDectCallbackAdapter callback) {
        recIDCardFront(TextDecFileUtil.getImageCacheFilePath(), callback);
    }

    public void recIDCardFront(String filePath, TextDectCallbackAdapter callback) {
        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath, callback);
    }

    public void recIDCardBack(TextDectCallbackAdapter callback) {
        recIDCardBack(TextDecFileUtil.getImageCacheFilePath(), callback);
    }

    public void recIDCardBack(String filePath, TextDectCallbackAdapter callback) {
        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath, callback);
    }

    /**
     * 身份证图像识别
     *
     * @param idCardSide
     * @param filePath
     */
    private void recIDCard(String idCardSide, String filePath, TextDectCallbackAdapter callback) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance(mActivity).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (callback.getClazzType().isAssignableFrom(IDCardEntity.class)) {
                    IDCardEntity entity = new IDCardEntity();
                    entity.setAddress(getWordsContent(result.getAddress()));
                    entity.setBirthday(getWordsContent(result.getBirthday()));
                    entity.setEthnic(getWordsContent(result.getEthnic()));
                    entity.setDirection(result.getDirection());
                    entity.setExpiryDate(getWordsContent(result.getExpiryDate()));
                    entity.setGender(getWordsContent(result.getGender()));
                    entity.setIdCardSide(result.getIdCardSide());
                    entity.setIdNumber(getWordsContent(result.getIdNumber()));
                    entity.setImageStatus(result.getImageStatus());
                    entity.setIssueAuthority(getWordsContent(result.getIssueAuthority()));
                    entity.setName(getWordsContent(result.getName()));
                    entity.setRiskType(result.getRiskType());
                    entity.setSignDate(getWordsContent(result.getSignDate()));
                    entity.setWordsResultNumber(result.getWordsResultNumber());
                    callback.onResult(entity);
                } else if (callback.getClazzType().isAssignableFrom(String.class)) {
                    callback.onResult(result.getJsonRes());
                }
            }

            @Override
            public void onError(OCRError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public DriveLicenseSub recDriveLicenseSub(String result) {
        if (TextDecTools.isJsonForGsonValid(result)) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(result).getAsJsonObject();
            JsonObject dataJO = jsonObject.get("data").getAsJsonObject();
            if (dataJO != null && dataJO.has("ret") && dataJO.has("templateSign")
                    && DriveLicenseSubSin.equals(dataJO.get("templateSign").getAsString())) {
                JsonArray rets = dataJO.get("ret").getAsJsonArray();
                HashMap<String, String> map = new HashMap<>();
                if (rets != null && rets.size() > 0) {
                    for (JsonElement element : rets) {
                        JsonObject item = element.getAsJsonObject();
                        String keyName = item.get("word_name").getAsString();
                        String keyValue = item.get("word").getAsString();
                        map.put(keyName, keyValue);
                    }
                } else {
                    return null;
                }
                DriveLicenseSub sub = new DriveLicenseSub();
                sub.setLicenseId(map.get("id"));
                sub.setLicenseName(map.get("name"));
                sub.setLicenseNo(map.get("no"));
                sub.setLicenseRecord(map.get("record"));
                return sub;
            }
        }
        return null;
    }

    public DriveLicenseMain recDriveLicenseMain(String result) {
        if (TextDecTools.isJsonForGsonValid(result)) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(result).getAsJsonObject();
            if (jsonObject != null && jsonObject.has("words_result")) {
                JsonObject resultJO = jsonObject.get("words_result").getAsJsonObject();
                if (resultJO == null || resultJO.size() == 0) return null;
                DriveLicenseMain main = new DriveLicenseMain();
                main.setLicenseAddress(getLicenseValue(resultJO, LicenseConstant.LICENSE_ADDRESS));
                main.setLicenseBirth(getLicenseValue(resultJO, LicenseConstant.LICENSE_BIRTH));
                main.setLicenseClass(getLicenseValue(resultJO, LicenseConstant.LICENSE_CLASS));
                main.setLicenseFirstIssue(getLicenseValue(resultJO, LicenseConstant.LICENSE_FIRST_ISSUE));
                main.setLicenseGender(genderConvert(getLicenseValue(resultJO, LicenseConstant.LICENSE_GENDER)));
                main.setLicenseName(getLicenseValue(resultJO, LicenseConstant.LICENSE_NAME));
                main.setLicenseNation(getLicenseValue(resultJO, LicenseConstant.NATIONALITY));
                main.setLicenseNo(getLicenseValue(resultJO, LicenseConstant.LICENSE_NO));
                main.setLicenseValidEnd(getLicenseValue(resultJO, LicenseConstant.VALIDE_PERIOD_GAP));
                main.setLicenseValidStart(getLicenseValue(resultJO, LicenseConstant.VALIDE_PERIOD));
                return main;
            }
        }
        return null;
    }

    public VehicleLicenseMain recVehicleLicenseMain(String result) {
        if (TextDecTools.isJsonForGsonValid(result)) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(result).getAsJsonObject();
            if (jsonObject != null && jsonObject.has("words_result")) {
                JsonObject resultJO = jsonObject.get("words_result").getAsJsonObject();
                if (resultJO == null || resultJO.size() == 0) return null;
                VehicleLicenseMain main = new VehicleLicenseMain();
                main.setLicenseAddress(getLicenseValue(resultJO, LicenseConstant.LICENSE_ADDRESS));
                main.setLicenseEngineNo(getLicenseValue(resultJO, LicenseConstant.LICENSE_ENGINE_NO));
                main.setLicenseIssueDate(getLicenseValue(resultJO, LicenseConstant.LICENSE_ISSUE_DATE));
                main.setLicenseModel(getLicenseValue(resultJO, LicenseConstant.LICENSE_MODEL));
                main.setLicenseOwner(getLicenseValue(resultJO, LicenseConstant.LICENSE_OWNER));
                main.setLicensePlateNo(getLicenseValue(resultJO, LicenseConstant.LICENSE_PLATE_NO));
                main.setLicenseRegisterDate(getLicenseValue(resultJO, LicenseConstant.LICENSE_REGISTER_DATE));
                main.setLicenseUseCharacter(getLicenseValue(resultJO, LicenseConstant.LICENSE_USE_CHARACTER));
                main.setLicenseVehicleType(getLicenseValue(resultJO, LicenseConstant.LICENSE_VEHICLE_TYPE));
                main.setLicenseVIN(getLicenseValue(resultJO, LicenseConstant.LICENSE_VIN));
                return main;
            }
        }
        return null;
    }

    private String getLicenseValue(JsonObject jsonObject, String key) {
        if (jsonObject != null) {
            if (jsonObject.has(key)) {
                JsonObject item = jsonObject.get(key).getAsJsonObject();
                if (item != null && item.has("words")) {
                    return item.get("words").getAsString();
                }
            }
        }
        return null;
    }

    private int genderConvert(String gender) {
        if (LicenseConstant.LICENSE_MALE.equals(gender)) {
            return 2;
        } else {
            return 1;
        }
    }

    private String getWordsContent(Word word) {
        if (word != null) {
            return word.getWords();
        } else {
            return null;
        }
    }

    private String getBytesContent(Byte[] bytes) {
        if (bytes != null) {
            return bytes.toString();
        } else {
            return null;
        }
    }
}
