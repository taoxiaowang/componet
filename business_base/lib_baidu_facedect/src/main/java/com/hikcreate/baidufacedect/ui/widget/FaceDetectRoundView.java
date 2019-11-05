package com.hikcreate.baidufacedect.ui.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 人脸检测区域View
 */
public class FaceDetectRoundView extends View {

    private static final String TAG = "CircleProgressView";

    public static final float HEIGHT_EXT_RATIO = 0.2f;
    public static final float WIDTH_SPACE_RATIO = 0.3f;
    public static final float WIDTH_RECT_SPACE_RATIO = 0.2f;
    public static final float HEIGHT_RATIO = 0.1f;
    public static final float SURFACE_RATIO = 0.75f;

    private final int ARC_YELLOW = Color.parseColor("#FFA800");
    private final int ARC_BLUE = Color.parseColor("#4E91F9");
    private final int ARC_BG_WHITE = Color.parseColor("#DFE3E9");
    private final int ARC_DOT = Color.parseColor("#7CFFFF");

    // 内圆半径
    private int mRadius;
    // 圆弧宽度
    private float mArcWidth = 5;
    // 小圆半径
    private float mSmallRadius = 4;
    // 圆心点坐标
    private Point mCenterPoint = new Point();
    // 圆弧一周最大值
    private int mMaxValue;
    // 开始角度,进度起点角度
    private int mStartProgressAngle = -94;
    // 当前值
    private int mCurrentProgressValue;
    // 当前需要画的进度
    private int mFinalCurrentProgressValue;
    // 差值器缓存数
    private int mTempProgressValue;
    // 进度圆环内半径
    private int mProgressInsideRadius;
    // 循环固定圆弧角度
    private float mCycleSweepAngle = 60;
    // 循环固定圆弧角度
    private float mCycleStartAngle = 0;
    // 循环画布旋转角度
    private float mCycleCanvasRotateStartAngle = 0;
    // 循环旋转不掉角度
    private float mCycleDeltaAngle = (float) (3.6);
    // 循环画布旋转角度当前角度
    private float mCycleCanvasRotateCurrentAngle = 0;
    // 循环动画结束成功失败状态
    private boolean mCycleFinishSuccess;

    // 圆弧边界
    private RectF mRectF = new RectF();
    private RectF mBgRectF = new RectF();
    private Rect mFaceDetectRect = new Rect();

    // 整个背景
    private Paint mBGPaint;
    // 圆弧背景画笔
    private Paint mBgArcPaint;
    // 圆弧画笔
    private Paint mArcPaint;
    // 内圆画笔
    private Paint mCirclePaint;
    // 内圆边框画笔
    private Paint mCircleBorderPaint;
    // 外边框画笔
    private Paint mRectBorderPaint;
    // 小圆圈画笔
    private Paint mSmallCirclePaint;
    // 渐变器
    private SweepGradient mSweepGradient;

    // 动画是否完成
    private boolean mAnimatorFinishTag = false;
    // 是否循环动画
    private boolean mIsCycleRunning = false;
    // 动画是否在运行
    private boolean mIsProgressRun = false;
    // 动画重置启动
    private boolean mIsNeedToReset = false;
    // 动画
    private ValueAnimator mAnimator;

    public FaceDetectRoundView(Context context) {
        this(context, null);
    }

    public FaceDetectRoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceDetectRoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // setLayerType(LAYER_TYPE_SOFTWARE, null);
        mArcWidth = dip2px(getContext(), mArcWidth);
        mSmallRadius = dip2px(getContext(), mSmallRadius);

