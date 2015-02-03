package com.androidtv.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.androidtv.utils.ImageReflect;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class FocusRelativeLayout extends RelativeLayout {
	private static final float DEFUALT_SCALE_VALUE = 1.0f;
	private static final float DEFUALT_REFLECT_PADDING = 4;

	private FocusBorderView mBorderView = null;
	private float scaleX = DEFUALT_SCALE_VALUE;
	private float scaleY = DEFUALT_SCALE_VALUE;
	private float reflectPadding = DEFUALT_REFLECT_PADDING;
	private boolean isBorderShow = true;

	private View drawChild = null;
	private static Map<View, Bitmap> reflectMap = new HashMap<View, Bitmap>();
	private FocusRelativeLayoutCallBack mFocusRelativeLayoutCallBack = null;

	public FocusRelativeLayout(Context context) {
		super(context);
		// initFocusRelativeLayout();
	}

	public FocusRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// initFocusRelativeLayout();
	}

	public FocusRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// initFocusRelativeLayout();
	}

	public void initFocusRelativeLayout() {
		// 让子控件放大，边框移动出去绘制出来.
		setClipChildren(false);
		setClipToPadding(false);
		// 边框初始化设置.
		mBorderView = new FocusBorderView(getContext());
		addView(mBorderView);
		mBorderView.setVisibility(View.INVISIBLE);
	}

	public void setBorderShow(boolean b) {
		isBorderShow = b;
		mBorderView.setShow(b);
	}

	/**
	 * 设置边框获取焦点的事件处�?.
	 */
	public void setOnFocusRelativeLayoutCallBack(FocusRelativeLayoutCallBack cb) {
		mFocusRelativeLayoutCallBack = cb;
	}

	private ViewGroup mViewGroupParent = null;

	/*
	 * 为了控制焦点顺序的BUG.
	 */
	public void setViewGroup(ViewGroup vg) {
		mViewGroupParent = vg;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		if (action == KeyEvent.ACTION_DOWN && mViewGroupParent != null) {
			int id = -1;
			int code = event.getKeyCode();
			switch (code) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				id = getFocusedChild().getNextFocusLeftId();
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				id = getFocusedChild().getNextFocusRightId();
				break;
			case KeyEvent.KEYCODE_PAGE_DOWN:
				id = getFocusedChild().getNextFocusDownId();
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				id = getFocusedChild().getNextFocusUpId();
				break;
			default:
				break;
			}
			/* 获取下一个焦点的子控件 */
			View requestFocusChild = mViewGroupParent.findViewById(id);
			if (requestFocusChild != null) {
				if (checkChildParent(requestFocusChild)) {
					requestFocusChild.requestFocus();
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 检查当前焦点的子控件的下一个焦点的父控件是否和一致.
	 */
	private boolean checkChildParent(View requestFocusChild) {
		return (!requestFocusChild.getParent().equals(
				getFocusedChild().getParent()));
	}

	/**
	 * 设置TV和手机之间的像素.
	 */
	public void setBorderTV(boolean b) {
		mBorderView.setBorderTV(b);
	}

	/**
	 * 设置外边框的背景.
	 */
	public void setBorderViewBg(int resid) {
		mBorderView.setBackgroundResource(resid);
	}

	/**
	 * 设置外边框的背景的阴影的大小.
	 */
	public void setBorderViewSize(int w, int h) {
		mBorderView.setBorderSize(w, h);
	}

	/**
	 * 设置外边框的缩放.
	 */
	public void setBorderScale(float x, float y) {
		scaleX = x;
		scaleY = y;
	}

	/**
	 * 设置倒影和原生控件之间的间距.
	 */
	public void setReflectPadding(int reflectPadding) {
		this.reflectPadding = reflectPadding;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		/* 绘制子控件的倒影 */
		findChildReflection(this, canvas);
		if (drawChild != null) {
			setDrawChildFunction(canvas);
		}
		super.dispatchDraw(canvas);
	}

	/**
	 * 查找�?要绘制�?�影的子控件.
	 */
	private void findChildReflection(ViewGroup vg, Canvas canvas) {
		for (int i = 0; i < vg.getChildCount(); i++) {
			View child1 = vg.getChildAt(i);
			if (checkReflectionRLay(child1) && !child1.equals(drawChild)) { // 判断是否�?.
				ReflectionRelativeLayout r = (ReflectionRelativeLayout) child1;
				if (r.isReflection()) {
					int w = child1.getWidth();
					int h = child1.getHeight();
					Rect child1Rect = new Rect(child1.getLeft(),
							child1.getTop(), child1.getRight(),
							child1.getBottom());
					float scaleValue = DEFUALT_SCALE_VALUE;
					onDrawScaleReflectio(child1, canvas, child1Rect, scaleValue);
				}
			} else if (child1 instanceof ViewGroup) {
				findChildReflection((ViewGroup) child1, canvas);
			}
		}
	}

	/**
	 * 绘制放大的�?�影.
	 */
	private void setDrawChildFunction(Canvas canvas) {
		View child1 = drawChild;
		if (checkReflectionRLay(child1)) {
			ReflectionRelativeLayout r = (ReflectionRelativeLayout) child1;
			if (r.isReflection()) {
				int w = child1.getWidth();
				int h = child1.getHeight();
				Rect child1Rect = new Rect(child1.getLeft(), child1.getTop(),
						child1.getRight(), child1.getBottom());
				float scaleValue = scaleX;
				onDrawScaleReflectio(drawChild, canvas, child1Rect, scaleValue);
			}
		}
	}

	/**
	 * 可以继承 FocusRelativeLayout 重写 绘制倒影的功能，可以绘制其它样式.
	 */
	public void onDrawScaleReflectio(View child, Canvas canvas, Rect rect,
			float scaleValue) {
		Matrix matrix = new Matrix();
		matrix.postScale(scaleValue, scaleValue);
		Bitmap rb = null;
		rb = reflectMap.get(child);
		/* 转换倒影图片 */
		if (rb == null) {
			Bitmap b = Bitmap.createBitmap(rect.width(), rect.height(),
					Config.ARGB_8888);
			Canvas bc = new Canvas(b);
			child.draw(bc);
			rb = ImageReflect.createCutReflectedImage(b); // 转换倒影bitmap.
			if (rb == null)
				return;
			reflectMap.put(child, rb);
		}
		/* 设置倒影图片位置 */
		if (scaleValue != DEFUALT_SCALE_VALUE) {
			int pScaleW = (int) ((rb.getWidth() * scaleValue) - rb.getWidth()) / 2;
			int pScaleH = (int) ((rb.getHeight() * scaleValue) - rb.getHeight());
			matrix.postTranslate(rect.left - pScaleW, rect.top + rect.height()
					+ pScaleH + reflectPadding);
		} else {
			matrix.postTranslate(rect.left, rect.top + rect.height()
					+ reflectPadding);
		}
		/* 绘制倒影 */
		if (rb != null) {
			canvas.drawBitmap(rb, matrix, null);
		}
	}

	/**
	 * 判断是否�? �?要边框的子控件布�?.
	 */
	private boolean checkReflectionRLay(View child) {
		return child instanceof ReflectionRelativeLayout;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initFocusRelativeLayout();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		/* 搜索子控件设置事�? */
		findChildFocusWidget(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 设置子控件的焦点事件.
	 */
	@SuppressLint("NewApi")
	private void findChildFocusWidget(ViewGroup vg) {
		for (int i = 0; i < vg.getChildCount(); i++) {
			View childView = vg.getChildAt(i);
			if (childView instanceof ViewGroup
					&& !(childView instanceof ReflectionRelativeLayout)) {
				findChildFocusWidget((ViewGroup) childView); // 继续搜索子控件view.
			} else if (checkReflectionRLay(childView)) { // 判断是否�?要显示移动边框的子控�?.
				setChildViewEvent(childView); // 设置焦点控件的事�?.
			} else {
				childView.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						mBorderView.setVisibility(View.GONE);
					}
				});
			}
		}
	}

	@SuppressLint("NewApi")
	private void setChildViewEvent(View childView) {
		childView.setFocusable(true);
		childView.setFocusableInTouchMode(true);
		childView.setClickable(true);
		//
		childView.setOnHoverListener(new OnHoverListener() {
			@Override
			public boolean onHover(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_HOVER_ENTER:
					v.requestFocus();
					break;
				case MotionEvent.ACTION_HOVER_EXIT:
					break;
				default:
					break;
				}
				return false;
			}
		});
		//
		childView.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (mBorderView != null && hasFocus) {
					// 设置焦点(!!!bug,无法到最前面).
					v.bringToFront();
					mBorderView.bringToFront();
					// 获取焦点前.
					if (mFocusRelativeLayoutCallBack != null
							&& (v instanceof ReflectionRelativeLayout)) {
						mFocusRelativeLayoutCallBack
								.onFirstFocusInChild((ReflectionRelativeLayout) v);
					}
					// 显示mborderview.
					if (isBorderShow) {
						mBorderView.setVisibility(View.VISIBLE);
					} else {
						mBorderView.setVisibility(View.GONE);
					}
					// 保存�?要放大的子控件�?�影.
					drawChild = v;
					// 矩形�?. (正常边框)
					mBorderView.runTranslateAnimation(v, scaleX, scaleY);
					// 放大动画.
					onFocusChildAnimator(v, scaleX, scaleY);
					// 获取焦点后.
					if (mFocusRelativeLayoutCallBack != null
							&& (v instanceof ReflectionRelativeLayout)) {
						mFocusRelativeLayoutCallBack
								.onLastFocusInChild((ReflectionRelativeLayout) v);
					}
				} else {
					//
					mBorderView.setVisibility(View.GONE);
					// 失去焦点前-事件回调.
					if (mFocusRelativeLayoutCallBack != null
							&& (v instanceof ReflectionRelativeLayout)) {
						mFocusRelativeLayoutCallBack
								.onFirstFocusOutChild((ReflectionRelativeLayout) v);
					}
					// 恢复动画.
					onFocusChildAnimator(v, DEFUALT_SCALE_VALUE,
							DEFUALT_SCALE_VALUE);
					// 清空 drawchild.
					if (v.equals(drawChild)) {
						drawChild = null;
						invalidate();
					}
					// 失去焦点后-事件回调.
					if (mFocusRelativeLayoutCallBack != null
							&& (v instanceof ReflectionRelativeLayout)) {
						mFocusRelativeLayoutCallBack
								.onLastFocusOutChild((ReflectionRelativeLayout) v);
					}
				}
			}
		});
	}

	/**
	 * 重写这个函数 <br>
	 * 设置子控件的动画效果，放大，缩小，还是跳动.
	 */
	public void onFocusChildAnimator(View v, float scaleX1, float scaleY1) {
		if (scaleX1 != DEFUALT_SCALE_VALUE) { // 放大.
			android.view.ViewPropertyAnimator animator = v.animate()
					.scaleX(scaleX1).scaleY(scaleY1);
			animator.start();
		} else { // 恢复.
			android.view.ViewPropertyAnimator animator = v.animate()
					.scaleX(scaleX1).scaleY(scaleY1);
			animator.start();
		}
	}

	// 回调事件.
	public static class FocusRelativeLayoutCallBack {
		// 子控件获取焦点.
		public void onFirstFocusInChild(
				ReflectionRelativeLayout reflectionRelativeLayout) {
		}

		// 子控件失去焦点.
		public void onFirstFocusOutChild(
				ReflectionRelativeLayout reflectionRelativeLayout) {
		}

		// 子控件获取焦点.
		public void onLastFocusInChild(
				ReflectionRelativeLayout reflectionRelativeLayout) {
		}

		// 子控件失去焦点.
		public void onLastFocusOutChild(
				ReflectionRelativeLayout reflectionRelativeLayout) {
		}
	}

}
