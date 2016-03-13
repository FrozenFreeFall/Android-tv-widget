package com.open.androidtvwidget.keyboard;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
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
		mInputModeSwitcher = new InputModeSwitcher();
		updateInputMode();
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	/**
	 * 设置软键盘类型.
	 */
	public void setInputMode(int inputMode) {
		mInputModeSwitcher.setInputMode(inputMode);
		updateInputMode();
	}

	public void updateInputMode() {
		int skbLayout = mInputModeSwitcher.getSkbLayout(); // 输入类型转换出布局XML id.
		if (mSkbLayout != skbLayout) {
			mSkbLayout = skbLayout;
			updateSkbLayout(); // 更新软键盘布局.
			setDefualtSelectKey(0, 0); // 设置默认选中的按键.
		}
	}

	private void updateSkbLayout() {
		SkbPool skbPool = SkbPool.getInstance();
		SoftKeyboard softKeyboard = null; // XML中读取保存的键值.
		switch (mSkbLayout) {
		case R.xml.sbd_qwerty: // 全英文键盘.
			softKeyboard = skbPool.getSoftKeyboard(mContext, R.xml.sbd_qwerty);
			break;
		default:
			break;
		}
		mSoftKeyboardView = (SoftKeyboardView) findViewById(R.id.softKeyboardView);
		// 重新绘制 软键盘.
		mSoftKeyboardView.setSoftKeyboard(softKeyboard);
	}

	public boolean onKeyDown() {
		return false;
	}

	SoftKeyBoardListener mSoftKeyListener;

	public void setOnSoftKeyBoardListener(SoftKeyBoardListener cb) {
		mSoftKeyListener = cb;
	}

	public void setDefualtSelectKey(int row, int index) {
		SoftKeyboard softKeyboard = mSoftKeyboardView.getSoftKeyboard();
		softKeyboard.setOneKeySelected(row, index);
	}
	
	/**
	 * 暂时测试,不能使用这个事件,<br>
	 * 必须使用activity的事件.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			String ch = ""; 
			SoftKeyboard softKeyboard = mSoftKeyboardView.getSoftKeyboard();
			SoftKey softKey = softKeyboard.getSelectSoftKey();
			if (softKey != null) {
				ch = softKey.getKeyLabel();
				Log.d(TAG, "onKeyDown onCommitText text:" + ch);
			}
			if (mSoftKeyListener != null) {
				mSoftKeyListener.onCommitText(ch);
			} 
		} else {
			actionForKeyEvent(keyCode); // 按键移动.
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	public boolean actionForKeyEvent(int direction) {
		if (mSoftKeyboardView != null) {
			return mSoftKeyboardView.moveToNextKey(direction);
		}
		return false;
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return super.onKeyLongPress(keyCode, event);
	}
	
}
