package com.open.androidtvwidget.adapter;

import com.open.androidtvwidget.view.MainUpView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * Anim Adapter 抽象类.
 * @author hailongqiu 356752238@qq.com
 *
 */
public abstract class BaseAnimAdapter implements IAnimAdapter {
	private static final int DEFUALT_TRAN_DUR_ANIM = 300;
	private MainUpView mMainUpView;
	
	@Override
	public void onInitAdapter(MainUpView view) {
	}
	
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
		Drawable drawableShadow = getMainUpView().getShadowDrawable();
		if (drawableShadow != null) {
			Rect shadowPaddingRect = getMainUpView().getDrawShadowRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			drawableShadow.getPadding(padding);
			drawableShadow.setBounds(-padding.left + (shadowPaddingRect.left), -padding.top + (shadowPaddingRect.top),
					width + padding.right - (shadowPaddingRect.right),
					height + padding.bottom - (shadowPaddingRect.bottom));
			drawableShadow.draw(canvas);
		}
	}
	
	/**
	 * 绘制最上层的移动边框.
	 */
	public void onDrawUpRect(Canvas canvas) {
		Drawable drawableUp = getMainUpView().getUpRectDrawable();
		if (drawableUp != null) {
			Rect paddingRect = getMainUpView().getDrawUpRect();
			int width = getMainUpView().getWidth();
			int height = getMainUpView().getHeight();
			Rect padding = new Rect();
			// 边框的绘制.
			drawableUp.getPadding(padding);
			drawableUp.setBounds(-padding.left + (paddingRect.left), -padding.top + (paddingRect.top),
					width + padding.right - (paddingRect.right), height + padding.bottom - (paddingRect.bottom));
			drawableUp.draw(canvas);
		}
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
	
	public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
		Rect fromRect = findLocationWithView(getMainUpView());
		Rect toRect = findLocationWithView(toView);
		float x = toRect.left - fromRect.left;
		float y = toRect.top - fromRect.top;
		flyWhiteBorder(toView, x, y, scaleX, scaleY);
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

}
