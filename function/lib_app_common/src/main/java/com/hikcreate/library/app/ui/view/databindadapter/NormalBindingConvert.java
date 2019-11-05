package com.hikcreate.library.app.ui.view.databindadapter;

import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;

/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class NormalBindingConvert {

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
