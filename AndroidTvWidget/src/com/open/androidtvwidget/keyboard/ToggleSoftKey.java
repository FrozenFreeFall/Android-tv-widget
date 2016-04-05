package com.open.androidtvwidget.keyboard;

import java.util.ArrayList;
import java.util.List;

import com.open.androidtvwidget.utils.OPENLOG;

import android.graphics.drawable.Drawable;

/**
 * 状态切换按键. 比如大小写切换.
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class ToggleSoftKey extends SoftKey {

	private static final String TAG = "ToggleSoftKey";

	private int mStateId; // 状态按键的状态ID.
	private int mSaveStateId; // 保存的状态ID.

	List<ToggleSoftKey> mStateKeyList = new ArrayList<ToggleSoftKey>(); // 保存状态.

	public void addStateKey(ToggleSoftKey stateKey) {
		mStateKeyList.add(stateKey);
	}

	public List<ToggleSoftKey> getStateKeyList() {
		return mStateKeyList;
	}

	public void saveStateId(int stateId) {
		mSaveStateId = stateId;
	}

	public int getSaveStateId() {
		return this.mSaveStateId;
	}

	public int getStateId() {
		return mStateId;
	}

	public void setStateId(int stateId) {
		this.mStateId = stateId;
	}

	/**
	 * 会根据现有状态ID返回.
	 */
	@Override
	public String getKeyLabel() {
		ToggleSoftKey toggleSoftKey = getToggleState();
		if (toggleSoftKey != null)
			return toggleSoftKey.getKeyLabel();
		return super.getKeyLabel();
	}

	@Override
	public float getTextSize() {
		ToggleSoftKey toggleSoftKey = getToggleState();
		if (toggleSoftKey != null)
			return toggleSoftKey.getTextSize();
		return super.getTextSize();
	}

	/**
	 * 根据状态的颜色.
	 */
	@Override
	public int getTextColor() {
		ToggleSoftKey toggleSoftKey = getToggleState();
		if (toggleSoftKey != null)
			return toggleSoftKey.getTextColor();
		return super.getTextColor();
	}

	/**
	 * 会根据现有状态ID返回.
	 */
	@Override
	public Drawable getKeyIcon() {
		ToggleSoftKey toggleSoftKey = getToggleState();
		if (toggleSoftKey != null)
			return toggleSoftKey.getKeyIcon();
		return super.getKeyIcon();
	}

	public ToggleSoftKey getToggleState() {
		for (ToggleSoftKey toggleSoftKey : mStateKeyList) {
			if (toggleSoftKey.getStateId() == mSaveStateId) { // 根据保存的状态.
				return toggleSoftKey;
			}
		}
		return null;
	}

	/**
	 * 改变状态按键的状态.
	 */
	public boolean enableToggleState(int stateId) {
		int tempStateId = getSaveStateId();
		saveStateId(stateId);
		// 检查是否存在.
		if (getToggleState() == null) {
			saveStateId(tempStateId); // 恢复.
			return false;
		}
		return true;
	}

	public void showStateListTest() {
		for (ToggleSoftKey stateKey : mStateKeyList) {
			OPENLOG.D(TAG, stateKey.toString());
		}
	}

	@Override
	public String toString() {
		return "ToggleSoftKey [stateId=" + mStateId + "]" + "[keyCode=" + getKeyCode() + "]" + "[keyLabel="
				+ getKeyLabel() + "]";
	}

}
