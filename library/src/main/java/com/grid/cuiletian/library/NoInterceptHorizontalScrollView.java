package com.grid.cuiletian.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by cuiletian on 15/10/8.
 */
public class NoInterceptHorizontalScrollView extends HorizontalScrollView {
    private OnScrollChangedListener listener;

    public NoInterceptHorizontalScrollView(Context context) {
        super(context);
    }

    public NoInterceptHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoInterceptHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) listener.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        this.listener = listener;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
