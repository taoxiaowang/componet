package com.hikcreate.library.app.ui.view.databindadapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hikcreate.library.plugin.glide.AImage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class NormalBindingAdapter {
    @BindingAdapter({"imageUrl", "error"})
    public static void loadImageWithError(ImageView view, String url, Drawable error) {
        AImage.load(url, view, error);
    }

    @BindingAdapter({"imageUrl", "errorId"})
    public static void loadImageWithErrorId(ImageView view, String url, int errorId) {
        AImage.load(url, view, view.getContext().getResources().getDrawable(errorId));
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImageOnly(ImageView view, String url) {
        AImage.load(url, view);
    }

    @BindingAdapter({"imageUrl", "imageRadius", "error"})
    public static void loadRoundImageWithError(ImageView view, String url, int radius, Drawable error) {
        AImage.loadRoundImage(url, view, radius, error);
    }

    @BindingAdapter(value = {"dateTime", "format"}, requireAll = false)
    public static void bindTextViewTimeFormat(TextView view, String date, String format) {
        if (date == null) {
            return;
        }
        SimpleDateFormat formatter;
        if (format != null) {
            formatter = new SimpleDateFormat(format);
        } else {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        Long timeStamp = Long.parseLong(date);
        view.setText(formatter.format(new Date(timeStamp)));
    }
}
