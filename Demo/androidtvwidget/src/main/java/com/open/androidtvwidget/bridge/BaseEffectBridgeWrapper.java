package com.open.androidtvwidget.bridge;

import com.open.androidtvwidget.view.MainUpView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class BaseEffectBridgeWrapper extends BaseEffectBridge {

	private static final float DEFAULT_SCALE = 1.0f;
	private static final int DEFAULT_TRAN_DUR_ANIM = 300;

	private MainUpView mMainUpView;
	private Drawable mDrawableShadow;
	private Drawable mDrawableUpRect;
	private Context mContext;
	private RectF mUpPaddingRect = new RectF(0, 0, 0, 0);
	private RectF mShadowPaddingRect = new RectF(0, 0, 0, 0);

	/**
	 * 继承这个类重写的话.<br>
	 * 记得要加 super.onInitBridge(view);
	 */
	@Override
	public void onInitBridge(MainUpView view) {
		mContext = view.getContext();
	}

	/**
	 * ==========设置阴影图片===========
	 */

	@Override
	public void setShadowResource(int resId) {
		try {
			this.mDrawableShadow = mContext.getResources().getDrawable(resId); // 移动的边框.
		} catch (Exception e) {
			this.mDrawableShadow = null;
			e.printStackTrace();
		}
	}

	/**
	 * 当图片边框不自带阴影的话，可以自行设置阴影图片. 设置阴影.
	 */
	@Override
	public void setShadowDrawable(Drawable shadowDrawable) {
		this.mDrawableShadow = shadowDrawable;
	}

	@Override
	public Drawable getShadowDrawable() {
		return this.mDrawableShadow;
	}

	/**
	 * 根据阴影图片边框 自行 填写 相差的边距. <br>
	 * 比如 res/drawble/white_shadow.9.png的图片，边距就差很多.
	 */
	public void setDrawShadowPadding(int size) {
		setDrawShadowRectPadding(new Rect(size, size, size, size));
	}

	/**
	 * 根据阴影图片边框 自行 填写 相差的边距. <br>
	 * 比如 res/drawble/white_shadow.9.png的图片，边距就差很多.
	 */
	@Override
	public void setDrawShadowRectPadding(Rect rect) {
		mShadowPaddingRect.set(rect);
	}

	@Override
	public void setDrawShadowRectPadding(RectF rectf) {
		mShadowPaddingRect.set(rectf);
	}

	@Override
	public RectF getDrawShadowRect() {
		return this.mShadowPaddingRect;
	}

	/**
	 * ==========设置边框图片===========
	 */

	@Override
	public void setUpRectResource(int resId) {
		try {
			this.mDrawableUpRect = mContext.getResources().getDrawable(resId); // 移动的边框.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置最上层的边框.
	 */
	@Override
	public void setUpRectDrawable(Drawable upRectDrawable) {
		this.mDrawableUpRect = upRectDrawable;
	}

	@Override
	public Drawable getUpRectDrawable() {
		return this.mDrawableUpRect;
	}

	/**
	 * 根据图片边框 自行 填写 相差的边距. <br>
	 * 比如 res/drawble/white_light_10.9.png的图片，边距就差很多.
	 *
	 * @param size
	 *            负数边框减小，正数反之(阴影边框一样的).
	 */
	public void setDrawUpRectPadding(int size) {
		setDrawUpRectPadding(new Rect(size, size, size, size));
	}

	/**
	 * 根据图片边框 自行 填写 相差的边距. <br>
	 * 比如 res/drawble/white_light_10.9.png的图片，边距就差很多.
	 */
	@Override
	public void setDrawUpRectPadding(Rect rect) {
		mUpPaddingRect.set(rect);
	}

	@Override
	public void setDrawUpRectPadding(RectF rectf) {
		mUpPaddingRect.set(rectf);
	}

	@Override
	public RectF getDrawUpRect() {
		return this.mUpPaddingRect;
	}

	/**
	 * ==========焦点View处理===========
	 */

	public void setFocusView(View newView, View oldView, float scale) {
		setFocusView(newView, scale);
		setUnFocusView(oldView);
	}

	/**
	 * 设置焦点子控件的移动和放大.
	 */
	public void setFocusView(View view, float scale) {
		setFocusView(view, scale, scale);
	}

	public void setFocusView(View view, float scaleX, float scaleY) {
		onFocusView(view, scaleX, scaleY);
	}

	/**
	 * 设置无焦点子控件还原.
	 */
	public void setUnFocusView(View view) {
		setUnFocusView(view, DEFAULT_SCALE, DEFAULT_SCALE);
	}

	public void setUnFocusView(View view, float scaleX, float scaleY) {
		onOldFocusView(view, scaleX, scaleY);
	}

	/**
	 * 老的焦点View.
	 */
	@Override
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
		if (oldFocusView != null) {
			oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(DEFAULT_TRAN_DUR_ANIM).start();
		}
	}

	/**
	 * 新的焦点View.
	 */
	@Override
	public void onFocusView(View focusView, float scaleX, float scaleY) {
		if (focusView != null) {
			focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(DEFAULT_TRAN_DUR_ANIM).start(); // 放大焦点VIEW的动画.
			runTranslateAnimation(focusView, scaleX, scaleY); // 移动边框的动画。
		}
	}

	/**
	 * ==========绘制处理===========
	 */

	/**
	 * 需要绘制的东西.
	 */
	@Override
	public boolean onDrawMainUpView(Canvas canvas) {
		canvas.save();
		// 绘制阴影.
		onDrawShadow(canvas);
		// 绘制最上层的边框.
		onDrawUpRect(canvas);
		canvas.restore();
		return true;
	}

	/**
	 * 绘制外部阴影.
	 */
	public void onDrawShadow(Canvas canvas) {
		Drawable drawableShadow = getShadowDrawable();
		if (drawableShadow != null) {
			RectF shadowPaddingRect = getDrawShadowRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			drawableShadow.getPadding(padding);
            //
            int left = (int)Math.rint(shadowPaddingRect.left);
            int right = (int)Math.rint(shadowPaddingRect.right);
            int bottom = (int)Math.rint(shadowPaddingRect.bottom);
            int top = (int)Math.rint(shadowPaddingRect.top);
            //
			drawableShadow.setBounds(-padding.left - (left), -padding.top - (top),
					width + padding.right + (right),
					height + padding.bottom + (bottom));
			drawableShadow.draw(canvas);
		}
	}

	/**
	 * 绘制最上层的移动边框.
	 */
	public void onDrawUpRect(Canvas canvas) {
		Drawable drawableUp = getUpRectDrawable();
		if (drawableUp != null) {
			RectF paddingRect = getDrawUpRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			// 边框的绘制.
			drawableUp.getPadding(padding);
            //
            int left = (int)Math.rint(paddingRect.left);
            int right = (int)Math.rint(paddingRect.right);
            int bottom = (int)Math.rint(paddingRect.bottom);
            int top = (int)Math.rint(paddingRect.top);
            //
			drawableUp.setBounds(-padding.left - (left), -padding.top - (top),
					width + padding.right + (right), height + padding.bottom + (bottom));
			drawableUp.draw(canvas);
		}
	}

	public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
		try {
			flyWhiteBorder(toView, getMainUpView(), scaleX, scaleY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Rect findLocationWithView(View view) {
		ViewGroup root = (ViewGroup) getMainUpView().getParent();
		Rect rect = new Rect();
		root.offsetDescendantRectToMyCoords(view, rect);
		return rect;
	}

	public int[] getViewLocationScreen(View v) {
		int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
	}

	public void flyWhiteBorder(final View focusView,  View moveView, float scaleX, float scaleY) {
		int newWidth = 0;
		int newHeight = 0;
		int oldWidth = 0;
		int oldHeight = 0;

		int newX = 0;
		int newY = 0;

		if (focusView != null) {
			// 有一点偏差,需要进行四舍五入.
			newWidth = (int) (Math.rint(focusView.getMeasuredWidth() * scaleX));
			newHeight = (int) (Math.rint(focusView.getMeasuredHeight() * scaleY));
			oldWidth = moveView.getMeasuredWidth();
			oldHeight = moveView.getMeasuredHeight();
			Rect fromRect = findLocationWithView(moveView);
			Rect toRect = findLocationWithView(focusView);
			int x = toRect.left - fromRect.left;
			int y = toRect.top - fromRect.top;
			newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
			newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
		}

		ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
		ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);
		// BUG，因为缩放会造成图片失真(拉伸).
		// hailong.qiu 2016.02.26 修复 :)
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "width", oldWidth,
				(int) newWidth);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "height", oldHeight,
				(int) newHeight);
		//
		AnimatorSet mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
		mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
		mAnimatorSet.setDuration(DEFAULT_TRAN_DUR_ANIM);
		mAnimatorSet.start();
	}

	/**
	 * 用於放大的view
	 */
	public class ScaleView {
		private View view;
		private int width;
		private int height;

		public ScaleView(View view) {
			this.view = view;
		}

		public int getWidth() {
			return view.getLayoutParams().width;
		}

		public void setWidth(int width) {
			this.width = width;
			view.getLayoutParams().width = width;
			view.requestLayout();
		}

		public int getHeight() {
			return view.getLayoutParams().height;
		}

		public void setHeight(int height) {
			this.height = height;
			view.getLayoutParams().height = height;
			view.requestLayout();
		}
	}

	/**
	 * 最上层的移动的view.
	 */
	@Override
	public void setMainUpView(MainUpView view) {
		this.mMainUpView = view;
	}

	/**
	 * 最上层移动的view.
	 */
	@Override
	public MainUpView getMainUpView() {
		return this.mMainUpView;
	}

}
