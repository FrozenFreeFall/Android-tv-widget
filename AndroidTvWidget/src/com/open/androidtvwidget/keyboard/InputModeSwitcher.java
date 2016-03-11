package com.open.androidtvwidget.keyboard;

import com.open.androidtvwidget.R;

/**
 * 软键盘类型切换. (全英文/T9/五笔)
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class InputModeSwitcher {

	private static final int INPUT_TYPE_ALL_EN = 0; // 全英文键盘.
	private static final int INPUT_TYPE_T9 = 1; // T9
	private static final int INPUT_TYPE_WUBI = 2; // 五笔

	private int mInputMode = INPUT_TYPE_ALL_EN;

//	private SoftKeyService mSoftService;
//
//	public InputModeSwitcher(SoftKeyService softService) {
//		mSoftService = softService;
//	}

	public int getInputMode() {
		return mInputMode;
	}

	/**
	 * 设置软键盘类型后.
	 * 
	 * 调用 SkbContainer 的 updateInputMode
	 */
	public void setInputMode(int inputMode) {
		this.mInputMode = inputMode;
	}

	// 获取键盘布局类型.
	public int getSkbLayout() {
		int layout = (mInputMode);
		switch (layout) {
		case INPUT_TYPE_ALL_EN: // 全英文.
			return R.xml.sbd_qwerty;
		case INPUT_TYPE_T9: // T9 键盘.
			return R.xml.sbd_qwerty;
		case INPUT_TYPE_WUBI: // 五笔
			return R.xml.sbd_qwerty;
		}
		return 0;
	}

}
