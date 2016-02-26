package com.open.androidtvwidget.view;

import java.util.ArrayList;
import java.util.List;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.DensityUtil;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 通过对viewpager再加工而成,当前版本只适用多fragment布局
 * 
 * @author keYence
 * @维护 hailong.qiu 356752238@qq.com
 */
public class TvTabHost extends RelativeLayout {

	private final String TAG = "TvTabHost";
	/**
	 * 光标
	 */
	private View cursor;
	/**
	 * 光标资源
	 */
	private int cursorRes;
	/**
	 * 可否缩放
	 */
	private boolean scalable;
	/**
	 * 放大比率
	 */
	private float scale;
	/**
	 * 放大用时
	 */
	private int durationLarge = 100;
	/**
	 * 缩小用时
	 */
	private int durationSmall = 100;
	/**
	 * 翻页滚动时间
	 */
	private int durationScroll;
	/**
	 * 触发延迟
	 */
	private int delay = 110;
	/**
	 * 光标宽高 包括阴影
	 */
	private int cursorWidth, cursorHeight;
	/**
	 * 光标相对位置
	 */
	private int cursorMarginLeft, cursorMarginTop, cursorMarginRight, cursorMarginBottom;
	private int paddingLeft, paddingTop;
	public int curPage, initPage, savePage;
	/**
	 * 页宽 页高
	 */
	private int pageWidth, pageHeight;
	/**
	 * 顶部栏与页间隔高度
	 */
	private int dividerHeight;
	/**
	 * 标题宽高 间隔及文字大小
	 */
	private int titleWidth, titleHeight, titleSpace, textSize;
	/**
	 * 页面回收标记
	 */
	private boolean isRestore = false;
	/**
	 * 顶部栏获得焦点
	 */
	private boolean isTopFocused = false;
	private ScrollPageChangerListener scrollPageChangerListener;
	private OnTopBarFocusChange onTopBarFocusChange;
	private List<View> titleList;
	private int titleMarginLeft;
	private int textColorDefault, textColorSelected;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	public TvTabHost(Context context) {
		this(context, null);
	}

	public TvTabHost(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TvTabHost(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray custom = getContext().obtainStyledAttributes(attrs, R.styleable.TvTabHost);
		
		this.cursorRes = custom.getResourceId(R.styleable.TvTabHost_cursorRes, 0); // 光标资源.

		this.titleWidth = (int) custom.getDimension(R.styleable.TvTabHost_titleWidth, 10);
		this.titleHeight = (int) custom.getDimension(R.styleable.TvTabHost_titleHeight, 10);
		this.titleSpace = (int) custom.getDimension(R.styleable.TvTabHost_titleSpace, 10);

		this.textColorDefault = custom.getColor(R.styleable.TvTabHost_textColorDefault, Color.BLACK);
		this.textColorSelected = custom.getColor(R.styleable.TvTabHost_textColorSelected, Color.WHITE);
		this.textSize = (int) custom.getDimension(R.styleable.TvTabHost_textSize, 10);

		this.dividerHeight = (int) custom.getDimension(R.styleable.TvTabHost_dividerHeight, 10);

		this.cursorWidth = (int) custom.getDimension(R.styleable.TvTabHost_cursorWidth, 0); // 光标宽度.
		this.cursorHeight = (int) custom.getDimension(R.styleable.TvTabHost_cursorHeight, 0);
		this.cursorMarginTop = (int) custom.getDimension(R.styleable.TvTabHost_cursorMarginTop, 0);
		this.cursorMarginRight = (int) custom.getDimension(R.styleable.TvTabHost_cursorMarginRight, 0);
		this.cursorMarginRight = (int) custom.getDimension(R.styleable.TvTabHost_cursorMarginRight, 0);
		this.cursorMarginBottom = (int) custom.getDimension(R.styleable.TvTabHost_cursorMarginBottom, 0);

		this.scalable = custom.getBoolean(R.styleable.TvTabHost_scalable, true);
		this.scale = custom.getFloat(R.styleable.TvTabHost_scale, 1.1f);
		this.delay = custom.getInteger(R.styleable.TvTabHost_delay, 110);
		this.durationLarge = custom.getInteger(R.styleable.TvTabHost_durationLarge, 100);
		this.durationSmall = custom.getInteger(R.styleable.TvTabHost_durationSmall, 100);
		this.durationScroll = custom.getInteger(R.styleable.TvTabHost_durationScroll, 370);

		custom.recycle();

		paddingLeft = (int) (titleWidth * (scale - 1) / 2 + 3 + this.getPaddingLeft());
		paddingTop = (int) (titleHeight * (scale - 1) / 2 + 3 + this.getPaddingTop());

		init();
	}

	private void init() {
		titleList = new ArrayList<View>();
	}

	/**
	 */
	public void addTitle(String title) {
		// 标题栏.
		TextView tv = new TextView(getContext());
		tv.setFocusable(true);
		tv.setTextColor(textColorDefault);
		tv.setTextSize(DensityUtil.px2dip(getContext(), textSize));
		tv.setText(title);
		tv.setGravity(Gravity.CENTER);
		tv.setFocusableInTouchMode(true);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				TextView textView = (TextView) v;
				int index = (Integer) textView.getTag();
				setCurrentPage(index);
				v.requestFocus();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (v.isFocused()) {
							moveCover(v);
						}
					}
				}, delay);
			}
		});
		tv.setId(titleList.size() + 1);
		tv.setTag(titleList.size());
		getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
			@Override
			public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
				if (newFocus != null) {
					// 翻页
					int targetPage = Integer.parseInt(newFocus.getTag().toString());
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (newFocus.isFocused()) {
								moveCover(newFocus);
							}
						}
					}, delay);

					if (isTopFocused) {
						if (onTopBarFocusChange != null) {
							onTopBarFocusChange.onFocusChange(isTopFocused, targetPage);
						}
					}
					isTopFocused = true;
					flushTopBar(targetPage);
				}
				if (oldFocus != null) {
					returnCover(oldFocus);
				}
			}
		});
