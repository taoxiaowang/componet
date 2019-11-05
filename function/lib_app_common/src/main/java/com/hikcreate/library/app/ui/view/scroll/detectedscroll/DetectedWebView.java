package com.hikcreate.library.app.ui.view.scroll.detectedscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Desc: 可设置渐变View的WebView
 * Author: gongwei 2018/12/24
 */
public class DetectedWebView extends WebView {
    private ScrollListener scrollListener;
    private ScrollFadeProxy scrollFadeProxy;

    /**
     * 设置执行渐变动画阙值距离
     *
     * @param top 离顶部距离
     */
    public void setFadeTop(int top) {
        if (scrollFadeProxy != null) {
            scrollFadeProxy.setFadeTop(top);
        }
    }

    /**
     * 设置动画反向
     *
     * @param isFadeReverse
     */
    public void setFadeReverse(boolean isFadeReverse) {
        if (scrollFadeProxy != null) {
            scrollFadeProxy.setFadeReverse(isFadeReverse);
        }
    }

    public void setDetectViewCallBack(DetectedViewCallBack detectViewCallBack) {
        if (scrollFadeProxy != null) {
            scrollFadeProxy.setDetectedViewCallBack(detectViewCallBack);
        }
    }

    /**
     * step one ,设置将要执行渐变动画的控件
     *
     * @param viewFaded
     */
    public void setViewFaded(ViewGroup viewFaded) {
        if (scrollFadeProxy != null) {
            scrollFadeProxy.setViewFaded(viewFaded);
        }
    }

    public DetectedWebView(Context context) {
        super(context);
        init();
    }

    public DetectedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DetectedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        scrollFadeProxy = new ScrollFadeProxy();
        scrollListener = scrollFadeProxy;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int deltaY = t - oldt;
        if (scrollFadeProxy.isUserTouched()) {
            scrollFadeProxy.setIsUp(deltaY > 0);
            if (scrollListener != null) {
                if (scrollFadeProxy.isUp()) {
                    scrollListener.up(deltaY, t);
                } else {
                    scrollListener.down(deltaY, t);
                }
            }
        } else {
            if (scrollListener != null) {
                if (scrollFadeProxy.isUp()) {
                    scrollListener.up(deltaY, t);
                } else {
                    scrollListener.down(deltaY, t);
                }
            }
        }
    }

    @Override
    public void flingScroll(int vx, int vy) {
        //容错，因为touch在极端情况下拿的不准
        scrollFadeProxy.setUserTouched(false);
        scrollFadeProxy.setIsUp(vy > 0);
        super.flingScroll(vx, vy);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            scrollFadeProxy.setUserTouched(true);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            scrollFadeProxy.setUserTouched(false);
        }
        return super.dispatchTouchEvent(ev);
    }
}
