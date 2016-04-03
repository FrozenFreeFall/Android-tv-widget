package com.open.androidtvwidget.adapter;

import com.open.androidtvwidget.view.MainUpView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * 自定义Anim Bridge DEMO. <br>
 * 如果你想实现自己不同风格的东西， <br>
 * 继承 BaseAnimBridge 重写几个函数吧. <br>
 * 后续将推出更多风格的 Anim Bridge. <br>
 * 
 * @author hailongqiu 356752238@qq.com
 *
 */
public class OpenEffectBridge extends BaseEffectBridgeWrapper {

	private static final int DEFUALT_TRAN_DUR_ANIM = 300;
	private int mTranDurAnimTime = DEFUALT_TRAN_DUR_ANIM;
	private AnimatorSet mCurrentAnimatorSet;
	private boolean isInDraw = false;
	private boolean mIsHide = false;
	private boolean mAnimEnabled = true;
	private boolean isDrawUpRect = true;
	private View mFocusView;
	private NewAnimatorListener mNewAnimatorListener;

	public OpenEffectBridge() {
	}

	@Override
	public void onInitBridge(MainUpView view) {
		super.onInitBridge(view);
		/**
		 * 防止边框第一次出现,<br>
		 * 从另一个地方飘过来的问题.<br>
		 */
		view.setVisibility(View.INVISIBLE);
	}

	/**
	 * 设置是否移动边框在最下层(绘制的层次). <br>
	 * true : 移动边框在最上层. 反之否. <br>
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
		getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
	}

	public interface NewAnimatorListener {
		public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation);
		public void onAnimationEnd(OpenEffectBridge bridge, View view, Animator animation);
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
			focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start();
			runTranslateAnimation(focusView, scaleX, scaleY);
		}
	}

	/**
	 * 重寫移動的邊框函數.
	 */
	@Override
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
				if (!isDrawUpRect)
					isInDraw = false;
				if (mIsHide) {
					getMainUpView().setVisibility(View.INVISIBLE);
				}
				if (mNewAnimatorListener != null)
					mNewAnimatorListener.onAnimationStart(OpenEffectBridge.this, focusView, animation);
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
				getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
				if (mNewAnimatorListener != null)
					mNewAnimatorListener.onAnimationEnd(OpenEffectBridge.this, focusView, animation);
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

	/**
	 * 重寫了繪製的函數.
	 */
	@Override
	public boolean onDrawMainUpView(Canvas canvas) {
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
		return true;
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

}
