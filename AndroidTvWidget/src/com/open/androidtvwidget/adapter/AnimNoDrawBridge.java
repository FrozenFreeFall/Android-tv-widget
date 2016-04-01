package com.open.androidtvwidget.adapter;

import com.open.androidtvwidget.view.MainUpView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 为了兼容4.3以下版本的 AnimAdapter.
 * 使用方法：
 * MainUpView.setAnimAdapter(new AnimNoDrawAdapter());
 * 如果边框带了阴影效果，使用这个函数自行调整:
 * MainUpView.setDrawUpRectPadding(12);
 * @author hailongqiu
 *
 */
public class AnimNoDrawBridge extends BaseAnimBridge {
	private static final int DEFUALT_TRAN_DUR_ANIM = 300;
	private int mTranDurAnimTime = DEFUALT_TRAN_DUR_ANIM;
	private AnimatorSet mCurrentAnimatorSet;
	private boolean mIsHide = false;
	private boolean mAnimEnabled = true;

	@Override
	public void onInitBridge(MainUpView view) {
		view.setVisibility(View.INVISIBLE); // 防止边框第一次出现问题.
		view.setBackgroundDrawable(view.getUpRectDrawable());
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
		if (!mAnimEnabled)
			return;
		if (focusView != null) {
			if (!mIsHide) {
				focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start();
			}
			runTranslateAnimation(focusView, scaleX, scaleY);
		}
	}

	/**
	 * 重寫移動的邊框函數.
	 */
	@Override
	public void flyWhiteBorder(final View focusView, float x, float y, float scaleX, float scaleY) {
		Rect paddingRect = getMainUpView().getDrawUpRect();
		int newWidth = 0;
		int newHeight = 0;
		int oldWidth = 0;
		int oldHeight = 0;
		if (focusView != null) {
			newWidth = (int) (focusView.getMeasuredWidth() * scaleX) + (paddingRect.left + paddingRect.right);
			newHeight = (int) (focusView.getMeasuredHeight() * scaleY) + (paddingRect.top + paddingRect.bottom);
			x = x + ((focusView.getMeasuredWidth() - newWidth) / 2); 
			y = y + ((focusView.getMeasuredHeight() - newHeight) / 2);
		}

		// 取消之前的动画.
		if (mCurrentAnimatorSet != null)
			mCurrentAnimatorSet.cancel();

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
		mAnimatorSet.setDuration(mTranDurAnimTime);
		mAnimatorSet.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (mIsHide) {
					getMainUpView().setVisibility(View.GONE);
				}
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				getMainUpView().setVisibility(mIsHide ? View.GONE : View.VISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		mAnimatorSet.start();
		mCurrentAnimatorSet = mAnimatorSet;
	}

	/**
	 * 重寫了繪製的函數.
	 */
	@Override
	public boolean onDrawMainUpView(Canvas canvas) {
		return false;
	}
}
