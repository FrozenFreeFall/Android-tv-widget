package com.open.androidtvwidget.keyboard;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.OPENLOG;

import android.content.Context;
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
public class SkbContainer extends RelativeLayout {

	private static final String TAG = "SkbContainer";

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

	private InputModeSwitcher mInputModeSwitcher;
	private SoftKeyboardView mSoftKeyboardView; // 主要的子软键盘.
	private SoftKeyboardView mPopupKeyboardView; // 弹出的软键盘.
	private int mSkbLayout;
	private Context mContext;

	/**
	 * 初始化.
	 */
	private void init(Context context, AttributeSet attrs) {
		this.mContext = context;
		View.inflate(context, R.layout.softkey_layout_view, this);
		setInputModeSwitcher(new InputModeSwitcher());
		updateInputMode(null);
	}

	public void setInputModeSwitcher(InputModeSwitcher imSwitch) {
		mInputModeSwitcher = imSwitch;
	}

	/**
	 * 更新软键盘类型.
	 */
	public void updateSoftKeyboardType() {

	}

	public void updateInputMode(SoftKey softKey) {
		int skbLayout = mInputModeSwitcher.getSkbLayout(); // 输入类型转换出布局XML id.
		// 重新加载布局(前提是不能喝前一个布局一样)
		if (mSkbLayout != skbLayout) {
			mSkbLayout = skbLayout;
			updateSkbLayout(); // 更新软键盘布局.
			requestLayout(); // 重新加载软键盘高度.
			setDefualtSelectKey(0, 0); // 设置默认选中的按键.
		}

		//
		if (mSoftKeyboardView != null) {
			SoftKeyboard skb = mSoftKeyboardView.getSoftKeyboard();
			if (null == skb)
				return;
			// 初始化状态.
			skb.enableToggleStates(mInputModeSwitcher.getToggleStates(), softKey);
			/**
			 * 清空原先的键盘缓存.<br>
			 * 比如 回车改变了状态.(onstartinputView触发)<br>
			 */
			mSoftKeyboardView.clearCacheBitmap();
		}
	}

	private void updateSkbLayout() {
		SkbPool skbPool = SkbPool.getInstance();
		SoftKeyboard softKeyboard = null; // XML中读取保存的键值.
		switch (mSkbLayout) {
		case R.xml.sbd_qwerty: // 全英文键盘.
			softKeyboard = skbPool.getSoftKeyboard(mContext, R.xml.sbd_qwerty);
			break;
		case R.xml.sbd_number: // 数字键盘.
			softKeyboard = skbPool.getSoftKeyboard(mContext, R.xml.sbd_number);
			break;
		default:
			softKeyboard = skbPool.getSoftKeyboard(mContext, R.xml.sbd_qwerty);
			break;
		}
		// 键盘的值.(英文键盘，大小写标志位)
		mInputModeSwitcher.getToggleStates().mQwertyUpperCase = softKeyboard.isQwertyUpperCase();
		mInputModeSwitcher.getToggleStates().mQwerty = softKeyboard.isQwerty();
		mInputModeSwitcher.getToggleStates().mPageState = InputModeSwitcher.TOGGLE_KEYCODE_PAGE_1;
		// 这样可以用于切换.(反位)
		softKeyboard.setQwertyUpperCase(!softKeyboard.isQwertyUpperCase());
		// 更新状态切换.
		mInputModeSwitcher.prepareToggleStates(null);
		//
		mSoftKeyboardView = (SoftKeyboardView) findViewById(R.id.softKeyboardView);
		// 重新绘制 软键盘.
		mSoftKeyboardView.setSoftKeyboard(softKeyboard);
		mInputModeSwitcher.getToggleStates().mSwitchSkb = false;
	}

	SoftKeyBoardListener mSoftKeyListener;

	public void setOnSoftKeyBoardListener(SoftKeyBoardListener cb) {
		mSoftKeyListener = cb;
	}

	public void setDefualtSelectKey(int row, int index) {
		if (mSoftKeyboardView != null) {
			SoftKeyboard softKeyboard = mSoftKeyboardView.getSoftKeyboard();
			if (softKeyboard != null)
				softKeyboard.setOneKeySelected(row, index);
		}
	}

