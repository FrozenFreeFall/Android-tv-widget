package com.open.androidtvwidget.keyboard;

import java.util.ArrayList;
import java.util.List;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.OPENLOG;

import android.view.inputmethod.EditorInfo;

/**
 * 软键盘类型切换.(英文，数字，符号键盘)
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class InputModeSwitcher {

	private static final String TAG = "InputModeSwitcher";

	public static final int MAX_TOGGLE_STATES = 4;

	private static final int INPUT_TYPE_ALL_EN = 0; // 全英文键盘.
	private static final int INPUT_TYPE_NUMBER = 1; // 数字键盘
	private static final int INPUT_TYPE_SYM = 2; // 符号键盘.

	private static final int USERDEF_KEYCODE_CASE_1 = -1; // 大小写.
	private static final int MODE_SKB_ENGLISH_UPPER = 3;
	private static final int MODE_SKB_ENGLISH_LOWER = 2;

	private static final int USERDEF_KEYCODE_PAGE = -100; // 翻页.
	public static final int TOGGLE_KEYCODE_PAGE_1 = 101;
	private static final int TOGGLE_KEYCODE_PAGE_2 = 102;

	private static final int MODE_SKB_NUMBER = -3;
	private static final int MODE_SKB_ALL_EN = -4;
	private static final int MODE_SKB_ALL_SYM = -5;

	private static final int TOGGLE_ENTER_GO = 7;
	private static final int TOGGLE_ENTER_SEARCH = 8;
	private static final int TOGGLE_ENTER_SEND = 9;
	private static final int TOGGLE_ENTER_NEXT = 10;
	private static final int TOGGLE_ENTER_DONE = 11;
	public static final int TOGGLE_ENTER_MULTI_LINE_DONE = 12;

	private int mInputMode = INPUT_TYPE_ALL_EN;

	public int getInputMode() {
		return mInputMode;
	}

	/**
	 * 设置软键盘类型后.
	 * 
	 * 调用 SkbContainer 的 updateInputMode
	 */
	public void setInputMode(EditorInfo editorInfo) {
		mEditorInfo = editorInfo;
		int inputType = editorInfo.inputType & EditorInfo.TYPE_MASK_CLASS;
		OPENLOG.D(TAG, "inputType:" + inputType);
		switch (inputType) {
		case EditorInfo.TYPE_CLASS_NUMBER:
		case EditorInfo.TYPE_CLASS_DATETIME:
		case EditorInfo.TYPE_CLASS_PHONE:
			mInputMode = INPUT_TYPE_NUMBER;
			break;
		case EditorInfo.TYPE_CLASS_TEXT:
			mInputMode = INPUT_TYPE_ALL_EN;
			break;
		default:
			mInputMode = INPUT_TYPE_ALL_EN;
			break;
		}
		//
		prepareToggleStates(null);
	}

	// 获取键盘布局类型.
	public int getSkbLayout() {
		int layout = (mInputMode);
		switch (layout) {
		case INPUT_TYPE_ALL_EN: // 全英文.
			return R.xml.sbd_qwerty;
		case INPUT_TYPE_NUMBER: // 数字键盘
			return R.xml.sbd_number;
		case INPUT_TYPE_SYM: // 五笔
			return R.xml.sbd_qwerty;
		default:
			return R.xml.sbd_qwerty;
		}
	}

	/**
	 * 自定义按键，比如 大小写转换等等. 自定义按键都必须小于0.
	 */
	public void switchModeForUserKey(SoftKey userKey) {
		prepareToggleStates(userKey); // 初始化状态切换值.
	}

	private ToggleStates mToggleStates = new ToggleStates();
	private EditorInfo mEditorInfo;

	/*
	 * 状态切换初始化.
	 */
	public void prepareToggleStates(SoftKey softKey) {
		int userKeyCode = (softKey != null) ? softKey.getKeyCode() : 0;
		// 防止数字键盘切换，上次的状态被清掉了.
		if (!mToggleStates.mSwitchSkb)
			mToggleStates.mKeyStates.clear();
		List<Integer> mKeyStates = mToggleStates.mKeyStates;

		if (mToggleStates.mQwerty && (mInputMode == INPUT_TYPE_ALL_EN) && (userKeyCode == 0)) { // 初始化英文键盘大小写.
			mKeyStates.add(mToggleStates.mQwertyUpperCase ? MODE_SKB_ENGLISH_UPPER : MODE_SKB_ENGLISH_LOWER);
		} else if ((mToggleStates.mQwerty && USERDEF_KEYCODE_CASE_1 == userKeyCode)) { // 大小写转换.
			mToggleStates.mQwertyUpperCase = !mToggleStates.mQwertyUpperCase;
			mKeyStates.add(mToggleStates.mQwertyUpperCase ? MODE_SKB_ENGLISH_UPPER : MODE_SKB_ENGLISH_LOWER);
		} else if (USERDEF_KEYCODE_PAGE == userKeyCode) { // 翻页.
			if (mToggleStates.mPageState == TOGGLE_KEYCODE_PAGE_1) {
				mToggleStates.mPageState = TOGGLE_KEYCODE_PAGE_2;
			} else {
				mToggleStates.mPageState = TOGGLE_KEYCODE_PAGE_1;
			}
			mKeyStates.add(mToggleStates.mPageState);
		} else if (MODE_SKB_NUMBER == userKeyCode) { // 切换到数字键盘.
			if (mInputMode == INPUT_TYPE_ALL_EN)
				mKeyStates.add(MODE_SKB_ALL_EN); // 状态(返回)
			mInputMode = INPUT_TYPE_NUMBER;
			mToggleStates.mSwitchSkb = true;
		} else if (MODE_SKB_ALL_EN == userKeyCode) { // 英文键盘.
			mInputMode = INPUT_TYPE_ALL_EN;
		}
		
	}

	private boolean isCenterMultiLine() {
		int f = mEditorInfo.inputType & EditorInfo.TYPE_MASK_FLAGS;
		return (f == EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
	}

	public ToggleStates getToggleStates() {
		return mToggleStates;
	}

	class ToggleStates {
		public int mPageState = TOGGLE_KEYCODE_PAGE_1;
		public boolean mSwitchSkb;
		public boolean mQwerty;
		public boolean mQwertyUpperCase;
		public List<Integer> mKeyStates = new ArrayList<Integer>();
	}

}
