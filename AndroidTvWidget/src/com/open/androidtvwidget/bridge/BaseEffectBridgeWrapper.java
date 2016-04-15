package com.open.androidtvwidget.bridge;

import com.open.androidtvwidget.view.MainUpView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class BaseEffectBridgeWrapper extends BaseEffectBridge {

	private static final float DEFUALT_SCALE = 1.0f;
	private static final int DEFUALT_TRAN_DUR_ANIM = 300;

	private MainUpView mMainUpView;
	private Drawable mDrawableShadow;
	private Drawable mDrawableUpRect;
	private Context mContext;
	private Rect mUpPaddingRect = new Rect(0, 0, 0, 0);
	private Rect mShadowPaddingRect = new Rect(0, 0, 0, 0);
	
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
	public Rect getDrawShadowRect() {
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
	public Rect getDrawUpRect() {
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
		setUnFocusView(view, DEFUALT_SCALE, DEFUALT_SCALE);
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
			oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(DEFUALT_TRAN_DUR_ANIM).start();
		}
	}

	/**
	 * 新的焦点View.
	 */
	@Override
	public void onFocusView(View focusView, float scaleX, float scaleY) {
		if (focusView != null) {
			focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(DEFUALT_TRAN_DUR_ANIM).start();
			runTranslateAnimation(focusView, scaleX, scaleY);
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
			Rect shadowPaddingRect = getDrawShadowRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			drawableShadow.getPadding(padding);
			drawableShadow.setBounds(-padding.left - (shadowPaddingRect.left), -padding.top - (shadowPaddingRect.top),
					width + padding.right + (shadowPaddingRect.right),
					height + padding.bottom + (shadowPaddingRect.bottom));
			drawableShadow.draw(canvas);
		}
	}

	/**
	 * 绘制最上层的移动边框.
	 */
	public void onDrawUpRect(Canvas canvas) {
		Drawable drawableUp = getUpRectDrawable();
		if (drawableUp != null) {
			Rect paddingRect = getDrawUpRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			// 边框的绘制.
			drawableUp.getPadding(padding);
			drawableUp.setBounds(-padding.left - (paddingRect.left), -padding.top - (paddingRect.top),
					width + padding.right + (paddingRect.right), height + padding.bottom + (paddingRect.bottom));
			drawableUp.draw(canvas);
		}
	}

	public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
		try {
			Rect fromRect = findLocationWithView(getMainUpView());
			Rect toRect = findLocationWithView(toView);
			float x = toRect.left - fromRect.left;
			float y = toRect.top - fromRect.top;
			flyWhiteBorder(toView, x, y, scaleX, scaleY);
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

	public void flyWhiteBorder(final View focusView, float x, float y, float scaleX, float scaleY) {
		int newWidth = 0;
		int newHeight = 0;
		int oldWidth = 0;
		int oldHeight = 0;
		if (focusView != null) {
			newWidth = (int) (focusView.getMeasuredWidth() * scaleX);
			newHeight = (int) (focusView.getMeasuredHeight() * scaleY);
			x = x + (focusView.getMeasuredWidth() - newWidth) / 2;
			y = y + (focusView.getMeasuredHeight() - newHeight) / 2;
		}

		oldWidth = getMainUpView().getMeasuredWidth();
		oldHeight = getMainUpView().getMeasuredHeight();

		ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(getMainUpView(), "translationX", x);
		ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(getMainUpView(), "translationY", y);
		// BUG，因为缩放会造成图片失真(拉伸).
		// hailong.qiu 2016.02.26 修复 :)
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(getMainUpView()), "width", oldWidth,
				(int) newWidth);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(getMainUpView()), "height", oldHeight,
				(int) newHeight);
		//
		AnimatorSet mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
		mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
		mAnimatorSet.setDuration(DEFUALT_TRAN_DUR_ANIM);
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
