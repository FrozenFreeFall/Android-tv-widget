package com.open.androidtvwidget.recycle;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 修复 GridLayoutManager 焦点错乱问题.
 */
public class GridLayoutManagerTV extends GridLayoutManager {

	public GridLayoutManagerTV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler,
			RecyclerView.State state) {
		View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
		if (nextFocus == null) {
			return nextFocus;
		}
		int fromPos = getPosition(focused); // 获取当焦点view位置.
		int nextPos = getNextViewPos(fromPos, focusDirection); // 下一个View位置获取.
		return findViewByPosition(nextPos); // 获取下一个位置的view.
	}
	
	/**
	 * 获取下一个View位置.
	 * @param fromPos 焦点View位置
	 * @param direction 焦点移动方向.
	 * @return 下一个位置.
	 */
	protected int getNextViewPos(int fromPos, int direction) {
		int offset = calcOffsetToNextView(direction); // 获取下一方向的位置.
		/**
		 * 判断下一个方向位置是否正确.
		 */
		if (hitBorder(fromPos, offset)) {
			return fromPos;
		}
		return fromPos + offset;
	}
	
	/**
	 * 根据焦点移动方向获取.
	 * @param direction 方向(FOCUS_DOWN等等).
	 * @return
	 */
	protected int calcOffsetToNextView(int direction) {
		int spanCount = getSpanCount();
		int orientation = getOrientation();

		if (orientation == VERTICAL) {
			switch (direction) {
			case View.FOCUS_DOWN:
				return spanCount;
			case View.FOCUS_UP:
				return -spanCount;
			case View.FOCUS_RIGHT:
				return 1;
			case View.FOCUS_LEFT:
				return -1;
			}
		} else if (orientation == HORIZONTAL) {
			switch (direction) {
			case View.FOCUS_DOWN:
				return 1;
			case View.FOCUS_UP:
				return -1;
			case View.FOCUS_RIGHT:
				return spanCount;
			case View.FOCUS_LEFT:
				return -spanCount;
			}
		}
		return 0;
	}
	
	/**
	 * 判断下一个方向是否正确.
	 */
	private boolean hitBorder(int from, int offset) {
		int spanCount = getSpanCount();
		if (Math.abs(offset) == 1) {
			int spanIndex = from % spanCount;
			int newSpanIndex = spanIndex + offset;
			return newSpanIndex < 0 || newSpanIndex >= spanCount;
		} else {
			int newPos = from + offset;
			return newPos < 0 && newPos >= spanCount;
		}
	}

}
