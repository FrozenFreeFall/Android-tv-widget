package com.androidtv.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.GridView;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class VerticalSmoothGridView extends GridView {

    private final static int SCROLL_ITEM_TIME = 1500;
    private int eventCount = 0;
    private final static int DOUBLE_ROW = 2; // "2"在双数行
    private final static int SINGLE_ROW = 1; // "1"在单�?

    /**
     * <默认构�?�函�?>
     */
    public VerticalSmoothGridView(Context context) {
        super(context);
    }

    public VerticalSmoothGridView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSmoothGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        int height = this.getChildAt(1).getHeight();
        eventCount++;
        // 该eventCount%2 是为了取的按键的第一�?,因为对不同的item,它会执行两次
        if (eventCount % 2 != 0) {
            int row = 0;
            row = getItemCurrentRow();
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN
                    && row == DOUBLE_ROW) {
                this.smoothScrollBy(height, SCROLL_ITEM_TIME);
                Log.d("", "向下..滑动执行�?");
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP
                    && row == SINGLE_ROW) {
                this.smoothScrollBy(-height, SCROLL_ITEM_TIME);
                Log.d("", "向上..滑动执行�?");
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 
     * 获取Gridview中当前item�?在的�?
     * 
     * @return
     */
    @SuppressLint("NewApi")
    public int getItemCurrentRow() {
        int row = 0;
        int position = 0;
        position = this.getSelectedItemPosition();
        Log.d("", "dispatchKeyEvent..position = " + position);
        Log.d("", "this.getNumColumns() = " + this.getNumColumns());
        int temp = (position / this.getNumColumns() + 1) % 2;
        if (temp == 0) {
            row = DOUBLE_ROW;
            Log.d("", "在双数行");
        } else {
            row = SINGLE_ROW;
            Log.d("", "在单�?");
        }
        return row;
    }

}