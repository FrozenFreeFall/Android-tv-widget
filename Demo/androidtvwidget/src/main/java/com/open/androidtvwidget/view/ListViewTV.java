package com.open.androidtvwidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class ListViewTV extends ListView {

    public ListViewTV(Context context) {
        this(context, null);
    }

    public ListViewTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 崩溃了.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isInTouchMode() {
        return !(hasFocus() && !super.isInTouchMode());
    }

    WidgetTvViewBring mWidgetTvViewBring;

    private void init(Context context, AttributeSet attrs) {
        this.setChildrenDrawingOrderEnabled(true);
        mWidgetTvViewBring = new WidgetTvViewBring(this);
    }

    @Override
    public void bringChildToFront(View child) {
        mWidgetTvViewBring.bringChildToFront(this, child);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return mWidgetTvViewBring.getChildDrawingOrder(childCount, i);
    }

    public void setDefualtSelect(int pos) {
        requestFocusFromTouch();
        setSelection(pos);
    }

}
