package com.hikcreate.library.util;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

/**
 * SVG 动态设置图片颜色
 *
 * @author yslei
 * @data 2019/4/30
 * @email leiyongsheng@hikcreate.com
 */
public class SVGUtils {

    public static void tintSVGDrawable(SVGImageItem item) {
        if (item != null && item.isLegal()) {
            //利用ContextCompat工具类获取drawable图片资源
            Drawable drawable = ContextCompat.getDrawable(item.mImageView.getContext(), item.getImgResId());
            //简单的使用tint改变drawable颜色
            Drawable wrap = DrawableCompat.wrap(drawable).mutate();
            DrawableCompat.setTint(wrap, ContextCompat.getColor(item.mImageView.getContext(), item.getColorId()));
            item.mImageView.setImageDrawable(wrap);
        }
    }

    public static class SVGImageItem {
        private int mImgResId;
        private ImageView mImageView;
        private int mColorId;

        public SVGImageItem(int imgResId, ImageView imageView) {
            mImgResId = imgResId;
            mImageView = imageView;
        }

        public SVGImageItem(int imgResId, ImageView imageView, int color) {
            mImgResId = imgResId;
            mImageView = imageView;
            mColorId = color;
        }

        public int getImgResId() {
            return mImgResId;
        }

        public void setImgResId(int imgResId) {
            mImgResId = imgResId;
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public void setImageView(ImageView imageView) {
            mImageView = imageView;
        }

        public int getColorId() {
            return mColorId;
        }

        public void setColorId(int colorId) {
            mColorId = colorId;
        }

        public boolean isLegal() {
            if (mColorId > 0 && mImageView != null && mImgResId > 0) {
                return true;
            }
            return false;
        }
    }
}
