package com.hikcreate.baidutextdect;

import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

/**
 * to do
 *
 * @author yslei
 * @data 2019/3/29
 * @email leiyongsheng@hikcreate.com
 */
public class BaiDuTextAccessTokenHelper {

    public static void initAccessTokenLicenseFile(Context context, ITextAccessTokenInitCallback callback) {
        OCR.getInstance(context).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                callback.onTextTokenGetSuccess(token);
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                callback.onTextTokenGetError(error.getMessage());
            }
        }, "aip.license", context.getApplicationContext());
    }

    public interface ITextAccessTokenInitCallback {
        void onTextTokenGetSuccess(String token);

        void onTextTokenGetError(String message);
    }
}