	/**
	 * 按下按键的处理.
	 */
	private boolean setKeyCodeEnter(SoftKey softKey) {
		if (softKey == null) {
			OPENLOG.E(TAG, "setKeyCodeEnter softKey is null");
			return false;
		}
		//
		int softKeyCode = softKey.getKeyCode();
		/**
		 * 自定义按键，比如大/小写转换, 键盘切换等等.<br>
		 * keyCode <= -1 <br>
		 */
		if (softKey.isUserDefKey()) {
			OPENLOG.D(TAG, "setKeyCodeEnter isUserKey keyCode:" + softKeyCode);
			mInputModeSwitcher.switchModeForUserKey(softKey);
			updateInputMode(softKey); // 大/小 写切换.
			return true;
		}
		/*
		 * 判断是否为 A 至 Z 的字母.
		 */
		if ((softKeyCode >= KeyEvent.KEYCODE_A && softKeyCode <= KeyEvent.KEYCODE_Z)
				|| (softKeyCode >= KeyEvent.KEYCODE_0 && softKeyCode <= KeyEvent.KEYCODE_9)) {
			String label = softKey.getKeyLabel();
			onCommitText(softKey);
			return true;
		}
		/*
		 * 处理按键的删除,回车,空格. <br> 光标移动. 返回.
		 */
		switch (softKeyCode) {
		case KeyEvent.KEYCODE_DEL: // 删除 67
			onDelete(softKey);
			break;
		case KeyEvent.KEYCODE_ENTER: // 回车 66
			onCenter(softKey);
			break;
		case KeyEvent.KEYCODE_SPACE: // 空格 62
			softKey.setKeyLabel(" ");
			onCommitText(softKey);
			break;
		case KeyEvent.KEYCODE_BACK: // 返回
			onBack(softKey);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: // 光标向左移动.
			onCursorLeftMove(softKey);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT: // 光标向右移动.
			onCursorRightMove(softKey);
			break;
		default: // 其它的输出.
			onCommitText(softKey);
			break;
		}

		return true;
	}

	public void onCommitText(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onCommitText(key);
		}
	}

	public void onCenter(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onCenter(key);
		}
	}

	public void onDelete(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onDelete(key);
		}
	}

	public void onBack(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onBack(key);
		}
	}

	public void onCursorLeftMove(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onCursorLeftMove(key);
		}
	}

	public void onCursorRightMove(SoftKey key) {
		if (mSoftKeyListener != null) {
			mSoftKeyListener.onCursorRightMove(key);
		}
	}

	/**
	 * 处理DOWN事件.
	 */
	public boolean onSoftKeyDown(int keyCode, KeyEvent event) {
		SoftKey key = new SoftKey();
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			SoftKeyboard softKeyboard = mSoftKeyboardView.getSoftKeyboard();
			SoftKey softKey = softKeyboard.getSelectSoftKey();
			mSoftKeyboardView.setSoftKeyPress(true);
			if (!setKeyCodeEnter(softKey)) {
				return false;
			}
			break;
		case KeyEvent.KEYCODE_DEL: // 删除
			key.setKeyCode(KeyEvent.KEYCODE_DEL);
			onDelete(key);
			break;
		case KeyEvent.KEYCODE_BACK: // 返回
			key.setKeyCode(KeyEvent.KEYCODE_BACK);
			onBack(key);
			break;
		case KeyEvent.KEYCODE_ESCAPE: // 空格
			key.setKeyCode(KeyEvent.KEYCODE_SPACE);
			key.setKeyLabel(" ");
			onCommitText(key);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: // 左
		case KeyEvent.KEYCODE_DPAD_RIGHT: // 右
		case KeyEvent.KEYCODE_DPAD_UP: // 上
		case KeyEvent.KEYCODE_DPAD_DOWN: // 下
			mSoftKeyboardView.setSoftKeyPress(false);
			actionForKeyEvent(keyCode); // 按键移动.
			break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * 处理UP的事件.
	 */
	public boolean onSoftKeyUp(int keyCode, KeyEvent event) {
		if (mSoftKeyboardView != null)
			mSoftKeyboardView.setSoftKeyPress(false);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_ESCAPE: // 键盘返回.
			return false;
		}
		return true;
	}

	/**
	 * 根据 上，下，左，右 来绘制按键位置.
	 */
	public boolean actionForKeyEvent(int direction) {
		if (mSoftKeyboardView != null) {
			return mSoftKeyboardView.moveToNextKey(direction);
		}
		return false;
	}

	private static final int LOG_PRESS_DELAYMILLIS = 200;

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
