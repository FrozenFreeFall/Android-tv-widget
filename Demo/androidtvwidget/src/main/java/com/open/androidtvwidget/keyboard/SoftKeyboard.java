package com.open.androidtvwidget.keyboard;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.view.KeyEvent;

/**
 * 软键盘
 * 
 * @author hailong.qiu 356752238@qq.com
 *	@维护者 hailong.qiu, 如狼(470502943)
 */
public class SoftKeyboard {

	private static final String TAG = "SoftKeyboard";

	private List<KeyRow> mKeyRows = new ArrayList<KeyRow>();
	private Drawable mKeyboardBg = null; // 键盘背景.
	private boolean mIsQwertyUpperCase; // 判断是否大写字母.
	private boolean mIsQwerty; // 英文键盘.
	private int mHeight; // 键盘高度.
	private boolean misLRMove; // 左右移动
	private boolean misTBMove; // 上下移动

	public void setHeight(int height) {
		this.mHeight = height;
	}

	public int getHeight() {
		return this.mHeight;
	}

	public boolean isQwertyUpperCase() {
		return this.mIsQwertyUpperCase;
	}

	public void setQwertyUpperCase(boolean isCase) {
		this.mIsQwertyUpperCase = isCase;
	}

	public boolean isQwerty() {
		return this.mIsQwerty;
	}

	public boolean isLRMove() {
		return this.misLRMove;
	}

	public boolean isTBMove() {
		return this.misTBMove;
	}

	/*
	 * 设置标志位.
	 */
	public void setFlags(boolean isQwerty, boolean isQwertyUpperCase, boolean isLRMove, boolean isTBMove) {
		this.mIsQwerty = isQwerty;
		this.mIsQwertyUpperCase = isQwertyUpperCase;
		this.misLRMove = isLRMove;
		this.misTBMove = isTBMove;
	}

	public Drawable getKeyboardBg() {
		return this.mKeyboardBg;
	}

	public void setKeyboardBg(Drawable keyboardBg) {
		this.mKeyboardBg = keyboardBg;
	}

	public KeyRow getKeyRowForDisplay(int row) {
		return mKeyRows.get(row);
	}

	public List<KeyRow> getKeyRows() {
		return mKeyRows;
	}

	public void setKeyRows(List<KeyRow> mKeyRows) {
		this.mKeyRows = mKeyRows;
	}

	public int getRowNum() {
		if (mKeyRows != null) {
			return mKeyRows.size();
		}
		return 0;
	}

	public void clear() {
		mKeyRows.clear();
	}

	public void addKeyRow(KeyRow keyRow) {
		if (null != mKeyRows && null != keyRow) {
			mKeyRows.add(keyRow);
		}
	}

	public KeyRow getLastKeyRow() {
		return mKeyRows.get(mKeyRows.size() - 1);
	}

	public boolean addSoftKey(SoftKey softKey) {
		if (mKeyRows.isEmpty())
			return false;
		KeyRow keyRow = mKeyRows.get(mKeyRows.size() - 1);
		if (null == keyRow)
			return false;
		List<SoftKey> softKeys = keyRow.getSoftKeys();
		softKeys.add(softKey);
		return true;
	}

	public SoftKey mapToKey(int x, int y) {
		int rowNum = getRowNum();
		for (int row = 0; row < rowNum; row++) {
			KeyRow keyRow = mKeyRows.get(row);
			keyRow.getSoftKeys();
			List<SoftKey> softKeys = keyRow.getSoftKeys();
			for (int index = 0; index < softKeys.size(); index++) {
				SoftKey sKey = softKeys.get(index);
				if (sKey.getRect().contains(x, y)) {
					mSelectRow = row;
					mSelectIndex = index;
					return sKey;
				}
			}
		}
		return null;
	}

	public SoftKey getSoftKey(int rIndex, int cIndex) {
		return null;
	}

	/**
	 * 设置按键被选中的状态.<br>
	 * onkeyDown, onKeyUp的移动效果.
	 */

	private SoftKey mSelectSoftKey = null;
	private int mSelectRow = 0;
	private int mSelectIndex = 0;

	public SoftKey getSelectSoftKey() {
		return this.mSelectSoftKey;
	}

	public int getSelectRow() {
		return this.mSelectRow;
	}

	public void setSelectRow(int selectRow) {
		this.mSelectRow = selectRow;
	}

