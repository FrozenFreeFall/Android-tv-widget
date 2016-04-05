package com.open.androidtvwidget.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * 让ViewGroup中的控件在最上层 <br>
 * ListView, GridView, ViewGroup中使用. 想要置顶的子控件，不要忘记调用 bringToFront() 噢.
 * 
 * @author hailongqiu
 *
 */
public class WidgetTvViewBring {

	private int position = 0;

	public WidgetTvViewBring() {
	}

	public WidgetTvViewBring(ViewGroup vg) {
		vg.setClipChildren(false);
		vg.setClipToPadding(false);
		// vg.setChildrenDrawingOrderEnabled(true);
	}

	public void bringChildToFront(ViewGroup vg, View child) {
		position = vg.indexOfChild(child);
		if (position != -1) {
			vg.postInvalidate();
		}
	}

	/**
	 * 此函数 dispatchDraw 中调用. <br>
	 * 原理就是和最后一个要绘制的view，交换了位置. <br>
	 * 因为dispatchDraw最后一个绘制的view是在最上层的. <br>
	 * 这样就避免了使用 bringToFront 导致焦点错乱问题. <br>
	 */
	public int getChildDrawingOrder(int childCount, int i) {
		if (position != -1) {
			if (i == childCount - 1)
				return position;
			if (i == position)
				return childCount - 1;
		}
		return i;
	}

}
