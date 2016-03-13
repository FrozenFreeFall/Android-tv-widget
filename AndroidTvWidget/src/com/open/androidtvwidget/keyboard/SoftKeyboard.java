package com.open.androidtvwidget.keyboard;

import java.util.ArrayList;
import java.util.List;

public class SoftKeyboard {

	private List<KeyRow> mKeyRows = new ArrayList<KeyRow>();

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
		return null;
	}

	public SoftKey getSoftKey(int rIndex, int cIndex) {
		return null;
	}

	/**
	 * 设置按键被选中的状态.
	 * 
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
	 */
	public boolean setOneKeySelected(SoftKey softKey) {
		if (mSelectSoftKey != null)
			mSelectSoftKey.setKeySelected(false);
		softKey.setKeySelected(true);
		mSelectSoftKey = softKey; // 保存被选中的按键.
		return true;
	}

	public boolean setOneKeySelected(int row, int index) {
		if (mKeyRows == null)
			return false;
		row = Math.max(Math.min(row, (mKeyRows.size() - 1)), 0);
		if (mKeyRows.get(row).getSoftKeys() == null)
			return false;
		List<SoftKey> softKeys = mKeyRows.get(row).getSoftKeys();
		index = Math.max(Math.min(row, (softKeys.size() - 1)), 0);
		SoftKey softKey = softKeys.get(index);
		if (softKey != null) {
			mSelectRow = row;
			mSelectIndex = index;
			setOneKeySelected(softKey);
		}
		return true;
	}

}