	public int getSelectIndex() {
		return this.mSelectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.mSelectIndex = selectIndex;
	}

	/**
	 * 设置选中的按键.(高亮)
	 * 如狼(470502943) 修复崩溃并添加取消按键高亮问题.
	 */
	public boolean setOneKeySelected(SoftKey softKey) {
		if (softKey == null && mSelectSoftKey != null) {
			mSelectSoftKey.setKeySelected(false);
			return false;
		}
		softKey.setKeySelected(true);
		mSelectSoftKey = softKey; // 保存被选中的按键.
		return true;
	}

	public SoftKey getMoveLeftSoftKey(int startRow, int endRow) {
		SoftKey selectKey = getSelectSoftKey();
		for (int row = startRow; row >= endRow; row--) {
			KeyRow keyRow = getKeyRowForDisplay(row);
			List<SoftKey> softKeys = keyRow.getSoftKeys();
			for (int index = 0; index < softKeys.size(); index++) {
				SoftKey key = softKeys.get(index);
				// 能向右移动的按键，高度都比原来的按键要高.
				if (key.getHeight() > selectKey.getHeight()) {
					if (key.getLeft() >= selectKey.getLeft() || key.getRight() >= selectKey.getRight()) {
						mSelectIndex = index;
						mSelectRow = row;
						return key;
					}
				}
			}
		}
		return null;
	}

	public SoftKey getMoveRightSoftKey(int startRow, int endRow) {
		SoftKey selectKey = getSelectSoftKey();
		for (int row = startRow; row >= endRow; row--) {
			KeyRow keyRow = getKeyRowForDisplay(row);
			List<SoftKey> softKeys = keyRow.getSoftKeys();
			for (int index = (softKeys.size() - 1); index >= 0; index--) {
				SoftKey key = softKeys.get(index);
				// 能向右移动的按键，高度都比原来的按键要高.
				if (key.getHeight() > selectKey.getHeight()) {
					if (key.getLeft() >= selectKey.getLeft() || key.getRight() >= selectKey.getRight()) {
						mSelectIndex = index;
						mSelectRow = row;
						return key;
					}
				}
			}
		}
		return null;
	}

	public SoftKey getMoveDownSoftKey(int startRow, int endRow) {
		SoftKey selectKey = getSelectSoftKey();
		for (int row = startRow; row < endRow; row++) {
			KeyRow keyRow = getKeyRowForDisplay(row);
			List<SoftKey> softKeys = keyRow.getSoftKeys();
			for (int index = 0; index < softKeys.size(); index++) {
				SoftKey key = softKeys.get(index);
				if (key.getLeft() >= selectKey.getLeft() || key.getRight() >= selectKey.getRight()) {
					mSelectIndex = index;
					mSelectRow = row;
					return key;
				}
			}
		}
		return null;
	}

	public SoftKey getMoveUpSoftKey(int startRow, int endRow) {
		SoftKey selectKey = getSelectSoftKey();
		for (int row = startRow; row >= endRow; row--) {
			KeyRow keyRow = getKeyRowForDisplay(row);
			List<SoftKey> softKeys = keyRow.getSoftKeys();
			for (int index = 0; index < softKeys.size(); index++) {
				SoftKey key = softKeys.get(index);
				if (key.getLeft() >= selectKey.getLeft() || key.getRight() >= selectKey.getRight()) {
					mSelectIndex = index;
					mSelectRow = row;
					return key;
				}
			}
		}
		return null;
	}

	public boolean setOneKeySelected(int row, int index) {
		if (mKeyRows == null)
			return false;
		row = Math.max(Math.min(row, (mKeyRows.size() - 1)), 0);
		if (mKeyRows.get(row).getSoftKeys() == null)
			return false;
		List<SoftKey> softKeys = mKeyRows.get(row).getSoftKeys();
		index = Math.max(Math.min(index, (softKeys.size() - 1)), 0);
		SoftKey softKey = softKeys.get(index);
		if (softKey != null) {
			mSelectRow = row;
			mSelectIndex = index;
			setOneKeySelected(softKey);
		}
		return true;
	}

	/**
	 * 判断是否为字母
	 */
	private boolean isLetter(SoftKey sKey) {
		return ((sKey.getKeyCode() >= KeyEvent.KEYCODE_A) && (sKey.getKeyCode() <= KeyEvent.KEYCODE_Z));
	}

}
