package com.hikcreate.module_router;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by wanglei on 2016/12/28.
 */

public class ErrorAction extends ModuleAction {

    private static final String DEFAULT_MESSAGE = "Something was really wrong. Ha ha!";
    private int mCode;
    private String mMessage;
    private boolean mAsync;

    public ErrorAction() {
        mCode = ModuleActionResult.CODE_ERROR;
        mMessage = DEFAULT_MESSAGE;
        mAsync = false;
    }

    public ErrorAction(boolean isAsync, int code, String message) {
        this.mCode = code;
        this.mMessage = message;
        this.mAsync = isAsync;
    }

    @Override
    public boolean isAsync(HashMap<String, String> requestData) {
        return mAsync;
    }

    @Override
    public ModuleActionResult invoke(Context context, HashMap<String, String> requestData) {
        ModuleActionResult result = new ModuleActionResult.Builder()
                .code(mCode)
                .msg(mMessage)
                .data(null)
                .object(null)
                .build();
        return result;
    }

}
