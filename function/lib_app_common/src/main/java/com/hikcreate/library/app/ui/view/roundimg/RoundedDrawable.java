package com.hikcreate.library.app.ui.view.roundimg;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView.ScaleType;

import com.hikcreate.library.util.LogCat;

/**
 * Class description 支持圆角的Drawable
 *
 * @author gongwei
 * @Date 2019.1.10
 */
public class RoundedDrawable extends Drawable {
    public static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private final RectF mBounds = new RectF();
    private final RectF mDrawableRect = new RectF();
    private final RectF mBitmapRect = new RectF();
    private final RectF mBorderRect = new RectF();
    private final Matrix mShaderMatrix = new Matrix();
    private float mCornerRadius = 0;
    private boolean mOval = false;
    private float mBorderWidth = 0;
    private ColorStateList mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    private final BitmapShader mBitmapShader;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final Paint mBorderPaint;

    public RoundedDrawable(Bitmap bitmap) {
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    /**
     * Method description
     *
     * @param bitmap
     * @return
     */
    public static RoundedDrawable fromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            return new RoundedDrawable(bitmap);
        } else {
            return null;
        }
    }

    /**
     * Method description
     *
     * @param drawable
     * @return
     */
    public static Drawable fromDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {

                // just return if it's already a RoundedDrawable
                return drawable;
            } else if (drawable instanceof LayerDrawable) {
                LayerDrawable ld = (LayerDrawable) drawable;
                int num = ld.getNumberOfLayers();

                // loop through layers to and change to RoundedDrawables if possible
                for (int i = 0; i < num; i++) {
                    Drawable d = ld.getDrawable(i);

                    ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d));
                }

                return ld;
            }

            // try to get a bitmap from the drawable and
            Bitmap bm = drawableToBitmap(drawable);

            if (bm != null) {
                return new RoundedDrawable(bm);
            } else {
                LogCat.w("Failed to create bitmap from drawable!");
            }
        }

        return drawable;
    }

    /**
     * Method description
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 1);
        int height = Math.max(drawable.getIntrinsicHeight(), 1);

        try {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);

            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }

        return bitmap;
    }

    /**
     * Method description
     *
     * @return
     */
    @Override
    public boolean isStateful() {
        return mBorderColor.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        int newColor = mBorderColor.getColorForState(state, 0);

        if (mBorderPaint.getColor() != newColor) {
            mBorderPaint.setColor(newColor);

            return true;
        } else {
            return super.onStateChange(state);
        }
    }

    /**
     * Method description
     */
    private void updateShaderMatrix() {
        float scale;
        float dx;
        float dy;

        switch (mScaleType) {
            case CENTER:
                mBorderRect.set(mBounds);
                mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
                mShaderMatrix.set(null);
                mShaderMatrix.setTranslate((int) ((mBorderRect.width() - mBitmapWidth) * 0.5f + 0.5f),
                        (int) ((mBorderRect.height() - mBitmapHeight) * 0.5f + 0.5f));

                break;

            case CENTER_CROP:
                mBorderRect.set(mBounds);
                mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
                mShaderMatrix.set(null);
                dx = 0;
                dy = 0;

                if (mBitmapWidth * mBorderRect.height() > mBorderRect.width() * mBitmapHeight) {
                    scale = mBorderRect.height() / (float) mBitmapHeight;
                    dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = mBorderRect.width() / (float) mBitmapWidth;
                    dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

                break;

            case CENTER_INSIDE:
                mShaderMatrix.set(null);

                if ((mBitmapWidth <= mBounds.width()) && (mBitmapHeight <= mBounds.height())) {
                    scale = 1.0f;
                } else {
                    scale = Math.min(mBounds.width() / (float) mBitmapWidth, mBounds.height() / (float) mBitmapHeight);
                }

                dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
                dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);
                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate(dx, dy);
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);

                break;

            default:
            case FIT_CENTER:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);

                break;

            case FIT_END:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);

                break;

            case FIT_START:
                mBorderRect.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
                mShaderMatrix.mapRect(mBorderRect);
                mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);

                break;

            case FIT_XY:
                mBorderRect.set(mBounds);
                mBorderRect.inset((mBorderWidth) / 2, (mBorderWidth) / 2);
                mShaderMatrix.set(null);
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);

                break;
        }

        mDrawableRect.set(mBorderRect);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.set(bounds);
        updateShaderMatrix();
    }

    /**
     * Method description
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if (mOval) {
            if (mBorderWidth > 0) {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
                canvas.drawOval(mBorderRect, mBorderPaint);
            } else {
                canvas.drawOval(mDrawableRect, mBitmapPaint);
            }
        } else {
            if (mBorderWidth > 0) {
                canvas.drawRoundRect(mDrawableRect, Math.max(mCornerRadius, 0), Math.max(mCornerRadius, 0),
                        mBitmapPaint);
                canvas.drawRoundRect(mBorderRect, mCornerRadius, mCornerRadius, mBorderPaint);
            } else {
                canvas.drawRoundRect(mDrawableRect, mCornerRadius, mCornerRadius, mBitmapPaint);
            }
        }
    }

    /**
     * Method description
     *
     * @return
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * Method description
     *
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
        invalidateSelf();
    }

    /**
     * Method description
     *
     * @param cf
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
        mBitmapPaint.setColorFilter(cf);
        invalidateSelf();
    }

    /**
     * Method description
     *
     * @param dither
     */
    @Override
    public void setDither(boolean dither) {
        mBitmapPaint.setDither(dither);
        invalidateSelf();
    }

    /**
     * Method description
     *
     * @param filter
     */
    @Override
    public void setFilterBitmap(boolean filter) {
        mBitmapPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    /**
     * Method description
     *
     * @return
     */
    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    /**
     * Method description
     *
     * @return
     */
    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    /**
     * Method description
     *
     * @return
     */
    public float getCornerRadius() {
        return mCornerRadius;
    }

    /**
     * Method description
     *
     * @param radius
     * @return
     */
    public RoundedDrawable setCornerRadius(float radius) {
        mCornerRadius = radius;

        return this;
    }

    /**
     * Method description
     *
     * @return
     */
    public float getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * Method description
     *
     * @param width
     * @return
     */
    public RoundedDrawable setBorderWidth(float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);

        return this;
    }

    /**
     * Method description
     *
     * @return
     */
    public int getBorderColor() {
        return mBorderColor.getDefaultColor();
    }

    /**
     * Method description
     *
     * @param color
     * @return
     */
    public RoundedDrawable setBorderColor(int color) {
        return setBorderColor(ColorStateList.valueOf(color));
    }

    /**
     * Method description
     *
     * @return
     */
    public ColorStateList getBorderColors() {
        return mBorderColor;
    }

    /**
     * Method description
     *
     * @param colors
     * @return
     */
    public RoundedDrawable setBorderColor(ColorStateList colors) {
        mBorderColor = (colors != null) ? colors : ColorStateList.valueOf(0);
        mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));

        return this;
    }

    /**
     * Method description
     *
     * @return
     */
    public boolean isOval() {
        return mOval;
    }

    /**
     * Method description
     *
     * @param oval
     * @return
     */
    public RoundedDrawable setOval(boolean oval) {
        mOval = oval;

        return this;
    }

    /**
     * Method description
     *
     * @return
     */
    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * Method description
     *
     * @param scaleType
     * @return
     */
    public RoundedDrawable setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.FIT_CENTER;
        }

        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            updateShaderMatrix();
        }

        return this;
    }

    /**
     * Method description
     *
     * @return
     */
    public Bitmap toBitmap() {
        return drawableToBitmap(this);
    }
}