package com.open.androidtvwidget.keyboard;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.OPENLOG;

import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 软键盘主容器.
 *
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class SkbContainer extends RelativeLayout implements SoftKeyBoardable {

	private static final String TAG = "SkbContainer";

	private static final int LOG_PRESS_DELAYMILLIS = 200;

	private SoftKeyboardView mSoftKeyboardView; // 主要的子软键盘.
	private int mSkbLayout;
	private Context mContext;
	private SoftKeyBoardListener mSoftKeyListener;

	public SkbContainer(Context context) {
		super(context);
		init(context, null);
	}

	public SkbContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SkbContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	/**
	 * 初始化.
	 */
	private void init(Context context, AttributeSet attrs) {
		this.mContext = context;
		View.inflate(context, R.layout.softkey_layout_view, this);
		mSoftKeyboardView = (SoftKeyboardView) findViewById(R.id.softKeyboardView);
	}

	@Override
	public void setSelectSofkKeyFront(boolean isFront) {
		mSoftKeyboardView.setSelectSofkKeyFront(isFront);
	}

	@Override
	public void setSoftKeySelectPadding(int padding) {
		mSoftKeyboardView.setSoftKeySelectPadding(padding);
	}

	@Override
	public void setSoftKeySelectPadding(RectF rectf) {
		mSoftKeyboardView.setSoftKeySelectPadding(rectf);
	}

	@Override
	public void setMoveDuration(int moveDuration) {
		mSoftKeyboardView.setMoveDuration(moveDuration);
	}

	@Override
	public void setMoveSoftKey(boolean isMoveRect) {
		mSoftKeyboardView.setMoveSoftKey(isMoveRect);
	}

	@Override
	public SoftKeyboardView getSoftKeyboardView() {
		return this.mSoftKeyboardView;
	}

	@Override
	public void setSkbLayout(int layoutID) {
		if (layoutID != mSkbLayout) {
			mSkbLayout = layoutID;
			updateSkbLayout(); // 更新软键盘布局.
			requestLayout(); // 重新加载软键盘高度.
			setDefualtSelectKey(0, 0); // 设置默认选中的按键.
		}
		if (mSoftKeyboardView != null) {
			SoftKeyboard skb = mSoftKeyboardView.getSoftKeyboard();
			if (null == skb)
				return;
			// 初始化状态.
			/**
			 * 清空原先的键盘缓存.<br>
			 */
			mSoftKeyboardView.clearCacheBitmap();
		}
	}

	@Override
	public int getSkbLayoutId() {
		return this.mSkbLayout;
	}

	@Override
	public void setSoftKeyboard(SoftKeyboard softSkb) {
		mSoftKeyboardView = (SoftKeyboardView) findViewById(R.id.softKeyboardView);
		mSoftKeyboardView.setSoftKeyboard(softSkb);
	}

	private void updateSkbLayout() {
		SkbPool skbPool = SkbPool.getInstance();
		SoftKeyboard softKeyboard = skbPool.getSoftKeyboard(mContext, mSkbLayout);
		mSoftKeyboardView = (SoftKeyboardView) findViewById(R.id.softKeyboardView);
		// 重新绘制 软键盘.
		if (softKeyboard != null) {
			mSoftKeyboardView.setSoftKeyboard(softKeyboard);
		}
	}

	@Override
	public void setOnSoftKeyBoardListener(SoftKeyBoardListener cb) {
		mSoftKeyListener = cb;
	}

	@Override
	public void setDefualtSelectKey(int row, int index) {
		if (mSoftKeyboardView != null) {
			SoftKeyboard softKeyboard = mSoftKeyboardView.getSoftKeyboard();
			if (softKeyboard != null)
				softKeyboard.setOneKeySelected(row, index);
		}
	}

	@Override
	public SoftKey getSelectKey() {
		SoftKeyboard softKeyboard = mSoftKeyboardView.getSoftKeyboard();
		if (softKeyboard != null)
			return softKeyboard.getSelectSoftKey();
		return null;
	}

	@Override
	public boolean setKeySelected(SoftKey softKey) {
		if (mSoftKeyboardView != null) {
			SoftKeyboard softKeyboard = mSoftKeyboardView.getSoftKeyboard();
			if (softKeyboard != null) {
				boolean isBool = softKeyboard.setOneKeySelected(softKey);
				mSoftKeyboardView.invalidate();
				return isBool;
			}
		}
		return false;
	}

	/**
	 * 按下按键的处理.
	 */
	private boolean setKeyCodeEnter(SoftKey softKey) {
		if (softKey == null) {
			OPENLOG.E("setKeyCodeEnter softKey is null");
			return false;
		}
		onCommitText(softKey);
		return true;
	}

	private void onCommitText(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onCommitText(key);
		}
	}

	private void onDelete(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onDelete(key);
		}
	}

	private void onBack(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onBack(key);
		}
	}

	/**
	 * 处理DOWN事件.
	 */
	@Override
	public boolean onSoftKeyDown(int keyCode, KeyEvent event) {
		if (!isFocused()) {
			return false;
		}
		SoftKey tempSoftKey = new SoftKey();
		OPENLOG.D("onSoftKeyDown keyCode:" + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			SoftKeyboard softKeyboard = mSoftKeyboardView.getSoftKeyboard();
			SoftKey softKey = softKeyboard.getSelectSoftKey();
			mSoftKeyboardView.setSoftKeyPress(true);
			OPENLOG.D("onSoftKeyDown softKey:" + softKey);
			if (!setKeyCodeEnter(softKey)) {
				return false;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
			tempSoftKey.setKeyCode(KeyEvent.KEYCODE_BACK);
			onBack(tempSoftKey);
			break;
		case KeyEvent.KEYCODE_DEL:
			tempSoftKey.setKeyCode(KeyEvent.KEYCODE_DEL);
			onDelete(tempSoftKey);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: // 左
		case KeyEvent.KEYCODE_DPAD_RIGHT: // 右
		case KeyEvent.KEYCODE_DPAD_UP: // 上
		case KeyEvent.KEYCODE_DPAD_DOWN: // 下
			mSoftKeyboardView.setSoftKeyPress(false);
			return actionForKeyEvent(keyCode); // 按键移动.
		default:
			OPENLOG.D("onSoftKeyDown false keyCode:" + keyCode);
			return false;
		}
		OPENLOG.D("onSoftKeyDown true keyCode:" + keyCode);
		return true;
	}

	/**
	 * 处理UP的事件.
	 */
	@Override
	public boolean onSoftKeyUp(int keyCode, KeyEvent event) {
		if (!isFocused()) {
			return false;
		}
		OPENLOG.D("onSoftKeyUp keyCode:" + keyCode);
		if (mSoftKeyboardView != null)
			mSoftKeyboardView.setSoftKeyPress(false);
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_DPAD_LEFT: // 左
		case KeyEvent.KEYCODE_DPAD_RIGHT: // 右
		case KeyEvent.KEYCODE_DPAD_UP: // 上
		case KeyEvent.KEYCODE_DPAD_DOWN: // 下
		case KeyEvent.KEYCODE_BACK:
			OPENLOG.D("onSoftKeyUp true keyCode:" + keyCode);
			return true;
		}
		OPENLOG.D("onSoftKeyUp false keyCode:" + keyCode);
		return false;
	}

	/**
	 * 根据 上，下，左，右 来绘制按键位置.
	 */
	private boolean actionForKeyEvent(int direction) {
		if (mSoftKeyboardView != null) {
			return mSoftKeyboardView.moveToNextKey(direction);
		}
		return true;
	}

	Handler longPressHandler = new Handler() {
		public void handleMessage(Message msg) {
			SoftKey downSKey = (SoftKey) msg.obj;
			if (downSKey != null) {
				setKeyCodeEnter(downSKey);
				// 长按按键.(继续发送) 知道松开按键.
				Message msg1 = longPressHandler.obtainMessage();
				msg1.obj = downSKey;
				longPressHandler.sendMessageDelayed(msg1, LOG_PRESS_DELAYMILLIS);
			}
		};
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			SoftKey downSKey = mSoftKeyboardView.onTouchKeyPress(x, y);
			if (downSKey != null) {
				mSoftKeyboardView.getSoftKeyboard().setOneKeySelected(downSKey);
				mSoftKeyboardView.setSoftKeyPress(true);
				setKeyCodeEnter(downSKey);
				// 长按按键.
				Message msg = longPressHandler.obtainMessage();
				msg.obj = downSKey;
				longPressHandler.sendMessageDelayed(msg, LOG_PRESS_DELAYMILLIS);
			}
			break;
		case MotionEvent.ACTION_UP:
			longPressHandler.removeCallbacksAndMessages(null); // 取消长按按键.
			mSoftKeyboardView.setSoftKeyPress(false);
			break;
		}
		return true;
	}

}
