package com.hikcreate.library.app.ui.view.scroll;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hikcreate.library.util.ViewUtil;

/**
 * 带下拉图片放大的ScrollView
 * Created by gongwei 2018.12.24
 */

public class ImageEnlargeScrollView extends StretchScrollView {

    public static final float IMAGE_FACTOR = 0.6f;

    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private DisplayMetrics metric;

    Context context;

    public ImageView imageView;

    private float distanceM;

    private float ratio;

    public ImageEnlargeScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public ImageEnlargeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ImageEnlargeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 恢复原来的尺寸
     */
    // 回弹动画 (使用了属性动画)
    public void replyImage() {
        final LayoutParams lp = (LayoutParams) imageView
                .getLayoutParams();
        final float wNew = imageView.getLayoutParams().width;// 图片当前宽度
        final float hNew = imageView.getLayoutParams().height;// 图片当前高度
        final float wOld = metric.widthPixels;// 图片原宽度
        final float hOld = metric.widthPixels / ratio;// 图片原高度
        final float margin = ((LayoutParams) imageView.getLayoutParams()).leftMargin;
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F)
                .setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (wNew - (wNew - wOld) * cVal);
                lp.height = (int) (hNew - (hNew - hOld) * cVal);
                //add by xiongwm 设置margin量即向右偏移值
                lp.setMargins((int) ((1 - cVal) * margin), 0, 0, 0);
                imageView.setLayoutParams(lp);
            }
        });
        anim.start();
    }

    /**
     * 设置图片放大效果
     *
     * @param imageView
     */
    public void setPullEnlarge(final ImageView imageView, final float ratio) {
        this.imageView = imageView;
        this.ratio = ratio;
        metric = new DisplayMetrics();
        Activity activityHost = ViewUtil.getActivityFromView(this);
        if (activityHost != null) {
            activityHost.getWindowManager().getDefaultDisplay().getMetrics(metric);
        }
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LayoutParams lp = (LayoutParams) imageView
                        .getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 手指离开后恢复图片
                        mScaling = false;
                        replyImage();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mScaling) {
                            if (getScrollY() == 0) {
                                mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
                            } else {
                                break;
                            }
                        }
                        int distance = (int) ((event.getY() - mFirstPosition) * IMAGE_FACTOR); // 滚动距离乘以一个系数
                        if (distance < 0) { // 当前位置比记录位置要小，正常返回
                            break;
                        }
                        // 处理放大
                        mScaling = true;
                        lp.width = metric.widthPixels + distance;
                        lp.height = (int) ((metric.widthPixels + distance) / ratio);
                        distanceM = distance / 2;
                        //add by xiongwm 设置margin量即向左偏移值
                        lp.setMargins((int) -distanceM, 0, 0, 0);
                        imageView.setLayoutParams(lp);
                        //modify by gongwei 2019.1.22：此Scroll集成了回弹效果
                        //return true; // 返回true表示已经完成触摸事件，不再处理
                        return false;
                    //end
                }
                return false;
            }
        });
    }

}
