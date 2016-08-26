package com.open.androidtvwidget.leanback.recycle;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.utils.OPENLOG;

/**
 * Created by hailongqiu on 2016/8/25.
 */
public class AutoMeaureGridLayoutManager extends GridLayoutManager {

    public AutoMeaureGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public AutoMeaureGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    private int[] mMeasuredDimension = new int[2];
    private boolean mIsAutoMeaure = true;

//    @Override
//    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//
//        if (!mIsAutoMeaure) {
//            super.onMeasure(recycler, state, widthSpec, heightSpec);
//        } else {
//
//            final int widthMode = View.MeasureSpec.getMode(widthSpec);
//            final int heightMode = View.MeasureSpec.getMode(heightSpec);
//            final int widthSize = View.MeasureSpec.getSize(widthSpec);
//            final int heightSize = View.MeasureSpec.getSize(heightSpec);
//
//            int width = 0;
//            int height = 0;
//
//            //
//            for (int i = 0; i < getItemCount(); i++) {
//                // grid layout 计算.
//                try {
//                    measureScrapChild(recycler, i,
//                            widthSpec,
//                            View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                            mMeasuredDimension);
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                }
//                if (getOrientation() == VERTICAL) {
//                    if (i % getSpanCount() != 0) {
//                        width += mMeasuredDimension[0];
//                    }
//                    if ((i % getSpanCount()) == 0) {
//                        height += mMeasuredDimension[1];
//                    }
//                } else {
//                    if (i % getSpanCount() == 0) {
//                        height += mMeasuredDimension[1];
//                        width += mMeasuredDimension[0] + 2;
//                    }
//                }
//            }
//
//            switch (widthMode) {
//                case View.MeasureSpec.UNSPECIFIED:
//                    break;
//                case View.MeasureSpec.AT_MOST: // wrap_parent
//                    width = widthSize;
//                    break;
//                case View.MeasureSpec.EXACTLY: // match_parent
//                    width = widthSize;
//                    break;
//            }
//
//            switch (heightMode) {
//                case View.MeasureSpec.UNSPECIFIED:
//                    break;
//                case View.MeasureSpec.AT_MOST: // wrap_parent
////                    height = heightSize;
//                    break;
//                case View.MeasureSpec.EXACTLY: // match_parent
//                    height = heightSize;
//                    break;
//            }
//            //
//            setMeasuredDimension(width, height);
//        }
//
//    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        View view = recycler.getViewForPosition(position);
        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(widthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }

    public void setAutoMeasureEnabled(boolean isAutoMeaure) {
        this.mIsAutoMeaure = isAutoMeaure;
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        return null;
    }

}
