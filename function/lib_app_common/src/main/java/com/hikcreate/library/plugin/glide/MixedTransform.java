package com.hikcreate.library.plugin.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 图片等比缩放
 *
 * @author gongwei
 * @date 2019/7/16
 */
public class MixedTransform extends BitmapTransformation {
    private static final String ID = "com.hikcreate.library.plugin.glide.MixedTransform";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName("UTF-8"));
    private int maxWidth, maxHeight;

    public MixedTransform(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }


    public Bitmap transform(Bitmap source) {
        //这里用最大值的1/3
        int targetWidth = maxWidth / 3;
        int targetHeight = targetWidth;
        if (source.getWidth() == 0 || source.getHeight() == 0) {
            return source;
        }
        if (source.getWidth() > source.getHeight()) {//横向长图
            if (source.getHeight() < targetHeight && source.getWidth() <= maxWidth) {
                return source;
            } else {
                //如果图片大小大于等于设置的高度，则按照设置的高度比例来缩放
                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int width = (int) (targetHeight * aspectRatio);
                if (width > maxWidth) { //对横向长图的宽度 进行二次限制
                    width = maxWidth;
                    targetHeight = (int) (width / aspectRatio);// 根据二次限制的宽度，计算最终高度
                }
                if (width != 0 && targetHeight != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, width, targetHeight, false);
                    return result;
                } else {
                    return source;
                }
            }
        } else {//竖向长图
            //如果图片小于设置的宽度，则返回原图
            if (source.getWidth() < targetWidth && source.getHeight() <= maxHeight) {
                return source;
            } else {
                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int height = (int) (targetWidth * aspectRatio);
                if (height > maxHeight) {//对横向长图的高度进行二次限制
                    height = maxHeight;
                    targetWidth = (int) (height / aspectRatio);//根据二次限制的高度，计算最终宽度
                }
                if (height != 0 && targetWidth != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, height, false);
                    return result;
                } else {
                    return source;
                }
            }
        }
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return transform(toTransform);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MixedTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
