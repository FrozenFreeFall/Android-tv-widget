package com.open.androidtvwidget.bridge;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.open.androidtvwidget.utils.Utils;

/**
 * 为了兼容4.3以下版本的 AnimBridge. <br>
 * 使用方法： MainUpView.setAnimBridge(new AnimNoDrawBridge()); <br>
 * 如果边框带了阴影效果，使用这个函数自行调整: MainUpView.setDrawUpRectPadding(-12);
 * 
 * @author hailongqiu
 *
 */
public class EffectNoDrawBridge extends OpenEffectBridge {
	protected AnimatorSet mCurrentAnimatorSet;

	@Override
	public void clearAnimator() {
		if (mCurrentAnimatorSet != null)
			mCurrentAnimatorSet.end();
	}

	/**
	 * 设置背景，边框不使用绘制.
	 */
	@Override
	public void setUpRectResource(int resId) {
		getMainUpView().setBackgroundResource(resId);
	}

	@Override
	public void setUpRectDrawable(Drawable upRectDrawable) {
		getMainUpView().setBackgroundDrawable(upRectDrawable);
	}

	@Override
	public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
		if (!isAnimEnabled())
			return;
		if (oldFocusView != null) {
			oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(getTranDurAnimTime()).start();
		}
	}

	@Override
	public void onFocusView(View focusView, float scaleX, float scaleY) {
		if (!isAnimEnabled())
			return;
		if (focusView != null) {
			/**
			 * 我这里重写了onFocusView. <br>
			 * 并且交换了位置. <br>
			 * 你可以写自己的动画效果. <br>
			 */
			runTranslateAnimation(focusView, scaleX, scaleY);
			focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(getTranDurAnimTime()).start();
		}
	}

	/**
	 * 重写边框移动函数.
	 */
	@Override
	public void flyWhiteBorder(final View focusView, View moveView, float scaleX, float scaleY) {
		RectF paddingRect = getDrawUpRect();
		int newWidth = 0;
		int newHeight = 0;
		int oldWidth = 0;
		int oldHeight = 0;
		
		int newX = 0;
		int newY = 0;
		
		if (focusView != null) {
			newWidth = (int) (focusView.getMeasuredWidth() * scaleX);
			newHeight = (int) (focusView.getMeasuredHeight() * scaleY);
			oldWidth = moveView.getMeasuredWidth();
			oldHeight = moveView.getMeasuredHeight();
			Rect fromRect = findLocationWithView(moveView);
			Rect toRect = findLocationWithView(focusView);
			int x = toRect.left - fromRect.left - ((int)Math.rint(paddingRect.left));
			int y = toRect.top - fromRect.top - ((int)Math.rint(paddingRect.top));
			newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
			newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
			//
			newWidth += ((int)Math.rint(paddingRect.right) + (int)Math.rint(paddingRect.left));
			newHeight += ((int)Math.rint(paddingRect.bottom) + (int)Math.rint(paddingRect.top));
		}

		// 取消之前的动画.
		if (mCurrentAnimatorSet != null)
			mCurrentAnimatorSet.cancel();

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
		mAnimatorSet.setDuration(getTranDurAnimTime());
		mAnimatorSet.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (isVisibleWidget()) {
					getMainUpView().setVisibility(View.GONE);
				}
				if (getNewAnimatorListener() != null)
					getNewAnimatorListener().onAnimationStart(EffectNoDrawBridge.this, focusView, animation);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				getMainUpView().setVisibility(isVisibleWidget() ? View.GONE : View.VISIBLE);
				if (getNewAnimatorListener() != null)
					getNewAnimatorListener().onAnimationEnd(EffectNoDrawBridge.this, focusView, animation);

				// XF add（先锋TV开发(404780246)修复)
				// BUG:5.0系统边框错位.
				if (Utils.getSDKVersion() >= 21) {
//					int newWidth = (int) (focusView.getMeasuredWidth() *
//							mScaleX);
//					int newHeight = (int) (focusView.getMeasuredHeight() *
//							mScaleY);
//					getMainUpView().getLayoutParams().width = newWidth;
//					getMainUpView().getLayoutParams().height = newHeight;
//					getMainUpView().requestLayout();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		mAnimatorSet.start();
		mCurrentAnimatorSet = mAnimatorSet;
	}

	/**
	 * 重写该函数，<br>
	 * 不进行绘制 边框和阴影.
	 */
	@Override
	public boolean onDrawMainUpView(Canvas canvas) {
		return false;
	}

}
