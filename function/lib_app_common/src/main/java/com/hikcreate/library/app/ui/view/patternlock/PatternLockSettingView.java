package com.hikcreate.library.app.ui.view.patternlock;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.hikcreate.library.R;

import java.util.List;

public class PatternLockSettingView extends android.support.constraint.ConstraintLayout {


    private TextView mTvTip;
    private PatternLockView mPatternLockViewTip;
    private PatternLockView mPatternLockViewSettingValue;
    private PatternLockViewSetListener mPatternLockViewSetListener;
    private String mFirstSetValue;
    private final static float mRate = 0.9f;

    public PatternLockSettingView(Context context) {
        super(context);
        init(context);
    }

    public PatternLockSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PatternLockSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_pattern_lock, this, true);
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvTip = findViewById(R.id.pattern_lock_state_tv_view);
        mTvTip.setText(R.string.lib_pattern_input_pattern_lock);
        mPatternLockViewTip = findViewById(R.id.pattern_lock_view_tip);
        mPatternLockViewTip.setInStealthMode(false);
        mPatternLockViewTip.setEnabled(false);
        mPatternLockViewTip.resetPattern();
        mPatternLockViewTip.setTactileFeedbackEnabled(false);

        mPatternLockViewSettingValue = findViewById(R.id.pattern_lock_view_set);
        mPatternLockViewSettingValue.addPatternLockListener(mPatternLockViewListener);
        mPatternLockViewSettingValue.setTactileFeedbackEnabled(true);//设置触觉是否能震动
        mPatternLockViewSettingValue.resetPattern();//恢复初始位置
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
            LayoutParams layoutParams = (LayoutParams) mPatternLockViewSettingValue.getLayoutParams();
            layoutParams.width = (int) (dm.widthPixels * mRate);
            layoutParams.height = layoutParams.width;
            mPatternLockViewSettingValue.setLayoutParams(layoutParams);

        }

    }

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {

        @Override
        public void onStarted() {

            mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Success));
            if (TextUtils.isEmpty(mFirstSetValue)) {
                mTvTip.setText(R.string.lib_pattern_input_pattern_lock);
            } else {
                mTvTip.setText(R.string.lib_pattern_input_pattern_lock_sure);
            }
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {


        }

        @Override
        public void onComplete(List<PatternLockView.Dot> progressPattern) {
            if (progressPattern.size() <= 0) {
                mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Error));
                mTvTip.setText(R.string.lib_pattern_input_pattern_lock_last_four_point);
            }
            if (TextUtils.isEmpty(mFirstSetValue)) {
                firstSetValueEnd(progressPattern);
            } else {
                sureValueEnd(progressPattern);
            }

        }

        @Override
        public void onCleared() {

        }
    };

    public boolean checkPattern(List<PatternLockView.Dot> progressPattern) {

        if (progressPattern.size() <= 0) {
            mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Error));
            mTvTip.setText(R.string.lib_pattern_input_pattern_lock_last_four_point);
            return false;
        }
        String password = PatternLockUtils.patternToString(mPatternLockViewSettingValue, progressPattern);


        if (TextUtils.isEmpty(password)) {
            mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Error));
            mTvTip.setText(R.string.lib_pattern_input_pattern_lock_last_four_point);
            return false;
        }

        String[] value = password.split(",");
        if (value.length < 4) {
            //设置错误模式
            mPatternLockViewSettingValue.setViewMode(PatternLockView.PatternViewMode.WRONG);
            mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Error));
            mTvTip.setText(R.string.lib_pattern_input_pattern_lock_last_four_point);
            return false;
        }

        return true;
    }


    public void sureValueEnd(List<PatternLockView.Dot> progressPattern) {

        String password = PatternLockUtils.patternToString(mPatternLockViewSettingValue, progressPattern);
        if (checkPattern(progressPattern)) {
            if (!mFirstSetValue.equals(password)) {
                mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Error));
                mTvTip.setText(R.string.lib_pattern_input_pattern_lock_no_same);
                String passwordTemp = PatternLockUtils.patternToStringLast(mPatternLockViewSettingValue, progressPattern);
                String result = passwordTemp.replace(",", "");
                if (mPatternLockViewSetListener != null) {
                    mPatternLockViewSetListener.onDifference(result);
                }
                return;
            }

            mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Success));
            mTvTip.setText(R.string.lib_pattern_input_pattern_lock_same);
//            String[] value = password.split(",");

            password = PatternLockUtils.patternToStringLast(mPatternLockViewSettingValue, progressPattern);
            String result = password.replace(",", "");
            if (mPatternLockViewSetListener != null) {
                mPatternLockViewSetListener.onComplete(result);
            }

        }

    }


    public void firstSetValueEnd(List<PatternLockView.Dot> progressPattern) {

        if (checkPattern(progressPattern)) {
            String password = PatternLockUtils.patternToString(mPatternLockViewSettingValue, progressPattern);
            mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Success));
            mTvTip.setText(R.string.lib_pattern_input_pattern_lock_sure);
            mFirstSetValue = password;
            mPatternLockViewTip.setPointString(password, PatternLockView.PatternViewMode.CORRECT);
            password = PatternLockUtils.patternToStringLast(mPatternLockViewSettingValue, progressPattern);
            String result = password.replace(",", "");
            if (mPatternLockViewSetListener != null) {
                mPatternLockViewSetListener.onCompleteFirstSet(result);
            }
            mPatternLockViewSettingValue.resetPattern();
        }

    }


    public void resetPattern() {
        mTvTip.setTextColor(ContextCompat.getColor(getContext(), R.color.col_PatternLock_Sure_Success));
        mTvTip.setText(R.string.lib_pattern_input_pattern_lock);
        mFirstSetValue = "";
        mPatternLockViewTip.resetPattern();
        mPatternLockViewSettingValue.resetPattern();
    }

    public PatternLockViewSetListener getmPatternLockViewSetListener() {
        return mPatternLockViewSetListener;
    }

    public void setmPatternLockViewSetListener(PatternLockViewSetListener mPatternLockViewSetListener) {
        this.mPatternLockViewSetListener = mPatternLockViewSetListener;
    }
}
