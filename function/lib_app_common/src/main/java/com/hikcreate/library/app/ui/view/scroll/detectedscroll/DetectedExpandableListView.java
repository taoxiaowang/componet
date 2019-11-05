package com.hikcreate.library.app.ui.view.scroll.detectedscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * Desc: 可设置渐变View的ExListView
 * Author: gongwei 2018/12/24
 */
public class DetectedExpandableListView extends ExpandableListView {
    private ScrollListener scrollListener;
    private ScrollFadeProxy scrollFadeProxy;
    private int top, deltaY;

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

    public DetectedExpandableListView(Context context) {
        super(context);
        init();
    }

    public DetectedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DetectedExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        scrollFadeProxy = new ScrollFadeProxy();
        scrollListener = scrollFadeProxy;
    }

    public void setTopDeltaY(int top, int deltaY) {
        this.top = top;
        this.deltaY = deltaY;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        t = top;
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
    public void fling(int velocityY) {
        //容错，因为touch在极端情况下拿的不准
        scrollFadeProxy.setUserTouched(false);
        scrollFadeProxy.setIsUp(velocityY > 0);
        super.fling(velocityY);
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
