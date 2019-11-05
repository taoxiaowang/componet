package com.hikcreate.library.app.ui.view.multiphotoview;

import android.content.Context;
import android.os.Handler;
import android.studio.view.ViewUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.hikcreate.library.app.config.AppContext;
import com.hikcreate.library.plugin.glide.AImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 九宫格图片显示
 * 布局规则：
 * 当为4个时特殊为2x2
 * 非4个时一排3个
 *
 * @author gongwei
 * @date 2019/2/26
 */
public class MultiPhotoView extends ViewGroup {

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public static final int DefRowSize = 3; //默认一行显示个数
    public static final int DefColumnSize = 2; //默认一列显示个数
    public static final int LineSpaceDip = 5; //间距
    public List<ImageWrap> imageList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private boolean requestItemLayout = false;
    private Handler handler = new Handler();

    public MultiPhotoView(Context context) {
        this(context, null);
    }

    public MultiPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addItem(String path, int width, int height) {
        imageList.add(new ImageWrap(path, width, height));
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(imageView);
    }

    public void clear() {
        imageList.clear();
        removeAllViews();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 行间距之和
     *
     * @return
     */
    private int getRowSpaceCount() {
        return (DefRowSize - 1) * getLineSpace();
    }

    /**
     * 列间距之和
     *
     * @return
     */
    private int getColumnSpaceCount() {
        return (DefColumnSize - 1) * getLineSpace();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        if (requestItemLayout || count == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int fWidth = MeasureSpec.getSize(widthMeasureSpec);
        int iSize = (fWidth - getRowSpaceCount()) / DefRowSize;
        if (count == 1) {
            View child = getChildAt(0);
            ImageWrap imageWrap = imageList.get(0);
            if (imageWrap.hasSize()) {
                imageWrap.setReferenceValue(iSize);
                childMeasureSpecExactly(child, imageWrap.scaleWidth, imageWrap.scaleHeight, imageWrap);
            } else {
                childMeasureSpecExactly(child, fWidth, iSize, imageWrap);
            }

            super.onMeasure(MeasureSpec.makeMeasureSpec(fWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY));
        } else {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                childMeasureSpecExactly(child, iSize, iSize, imageList.get(i));
            }

            int rowNum = count / DefRowSize + (count % DefRowSize > 0 ? 1 : 0);
            int rowHeight = rowNum * iSize + (rowNum - 1) * getLineSpace();
            super.onMeasure(MeasureSpec.makeMeasureSpec(fWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(rowHeight, MeasureSpec.EXACTLY));
        }
    }

    private void childMeasureSpecExactly(final View child, int width, int height, final ImageWrap imageWrap) {
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        child.measure(widthMeasureSpec, heightMeasureSpec);
        if (child instanceof ImageView) {
            if (URLUtil.isNetworkUrl(imageWrap.path)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AImage.loadCenterCrop(imageWrap.path, (ImageView) child);
                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AImage.loadCenterCrop(/*AppContext.BASE_URL_IMAGE + */imageWrap.path, (ImageView) child);
                    }
                });
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mPainterPosX = 0; //l;
        int mPainterPosY = 0; //t;
        int count = this.getChildCount();

        for (int i = 0; i < count; ++i) {
            View child = this.getChildAt(i);
            addItemClickListenner(child, i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            if (count == 4) {
                if (isNewLine(i, 2)) {
                    mPainterPosX = 0;
                    mPainterPosY += height + getLineSpace();
                }
            } else {
                if (isNewLine(i, DefRowSize)) {
                    mPainterPosX = 0;
                    mPainterPosY += height + getLineSpace();
                }
            }

            child.layout(mPainterPosX, mPainterPosY, mPainterPosX + width, mPainterPosY + height);
            mPainterPosX += width + getLineSpace();
        }
    }

    private void addItemClickListenner(View child, final int i) {
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, i);
                }
            }
        });
    }

    private boolean isNewLine(int i, int rowNum) {
        return i % rowNum == 0 && i > 0;
    }

    private int getLineSpace() {
        return ViewUtils.dip2px(getContext(), LineSpaceDip);
    }

    private boolean isRow4(int count, int i) {
        return count == 4 && i % 2 == 0 && i > 0;
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    class ImageWrap {

        public String path;
        public int width;
        public int height;
        public transient int scaleWidth;
        public transient int scaleHeight;

        public ImageWrap(String path, int width, int height) {
            this.path = path;
            this.width = width;
            this.height = height;
        }

        boolean hasSize() {
            return width > 0 && height > 0;
        }

        public void setReferenceValue(int iSize) {
            scaleWidth = iSize;
            scaleHeight = iSize;
            if (hasSize()) {
                float scale = Math.min(width / (float) iSize, height / (float) iSize);
                if (width > height) {
                    int rowWidth = iSize * DefRowSize + getRowSpaceCount();
                    if (width / scale > rowWidth) {
                        scaleWidth = rowWidth;
                    } else {
                        if (width / scale > iSize) {
                            scaleWidth = (int) (width / scale);
                        }
                    }
                } else {
                    int columnHeight = iSize * DefColumnSize + getColumnSpaceCount();
                    if (height / scale > columnHeight) {
                        scaleHeight = columnHeight;
                    } else {
                        if (height / scale > iSize) {
                            scaleHeight = (int) (height / scale);
                        }
                    }
                }
            }
        }
    }
}
