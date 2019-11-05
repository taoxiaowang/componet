package com.hikcreate.library.app.ui.view.scroll;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * 可回调onScrollChanged的ScrollView
 *
 * @author gongwei
 * @date 2019/7/31
 * @mail gongwei5@hikcreate.com
 */
public class ObservableScrollView extends NestedScrollView {

    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public ObservableScrollView(Context context) {
        this(context, null);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public interface ScrollViewListener {
        void onScrollChanged(NestedScrollView view, int l, int t, int oldl, int oldt);
    }
}
