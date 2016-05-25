package com.open.androidtvwidget.recycle;

import com.open.androidtvwidget.utils.OPENLOG;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LinearLayoutManagerTV extends LinearLayoutManager {
	
	public LinearLayoutManagerTV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	public LinearLayoutManagerTV(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
	}

	RecyclerView mParent;
	int mTopPadding = 0;
	int mBottomPadding = 0;
	int mLeftPadding = 0;
	int mRightPadding = 0;
	int mDy = 0;
	private OnChildSelectedListener mChildSelectedListener = null;
	View mSelectedView;
	SelectionNotifier mSelectionNotifier;
	boolean isFirst = true;

	@Override
	public boolean requestChildRectangleOnScreen(final RecyclerView parent, View child, Rect rect, boolean immediate) {
		OPENLOG.D("requestChildRectangleOnScreen");
		mParent = parent;
		int topPadding = mTopPadding;
		int bottomPadding = mBottomPadding;
		int leftPadding = mLeftPadding;
		int rightPadding = mRightPadding;
		//
		final int parentLeft = getPaddingLeft();
		final int parentTop = getPaddingTop();
		final int parentRight = getWidth() - getPaddingRight();
		final int parentBottom = getHeight() - getPaddingBottom();
		final int childLeft = child.getLeft() + rect.left;
		final int childTop = child.getTop() + rect.top;
		final int childRight = childLeft + rect.width();
		final int childBottom = childTop + rect.height();

		final int offScreenLeft = Math.min(0, childLeft - parentLeft - leftPadding);
		final int offScreenTop = Math.min(0, childTop - parentTop - topPadding);
		final int offScreenRight = Math.max(0, childRight - parentRight + leftPadding);
		final int offScreenBottom = Math.max(0, childBottom - parentBottom + bottomPadding);

		Rect childRect = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
		// Favor the "start" layout direction over the end when bringing one
		// side or the other
		// of a large rect into view. If we decide to bring in end because start
		// is already
		// visible, limit the scroll such that start won't go out of bounds.
		final int dx;
		if (getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL) {
			dx = offScreenRight != 0 ? offScreenRight : Math.max(offScreenLeft, childRight - parentRight);
		} else {
			dx = offScreenLeft != 0 ? offScreenLeft : Math.min(childLeft - parentLeft, offScreenRight);
		}

		// Favor bringing the top into view over the bottom. If top is already
		// visible and
		// we should scroll to make bottom visible, make sure top does not go
		// out of bounds.
		int dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
		//
		this.mDy = dy;
		mSelectedView = child;
		if (mSelectionNotifier == null) {
			mSelectionNotifier = new SelectionNotifier();
		}
		//
		if (dx != 0 || dy != 0) {
			if (immediate) {
				parent.scrollBy(dx, dy);
			} else {
				parent.smoothScrollBy(dx, dy);
			}
			//
			if (isFirst) {
				parent.addOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
						if (newState == RecyclerView.SCROLL_STATE_IDLE) {
							parent.post(mSelectionNotifier);
						} else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
						}
					}

					@Override
					public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
						// parent.post(mSelectionNotifier);
					}
				});
				isFirst = false;
			}
			return true;
		}
		//
		parent.post(mSelectionNotifier);
		return false;
	}

	/**
	 * 焦点搜索失败处理.
	 */
	@Override
	public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler,
			RecyclerView.State state) {
		View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
		return null;
	}

	private class SelectionNotifier implements Runnable {
		@Override
		public void run() {
			fireOnSelected();
		}
	}

	public View getSelectedView() {
		return mSelectedView;
	}

	public void setTopPadding(int topPadding) {
		this.mTopPadding = topPadding;
	}

	public void setBottomPadding(int bottomPadding) {
		this.mBottomPadding = bottomPadding;
	}

	public void setLeftPadding(int leftPadding) {
		this.mLeftPadding = leftPadding;
	}

	public void setRightPadding(int rightPadding) {
		this.mRightPadding = rightPadding;
	}

	private void fireOnSelected() {
		if (mChildSelectedListener != null) {
			int pos = getPosition(getSelectedView());
			View view = getSelectedView();
			mChildSelectedListener.onChildSelected(mParent, getSelectedView(), pos, mDy);
		}
	}

	public void setOnChildSelectedListener(OnChildSelectedListener listener) {
		mChildSelectedListener = listener;
	}
}
