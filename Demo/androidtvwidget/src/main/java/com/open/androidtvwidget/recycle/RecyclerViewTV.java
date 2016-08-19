package com.open.androidtvwidget.recycle;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.open.androidtvwidget.utils.OPENLOG;

/**
 * RecyclerView TV适配版本.
 * https://github.com/zhousuqiang/TvRecyclerView(参考源码)
 */
public class RecyclerViewTV extends RecyclerView {

    public RecyclerViewTV(Context context) {
        this(context, null);
    }

    public RecyclerViewTV(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RecyclerViewTV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private View mItemView;
    private boolean mSelectedItemCentered = true;
    private int mSelectedItemOffsetStart;
    private int mSelectedItemOffsetEnd;
    private int position = 0;
    private OnItemListener mOnItemListener;
    private ItemListener mItemListener;
    private int offset = -1;

    private void init(Context context) {
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setHasFixedSize(true);
        setWillNotDraw(true);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setChildrenDrawingOrderEnabled(true);
        //
        setClipChildren(false);
        setClipToPadding(false);

        setClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //
        mItemListener = new ItemListener() {
            /**
             * 子控件的点击事件
             * @param itemView
             */
            @Override
            public void onClick(View itemView) {
                if (null != mOnItemListener) {
                    mOnItemListener.onItemClick(RecyclerViewTV.this, itemView, getChildLayoutPosition(itemView));
                }
            }

            /**
             * 子控件的焦点变动事件
             * @param itemView
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View itemView, boolean hasFocus) {
                if (null != mOnItemListener) {
                    if (null != itemView) {
                        mItemView = itemView; // 选中的item.
                        itemView.setSelected(hasFocus);
                        if (hasFocus) {
                            mOnItemListener.onItemSelected(RecyclerViewTV.this, itemView, getChildLayoutPosition(itemView));
                        } else {
                            mOnItemListener.onItemPreSelected(RecyclerViewTV.this, itemView, getChildLayoutPosition(itemView));
                        }
                    }
                }
            }
        };
    }

    private int getFreeWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getFreeHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    @Override
    public void onChildAttachedToWindow(View child) {
        child.setOnClickListener(mItemListener);
        child.setOnFocusChangeListener(mItemListener);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        OPENLOG.D("gainFocus:" + gainFocus + " ,direction=" + direction);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean hasFocus() {
        OPENLOG.D("hasFocus");
        return super.hasFocus();
    }

    @Override
    public boolean isInTouchMode() {
        boolean result = super.isInTouchMode();
        // 解决4.4版本抢焦点的问题
        if (Build.VERSION.SDK_INT == 19) {
            return !(hasFocus() && !result);
        } else {
            return result;
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (null != child) {
            if (mSelectedItemCentered) {
                mSelectedItemOffsetStart = !isVertical() ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
                mSelectedItemOffsetStart /= 2;
                mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
            }
        }
        super.requestChildFocus(child, focused);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = getWidth() - getPaddingRight();
        final int parentBottom = getHeight() - getPaddingBottom();

        final int childLeft = child.getLeft() + rect.left;
        final int childTop = child.getTop() + rect.top;

//        final int childLeft = child.getLeft() + rect.left - child.getScrollX();
//        final int childTop = child.getTop() + rect.top - child.getScrollY();

        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
        final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
        final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);

        final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
        final boolean canScrollVertical = getLayoutManager().canScrollVertically();

        // Favor the "start" layout direction over the end when bringing one side or the other
        // of a large rect into view. If we decide to bring in end because start is already
        // visible, limit the scroll such that start won't go out of bounds.
        final int dx;
        if (canScrollHorizontal) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                dx = offScreenRight != 0 ? offScreenRight
                        : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft
                        : Math.min(childLeft - parentLeft, offScreenRight);
            }
        } else {
            dx = 0;
        }

        // Favor bringing the top into view over the bottom. If top is already visible and
        // we should scroll to make bottom visible, make sure top does not go out of bounds.
        final int dy;
        if (canScrollVertical) {
            dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
        } else {
            dy = 0;
        }
        if (cannotScrollForwardOrBackward(isVertical() ? dy : dx)) {
            offset = -1;
        } else {
            offset = isVertical() ? dy : dx;
            if (dx != 0 || dy != 0) {
                if (immediate) {
                    scrollBy(dx, dy);
                } else {
                    smoothScrollBy(dx, dy);
                }
                return true;
            }

        }

        // 重绘是为了选中item置顶，具体请参考getChildDrawingOrder方法
        postInvalidate();

        return false;
    }

    private boolean cannotScrollForwardOrBackward(int value) {
//        return cannotScrollBackward(value) || cannotScrollForward(value);
        return false;
    }

    /**
     * 判断第一个位置，没有移动.
     * getStartWithPadding --> return (mIsVertical ? getPaddingTop() : getPaddingLeft());
     */
    public boolean cannotScrollBackward(int delta) {
        return (getFirstVisiblePosition() == 0 && delta <= 0);
    }

    /**
     * 判断是否达到了最后一个位置，没有再移动了.
     * getEndWithPadding -->  mIsVertical ?  (getHeight() - getPaddingBottom()) :
     * (getWidth() - getPaddingRight());
     */
    public boolean cannotScrollForward(int delta) {
        return ((getFirstVisiblePosition() + getLayoutManager().getChildCount()) == getLayoutManager().getItemCount()) && (delta >= 0);
    }

    @Override
    public int getBaseline() {
        return offset;
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        // ViewFlinger --> smoothScrollBy(int dx, int dy, int duration, Interpolator interpolator)
        //  ViewFlinger --> run --> hresult = mLayout.scrollHorizontallyBy(dx, mRecycler, mState);
        // LinearLayoutManager --> scrollBy --> mOrientationHelper.offsetChildren(-scrolled);
        super.smoothScrollBy(dx, dy);
    }

    public int getSelectedItemOffsetStart() {
        return mSelectedItemOffsetStart;
    }

    public int getSelectedItemOffsetEnd() {
        return mSelectedItemOffsetEnd;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    /**
     * 判断是垂直，还是横向.
     */
    private boolean isVertical() {
        LinearLayoutManager layout = (LinearLayoutManager) getLayoutManager();
        return layout.getOrientation() == LinearLayoutManager.VERTICAL;
    }

    /**
     * 设置选中的Item距离开始或结束的偏移量；
     * 与滚动方向有关；
     * 与setSelectedItemAtCentered()方法二选一
     *
     * @param offsetStart
     * @param offsetEnd
     */
    public void setSelectedItemOffset(int offsetStart, int offsetEnd) {
        this.mSelectedItemOffsetStart = offsetStart;
        this.mSelectedItemOffsetEnd = offsetEnd;
    }

    /**
     * 设置选中的Item居中；
     * 与setSelectedItemOffset()方法二选一
     *
     * @param isCentered
     */
    public void setSelectedItemAtCentered(boolean isCentered) {
        this.mSelectedItemCentered = isCentered;
    }

    public View getSelectView() {
        if (mItemView == null)
            mItemView = getFocusedChild();
        return mItemView;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View view = getFocusedChild();
        if (null != view) {
            position = getChildAdapterPosition(view) - getFirstVisiblePosition();
            if (position < 0) {
                return i;
            } else {
                if (i == childCount - 1) {//这是最后一个需要刷新的item
                    if (position > i) {
                        position = i;
                    }
                    return position;
                }
                if (i == position) {//这是原本要在最后一个刷新的item
                    return childCount - 1;
                }
            }
        }
        return i;
    }

    public int getFirstVisiblePosition() {
        if (getChildCount() == 0)
            return 0;
        else
            return getChildLayoutPosition(getChildAt(0));
    }

    public int getLastVisiblePosition() {
        final int childCount = getChildCount();
        if (childCount == 0)
            return 0;
        else
            return getChildLayoutPosition(getChildAt(childCount - 1));
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {
            offset = -1;
            final View focuse = getFocusedChild();
            if (null != mOnItemListener && null != focuse) {
                mOnItemListener.onReviseFocusFollow(this, focuse, getChildLayoutPosition(focuse));
            }
        }
        super.onScrollStateChanged(state);
    }

    private interface ItemListener extends OnClickListener, OnFocusChangeListener {
    }

    public interface OnItemListener {
        void onItemPreSelected(RecyclerViewTV parent, View itemView, int position);

        void onItemSelected(RecyclerViewTV parent, View itemView, int position);

        void onItemClick(RecyclerViewTV parent, View itemView, int position);

        void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        mOnItemListener = onItemListener;
    }

}
