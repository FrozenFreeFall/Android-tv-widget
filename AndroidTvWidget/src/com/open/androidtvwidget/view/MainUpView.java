package com.open.androidtvwidget.view;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.DensityUtil;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;

public class MainUpView extends View {

	private static final String TAG = "MainUpView";
	private Drawable mDrawableShadow;
	private Drawable mDrawableUpRect;
	private View mFocusView;
	private Context mContext;

	private boolean isTvScreen = false;
	private boolean isDrawUpRect = true;

	public MainUpView(Context context) {
		super(context, null, 0);
		init(context);
	}

	public MainUpView(Context context, View view) {
		super(context, null, 0);
		// 如果是单独添加，就将view加进来.
		if (view != null) {
			ViewGroup viewGroup = (ViewGroup) view.getRootView();
			if (viewGroup != null && this.getParent() != viewGroup) {
				LayoutParams layParams = new LayoutParams(500, 500);
				viewGroup.addView(this, layParams);
			}
		}
		//
		init(context);
	}

	public MainUpView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init(context);
	}

	public MainUpView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		setVisibility(View.GONE);
		mContext = context;
		try {
			mDrawableUpRect = mContext.getResources().getDrawable(R.drawable.white_light_10); // 移动的边框.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isTvScreenEnabled() {
		return isTvScreen;
	}

	public void setTvScreenEnabled(boolean isTvScreen) {
		this.isTvScreen = isTvScreen;
		invalidate();
	}

	/**
	 * 设置是否移动边框在最下层. true : 移动边框在最上层. 反之否.
	 */
	public void setDrawUpRectEnabled(boolean isDrawUpRect) {
		this.isDrawUpRect = isDrawUpRect;
		invalidate();
	}

	public void setUpRectResource(int resId) {
		try {
			this.mDrawableUpRect = mContext.getResources().getDrawable(resId); // 移动的边框.
			invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置最上层的边框.
	 */
	public void setUpRectDrawable(Drawable upRectDrawable) {
		this.mDrawableUpRect = upRectDrawable;
		invalidate();
	}

	public void setShadowResource(int resId) {
		try {
			this.mDrawableShadow = mContext.getResources().getDrawable(resId); // 移动的边框.
			invalidate();
		} catch (Exception e) {
			this.mDrawableShadow = null;
			e.printStackTrace();
		}
	}

	/**
	 * 当图片边框不自带阴影的话，可以自行设置阴影图片. 设置阴影.
	 */
	public void setShadowDrawable(Drawable shadowDrawable) {
		this.mDrawableShadow = shadowDrawable;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		onDrawMainUpView(canvas);
	}

	private void onDrawMainUpView(Canvas canvas) {
		canvas.save();
		// 绘制阴影.
		onDrawShadow(canvas);
		// onTestDrawRect(canvas);
		// 绘制最上层的边框.
		if (!isDrawUpRect) {
			onDrawUpRect(canvas);
		}
		// 绘制焦点子控件.
		if (mFocusView != null && !isDrawUpRect) {
			onDrawFocusView(canvas);
		}
		// 绘制最上层的边框.
		if (isDrawUpRect) {
			onDrawUpRect(canvas);
		}
		canvas.restore();
	}

	private void onDrawFocusView(Canvas canvas) {
		View view = mFocusView;
		canvas.save();
		if (mFocusView instanceof ReflectItemView) {
			ReflectItemView reflectItemView = (ReflectItemView) mFocusView;
			View tempView = reflectItemView.getChildAt(0);
			if (tempView != null) {
				view = tempView;
			}
		}
		float scaleX = (float) (this.getWidth()) / (float) view.getWidth();
		float scaleY = (float) (this.getHeight()) / (float) view.getHeight();
		canvas.scale(scaleX, scaleY);
		view.draw(canvas);
		canvas.restore();
	}

	private Rect mPaddingRect = new Rect(0, 0, 0, 0);
	private Rect mShadowPaddingRect = new Rect(0, 0, 0, 0);

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
	public void setDrawShadowRectPadding(Rect rect) {
		mShadowPaddingRect.set(rect);
		invalidate();
	}
	
	/**
	 * 绘制外部阴影.
	 */
	private void onDrawShadow(Canvas canvas) {
		if (mDrawableShadow != null) {
			int width = getWidth();
			int height = getHeight();
			Rect padding = new Rect();
			mDrawableShadow.getPadding(padding);
			// mDrawableShadow.setBounds(-padding.left, -padding.top, width +
			// padding.right, padding.bottom + height);
			mDrawableShadow.setBounds(-padding.left + (mShadowPaddingRect.left),
					-padding.top + (mShadowPaddingRect.top), width + padding.right - (mShadowPaddingRect.right),
					height + padding.bottom - (mShadowPaddingRect.bottom));
			mDrawableShadow.draw(canvas);
		}
	}

	/**
	 * 根据图片边框 自行 填写 相差的边距. <br>
	 * 比如 res/drawble/white_light_10.9.png的图片，边距就差很多.
	 */
	public void setDrawUpRectPadding(int size) {
		setDrawUpRectPadding(new Rect(size, size, size, size));
	}

	/**
	 * 根据图片边框 自行 填写 相差的边距. <br>
	 * 比如 res/drawble/white_light_10.9.png的图片，边距就差很多.
	 */
	public void setDrawUpRectPadding(Rect rect) {
		mPaddingRect.set(rect);
		invalidate();
	}

	/**
	 * 绘制最上层的移动边框.
	 */
	private void onDrawUpRect(Canvas canvas) {
		if (mDrawableUpRect != null) {
			int width = getWidth();
			int height = getHeight();
			Rect padding = new Rect();
			// 边框的绘制.
			mDrawableUpRect.getPadding(padding);
			mDrawableUpRect.setBounds(-padding.left + (mPaddingRect.left), -padding.top + (mPaddingRect.top),
					width + padding.right - (mPaddingRect.right), height + padding.bottom - (mPaddingRect.bottom));
			mDrawableUpRect.draw(canvas);
		}
	}

	/**
	 * 设置焦点子控件的移动和放大.
	 */
	public void setFocusView(View view, float scale) {
		if (view != null && mFocusView != view) {
			mScale = scale;
			mFocusView = view;
			mNewFocus = view;
			mFocusView.animate().scaleX(scale).scaleY(scale).setDuration(TRAN_DUR_ANIM).setListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
				}
				@Override
				public void onAnimationRepeat(Animator animation) {
				}
				@Override
				public void onAnimationEnd(Animator animation) {
					setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationCancel(Animator animation) {
				}
			}).start();
			runTranslateAnimation(mFocusView, scale, scale);
		}
	}

	public void setFocusView(View newView, View oldView, float scale) {
		setFocusView(newView, scale);
		setUnFocusView(oldView);
	}

	/**
	 * 设置无焦点子控件还原.
	 */
	public void setUnFocusView(View view) {
		if (view != null) {
			view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(TRAN_DUR_ANIM).start();
		}
	}

	private static int TRAN_DUR_ANIM = 300;

	/**
	 * 控件动画时间.
	 */
	public void setTranDurAnimTime(int time) {
		MainUpView.TRAN_DUR_ANIM = time;
		invalidate();
	}

	/**
	 */
	public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
		Rect fromRect = findLocationWithView(this);
		Rect toRect = findLocationWithView(toView);

		float x = toRect.left - fromRect.left;
		float y = toRect.top - fromRect.top;

		/**
		 * 有一些分辨率和TV的不一样.
		 */
		if (isTvScreen) {
			x = DensityUtil.dip2px(this.getContext(), x);
			y = DensityUtil.dip2px(this.getContext(), y);
		} 
		
		flyWhiteBorder(x, y);
	}

	private View mNewFocus;
	private float mScale = 1.0f;
	private int mNewWidth;
	private int mNewHeight;
	private int mOldWidth;
	private int mOldHeight;

	/**
	 * */
	private void flyWhiteBorder(float x, float y) {
		if (mNewFocus != null) {
			mNewWidth = (int) ((float) mNewFocus.getMeasuredWidth() * mScale);
			mNewHeight = (int) ((float) mNewFocus.getMeasuredHeight() * mScale);
			x = x + (mNewFocus.getMeasuredWidth() - mNewWidth) / 2;
			y = y + (mNewFocus.getMeasuredHeight() - mNewHeight) / 2;
		}

		mOldWidth = this.getMeasuredWidth();
		mOldHeight = this.getMeasuredHeight();

		ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(this, "translationX", x);
		ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(this, "translationY", y);
		// BUG，因为缩放会造成图片失真(拉伸).
		// hailong.qiu 2016.02.26 修复 :)
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(this), "width", mOldWidth, mNewWidth);
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(this), "height", mOldHeight, mNewHeight);

		AnimatorSet mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
		mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
		mAnimatorSet.setDuration(TRAN_DUR_ANIM);
		mAnimatorSet.start();
	}

	/*
	 * BUG 2016.02.26
	 * 因为以前是顶层移动边框不改变宽高，
	 * 原有是放大，会导致图片严重的失真变形，
	 * 无法适应现有开发中去，所以才去改成改变宽高.
	 */
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

	public Rect findLocationWithView(View view) {
		ViewGroup root = (ViewGroup) this.getParent();
		Rect rect = new Rect();
		root.offsetDescendantRectToMyCoords(view, rect);
		return rect;
	}

}
