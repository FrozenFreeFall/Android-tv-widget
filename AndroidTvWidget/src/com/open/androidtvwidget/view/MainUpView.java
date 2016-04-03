package com.open.androidtvwidget.view;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.adapter.BaseEffectBridge;
import com.open.androidtvwidget.adapter.OpenEffectBridge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MainUpView extends FrameLayout {

	private static final String TAG = "MainUpView";

	private BaseEffectBridge mEffectBridge;
	private static final float DEFUALT_SCALE = 1.0f;
	private static final int DEFULAT_SIZE = 500;

	public MainUpView(Context context) {
		super(context, null, 0);
		init(context, null);
	}

	public MainUpView(Context context, View view) {
		super(context, null, 0);
		// 如果是单独添加，就将view加进来.
		if (view != null) {
			ViewGroup viewGroup = (ViewGroup) view.getRootView();
			if (viewGroup != null && this.getParent() != viewGroup) {
				LayoutParams layParams = new LayoutParams(DEFULAT_SIZE, DEFULAT_SIZE);
				viewGroup.addView(this, layParams);
			}
		}
		//
		init(context, null);
	}

	public MainUpView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init(context, attrs);
	}

	public MainUpView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setWillNotDraw(false);
		initEffectBridge();
		// 初始化.
		if (attrs != null) {
			TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.mainUpView);// 获取配置属性
			Drawable drawableUpRect = tArray.getDrawable(R.styleable.mainUpView_upImageRes); // 顶层图片.
			Drawable drawableShadow = tArray.getDrawable(R.styleable.mainUpView_shadowImageRes); // 阴影图片.
			setUpRectDrawable(drawableUpRect);
			setShadowDrawable(drawableShadow);
			tArray.recycle();
		}
	}

	private void initEffectBridge() {
		BaseEffectBridge baseEffectBridge = new OpenEffectBridge();
		baseEffectBridge.onInitBridge(this);
		baseEffectBridge.setMainUpView(this);
		setEffectBridge(baseEffectBridge);
	}

	public void setUpRectResource(int resId) {
		if (mEffectBridge != null)
			mEffectBridge.setUpRectResource(resId);
	}

	/**
	 * 设置最上层的边框.
	 */
	public void setUpRectDrawable(Drawable upRectDrawable) {
		if (mEffectBridge != null)
			mEffectBridge.setUpRectDrawable(upRectDrawable);
	}

	public Drawable getUpRectDrawable() {
		if (mEffectBridge != null) {
			return mEffectBridge.getUpRectDrawable();
		}
		return null;
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
	public void setDrawUpRectPadding(Rect rect) {
		if (mEffectBridge != null) {
			mEffectBridge.setDrawUpRectPadding(rect);
			invalidate();
		}
	}

	public Rect getDrawUpRect() {
		if (mEffectBridge != null) {
			return mEffectBridge.getDrawUpRect();
		}
		return null;
	}

	public void setShadowResource(int resId) {
		if (mEffectBridge != null) {
			this.mEffectBridge.setShadowResource(resId);
			invalidate();
		}
	}

	/**
	 * 当图片边框不自带阴影的话，可以自行设置阴影图片. 设置阴影.
	 */
	public void setShadowDrawable(Drawable shadowDrawable) {
		if (mEffectBridge != null) {
			this.mEffectBridge.setShadowDrawable(shadowDrawable);
			invalidate();
		}
	}

	public Drawable getShadowDrawable() {
		if (mEffectBridge != null) {
			return this.mEffectBridge.getShadowDrawable();
		}
		return null;
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
	public void setDrawShadowRectPadding(Rect rect) {
		if (mEffectBridge != null) {
			mEffectBridge.setDrawShadowRectPadding(rect);
			invalidate();
		}
	}

	public Rect getDrawShadowRect() {
		if (mEffectBridge != null) {
			return mEffectBridge.getDrawShadowRect();
		}
		return null;
	}

	/**
	 * 焦点控件处理.
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
		if (this.mEffectBridge != null)
			this.mEffectBridge.onFocusView(view, scaleX, scaleY);
	}

	/**
	 * 设置无焦点子控件还原.
	 */
	public void setUnFocusView(View view) {
		setUnFocusView(view, DEFUALT_SCALE, DEFUALT_SCALE);
	}

	public void setUnFocusView(View view, float scaleX, float scaleY) {
		if (this.mEffectBridge != null)
			this.mEffectBridge.onOldFocusView(view, scaleX, scaleY);
	}

	/**
	 * 设置EffectBridge.
	 */
	public void setEffectBridge(BaseEffectBridge effectBridge) {
		this.mEffectBridge = effectBridge;
		if (this.mEffectBridge != null) {
			this.mEffectBridge.onInitBridge(this);
			this.mEffectBridge.setMainUpView(this);
			invalidate();
		}
	}

	public BaseEffectBridge getEffectBridge() {
		return this.mEffectBridge;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (this.mEffectBridge != null) {
			if (this.mEffectBridge.onDrawMainUpView(canvas)) {
				return;
			}
		}
		super.onDraw(canvas);
	}

}
