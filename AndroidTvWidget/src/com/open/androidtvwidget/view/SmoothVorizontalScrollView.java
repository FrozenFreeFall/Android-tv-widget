package com.open.androidtvwidget.view;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class SmoothVorizontalScrollView extends ScrollView {

	public SmoothVorizontalScrollView(Context context) {
		super(context, null, 0);
	}

	public SmoothVorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public SmoothVorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		if (getChildCount() == 0)
			return 0;
		int height = getHeight();
		int screenTop = getScrollY();
		int screenBottom = screenTop + height;
		int fadingEdge = this.getResources().getDimensionPixelSize(
				R.dimen.fading_edge);
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

//	@Override
//	protected boolean onRequestFocusInDescendants(int direction,
//			Rect previouslyFocusedRect) {
//
//		// convert from forward / backward notation to up / down / left / right
//		// (ugh).
//
//		if (previouslyFocusedRect != null) {
//			if (direction == View.FOCUS_FORWARD) {
//				direction = View.FOCUS_RIGHT;
//			} else if (direction == View.FOCUS_BACKWARD) {
//				direction = View.FOCUS_LEFT;
//			}
//			View nextFocus = FocusFinder.getInstance().findNextFocusFromRect(
//					this, previouslyFocusedRect, direction);
//			if (nextFocus == null) {
//				return false;
//			}
//			return nextFocus.requestFocus(direction, previouslyFocusedRect);
//		} else {
//			int index;
//			int increment;
//			int end;
//			int count = this.getChildCount();
//			if ((direction & FOCUS_FORWARD) != 0) {
//				index = 0;
//				increment = 1;
//				end = count;
//			} else {
//				index = count - 1;
//				increment = -1;
//				end = -1;
//			}
//			for (int i = index; i != end; i += increment) {
//				View child = this.getChildAt(i);
//				if (child.getVisibility() == View.VISIBLE) {
//					if (child.requestFocus(direction, previouslyFocusedRect)) {
//						return true;
//					}
//				}
//			}
//			return false;
//		}
//	}
	
}
