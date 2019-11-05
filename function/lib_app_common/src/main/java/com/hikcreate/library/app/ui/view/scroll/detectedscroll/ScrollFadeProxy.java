package com.hikcreate.library.app.ui.view.scroll.detectedscroll;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * 渐变控制
 * Author: gongwei 2018/12/24
 */
public class ScrollFadeProxy implements ScrollListener {
    private float oldAlpha = 0f;
    private boolean giveToRunnableOut, giveToRunnableIn;
    private AlphaThread alphaThreadIn, alphaThreadOut;
    private ViewGroup viewFaded;
    private int fadeTop = 400; //滚动到了这个距离才做动画,默认值,靠setFadeTop方法修改
    private boolean isUserTouched;
    private boolean isUp = false;
    private boolean isFadeReverse = true; //默认值
    private float alphaStep = 0.02f;
    private DetectedViewCallBack detectedViewCallBack;
    private int lastTop;

    private float oldAlphaFor50Percent = 0f;//新增一个标志位，用于判断Alpha阈值是否越过0.5

    /**
     * 设置需要渐变的ViewGroup
     *
     * @param viewFaded
     */
    public void setViewFaded(final ViewGroup viewFaded) {
        this.viewFaded = viewFaded;
        setViewFadedAlpha(this.viewFaded.getAlpha());
    }

