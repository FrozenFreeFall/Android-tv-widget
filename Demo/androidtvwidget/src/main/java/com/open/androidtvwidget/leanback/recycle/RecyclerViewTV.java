package com.open.androidtvwidget.leanback.recycle;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.recycle.impl.PrvInterface;
import com.open.androidtvwidget.utils.OPENLOG;

import java.util.ArrayList;

/**
 * RecyclerView TV适配版本.
 * https://github.com/zhousuqiang/TvRecyclerView(参考源码)
 */
public class RecyclerViewTV extends RecyclerView implements PrvInterface {

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
    private OnItemClickListener mOnItemClickListener; // item 单击事件.
    private ItemListener mItemListener;
    private int offset = -1;

    private RecyclerViewTV.OnChildViewHolderSelectedListener mChildViewHolderSelectedListener;

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
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(RecyclerViewTV.this, itemView, getChildLayoutPosition(itemView));
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
        // 设置单击事件，修复.
        if (!child.hasOnClickListeners()) {
            child.setOnClickListener(mItemListener);
        }
        // 设置焦点事件，修复.
        if (child.getOnFocusChangeListener() == null) {
            child.setOnFocusChangeListener(mItemListener);
        }
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
        // 解决4.4版本抢焦点的问题
        if (Build.VERSION.SDK_INT == 19) {
            return !(hasFocus() && !super.isInTouchMode());
        } else {
            return super.isInTouchMode();
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        // 一行的选中.
        if (mChildViewHolderSelectedListener != null) {
            int pos = getPositionByView(child);
            RecyclerView.ViewHolder vh = getChildViewHolder(child);
            mChildViewHolderSelectedListener.onChildViewHolderSelected(this, vh, pos);
        }
        //
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
     * @param offsetEnd 从结尾到你移动的位置.
     */
    public void setSelectedItemOffset(int offsetStart, int offsetEnd) {
        setSelectedItemAtCentered(false);
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

    public int getSelectPostion() {
        View view = getSelectView();
        if (view != null)
            return getPositionByView(view);
        return -1;
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

        void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position);
    }

    public interface OnChildViewHolderSelectedListener {
        public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder vh,
                                              int position);
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerViewTV parent, View itemView, int position);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 控制焦点高亮问题.
     * 2016.08.29
     */
    public void setOnChildViewHolderSelectedListener(OnChildViewHolderSelectedListener listener) {
        mChildViewHolderSelectedListener = listener;
    }

    private int getPositionByView(View view) {
        if (view == null) {
            return NO_POSITION;
        }
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        if (params == null || params.isItemRemoved()) {
            // when item is removed, the position value can be any value.
            return NO_POSITION;
        }
        return params.getViewPosition();
    }

    /////////////////// 按键加载更多 start start start //////////////////////////

    private PagingableListener mPagingableListener;
    private boolean isLoading = false;

    public interface PagingableListener {
        void onLoadMoreItems();
    }

    @Override
    public void setOnLoadMoreComplete() {
        isLoading = false;
    }

    @Override
    public void setPagingableListener(PagingableListener pagingableListener) {
        this.mPagingableListener = pagingableListener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        if (action == KeyEvent.ACTION_UP) {
            if (!isHorizontalLayoutManger() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                // 垂直布局向下按键.
                exeuteKeyEvent();
            } else if (isHorizontalLayoutManger() && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                // 横向布局向右按键.
                exeuteKeyEvent();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean exeuteKeyEvent() {
        int totalItemCount = getLayoutManager().getItemCount();
        int lastVisibleItem = findLastVisibleItemPosition();
        int lastComVisiPos = findLastCompletelyVisibleItemPosition();
        int visibleItemCount = getChildCount();
        int firstVisibleItem = findFirstVisibleItemPosition();
        // 判断是否显示最底了.
        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            isLoading = true;
            if (mPagingableListener != null) {
//                OPENLOG.D(" totalItemCount: " + totalItemCount +
//                        " lastVisibleItem: " + lastVisibleItem +
//                        " lastComVisiPos: " + lastComVisiPos);
                mPagingableListener.onLoadMoreItems();
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为横向布局
     */
    private boolean isHorizontalLayoutManger() {
        LayoutManager lm = getLayoutManager();
        if (lm != null) {
            if (lm instanceof LinearLayoutManager) {
                LinearLayoutManager llm = (LinearLayoutManager) lm;
                return LinearLayoutManager.HORIZONTAL == llm.getOrientation();
            }
            if (lm instanceof GridLayoutManager) {
                GridLayoutManager glm = (GridLayoutManager) lm;
                return GridLayoutManager.HORIZONTAL == glm.getOrientation();
            }
        }
        return false;
    }

    /**
     * 最后的位置.
     */
    public int findLastVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager instanceof GridLayoutManager) {
                return ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /**
     * 滑动到底部.
     */
    public int findLastCompletelyVisibleItemPosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
            if (layoutManager instanceof GridLayoutManager) {
                return ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
        }
        return RecyclerView.NO_POSITION;
    }

    public int findFirstVisibleItemPosition() {
        LayoutManager lm = getLayoutManager();
        if (lm != null) {
            if (lm instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
            }
            if (lm instanceof GridLayoutManager) {
                return ((GridLayoutManager) lm).findFirstVisibleItemPosition();
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /////////////////// 按键加载更多 end end end //////////////////////////

    /////////////////// 按键拖动 Item start start start ///////////////////////

    private final ArrayList<OnItemKeyListener> mOnItemKeyListeners =
            new ArrayList<OnItemKeyListener>();

    public static interface OnItemKeyListener {
        public boolean dispatchKeyEvent(KeyEvent event);
    }

    public void addOnItemKeyListener(OnItemKeyListener listener) {
        mOnItemKeyListeners.add(listener);
    }

    public void removeOnItemKeyListener(OnItemKeyListener listener) {
        mOnItemKeyListeners.remove(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    ////////////////// 按键拖动 Item end end end /////////////////////////

    /**
     * 设置默认选中.
     */
    public void setDefaultSelect(int pos) {
        GeneralAdapter.ViewHolder vh = (GeneralAdapter.ViewHolder) findViewHolderForAdapterPosition(pos);
        requestFocusFromTouch();
        if (vh != null)
            vh.itemView.requestFocus();
    }

}