        initPaint();
    }

    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    private void initPaint() {
        // 背景
        mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBGPaint.setColor(Color.WHITE);
        mBGPaint.setStyle(Paint.Style.FILL);
        mBGPaint.setAntiAlias(true);
        mBGPaint.setDither(true);
        // 内圆
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mCirclePaint.setStyle(Paint.Style.FILL);

        // 白色中内圆
        mCircleBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleBorderPaint.setAntiAlias(true);
        mCircleBorderPaint.setDither(true);
        mCircleBorderPaint.setColor(Color.WHITE);
        mCircleBorderPaint.setStrokeWidth(mArcWidth);
        mCircleBorderPaint.setStyle(Paint.Style.STROKE);

        // 圆弧背景
        mBgArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgArcPaint.setAntiAlias(true);
        mBgArcPaint.setColor(ARC_BG_WHITE);
        mBgArcPaint.setStyle(Paint.Style.STROKE);
        mBgArcPaint.setStrokeWidth(mArcWidth);
        mBgArcPaint.setDither(true);

        // 圆弧
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        // 发光小圆
        mSmallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallCirclePaint.setAntiAlias(true);
        mSmallCirclePaint.setDither(true);
        mSmallCirclePaint.setColor(ARC_DOT);
        mSmallCirclePaint.setStyle(Paint.Style.FILL);
        mSmallCirclePaint.setMaskFilter(new BlurMaskFilter(mSmallRadius / 2, BlurMaskFilter.Blur.SOLID));

        // 整体外边框
        mRectBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectBorderPaint.setAntiAlias(true);
        mRectBorderPaint.setDither(true);
        mRectBorderPaint.setColor(Color.RED);
        mRectBorderPaint.setStrokeWidth(1);
        mRectBorderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        //求圆弧和背景圆弧的最大宽度
        int arcWidth = (int) mArcWidth;
        //求最小值作为实际值,这是直径
        int minSize = Math.min(w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingTop() - getPaddingBottom());
        //内圆半径： 进度圆环内半径 - 中间空余距离为17px = 内圆半径
        mRadius = (int) ((minSize / 2) - ((minSize / 2) * WIDTH_SPACE_RATIO));
        //获取圆的相关参数
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2;
        //绘制圆的边界
        mRectF.left = mCenterPoint.x - mRadius - arcWidth;
        mRectF.top = mCenterPoint.y - mRadius - arcWidth;
        mRectF.right = mCenterPoint.x + mRadius + arcWidth;
        mRectF.bottom = mCenterPoint.y + mRadius + arcWidth;
        //绘制背景圆弧的边界
        mBgRectF.left = mCenterPoint.x - mRadius - arcWidth;
        mBgRectF.top = mCenterPoint.y - mRadius - arcWidth;
        mBgRectF.right = mCenterPoint.x + mRadius + arcWidth;
        mBgRectF.bottom = mCenterPoint.y + mRadius + arcWidth;
        // 人脸框
        mFaceDetectRect.left = (int) (mBgRectF.left - arcWidth);
        mFaceDetectRect.top = (int) (mBgRectF.top - arcWidth - mRadius * HEIGHT_EXT_RATIO);
        mFaceDetectRect.right = (int) (mBgRectF.right + arcWidth);
        mFaceDetectRect.bottom = (int) (mBgRectF.bottom + arcWidth + mRadius * HEIGHT_EXT_RATIO);

        updateArcPaint();
    }

    public void startCycleAnimator() {
        mIsCycleRunning = true;
        mAnimatorFinishTag = false;
        mCycleFinishSuccess = false;
        mMaxValue = 100;
        initCycleArc();
        needToReset();
    }

    public void stopCycleAnimator(boolean cycleFinishSuccess) {
        mAnimatorFinishTag = true;
        mMaxValue = 0;
        mCycleFinishSuccess = cycleFinishSuccess;
        needToReset();
    }

    private void initCycleArc() {
        mCycleStartAngle = 4;
        mCycleSweepAngle = 60;
        mCycleCanvasRotateStartAngle = 0;
        mCycleCanvasRotateCurrentAngle = 0;
        mCycleDeltaAngle = (float) (360.0 / 100);
    }

    public Rect getFaceRoundRect() {
        if (mFaceDetectRect != null) {
            Log.e(TAG, mFaceDetectRect.toString());
        }
        return mFaceDetectRect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // long startTime = System.currentTimeMillis();
        // Log.d("yslei","-----------------draw start");
        drawArc(canvas);
        // Log.d("yslei","-----------------draw end:"+(System.currentTimeMillis() - startTime));
    }

    private void drawArc(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPaint(mBGPaint);
        // 画内圆
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCirclePaint);
        // 画内圆边框
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCircleBorderPaint);
        // 圆环背景
        canvas.drawArc(mBgRectF, 0, 360, false, mBgArcPaint);
        // 外边框
        //canvas.drawRect(mFaceDetectRect, mRectBorderPaint);

        if (mCurrentProgressValue == 0 && !mIsCycleRunning) {
            return;
        }

        if (mIsNeedToReset) {
            mIsNeedToReset = false;
            mTempProgressValue = 0;
            if (mIsCycleRunning) {
                if (mAnimatorFinishTag) {
                    stopRepeatAnimator();
                    startAnimator();
                } else {
                    startRepeatAnimator();
                }
            } else {
                startAnimator();
            }
            return;
        }

        if (mIsCycleRunning) {
            if (mAnimatorFinishTag) {
                if (mCycleFinishSuccess) {
                    drawProgressArc(canvas, ARC_BLUE);
                } else {
                    drawProgressArc(canvas, ARC_YELLOW);
                }
            } else {
                drawRunningArc(canvas);
            }
        } else {
            drawProgressArc(canvas, ARC_BLUE);
        }
    }

    private void drawProgressArc(Canvas canvas, int color) {
        canvas.rotate(mStartProgressAngle, mCenterPoint.x, mCenterPoint.y);
        float currentAngle = mFinalCurrentProgressValue * 1.0f / mMaxValue * 360;
        mArcPaint.setShader(null);
        mArcPaint.setColor(color);
        canvas.drawArc(mRectF, 4, currentAngle, false, mArcPaint);

        drawShineDot(canvas, currentAngle);
    }

    private void drawRunningArc(Canvas canvas) {
        mArcPaint.setShader(mSweepGradient);
        boolean flag = false;
        mCycleCanvasRotateStartAngle += mCycleDeltaAngle;
        if (mCycleSweepAngle >= 360 - mCycleDeltaAngle) {
            flag = true;
        }
        if (mCycleSweepAngle <= mCycleDeltaAngle) {
            flag = false;
        }
        if (flag) {
            mCycleSweepAngle -= mCycleDeltaAngle;
        } else {
            mCycleSweepAngle += mCycleDeltaAngle;
        }

        mCycleCanvasRotateCurrentAngle += mCycleDeltaAngle;
        canvas.rotate(mCycleCanvasRotateCurrentAngle, mCenterPoint.x, mCenterPoint.y);
        canvas.drawArc(mRectF, mCycleStartAngle, mCycleSweepAngle, false, mArcPaint);

        //drawShineDot(canvas,mCycleStartAngle - 4);
    }

    private void drawShineDot(Canvas canvas, float currentAngle) {
        // 设置发光的圆
        // setLayerType(LAYER_TYPE_SOFTWARE, null);
        // 计算小圆距离中心点的距离
        float tempRadius = mRadius + mArcWidth + 1;
        // 根据求圆上一点的方式，求出圆上的点相对于圆心的距离
        if (currentAngle >= 360) currentAngle = 358;
        float y1 = tempRadius * (float) Math.sin((currentAngle + 4) * Math.PI / 180);
        float x1 = tempRadius * (float) Math.cos((currentAngle + 4) * Math.PI / 180);
        // 算出小圆圆心坐标，根据此坐标，画出小圆
        canvas.drawCircle(mCenterPoint.x + x1, mCenterPoint.y + y1, mSmallRadius, mSmallCirclePaint);
    }

    private void updateArcPaint() {
        // 设置渐变
        int[] mGradientColors = {Color.parseColor("#DFE3E9"), Color.parseColor("#4E91F9")};
        // 0点钟和9点钟位置
        float[] positions = {0f, 0.5f};
        mSweepGradient = new SweepGradient(mCenterPoint.x, mCenterPoint.y, mGradientColors, null);
    }

    @SuppressLint("WrongConstant")
    private void startRepeatAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mAnimator = ValueAnimator.ofInt(0, mMaxValue);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.setDuration(1500);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.addUpdateListener(animation -> {
            mFinalCurrentProgressValue = (int) (animation.getAnimatedValue());
            invalidate();
        });
        mAnimator.start();
    }

    private void stopRepeatAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator.clone();
        }
    }

    private void startAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mAnimator = ValueAnimator.ofInt(0, mIsCycleRunning ? 100 : mCurrentProgressValue);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.setDuration(1500);
        final int finalTempCurrentValue = mCurrentProgressValue;
        mAnimator.addUpdateListener(animation -> {
            mFinalCurrentProgressValue = (int) (animation.getAnimatedValue());
            mIsProgressRun = mFinalCurrentProgressValue != finalTempCurrentValue;
            if (mFinalCurrentProgressValue > mTempProgressValue) {
                mTempProgressValue = mFinalCurrentProgressValue;
                invalidate();
            }
        });
        mAnimator.start();
    }

    private void needToReset() {
        mIsNeedToReset = true;
        invalidate();
    }

    /**
     * 设置圆弧当前值
     */
    public void setCurrentProgressValue(int currentProgressValue) {
        mCurrentProgressValue = currentProgressValue;
        if (mCurrentProgressValue > mMaxValue)
            mCurrentProgressValue = mMaxValue;
        if (mIsProgressRun) return;
        needToReset();
    }

    /**
     * 设置最大值
     */
    public void setMaxValue(int mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    public float getRound() {
        return mRadius;
    }

    public static Rect getPreviewDetectRect(int w, int pw, int ph) {
        float round = (w / 2) - ((w / 2) * WIDTH_RECT_SPACE_RATIO);
        float x = pw / 2;
        float y = (ph / 2) - ((ph / 2) * HEIGHT_RATIO);
        float r = (pw / 2) > round ? round : (pw / 2);
        float hr = r + (r * HEIGHT_EXT_RATIO);
        Rect rect = new Rect((int) (x - r),
                (int) (y - hr),
                (int) (x + r),
                (int) (y + hr));
        // Log.e(TAG, "FaceRoundView getPreviewDetectRect " + pw + "-" + ph + "-" + rect.toString());
        return rect;
    }

}