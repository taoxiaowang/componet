package com.hikcreate.library.plugin.svg;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;

/**
 * Module for the SVG sample app.
 * https://muyangmin.github.io/glide-docs-cn/doc/generatedapi.html
 * https://github.com/bumptech/glide/tree/master/samples/svg/src/main/java/com/bumptech/glide/samples/svg
 *
 * @author yslei
 * @data 2019/4/30
 * @email leiyongsheng@hikcreate.com
 */
@GlideModule
public class SvgModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {
        registry.register(SVG.class, PictureDrawable.class, new SvgDrawableTranscoder())
                .append(InputStream.class, SVG.class, new SvgDecoder());
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