//		tv.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(final View item, boolean focus) {
//				if (focus) {
//					// 翻页
//					int targetPage = Integer.parseInt(item.getTag().toString());
//					new Handler().postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							if (item.isFocused()) {
//								moveCover(item);
//							}
//						}
//					}, delay);
//
//					if (isTopFocused) {
//						if (onTopBarFocusChange != null) {
//							onTopBarFocusChange.onFocusChange(isTopFocused, targetPage);
//						}
//					}
//					isTopFocused = true;
//					flushTopBar(targetPage);
//				} else {
//					returnCover(item);
//				}
//			}
//		});
		tv.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int id, KeyEvent key) {
				final int targetPage = Integer.parseInt(v.getTag().toString());
				if (key.getAction() == KeyEvent.ACTION_DOWN) {
					if (key.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
						isTopFocused = false;
					} else if (key.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
						if (targetPage == 0) {
							isTopFocused = false;
						}
					} else if (key.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						if (targetPage == (titleList.size() - 1)) {
							isTopFocused = false;
						}
					}
					if (onTopBarFocusChange != null) {
						onTopBarFocusChange.onFocusChange(isTopFocused, targetPage);
					}
				}
				return false;
			}
		});
		titleList.add(tv);
	}

	/**
	 * 加载完毕后调用
	 */
	public void buildLayout() {

		/**
		 * 修改frag添加方式使用adapter 然后 addView();
		 */

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();

		RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(params.width, params.height);
		newParams.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
		this.setLayoutParams(newParams);

		titleMarginLeft = (params.width - (titleList.size() * titleWidth + (titleList.size() - 1) * titleSpace)) / 2;
		// 加载标题
		for (int i = 0; i < titleList.size(); i++) {
			TextView title = (TextView) titleList.get(i);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(titleWidth, titleHeight);
			// 默认居中
			rlp.setMargins((titleSpace + titleWidth) * i + titleMarginLeft, 0, 0, 0);
			this.addView(title, rlp);
		}

		// 加载页
		if (isRestore) {
			this.curPage = savePage;
		}

	}

	private void flushTopBar(int position) {
		Log.d(TAG, "flushTopBar position:" + position);
		if (this.findFocus() != null) {
			titleList.get(position).requestFocus();
		}

		for (int i = 0; i < titleList.size(); i++) {
			// 变色
			TextView title = (TextView) titleList.get(i);
			if (i == position) {
				title.setTextColor(textColorSelected);
			} else {
				title.setTextColor(textColorDefault);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
		 */
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

		// 计算出所有的childView的宽和高
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		/**
		 * 记录如果是wrap_content是设置的宽和高
		 */
		int width = 0;
		int height = 0;

		int cCount = getChildCount();

		int cWidth = 0;
		int cHeight = 0;
		/**
		 * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
		 */

		for (int i = 0; i < cCount; i++) {

			View childView = getChildAt(i);

			if (childView instanceof ViewGroup) {
				width = sizeWidth;
				height = sizeHeight;
			} else {
				cWidth = childView.getMeasuredWidth();
				cHeight = childView.getMeasuredHeight();
				width += cWidth;
				height += cHeight;
			}

		}
		/**
		 * 如果是wrap_content设置为我们计算的值 否则：直接设置为父容器计算的值
		 */
		setMeasuredDimension((widthMode == MeasureSpec.EXACTLY || width == 0) ? sizeWidth : width,
				(heightMode == MeasureSpec.EXACTLY || height == 0) ? sizeHeight : height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int cCount = getChildCount();
			int cWidth = 0;
			int cHeight = 0;
			int titleIndex = 0;
			/**
			 * 遍历所有childView根据其宽和高，以及margin进行布局
			 */
			for (int i = 0; i < cCount; i++) {
				View childView = getChildAt(i);
				if (childView instanceof ViewGroup) {
					// 页布局
					childView.layout(0, t + titleHeight, 0, b + titleHeight);
				} else {
					cWidth = childView.getMeasuredWidth();
					cHeight = childView.getMeasuredHeight();
					int cl = 0, ct = 0, cr = 0, cb = 0;
					cl = titleIndex * (titleWidth + titleSpace) + titleMarginLeft + paddingLeft;
					ct = 0;
					cr = cl + cWidth;
					cb = cHeight + ct;
					childView.layout(cl, ct + paddingTop, cr, cb + paddingTop);
					titleIndex++;
				}
			}
		}
	}

	/**
	 * 滑至指定页面
	 * 
	 * @param position
	 */
	public void setCurrentPage(int position) {
		if (onTopBarFocusChange != null) {
			onTopBarFocusChange.onFocusChange(isTopFocused, position);
		}
		flushTopBar(position);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		// 保存当前状态
		return super.onSaveInstanceState();
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// 读取保存状态
		isRestore = true;
		super.onRestoreInstanceState(state);
	}

	/**
	 * 光标移动 到达后 与控件同时放大
	 */
	private void moveCover(View item) {
		if (cursor == null) {
			cursor = new ImageView(getContext());
			cursor.setBackgroundResource(cursorRes);
			this.addView(cursor);
		}
		setBorderParams(item);
		item.bringToFront();
		cursor.bringToFront();
		if (scalable) {
			scaleToLarge(item);
		}

	}

	/**
	 * 还原控件状态
	 */
	public void returnCover(View item) {
		if (cursor != null) {
			cursor.setVisibility(View.INVISIBLE);
		}

		if (scalable) {
			scaleToNormal(item);
		}
	}

	private AnimatorSet animatorSet;
	private ObjectAnimator largeX;

	private void scaleToLarge(View item) {

		if (!item.isFocused()) {
			return;
		}

		animatorSet = new AnimatorSet();
		largeX = ObjectAnimator.ofFloat(item, "ScaleX", 1f, scale);
		ObjectAnimator largeY = ObjectAnimator.ofFloat(item, "ScaleY", 1f, scale);
		ObjectAnimator cursorX = ObjectAnimator.ofFloat(cursor, "ScaleX", 1f, scale);
		ObjectAnimator cursorY = ObjectAnimator.ofFloat(cursor, "ScaleY", 1f, scale);

		animatorSet.setDuration(durationLarge);
		animatorSet.play(largeX).with(largeY).with(cursorX).with(cursorY);

		animatorSet.start();
	}

	public void scaleToNormal(View item) {
		if (animatorSet == null) {
			return;
		}
		if (animatorSet.isRunning()) {
			animatorSet.cancel();
		}
		ObjectAnimator oa = ObjectAnimator.ofFloat(item, "ScaleX", 1f);
		oa.setDuration(durationSmall);
		oa.start();
		ObjectAnimator oa2 = ObjectAnimator.ofFloat(item, "ScaleY", 1f);
		oa2.setDuration(durationSmall);
		oa2.start();
	}

	/**
	 * 指定光标相对位置
	 */
	private void setBorderParams(View item) {
		cursor.clearAnimation();
		cursor.setVisibility(View.VISIBLE);
		// 判断类型
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) item.getLayoutParams();

		int l, t, r, b;

		l = params.leftMargin + paddingLeft + cursorMarginLeft;
		t = params.topMargin + paddingTop + cursorMarginTop;
		r = l + cursorWidth;
		b = t + cursorHeight;
		cursor.layout(l, t, r, b);

	}

	public void setOnPageChangeListener(ScrollPageChangerListener listener) {
		this.scrollPageChangerListener = listener;
	}

	public void setOnTopBarFocusChangeListener(OnTopBarFocusChange listener) {
		this.onTopBarFocusChange = listener;
	}

	public interface ScrollPageChangerListener {
		public void onPageSelected(int pageCurrent);
	}

	public interface OnTopBarFocusChange {
		public void onFocusChange(boolean hasFocus, int postion);
	}

}
