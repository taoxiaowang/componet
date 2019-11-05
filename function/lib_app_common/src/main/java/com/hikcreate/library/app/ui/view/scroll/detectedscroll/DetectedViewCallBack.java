package com.hikcreate.library.app.ui.view.scroll.detectedscroll;

/**
 * Desc:
 * Author: gongwei 2018/12/24
 */
public interface DetectedViewCallBack {
    void onScrollFadeIn(int top);

    void onScrollFadeOut(int top);

    void fadeIn50Percent(boolean fadeIn50Percent);
}
