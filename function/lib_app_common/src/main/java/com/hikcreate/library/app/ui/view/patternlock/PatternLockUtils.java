

package com.hikcreate.library.app.ui.view.patternlock;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * 手势工具类
 */
public class PatternLockUtils {


    /**
     * 转换成String
     */
    public static String patternToString(PatternLockView patternLockView,
                                         List<PatternLockView.Dot> pattern) {
        if (pattern == null) {
            return "";
        }
        int patternSize = pattern.size();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < patternSize; i++) {
            PatternLockView.Dot dot = pattern.get(i);
            stringBuilder.append(dot.getRow() * patternLockView.getDotCount() + dot.getColumn()).append(",");
        }

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }


    /**
     * 转换成String 适应业务需求每位加1
     */
    public static String patternToStringLast(PatternLockView patternLockView,
                                             List<PatternLockView.Dot> pattern) {
        if (pattern == null) {
            return "";
        }
        int patternSize = pattern.size();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < patternSize; i++) {
            PatternLockView.Dot dot = pattern.get(i);
            stringBuilder.append(dot.getRow() * patternLockView.getDotCount() + dot.getColumn() + 1).append(",");
        }

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }


    /**
     * 设置选中的点
     */
    public static void setSelectPoint(PatternLockView patternLockView,
                                      List<PatternLockView.Dot> pattern) {


    }


    /**
     * 震动
     */
    public static void shock(TextView view) {
        int shakeDegrees = 20;
        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, 0f),
                Keyframe.ofFloat(0.3f, shakeDegrees),
                Keyframe.ofFloat(0.4f, 0f),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, 0f),
                Keyframe.ofFloat(0.7f, shakeDegrees),
                Keyframe.ofFloat(0.8f, 0f),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f));

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, rotateValuesHolder);
        objectAnimator.setDuration(500);
        objectAnimator.start();

        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                        | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
    }


}
