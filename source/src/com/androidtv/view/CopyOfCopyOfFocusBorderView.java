package com.androidtv.view;

import com.androidtv.R;
import com.androidtv.utils.DensityUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

//import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * 
 * @author Frozen Free Fall
 *
 */
public class CopyOfCopyOfFocusBorderView extends ImageView {
	private static int X_BORDER_SIZE = 0;
	private static int Y_BORDER_SIZE = 0;
	private static int TRAN_DUR_ANIM = 250;

	public CopyOfCopyOfFocusBorderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setBackgroundResource(R.drawable.focus_bound);
		this.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 设置边界框的外框大小
	 * 
	 * @param size
	 */
	public void setBorderSize(int w, int h) {
		X_BORDER_SIZE = w;
		Y_BORDER_SIZE = h;
	}

	/**
	 * 设置位移动画时间
	 * 
	 * @param dur
	 */
	public void setTranslateAnimtionDuration(int dur) {
		TRAN_DUR_ANIM = dur;
	}

	private int mLeft, mTop, mRight, mBottom;

	public void setLocation(View view) {
		ViewLocation location = findLocationWithView(view);
		mLeft = location.x - (int) X_BORDER_SIZE;
		mTop = location.y - (int) Y_BORDER_SIZE;
		mRight = location.x + (int) X_BORDER_SIZE + view.getWidth();
		mBottom = location.y + (int) Y_BORDER_SIZE + view.getHeight();
		this.layout(mLeft, mTop, mRight, mBottom);
		this.clearAnimation();
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * 记录上一次的焦点组件，用于判断是否未移动控件的焦点，相同则不重新加载动画
	 */
	private View mLastFocusView;

	/**
	 * 启动焦点框位移动�?
	 */
	public void runTranslateAnimation(View toView) {
		runTranslateAnimation(toView, 1.0F, 1.0F);
	}

	/**
	 * 启动焦点框位移及缩放动画
	 */
	public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
		if (toView == null || mLastFocusView == toView) {
			return;
		}

		int x = toView.getLeft() - this.getLeft();
		int y = toView.getTop() - this.getTop();

		Log.d("LIF", "this.getLeft() = " + this.getLeft());
		Log.d("LIF", "this.getTop() = " + this.getTop());
		Log.d("LIF", "toView.getLeft() = " + toView.getLeft());
		Log.d("LIF", "toView.getTop() = " + toView.getTop());

		ViewLocation locationTo = findLocationWithView(toView);
		ViewLocation locationFrom = findLocationWithView(this);

		/*
		 * int x = locationTo.x - locationFrom.x; int y = locationTo.y -
		 * locationFrom.y;
		 */

		int deltaX = (toView.getWidth() - this.getWidth()) / 2;
		int deltaY = (toView.getHeight() - this.getHeight()) / 2;
		x = DensityUtil.dip2px(this.getContext(), x + deltaX);
		y = DensityUtil.dip2px(this.getContext(), y + deltaY);

		float toWidth = toView.getWidth() * scaleX;
		float toHeight = toView.getHeight() * scaleY;
		float targetScaleX = (float) toWidth
				/ (float) (this.getWidth() - 2 * X_BORDER_SIZE);
		float targetScaleY = (float) toHeight
				/ (float) (this.getHeight() - 2 * Y_BORDER_SIZE);
		int width = (int) (toWidth + 2 * X_BORDER_SIZE * targetScaleX);
		int height = (int) (toHeight + 2 * Y_BORDER_SIZE * targetScaleY);

		flyWhiteBorder(width, height, x, y);

		mLastFocusView = toView;
	}

	private float mLastXPos = 0F;
	private float mLastYPos = 0F;
	private float mLastXScale = 1F;
	private float mLastYScale = 1F;

	/**
	 * 白色焦点框飞动�?�移动�?�变�?
	 * 
	 * @param width
	 *            白色框的�?(非放大后�?)
	 * @param height
	 *            白色框的�?(非放大后�?)
	 * @param paramFloat1
	 *            x坐标偏移量，相对于初始的白色框的中心�?
	 * @param paramFloat2
	 *            y坐标偏移量，相对于初始的白色框的中心�?
	 * */
	@SuppressLint("NewApi")
	private void flyWhiteBorder(int width, int height, float x, float y) {
		// this.setVisibility(View.VISIBLE);
		int mWidth = this.getWidth();
		int mHeight = this.getHeight();

		if (mWidth == 0 || mHeight == 0) {
			mWidth = 10;
			mHeight = 10;
		}

		float scaleX = (float) width / (float) mWidth;
		float scaleY = (float) height / (float) mHeight;

		/*
		 * Log.d("LIF", "mWidth = " + mWidth + ", mHeight = " + mHeight);
		 * Log.d("LIF", "width = " + width + ", height = " + height);
		 */
		Log.d("LIF", "x = " + x + ", y = " + y);
		Log.d("LIF", "scaleX = " + scaleX + ", scaleY = " + scaleY);

		/*
		 * animate().translationX(x).translationY(y).setDuration(TRAN_DUR_ANIM)
		 * .scaleX(scaleX).scaleY(scaleY) .setInterpolator(new
		 * DecelerateInterpolator()) .setListener(flyListener).start();
		 */

		float scaleFromX = mLastXScale;
		float scaleFromY = mLastYScale;
		float scaleToX = scaleX;
		float scaleToY = scaleY;

		float translateFromY = mLastYPos;
		float translateToY = y;
		float translateFromX = mLastXPos;
		float translateToX = x;

		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(this, "scaleX", scaleFromX,
				scaleToX), ObjectAnimator.ofFloat(this, "scaleY", scaleFromY,
				scaleToY), ObjectAnimator.ofFloat(this, "translationX",
				translateFromX, translateToX),

		ObjectAnimator.ofFloat(this, "translationY", translateFromY,
				translateToY));
		set.addListener(new AnimatorListener() {

			@Override
			public void onAnimationCancel(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				if (CopyOfCopyOfFocusBorderView.this.getVisibility() != View.VISIBLE) {
					CopyOfCopyOfFocusBorderView.this
							.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationStart(Animator arg0) {
			}

		});
		set.setDuration(250).start();

		mLastXPos = x;
		mLastYPos = y;
		mLastXScale = scaleX;
		mLastYScale = scaleY;
	}

	private class ViewLocation {
		private int x;
		private int y;

		public ViewLocation(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * 获取View的位�?
	 * 
	 * @param view
	 *            获取的控�?
	 * @return 位置
	 */
	public ViewLocation findLocationWithView(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		// view.getLocationInWindow(location);
		Log.d("LIF", "location[0] = " + location[0]);
		Log.d("LIF", "location[1] = " + location[1]);
		return new ViewLocation(location[0], location[1]);
	}

	/*
	 * @SuppressLint("NewApi") private Animator.AnimatorListener flyListener =
	 * new Animator.AnimatorListener() {
	 * 
	 * @Override public void onAnimationCancel(Animator arg0) { }
	 * 
	 * @Override public void onAnimationEnd(Animator arg0) { if
	 * (FocusBorderView.this.getVisibility() != View.VISIBLE) {
	 * FocusBorderView.this.setVisibility(View.VISIBLE); } }
	 * 
	 * @Override public void onAnimationRepeat(Animator arg0) { }
	 * 
	 * @Override public void onAnimationStart(Animator arg0) { }
	 * 
	 * };
	 */
}
