package com.open.androidtvwidget.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.DensityUtil;

public class MainUpView extends View {

	private static final String TAG = "MainUpView";
	private Drawable mDrawableShadow;
	private Drawable mDrawableUpRect;
	private View mFocusView;
	private Context mContext;

	private boolean isInDraw = true;
	private boolean isTvScreen = false;

	public MainUpView(Context context) {
		super(context, null, 0);
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
		mContext = context;
		try {
			mDrawableUpRect = mContext.getResources().getDrawable(
					R.drawable.item_highlight); // 边框.
			mDrawableShadow = mContext.getResources().getDrawable(
					R.drawable.item_shadow); // 阴影.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setInDraw(boolean isInDraw) {
		this.isInDraw = isInDraw;
		invalidate();
	}

	public boolean isTvScreen() {
		return isTvScreen;
	}

	public void setTvScreen(boolean isTvScreen) {
		this.isTvScreen = isTvScreen;
		invalidate();
	}

	/**
	 * 设置最上层的边框.
	 */
	public void setUpRect(Drawable upRectDrawable) {
		this.mDrawableUpRect = upRectDrawable;
		invalidate();
	}

	/**
	 * 设置阴影.
	 */
	public void setShadow(Drawable shadowDrawable) {
		this.mDrawableShadow = shadowDrawable;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		onDrawMainUpView(canvas);
	}

	private void onDrawMainUpView(Canvas canvas) {
		canvas.save();
		// 绘制倒影.
//		if (mFocusView != null && mFocusView instanceof ReflectItemView
//				&& isInDraw) {
//			Bitmap bmp = ((ReflectItemView) mFocusView).getReflectBitmap(); //
//			// 获取倒影bitmap.
//			if (bmp != null) {
//				canvas.save();
//				float scaleX = (float) (this.getWidth())
//						/ (float) mFocusView.getWidth();
//				float scaleY = (float) (this.getHeight())
//						/ (float) mFocusView.getHeight();
//				canvas.scale(scaleX, scaleY);
//				canvas.drawBitmap(bmp, 0, mFocusView.getHeight(), null);
//				canvas.restore();
//			}
//		}
		// 绘制阴影.
		if (isInDraw) {
			onDrawShadow(canvas);
			// onTestDrawRect(canvas);
		}
		// 绘制最上层的边框.
		onDrawUpRect(canvas);
		// 绘制焦点子控件.
		if (mFocusView != null && isInDraw) {
			View view = mFocusView;
			canvas.save();
			float scaleX = (float) (this.getWidth()) / (float) view.getWidth();
			float scaleY = (float) (this.getHeight())
					/ (float) view.getHeight();
			canvas.scale(scaleX, scaleY);
			view.draw(canvas);
			canvas.restore();
		}
		canvas.restore();
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
			mDrawableShadow.setBounds(-padding.left, -padding.top, width
					+ padding.right, padding.bottom + height);
			// mDrawableShadow.setAlpha((int)(255*(scale-1)*10));
			mDrawableShadow.draw(canvas);
		}
	}

	/**
	 * 绘制最上层的边框.
	 */
	private void onDrawUpRect(Canvas canvas) {
		if (mDrawableUpRect != null) {
			int width = getWidth();
			int height = getHeight();
			Rect padding = new Rect();
			// 边框的绘制.
			mDrawableUpRect.getPadding(padding);
			mDrawableUpRect.setBounds(-padding.left - 2, -padding.top - 2,
					width + padding.right + 2, height + padding.bottom + 2);
			// mDrawableWhite.setAlpha((int)(255*(scale-1)*10));
			mDrawableUpRect.draw(canvas);
		}
	}

	/**
	 * 测试.
	 */
	private void onTestDrawRect(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		canvas.drawRect(0, 0, 0 + getWidth(), 0 + getHeight(), paint);
	}

	/**
	 * 设置焦点子控件的移动和放大.
	 */
	public void setFocusView(View view, float scale) {
		if (mFocusView != view) {
			mFocusView = view;
			mFocusView.animate().scaleX(scale).scaleY(scale).start();
			runTranslateAnimation(mFocusView, scale, scale);
		}
	}

	/**
	 * 设置无焦点子控件还原.
	 */
	public void setUnFocusView(View view) {
		view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(TRAN_DUR_ANIM)
				.start();
	}

	private static int TRAN_DUR_ANIM = 500;

	/**
	 */
	public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
		Rect fromRect = findLocationWithView(this);
		Rect toRect = findLocationWithView(toView);

		int x = toRect.left - fromRect.left;
		int y = toRect.top - fromRect.top;

		int deltaX = (toView.getWidth() - this.getWidth()) / 2;
		int deltaY = (toView.getHeight() - this.getHeight()) / 2;
		// tv
		if (isTvScreen) {
			x = DensityUtil.dip2px(this.getContext(), x + deltaX);
			y = DensityUtil.dip2px(this.getContext(), y + deltaY);
		} else {
			x = x + deltaX;
			y = y + deltaY;
		}
		float toWidth = toView.getWidth() * scaleX;
		float toHeight = toView.getHeight() * scaleY;
		int width = (int) (toWidth);
		int height = (int) (toHeight);

		flyWhiteBorder(width, height, x, y);
	}

	/**
	 * */
	private void flyWhiteBorder(int width, int height, float x, float y) {
		int mWidth = this.getWidth();
		int mHeight = this.getHeight();

		float scaleX = (float) width / (float) mWidth;
		float scaleY = (float) height / (float) mHeight;

		animate().translationX(x).translationY(y).setDuration(TRAN_DUR_ANIM)
				.scaleX(scaleX).scaleY(scaleY)
				.setInterpolator(new DecelerateInterpolator())
				.setListener(flyListener).start();
	}

	public Rect findLocationWithView(View view) {
		ViewGroup root = (ViewGroup) this.getParent();
		Rect rect = new Rect();
		root.offsetDescendantRectToMyCoords(view, rect);
		return rect;
	}

	private Animator.AnimatorListener flyListener = new Animator.AnimatorListener() {

		@Override
		public void onAnimationCancel(Animator arg0) {
			setInDraw(true);
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
			setInDraw(true);
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
		}

		@Override
		public void onAnimationStart(Animator arg0) {
			setInDraw(false);
		}

	};

}
