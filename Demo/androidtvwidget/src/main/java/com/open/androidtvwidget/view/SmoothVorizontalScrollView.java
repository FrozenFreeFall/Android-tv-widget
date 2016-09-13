package com.open.androidtvwidget.view;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 当你使用滚动窗口焦点错乱的时候，就可以使用这个控件.
 * <p/>
 * 使用方法和滚动窗口是一样的，具体查看DEMO吧.
 * <p/>
 * 如果想改变滚动的系数，R.dimen.fading_edge
 * <p/>
 *
 * @author hailongqiu
 */
public class SmoothVorizontalScrollView extends ScrollView {

    public SmoothVorizontalScrollView(Context context) {
        super(context, null, 0);
    }

    private int mFadingEdge;

    public SmoothVorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SmoothVorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFadingEdge(int fadingEdge) {
        this.mFadingEdge = fadingEdge;
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0)
            return 0;
        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        int fadingEdge = mFadingEdge > 0 ? mFadingEdge : this.getResources().getDimensionPixelSize(R.dimen.fading_edge);
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        //
        int scrollYDelta = 0;
        if (rect.bottom > screenBottom && rect.top > screenTop) {
            if (rect.height() > height) {
                scrollYDelta += (rect.top - screenTop);
            } else {
                scrollYDelta += (rect.bottom - screenBottom);
            }
            int bottom = getChildAt(0).getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);
        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            if (rect.height() > height) {
                scrollYDelta -= (screenBottom - rect.bottom);
            } else {
                scrollYDelta -= (screenTop - rect.top);
            }
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
        }
        return scrollYDelta;
    }

}
