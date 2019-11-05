package com.hikcreate.library.plugin.netbase.exception;

import android.text.TextUtils;
import android.widget.TabHost;

/**
 * 服务器产生的Excption，比如404，503等等服务器返回的Excption。
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class ApiException extends RuntimeException {
    private int code;
    private String displayMessage;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        if (throwable != null) {
            this.displayMessage = throwable.getMessage();
        }
    }

    public ApiException(int code, String displayMessage) {
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public ApiException(int code, String message, String displayMessage) {
        super(message);
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    @Override
    public String getMessage() {
        if (!TextUtils.isEmpty(getDisplayMessage())) {
            return getDisplayMessage();
        } else {
            return super.getMessage();
        }
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "code=" + code +
                ", displayMessage='" + displayMessage + '\'' +
                '}';
    }
}