    private void setViewFadedAlpha(float alpha) {
        if (viewFaded != null) {
            viewFaded.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);////设为Gone是为了不拦截屏幕touch事件
            viewFaded.setAlpha(alpha);
            check50Percent(alpha);
        }
    }

    /**
     * 渐变超过50%的阈值回调
     *
     * @param alpha
     */
    private void check50Percent(float alpha) {
        if (alpha > 0.5 && oldAlphaFor50Percent <= 0.5) {
            if (detectedViewCallBack != null) {
                detectedViewCallBack.fadeIn50Percent(true);
            }
        } else if (alpha < 0.5 && oldAlphaFor50Percent >= 0.5) {
            if (detectedViewCallBack != null) {
                detectedViewCallBack.fadeIn50Percent(false);
            }
        }
        oldAlphaFor50Percent = alpha;
    }

    public void setDetectedViewCallBack(DetectedViewCallBack detectedViewCallBack) {
        this.detectedViewCallBack = detectedViewCallBack;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (viewFaded != null && fadeTop > 0) {
                oldAlpha = (Float) msg.obj;
                if (msg.what == AlphaThread.TYPE_FADE_IN) {
                    if (oldAlpha >= 1) {
                        oldAlpha = 1;
                        giveToRunnableIn = false;
                    }
                } else if (msg.what == AlphaThread.TYPE_FADE_OUT) {
                    if (oldAlpha <= 0) {
                        oldAlpha = 0;
                        giveToRunnableOut = false;
                    }
                }
                setViewFadedAlpha(oldAlpha);
            }
            return false;
        }
    });

    /**
     * 用户是否正在触摸
     *
     * @param userTouched
     */
    public void setUserTouched(boolean userTouched) {
        isUserTouched = userTouched;
    }

    /**
     * 是否朝上滑
     *
     * @param isUp
     */
    public void setIsUp(boolean isUp) {
        this.isUp = isUp;
    }

    public void setFadeReverse(boolean isFadeReverse) {
        this.isFadeReverse = isFadeReverse;
        if (isFadeReverse) {
            oldAlpha = 0;
        }
    }

    /**
     * 设置执行渐变动画阙值距离
     *
     * @param top 离顶部距离
     */
    public void setFadeTop(int top) {
        this.fadeTop = top;
        if (top != 0) {
            alphaStep = (200f / top) * alphaStep;
        }
    }

    public boolean isUserTouched() {
        return isUserTouched;
    }

    public boolean isUp() {
        return isUp;
    }

    @Override
    public void up(int deltaY, int top) {
        if (isFadeReverse) {
            doFadeIn(deltaY, top);
        } else {
            if (top > fadeTop) {
                doFadeOut(deltaY, top);
            }
        }
        lastTop = top;
    }

    @Override
    public void down(int deltaY, int top) {
        if (isFadeReverse) {
            if (top < fadeTop) {
                doFadeOut(deltaY, top);
            }
        } else {
            doFadeIn(deltaY, top);
        }
        lastTop = top;
    }

    private void doFadeOut(int deltaY, int top) {
        if (viewFaded != null && fadeTop > 0 && deltaY != 0) {
            if (giveToRunnableIn) {
                alphaThreadIn.interrupt();
                giveToRunnableIn = false;
            }
            if (!giveToRunnableOut) {
                float alpha = oldAlpha - alphaStep;
                if (alpha <= 0.0) {
                    alpha = 0.0f;
                }
                if ((!isFadeReverse && top > fadeTop) || (isFadeReverse && ((top < 20) || (Math.abs(lastTop) == Math.abs(deltaY))))) {
                    giveToRunnableOut = true;
                    alphaThreadOut = new AlphaThread(AlphaThread.TYPE_FADE_OUT, alpha);
                    alphaThreadOut.start();
                    return;
                }
                setViewFadedAlpha(alpha);
                oldAlpha = alpha;
                if (detectedViewCallBack != null) {
                    detectedViewCallBack.onScrollFadeOut(top);
                }
            }
        }
    }

    private void doFadeIn(int deltaY, int top) {
        if (viewFaded != null && fadeTop > 0 && deltaY != 0) {
            if (giveToRunnableOut) {
                alphaThreadOut.interrupt();
                giveToRunnableOut = false;
            }
            if (!giveToRunnableIn) {
                float alpha = oldAlpha + alphaStep;
                if (alpha >= 1.0) {
                    alpha = 1.0f;
                }
                if ((isFadeReverse && top > fadeTop) || (!isFadeReverse && ((top < 20) || (Math.abs(lastTop) == Math.abs(deltaY))))) {
                    giveToRunnableIn = true;
                    alphaThreadIn = new AlphaThread(AlphaThread.TYPE_FADE_IN, alpha);
                    alphaThreadIn.start();
                    return;
                }
                setViewFadedAlpha(alpha);
                oldAlpha = alpha;
                if (detectedViewCallBack != null) {
                    detectedViewCallBack.onScrollFadeIn(top);
                }
            }
        }
    }


    class AlphaThread extends Thread {
        public static final int TYPE_FADE_IN = 1;
        public static final int TYPE_FADE_OUT = 2;
        float alpha;
        int type; //fadeIn->1/fadeOut->2

        public AlphaThread(int type, float alpha) {
            this.alpha = alpha;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                if (type == TYPE_FADE_IN) {
                    while (alpha <= 1.0f) {
                        Message message = new Message();
                        alpha += 0.05f;
                        message.obj = alpha;
                        message.what = type;
                        handler.sendMessage(message);
                        Thread.sleep(15L);
                    }
                } else if (type == TYPE_FADE_OUT) {
                    while (alpha >= 0.0f) {
                        Message message = new Message();
                        alpha -= 0.05f;
                        message.obj = alpha;
                        message.what = type;
                        handler.sendMessage(message);
                        Thread.sleep(15L);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public double getScrollDistance(Context context, int velocityY) {
        final double l = getG(context, velocityY);
        final double decelMinusOne = (float) (Math.log(0.78) / Math.log(0.9)) - 1.0;
        return ViewConfiguration.getScrollFriction() * (SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * (context.getResources().getDisplayMetrics().density * 160.0f)
                * 0.84f) * Math.exp((float) (Math.log(0.78) / Math.log(0.9)) / decelMinusOne * l);
    }

    public double getG(Context context, int velocityY) {
        return Math.log(0.35f * Math.abs(velocityY) / (ViewConfiguration.getScrollFriction() * SensorManager.GRAVITY_EARTH
                * 39.37f // inch/meter
                * (context.getResources().getDisplayMetrics().density * 160.0f)
                * 0.84f));
    }
}
