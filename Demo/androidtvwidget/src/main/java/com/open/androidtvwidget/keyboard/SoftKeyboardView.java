package com.open.androidtvwidget.keyboard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.open.androidtvwidget.keyboard.SoftKey.SaveSoftKey;
import com.open.androidtvwidget.utils.OPENLOG;

import java.util.List;

/**
 * 软键盘绘制控件.(主软键盘，弹出键盘)
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class SoftKeyboardView extends View {

	private static final String TAG = "SoftKeyboardView";

	public SoftKeyboardView(Context context) {
		super(context);
		init(context, null);
	}

	public SoftKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SoftKeyboardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mFmi = mPaint.getFontMetricsInt();
	}

	private SoftKeyboard mSoftKeyboard;
	private Bitmap mCacheBitmap;

	/*
	 * 传入需要绘制的键盘(从XML读取出来的).
	 */
	public void setSoftKeyboard(SoftKeyboard softSkb) {
		this.mSoftKeyboard = softSkb;
		clearCacheBitmap();
	}

	public void clearCacheBitmap() {
		mCacheBitmap = null;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas rootCanvas) {
		if (mSoftKeyboard == null)
			return;
		if (mCacheBitmap == null) {
			OPENLOG.D("onDraw mCacheBitmap:" + mCacheBitmap);
			mCacheBitmap = createCacheBitmap();
			Canvas canvas = new Canvas(mCacheBitmap);
			// 绘制键盘背景.
			drawKeyboardBg(canvas);
			// 绘制键盘的按键.
			int rowNum = this.mSoftKeyboard.getRowNum();
			for (int row = 0; row < rowNum; row++) {
				KeyRow keyRow = this.mSoftKeyboard.getKeyRowForDisplay(row);
				if (keyRow == null)
					continue;
				List<SoftKey> softKeys = keyRow.getSoftKeys();
				int keyNum = softKeys.size();
				for (int i = 0; i < keyNum; i++) {
					SoftKey softKey = softKeys.get(i);
					drawSoftKey(canvas, softKey, false);
				}
			}
		}
		// 绘制缓存.
		drawCacheBitmap(rootCanvas);
		// 绘制按键.
		SoftKey key = mSoftKeyboard.getSelectSoftKey();
		drawSoftKey(rootCanvas, key, true);
	}

	/**
	 * Bitmap.Config ARGB_8888：<br>
	 * 每个像素占四位，即A=8，R=8，G=8，B=8，<br>
	 * 那么一个像素点占8+8+8+8=32位<br>
	 */
	private Bitmap createCacheBitmap() {
		return Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
	}

	private void drawCacheBitmap(Canvas rootCanvas) {
		if (mCacheBitmap != null) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			rootCanvas.drawBitmap(mCacheBitmap, 0, 0, paint);
		}
	}

	private Paint mPaint;

	/**
	 * 绘制键盘的背景.
	 */
	private void drawKeyboardBg(Canvas canvas) {
		Drawable bg = mSoftKeyboard.getKeyboardBg();
		Rect rect = new Rect(0, 0, getWidth(), getHeight());
		if (bg != null) {
			bg.setBounds(rect);
			bg.draw(canvas);
		} else {
			Paint paint = new Paint();
			canvas.drawRect(rect, paint);
		}
	}

	/**
	 * 绘制键值.
	 */
	private void drawSoftKey(Canvas canvas, SoftKey softKey, boolean isDrawState) {
		if (softKey == null) {
			OPENLOG.E("softKey is null...");
			return;
		}
		// 绘制按键背景.
		drawSoftKeyBg(canvas, softKey);
		// 绘制选中状态.
		if (isDrawState) {
			if (softKey.isKeySelected()) {
				drawSoftKeySelectState(canvas, softKey);
			}
			if (softKey.isKeyPressed()) {
				drawSoftKeyPressState(canvas, softKey);
			}
		}
		// 绘制选中状态 and 选中边框最前面.
		// BUG(避免重复绘制) 主要用于挡住文字的，在绘制一次而已.
		// 如果是透明的选中背景，那就不需要再重复绘制一次文字或者图标了.
		if (isDrawState && mIsFront) {
			return;
		}
		// 绘制按键内容.
		String keyLabel = softKey.getKeyLabel();
		Drawable keyIcon = softKey.getKeyIcon();
		if (keyIcon != null) {
			drawSoftKeyIcon(canvas, softKey, keyIcon);
		} else if (!TextUtils.isEmpty(keyLabel)) {
			drawSoftKeyText(canvas, softKey, keyLabel);
		}
	}

	/**
	 * 绘制按键的图片.
	 */
	private void drawSoftKeyIcon(Canvas canvas, SoftKey softKey, Drawable keyIcon) {
		int marginLeft = Math.abs((int) ((softKey.getWidth() - keyIcon.getIntrinsicWidth()) / 2)) + 2;
		int marginRight = Math.abs((int) (softKey.getWidth() - keyIcon.getIntrinsicWidth() - marginLeft)) + 4;
		int marginTop = Math.abs((int) ((softKey.getHeight() - keyIcon.getIntrinsicHeight()) / 2)) + 2;
		int marginBottom = Math.abs((int) (softKey.getHeight() - keyIcon.getIntrinsicHeight() - marginTop)) + 4;
		keyIcon.setBounds(softKey.getLeft() + marginLeft, softKey.getTop() + marginTop,
				softKey.getRight() - marginRight, softKey.getBottom() - marginBottom);
		keyIcon.draw(canvas);
	}

	private FontMetricsInt mFmi;

	/**
	 * 绘制按键的文本字符.
	 */
	private void drawSoftKeyText(Canvas canvas, SoftKey softKey, String keyLabel) {
		mPaint.setTextSize(softKey.getTextSize()); // 文本大小.
		mPaint.setColor(softKey.getTextColor()); // 文本颜色.
		mFmi = mPaint.getFontMetricsInt();
		int fontHeight = mFmi.bottom - mFmi.top; // 字體的高度.
		float fontWidth = mPaint.measureText(keyLabel);
		float marginX = (softKey.getWidth() - fontWidth) / 2.0f;
		float marginY = (softKey.getHeight() - fontHeight) / 2.0f;
		float x = softKey.getLeftF() + marginX;
		// float y = softKey.getTopF() - (mFmi.top) + marginY;
		/**
		 * +1，绘制文字的地方才不会出现问题。
		 */
		float y = softKey.getTopF() - (mFmi.top + 1) + marginY;
		canvas.drawText(keyLabel, x, y, mPaint);
	}

	/**
	 * 绘制按键背景.
	 */
	private void drawSoftKeyBg(Canvas canvas, SoftKey softKey) {
		Drawable bgDrawable = softKey.getKeyBgDrawable();
		if (bgDrawable != null) {
			Rect rect = softKey.getRect();
			bgDrawable.setBounds(rect);
			bgDrawable.draw(canvas);
		}
	}

	/**
	 * 绘制按键的选中状态.
	 */
	private void drawSoftKeySelectState(Canvas canvas, SoftKey softKey) {
		Drawable selectDrawable = softKey.getKeySelectDrawable();
		if (selectDrawable != null) {
			Rect rect = mIsMoveRect ? softKey.getMoveRect() : softKey.getRect();
			int leftPadding, rightPadding, topPadding, bottomPadding;
			leftPadding = rightPadding = topPadding = bottomPadding = 0;
			if (this.mSelectBgRect != null) {
				leftPadding = (int)Math.rint(mSelectBgRect.left);
				rightPadding = (int)Math.rint(mSelectBgRect.right);
				topPadding = (int)Math.rint(mSelectBgRect.top);
				bottomPadding = (int)Math.rint(mSelectBgRect.bottom);
			}
			rect = new Rect(rect.left - leftPadding, rect.top - rightPadding, rect.right + topPadding, rect.bottom + bottomPadding);
			selectDrawable.setBounds(rect);
			selectDrawable.draw(canvas);
		}
	}

	/**
	 * 绘制按下的状态.
	 */
	private void drawSoftKeyPressState(Canvas canvas, SoftKey softKey) {
		Drawable pressDrawable = softKey.getKeyPressDrawable();
		if (pressDrawable != null) {
			pressDrawable.setBounds(softKey.getRect());
			pressDrawable.draw(canvas);
		}
	}

	public SoftKeyboard getSoftKeyboard() {
		return mSoftKeyboard;
	}

	private RectF mSelectBgRect;

	/**
	 * 设置移动边框的相差的间距.
	 */
	public void setSoftKeySelectPadding(int padding) {
		setSoftKeySelectPadding(new RectF(padding, padding, padding, padding));
	}

	public void setSoftKeySelectPadding(RectF rect) {
		this.mSelectBgRect = rect;
	}

	public void setSoftKeyPress(boolean isPress) {
		if (mSoftKeyboard == null) {
			OPENLOG.E("setSoftKeyPress isPress:" + isPress);
			return;
		}
		SoftKey softKey = mSoftKeyboard.getSelectSoftKey();
		if (softKey != null) {
			softKey.setKeyPressed(isPress);
			invalidate();
		}
	}

	public SoftKey onTouchKeyPress(int x, int y) {
		SoftKey softKey = mSoftKeyboard.mapToKey(x, y);
		return softKey;
	}

	/**
	 * 按键移动. <br>
	 * 感觉按照left,top,right,bottom区域<br>
	 * 来查找按键,会影响效率.<br>
	 * 所以使用了最简单的 行,列概念.<br>
	 * 总比谷歌英文输入法（ASOP）的一维好.
	 */
	public boolean moveToNextKey(int direction) {
		if (mSoftKeyboard == null) {
			OPENLOG.E("moveToNextKey mSoftKeyboard is null");
			return false;
		}

		int currentRow = mSoftKeyboard.getSelectRow();
		int currentIndex = mSoftKeyboard.getSelectIndex();

		KeyRow keyRow = mSoftKeyboard.getKeyRowForDisplay(currentRow);
		if (keyRow == null) {
			OPENLOG.E("moveToNextKey keyRow is null");
			return false;
		}

		List<SoftKey> softKeys = keyRow.getSoftKeys();
		if (softKeys == null) {
			OPENLOG.E("moveToNextKey keyRow -> softKeys is null");
			return false;
		}
		SoftKey softKey = null;
		SoftKey selectKey = mSoftKeyboard.getSelectSoftKey();

		switch (direction) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			// 快速查询按键.
			SaveSoftKey saveLSoftKey = selectKey.getNextLeftKey();
			softKey = saveLSoftKey.key;
			//
			if (softKey != null) {
				currentIndex = saveLSoftKey.index;
				currentRow = saveLSoftKey.row;
			} else {
				currentIndex--;
				if (currentIndex < 0) {
					// 判断是否可以左右移动(第一个向左移动到最后一个)
					if (!mSoftKeyboard.isLRMove()) {
						return false;
					}
					if (mSoftKeyboard.isLRMove()) {
						softKey = mSoftKeyboard.getMoveLeftSoftKey(mSoftKeyboard.getSelectRow() - 1, 0);
						currentIndex = mSoftKeyboard.getSelectIndex();
						currentRow = mSoftKeyboard.getSelectRow();
						if (softKey == null) {
							currentIndex = softKeys.size() - 1;
						}
					} else {
						currentIndex = 0;
					}
				}
				// 防止重复刷新.
				if (softKey == null && currentIndex != mSoftKeyboard.getSelectIndex()) {
					softKey = softKeys.get(currentIndex);
				}
				// 保存下个方向的按键.
				if (softKey != null) {
					selectKey.setNextLeftKey(softKey, currentRow, currentIndex);
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			// 快速查询按键.
			SaveSoftKey saveRSoftKey = selectKey.getNextRightKey();
			softKey = saveRSoftKey.key;
			if (softKey != null) {
				currentIndex = saveRSoftKey.index;
				currentRow = saveRSoftKey.row;
			} else {
				currentIndex++;
				if (currentIndex > (softKeys.size() - 1)) {
					if (!mSoftKeyboard.isLRMove()) {
						return false;
					}
					if (mSoftKeyboard.isLRMove()) {
						softKey = mSoftKeyboard.getMoveRightSoftKey(mSoftKeyboard.getSelectRow() - 1, 0);
						currentIndex = mSoftKeyboard.getSelectIndex();
						currentRow = mSoftKeyboard.getSelectRow();
						// 如果区域查找，右边没有高度很高的按键，则跳到第一个.
						if (softKey == null)
							currentIndex = 0;
					} else {
						currentIndex = (softKeys.size() - 1);
					}
				}
				// 防止重复刷新.
				if (softKey == null && currentIndex != mSoftKeyboard.getSelectIndex()) {
					softKey = softKeys.get(currentIndex);
				}
				// 保存下个方向的按键.
				if (softKey != null) {
					selectKey.setNextRightKey(softKey, currentRow, currentIndex);
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			// 快速查询按键.
			SaveSoftKey saveBSoftKey = selectKey.getNextBottomKey();
			softKey = saveBSoftKey.key;
			if (softKey != null) {
				currentIndex = saveBSoftKey.index;
				currentRow = saveBSoftKey.row;
			} else {
				softKey = mSoftKeyboard.getMoveDownSoftKey(mSoftKeyboard.getSelectRow() + 1, mSoftKeyboard.getRowNum());
				currentIndex = mSoftKeyboard.getSelectIndex();
				currentRow = mSoftKeyboard.getSelectRow();

				if (mSoftKeyboard.isTBMove() && softKey == null) {
					softKey = mSoftKeyboard.getMoveDownSoftKey(0, mSoftKeyboard.getSelectRow());
					currentIndex = mSoftKeyboard.getSelectIndex();
					currentRow = mSoftKeyboard.getSelectRow();
				}
				/**
				 * 区域查找，没有查找到的话<br>
				 * 根据行，列查找.
				 */
				if (softKey == null) {
					currentRow++;
					if (currentRow > (mSoftKeyboard.getRowNum() - 1)) {
						if (!mSoftKeyboard.isTBMove()) {
							return false;
						}
						// 判断是否可以上下(最后一个是否可以向下移动到第一个).
						if (mSoftKeyboard.isTBMove()) {
							currentRow = 0;
						} else {
							currentRow = (mSoftKeyboard.getRowNum() - 1);
						}
					}
					// 防止重复刷新.
					if (softKey == null && currentRow != mSoftKeyboard.getSelectRow()) {
						keyRow = mSoftKeyboard.getKeyRowForDisplay(currentRow);
						softKeys = keyRow.getSoftKeys();
						currentIndex = Math.max(Math.min(currentIndex, softKeys.size() - 1), 0);
						softKey = softKeys.get(currentIndex);
					}
				}
				// 保存下个方向的按键.
				if (softKey != null) {
					selectKey.setNextBottomKey(softKey, currentRow, currentIndex);
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			// 快速查询按键.
			SaveSoftKey saveTSoftKey = selectKey.getNextTopKey();
			softKey = saveTSoftKey.key;
			if (softKey != null) {
				currentIndex = saveTSoftKey.index;
				currentRow = saveTSoftKey.row;
			} else {
				softKey = mSoftKeyboard.getMoveUpSoftKey(mSoftKeyboard.getSelectRow() - 1, 0);
				currentIndex = mSoftKeyboard.getSelectIndex();
				currentRow = mSoftKeyboard.getSelectRow();

				if (mSoftKeyboard.isTBMove() && softKey == null) {
					softKey = mSoftKeyboard.getMoveUpSoftKey(mSoftKeyboard.getRowNum() - 1,
							mSoftKeyboard.getSelectRow() + 1);
					currentIndex = mSoftKeyboard.getSelectIndex();
					currentRow = mSoftKeyboard.getSelectRow();
					/**
					 * 区域查找未找到的情况下.
					 */
					if (softKey == null) {
						currentRow--;
						if (currentRow < 0) {
							if (!mSoftKeyboard.isTBMove()) {
								return false;
							}
							if (mSoftKeyboard.isTBMove()) {
								currentRow = (mSoftKeyboard.getRowNum() - 1);
							} else {
								currentRow = 0;
							}
						}
						// 防止重复刷新.
						if (softKey == null && currentRow != mSoftKeyboard.getSelectRow()) {
							keyRow = mSoftKeyboard.getKeyRowForDisplay(currentRow);
							softKeys = keyRow.getSoftKeys();
							currentIndex = Math.max(Math.min(currentIndex, softKeys.size() - 1), 0);
							softKey = softKeys.get(currentIndex);
						}
					}
				}
				// 保存下个方向的按键.
				if (softKey != null) {
					selectKey.setNextTopKey(softKey, currentRow, currentIndex);
				}
			}
			break;
		default:
			break;
		}
		// 刷新移动的位置.
		if (softKey != null) {
			SoftKey oldsoftkey = mSoftKeyboard.getSelectSoftKey();
			mSoftKeyboard.setOneKeySelected(softKey);
			mSoftKeyboard.setSelectRow(currentRow);
			mSoftKeyboard.setSelectIndex(currentIndex);
			if (!mIsMoveRect) {
				invalidate(softKey.getRect()); // 优化绘制区域.
			} else {
				setMoveSoftKeyAnimation(oldsoftkey, softKey); // 移动边框.
			}
		}
		return true;
	}

	private void setMoveSoftKeyAnimation(SoftKey oldSoftkey, SoftKey targetSoftKey) {
		PropertyValuesHolder valuesXHolder = PropertyValuesHolder.ofFloat("Left", oldSoftkey.getLeft(), targetSoftKey.getLeft());
		PropertyValuesHolder valuesX1Holder = PropertyValuesHolder.ofFloat("Right", oldSoftkey.getRight(), targetSoftKey.getRight());
		PropertyValuesHolder valuesYHolder = PropertyValuesHolder.ofFloat("Top", oldSoftkey.getTop(), targetSoftKey.getTop());
		PropertyValuesHolder valuesY1Holder = PropertyValuesHolder.ofFloat("Bottom", oldSoftkey.getBottom(), targetSoftKey.getBottom());
		//
		final ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(targetSoftKey, valuesXHolder, valuesX1Holder, valuesYHolder, valuesY1Holder);
		scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float left = (float) animation.getAnimatedValue("Left");
				float top = (float) animation.getAnimatedValue("Top");
				float right = (float) animation.getAnimatedValue("Right");
				float bottom = (float) animation.getAnimatedValue("Bottom");
				SoftKey softKey = (SoftKey) scaleAnimator.getTarget();
				softKey.setMoveLeft(left);
				softKey.setMoveTop(top);
				softKey.setMoveRight(right);
				softKey.setMoveBottom(bottom);
				postInvalidate();
			}
		});
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(scaleAnimator);
		animatorSet.setDuration(mMoveDuration);
		animatorSet.start();
	}

	private static final int DEFAULT_MOVE_DURATION = 300;
	private boolean mIsMoveRect = false;
	private int mMoveDuration = DEFAULT_MOVE_DURATION;
	private boolean mIsFront = false;

	/**
	 * 设置按键边框的移动时间.
     */
	public void setMoveDuration(int moveDuration) {
		this.mMoveDuration = moveDuration;
	}

	/**
	 * 是否移动按键的边框.
     */
	public void setMoveSoftKey(boolean isMoveRect) {
		this.mIsMoveRect = isMoveRect;
	}

	/**
	 * 设置选中的按键边框在最前面还是最后面
	 * @param isFront true 最前面, false 反之 (默认为 false).
     */
	public void setSelectSofkKeyFront(boolean isFront) {
		this.mIsFront = isFront;
		postInvalidate();
	}

}
