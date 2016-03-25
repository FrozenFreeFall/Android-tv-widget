package com.open.androidtvwidget.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class OpenBaseAnimAdapter extends BaseAnimAdapter {

	private static final int DEFUALT_TRAN_DUR_ANIM = 300;
	private int mTranDurAnimTime = DEFUALT_TRAN_DUR_ANIM;
	private AnimatorSet mCurrentAnimatorSet;
	private boolean isInDraw = false;
	private MainUpView mMainUpView;
	private boolean mIsHide = false;
	private boolean mAnimEnabled = true;
	private boolean isDrawUpRect = true;
	private View mFocusView;
	private int mNewWidth;
	private int mNewHeight;
	private int mOldWidth;
	private int mOldHeight;
	private NewAnimatorListener mNewAnimatorListener;

	/**
	 * 设置是否移动边框在最下层. true : 移动边框在最上层. 反之否.
	 */
	public void setDrawUpRectEnabled(boolean isDrawUpRect) {
		this.isDrawUpRect = isDrawUpRect;
		getMainUpView().invalidate();
	}

	/**
	 * 控件动画时间.
	 */
	public void setTranDurAnimTime(int time) {
		mTranDurAnimTime = time;
		getMainUpView().invalidate();
	}

	/**
	 * 让动画失效.
	 */
	public void setAnimEnabled(boolean animEnabled) {
		this.mAnimEnabled = animEnabled;
	}

	/**
	 * 隐藏移动的边框.
	 */
	public void setVisibleWidget(boolean isHide) {
		this.mIsHide = isHide;
	}

	public interface NewAnimatorListener {
		public void onAnimationStart(View view, Animator animation);

		public void onAnimationEnd(View view, Animator animation);
	}

	/**
	 * 监听动画的回调.
	 */
	public void setOnAnimatorListener(NewAnimatorListener newAnimatorListener) {
		mNewAnimatorListener = newAnimatorListener;
	}

	@Override
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
		if (!mAnimEnabled)
			return;
		if (oldFocusView != null) {
			oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start();
		}
	}

	@Override
	public void onFocusView(View focusView, float scaleX, float scaleY) {
		mFocusView = focusView;
		if (!mAnimEnabled)
			return;
		if (focusView != null) {
			if (!mIsHide) {
				focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start();
			}
			runTranslateAnimation(focusView, scaleX, scaleY);
		}
	}

	public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
		Rect fromRect = findLocationWithView(mMainUpView);
		Rect toRect = findLocationWithView(toView);
		float x = toRect.left - fromRect.left;
		float y = toRect.top - fromRect.top;
		flyWhiteBorder(toView, x, y, scaleX, scaleY);
	}

	public Rect findLocationWithView(View view) {
		ViewGroup root = (ViewGroup) mMainUpView.getParent();
		Rect rect = new Rect();
		root.offsetDescendantRectToMyCoords(view, rect);
		return rect;
	}

	public void flyWhiteBorder(final View focusView, float x, float y, float scaleX, float scaleY) {
		if (focusView != null) {
			mNewWidth = (int) (focusView.getMeasuredWidth() * scaleX);
			mNewHeight = (int) (focusView.getMeasuredHeight() * scaleY);
			x = x + (focusView.getMeasuredWidth() - mNewWidth) / 2;
			y = y + (focusView.getMeasuredHeight() - mNewHeight) / 2;
		}

		// 取消之前的动画.
		if (mCurrentAnimatorSet != null)
			mCurrentAnimatorSet.cancel();

		mOldWidth = getMainUpView().getMeasuredWidth();
		mOldHeight = getMainUpView().getMeasuredHeight();

		ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(getMainUpView(), "translationX", x);
		ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(getMainUpView(), "translationY", y);
		// BUG，因为缩放会造成图片失真(拉伸).
		// hailong.qiu 2016.02.26 修复 :)
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(getMainUpView()), "width", mOldWidth,
				(int) mNewWidth);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(getMainUpView()), "height", mOldHeight,
				(int) mNewHeight);
		//
		AnimatorSet mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
		mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
		mAnimatorSet.setDuration(mTranDurAnimTime);
		mAnimatorSet.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
				if (mIsHide) {
					getMainUpView().setVisibility(View.GONE);
				}
				if (mNewAnimatorListener != null)
					mNewAnimatorListener.onAnimationStart(focusView, animation);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = true;
				getMainUpView().setVisibility(mIsHide ? View.GONE : View.VISIBLE);
				if (mNewAnimatorListener != null)
					mNewAnimatorListener.onAnimationEnd(focusView, animation);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				if (!isDrawUpRect)
					isInDraw = false;
			}
		});
		mAnimatorSet.start();
		mCurrentAnimatorSet = mAnimatorSet;
	}

	@Override
	public void setMainUpView(MainUpView view) {
		this.mMainUpView = view;
	}

	@Override
	public MainUpView getMainUpView() {
		return this.mMainUpView;
	}

	@Override
	public void onDrawMainUpView(Canvas canvas) {
		canvas.save();
		if (!isDrawUpRect) {
			// 绘制阴影.
			onDrawShadow(canvas);
			// 绘制最上层的边框.
			onDrawUpRect(canvas);
		}
		// 绘制焦点子控件.
		if (mFocusView != null && (!isDrawUpRect && isInDraw)) {
			onDrawFocusView(canvas);
		}
		//
		if (isDrawUpRect) {
			// 绘制阴影.
			onDrawShadow(canvas);
			// 绘制最上层的边框.
			onDrawUpRect(canvas);
		}
		canvas.restore();
	}

	public void onDrawFocusView(Canvas canvas) {
		View view = mFocusView;
		canvas.save();
		float scaleX = (float) (getMainUpView().getWidth()) / (float) view.getWidth();
		float scaleY = (float) (getMainUpView().getHeight()) / (float) view.getHeight();
		canvas.scale(scaleX, scaleY);
		view.draw(canvas);
		canvas.restore();
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

	private class ScaleView {
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
