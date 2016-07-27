/*
Copyright 2016 The Open Source Project

Author: hailongqiu <356752238@qq.com>
Maintainer: hailongqiu <356752238@qq.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.open.androidtvwidget.view;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.bridge.BaseEffectBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * MainUpView是一个在最上层的移动边框，你可以使用它的API来完成你想要的效果.
 * <p>
 * <ul>
 * <p>
 * 边框图片相关API:
 * <li>{@link #setUpRectResource}
 * <li>{@link #setUpRectDrawable}
 * <li>{@link #getUpRectDrawable}
 * <li>{@link #setDrawUpRectPadding(int)} 用于调整边框图片边距.
 * <li>{@link #setDrawUpRectPadding(Rect)} 用于调整边框图片边距.
 * <p>
 * 阴影图片相关API:
 * <li>{@link #setShadowResource}
 * <li>{@link #setShadowDrawable}
 * <li>{@link #getShadowDrawable}
 * <li>{@link #setDrawShadowPadding(int)} 用于调整阴影图片边距.
 * <li>{@link #setDrawShadowPadding(RectF)} 用于调整阴影图片边距.
 * <p>
 * Effcet相关API:
 * <li>{@link #setEffectBridge} 你可以设置自己的放大，边框移动的动画效果.
 * <li>{@link #getEffectBridge}
 * <p>
 * 焦点View相关API:
 * <li>{@link #setFocusView(View, float, float)}
 * <li>{@link #setFocusView(View, float)}
 * <li>{@link #setUnFocusView}
 * </ul>
 * <p>
 *
 * <pre class="prettyprint">
 * public class CalendarActivity extends Activity {
 *     ... ...
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         // 第一种调用方式(XML布局中加载，注意，必须将控件放在最上面的一层，参考DEMO的XML布局).
 *			mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
 *         // 第二种调用方式(不放在XML布局中).
 *         mainUpView1 = new MainUpView(this);
 *     }
 *		... ...
 * </pre>
 *
 * <p>
 * XML自定义属性:
 * <p>
 * xmlns:app="http://schemas.android.com/apk/res-auto"(XML布局加入这句)
 * <p>
 * <ul>
 * <li>attr ref #MainUpView_upImageRes : app:MainUpView_upImageRes="@drawble..."
 * <li>attr ref #MainUpView_shadowImageRes
 * </ul>
 */
public class MainUpView extends FrameLayout {

	private static final String TAG = "MainUpView";
	private static final float DEFUALT_SCALE = 1.0f;

	private BaseEffectBridge mEffectBridge;

	public MainUpView(Context context) {
		super(context, null, 0);
		init(context, null);
	}

	/**
	 * 手动添加，不在XML添加的话.
	 */
	public void attach2Window(Activity activity) {
		ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rootView.addView(this, layoutParams);
		rootView.setClipChildren(false);
		rootView.setClipToPadding(false);
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
			TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.MainUpView);// 获取配置属性
			Drawable drawableUpRect = tArray.getDrawable(R.styleable.MainUpView_upImageRes); // 顶层图片.
			Drawable drawableShadow = tArray.getDrawable(R.styleable.MainUpView_shadowImageRes); // 阴影图片.
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

	/**
	 * 设置最上层的图片资源ID.
	 */
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

	/**
	 * 获取最上层图片.
	 */
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

	public void setDrawUpRectPadding(RectF rect) {
		if (mEffectBridge != null) {
			mEffectBridge.setDrawUpRectPadding(rect);
			invalidate();
		}
	}

	/**
	 * 获取最上层图片 间距矩形(Rect).
	 */
	public Rect getDrawUpRect() {
		if (mEffectBridge != null) {
			RectF rectf = mEffectBridge.getDrawUpRect();
			int left = (int)Math.rint(rectf.left);
			int right = (int)Math.rint(rectf.right);
			int bottom = (int)Math.rint(rectf.bottom);
			int top = (int)Math.rint(rectf.top);
			return new Rect(left, top, right, bottom);
		}
		return null;
	}

	public RectF getDrawUpRectF() {
		if (mEffectBridge != null) {
			return mEffectBridge.getDrawUpRect();
		}
		return null;
	}

	/**
	 * 设置阴影图片资源ID.
	 */
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

	/**
	 * 获取阴影图片.
	 */
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

	/**
	 * 获取阴影图片边距.
	 */
	public Rect getDrawShadowRect() {
		if (mEffectBridge != null) {
			RectF rectf = mEffectBridge.getDrawShadowRect();
			int left = (int)Math.rint(rectf.left);
			int right = (int)Math.rint(rectf.right);
			int bottom = (int)Math.rint(rectf.bottom);
			int top = (int)Math.rint(rectf.top);
			return new Rect(left, top, right, bottom);
		}
		return null;
	}

	/**
	 * 获取阴影图片边距.
	 */
	public RectF getDrawShadowRectF() {
		if (mEffectBridge != null) {
			return mEffectBridge.getDrawShadowRect();
		}
		return null;
	}

	/**
	 * 移动到 view的位置，并且放大 view.
	 *
	 * @param newView
	 *            新焦点View
	 * @param oldView
	 *            老焦点View
	 * @param scale
	 *            放大的比例, 可以设置为1.2f, 1.1f或者其它
	 */
	public void setFocusView(View newView, View oldView, float scale) {
		setFocusView(newView, scale);
		setUnFocusView(oldView);
	}

	/**
	 * 移动到 view的位置，并且放大 view.
	 *
	 * @param view
	 * @param scale
	 *            放大比例
	 */
	public void setFocusView(View view, float scale) {
		setFocusView(view, scale, scale);
	}

	/**
	 * 移动到 view的位置，并且放大 view.
	 * @param view
	 * @param scaleX
	 *            X放大比例
	 * @param scaleY
	 *            Y放大比例
	 */
	public void setFocusView(View view, float scaleX, float scaleY) {
		if (this.mEffectBridge != null)
			this.mEffectBridge.onFocusView(view, scaleX, scaleY);
	}

	/**
	 * 设置无焦点子控件还原.(默认为1.0F)
	 *
	 * @param view
	 *            老焦点View.
	 */
	public void setUnFocusView(View view) {
		setUnFocusView(view, DEFUALT_SCALE, DEFUALT_SCALE);
	}

	/**
	 * 老焦点VIEW处理.
	 *
	 * @param view
	 * @param scaleX
	 *            X缩放比例
	 * @param scaleY
	 *            Y缩放比例
	 */
	public void setUnFocusView(View view, float scaleX, float scaleY) {
		if (this.mEffectBridge != null)
			this.mEffectBridge.onOldFocusView(view, scaleX, scaleY);
	}

	/**
	 * 设置EffectBridge.
	 * BaseEffectBridge 处理了边框的移动，边框的绘制.
	 * 具体查看 BaseEffectBridge 类.
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
