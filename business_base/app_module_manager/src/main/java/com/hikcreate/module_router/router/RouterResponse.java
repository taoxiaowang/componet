package com.hikcreate.module_router.router;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author wangtao55
 * @date 2019/9/19
 * @mail wangtao55@hikcreate.com
 */

public class RouterResponse {
    /**
     * This field is MaActionResult.toString()
     */
    public String mResultString;
    public boolean mIsAsync = true;
    public Future<String> mAsyncResponse;
    public Object mObject;
    private static final int TIME_OUT = 30 * 1000;
    private long mTimeOut;
    private boolean mHasGet = false;
    private int mCode = -1;
    private String mMessage = "";
    private String mData;
    HashMap<String, String> keyValues;

    public RouterResponse() {
        this(TIME_OUT);
    }

    public RouterResponse(long timeout) {
        if (timeout > TIME_OUT * 2 || timeout < 0) {
            timeout = TIME_OUT;
        }
        mTimeOut = timeout;
    }

    public boolean isAsync() {
        return mIsAsync;
    }

    public String get() {
        try {
            if (mIsAsync) {
                mResultString = mAsyncResponse.get(mTimeOut, TimeUnit.MILLISECONDS);
                parseResult();
            } else {
                parseResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mResultString;
    }

    private void parseResult() {
        if (!mHasGet) {
            try {
                JSONObject jsonObject = new JSONObject(mResultString);
                this.mCode = jsonObject.getInt("code");
                this.mMessage = jsonObject.getString("msg");
                this.mData = jsonObject.getString("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mHasGet = true;
        }
    }

    public int getCode() {
        if (!mHasGet) {
            get();
        }
        return mCode;
    }

    public String getMessage() {
        if (!mHasGet) {
            get();
        }
        return mMessage;
    }

    public String getData() {
        if (!mHasGet) {
            get();
        }
        return mData;
    }

    public Object getObject() {
        if (!mHasGet) {
            get();
        }
        return mObject;
    }
}
