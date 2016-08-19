package com.open.androidtvwidget.bridge;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.open.androidtvwidget.utils.OPENLOG;

public class RecyclerViewBridge extends EffectNoDrawBridge {
	
	private AnimatorSet mCurrentAnimatorSet;
	
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

			// 处理 RecyclerView TV 上 移动边框跑偏的问题.
			if (null != focusView.getParent() && focusView.getParent() instanceof RecyclerView) {
				final RecyclerView rv = (RecyclerView) focusView.getParent();
				final int offset = rv.getBaseline();
				if (offset != -1) {
					toRect.offset(rv.getLayoutManager().canScrollHorizontally() ? -offset : 0,
							rv.getLayoutManager().canScrollVertically() ? -offset : 0);
				}
			}
			//
			int x = toRect.left - fromRect.left - ((int)Math.rint(paddingRect.left));
			int y = toRect.top - fromRect.top - ((int)Math.rint(paddingRect.top));
			newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
			newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
			//
			newWidth += ((int)Math.rint(paddingRect.right) + (int)Math.rint(paddingRect.left));
			newHeight += ((int)Math.rint(paddingRect.bottom) + (int)Math.rint(paddingRect.top));
		}

		// 取消之前的动画.
		if (mCurrentAnimatorSet != null) {
			mCurrentAnimatorSet.cancel();
			mCurrentAnimatorSet = null;
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
		mAnimatorSet.setDuration(getTranDurAnimTime());
		mAnimatorSet.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
				if (isVisibleWidget()) {
					getMainUpView().setVisibility(View.GONE);
				}
				if (getNewAnimatorListener() != null)
					getNewAnimatorListener().onAnimationStart(RecyclerViewBridge.this, focusView, animation);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				getMainUpView().setVisibility(isVisibleWidget() ? View.GONE : View.VISIBLE);
				if (getNewAnimatorListener() != null)
					getNewAnimatorListener().onAnimationEnd(RecyclerViewBridge.this, focusView, animation);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		mAnimatorSet.start();
		mCurrentAnimatorSet = mAnimatorSet;
	}
}
