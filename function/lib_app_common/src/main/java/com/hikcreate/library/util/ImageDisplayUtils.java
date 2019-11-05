package com.hikcreate.library.util;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

/**
 * 图像处理工具
 *
 * @author yslei
 * @data 2019/4/30
 * @email leiyongsheng@hikcreate.com
 */
public class ImageDisplayUtils {

    /**
     * 改变image为灰色
     *
     * @param imageView
     */
    public static void changeImgToGray(ImageView imageView) {
        //将ImageView变成灰色
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    /**
     * 恢复image显示（默认图像显示）
     *
     * @param imageView
     */
    public static void changeImgToNormal(ImageView imageView) {
        imageView.setColorFilter(null);
    }
}
