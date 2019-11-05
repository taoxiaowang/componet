package com.hikcreate.library.plugin.glide;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.studio.view.ViewUtils;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import com.hikcreate.library.R;
import com.hikcreate.library.app.HikApplicationCreate;
import com.hikcreate.library.util.OS;
import com.hikcreate.library.util.ViewUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片加载工具类
 */
public class AImage {

    private static int defaultResId = /*R.drawable.ic_img_default*/0;
    public static String RES_PIC_URL_START = "res://";

    public static RequestOptions getOptions() {
        return new RequestOptions().placeholder(defaultResId);
    }

    private static RequestOptions getOptions(int defaultResId) {
        return new RequestOptions().placeholder(defaultResId);
    }

    //For resource
    public static void load(int res, ImageView imageView) {
        imageView.setImageResource(res);
    }

    public static void loadCenterCrop(int res, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(res);
    }

    //For hik url
    public static void load(String url, ImageView imageView) {
        load(url, null, imageView);
    }

    public static void load(String url, ImageView imageView, int defaultResId) {
        load(url, getOptions(defaultResId), imageView);
    }

    public static void load(String url, ImageView imageView, Drawable drawable) {
        load(url, new RequestOptions().placeholder(drawable), imageView);
    }

    public static void load(String url, ImageView imageView, RequestListener listener) {
        load(url, getOptions(), imageView, listener);
    }

    public static void loadRoundImage(String url, ImageView imageView, int imageRadius, Drawable drawable) {
        load(url, RequestOptions.bitmapTransform(new GlideRoundTransform(imageView.getContext(), imageRadius)).placeholder(drawable), imageView, null);
    }

    public static void loadCenterCrop(String url, ImageView imageView) {
        load(url, getOptions().centerCrop(), imageView);
    }

    public static void loadCenterInside(String url, ImageView imageView) {
        load(url, getOptions().centerInside(), imageView);
    }

    public static void loadCenterCrop(String url, ImageView imageView, int defaultResId) {
        load(url, getOptions(defaultResId).centerCrop(), imageView);
    }

    public static void loadCenterCrop(String url, ImageView imageView, Drawable drawable) {
        load(url, getOptions().centerCrop().error(drawable), imageView);
    }

    public static void load(String url, RequestOptions options, ImageView imageView) {
        load(url, options, imageView, null);
    }

    public static void loadHead(String url, ImageView imageView) {
        load(url, getOptions(R.drawable.ic_head_default).centerCrop(), imageView);
    }

    public static void load(String url, RequestOptions options, ImageView imageView, RequestListener listener) {
        load(url, null, options, imageView, listener);
    }

    public static void load(String url, Map<String, String> header, RequestOptions options, ImageView imageView, RequestListener listener) {
        if (options == null) options = new RequestOptions().placeholder(defaultResId);
        if (url != null && url.startsWith(RES_PIC_URL_START)) {
            int resId = Integer.valueOf(url.substring(RES_PIC_URL_START.length()));
            Drawable src = HikApplicationCreate.getApplication().getResources().getDrawable(resId);
            imageView.setImageDrawable(src);
            if (listener != null) {
                listener.onResourceReady(null,
                        null, null,
                        null, false);
            }
            return;
        }
        Activity imageActivity = ViewUtil.getActivityFromView(imageView);
        if (imageActivity != null && !imageActivity.isDestroyed()) {
            if (header != null && URLUtil.isNetworkUrl(url)) {
                options.diskCacheStrategy(DiskCacheStrategy.NONE);
                GlideUrl glideUrl = new GlideUrl(url, () -> header);
                Glide.with(imageActivity).load(glideUrl).apply(options).listener(listener).into(imageView);
            } else {
                Glide.with(imageActivity).load(url).apply(options).listener(listener).into(imageView);
            }
        }
    }

    public static Bitmap loadResizeBitmap(ImageView imageView, String uri, MixedTransform mixedTransform) {
        try {
            Activity imageActivity = ViewUtil.getActivityFromView(imageView);
            return Glide.with(imageActivity).asBitmap().load(uri).transform(mixedTransform).submit().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保持原始比例加载图片
     *
     * @param imageView
     * @param imageUrl
     * @param defaultWidth  图片显示的宽度
     * @param defaultHeight 图片显示的高度
     */
    public static void loadKeepOriginalSize(ImageView imageView, String imageUrl, int defaultWidth, int defaultHeight) {
        if (imageView == null || TextUtils.isEmpty(imageUrl)) return;
        Activity activity = ViewUtil.getActivityFromView(imageView);
        if (defaultWidth == 0) defaultWidth = OS.getScreenWidth(activity);
        int finalImageWidth = defaultWidth;
        Observable.create((ObservableOnSubscribe<ImageViewHolder>) e -> {
            ImageViewHolder holder = new ImageViewHolder();
            holder.imageView = imageView;
            holder.width = finalImageWidth;
            try {
                Bitmap bitmapTemp = AImage.loadResizeBitmap(imageView, imageUrl, new MixedTransform(finalImageWidth, OS.getScreenHeight(activity)));
                int bitmapWidth = bitmapTemp.getWidth();
                int bitmapHeight = bitmapTemp.getHeight();
                bitmapTemp = null;
                int realHeight = bitmapHeight * finalImageWidth / bitmapWidth;
                holder.height = realHeight;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.onNext(holder);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(holder -> {
            if (holder != null && holder.imageView != null) {
                ViewGroup.LayoutParams p = holder.imageView.getLayoutParams();
                p.width = holder.width;
                p.height = holder.height;
                holder.imageView.setLayoutParams(p);
                load(imageUrl, holder.imageView);//这里为什么要重新load而不是使用下载的bitmap，因为要支持gif
            }
        });
    }

    // For file
    public static void load(File file, ImageView imageView) {
        load(file, getOptions(), imageView, null);
    }

    public static void loadCenterCropResizeFile(File file, ImageView imageView, int width, int height) {
        load(file, getOptions().override(width, height).centerCrop(), imageView, null);
    }

    public static void load(File file, RequestOptions options, ImageView imageView, RequestListener listener) {
        if (options == null) options = new RequestOptions().placeholder(defaultResId);
        Glide.with(imageView.getContext()).load(file).apply(options).listener(listener).into(imageView);
    }

    // static classes
    private static class ImageViewHolder {
        public ImageView imageView;
        public int width;//for image
        public int height;//for image
    }
}
