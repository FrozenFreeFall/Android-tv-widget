package com.open.androidtvwidget.keyboard;

import android.graphics.RectF;
import android.view.KeyEvent;

/**
 * 键盘接口.
 * @author hailongqiu
 *
 */
public interface SoftKeyBoardable {

	/**
	 * 设置选中的按键边框在最前面还是最后面
	 * @param isFront true 最前面, false 反之 (默认为 false).
	 */
	public void setSelectSofkKeyFront(boolean isFront);

	/**
	 * 设置移动边框和按键背景相差的间距.
     */
	public void setSoftKeySelectPadding(int padding);
	public void setSoftKeySelectPadding(RectF rectf);

	/**
	 * 设置移动边框的时间.
     */
	public void setMoveDuration(int moveDuration);

	/**
	 * 设置移动边框的标志位.
	 * @param isMoveRect  true : 移动 false ： 反之.
     */
	public void setMoveSoftKey(boolean isMoveRect);

	public SoftKeyboardView getSoftKeyboardView();

	/**
	 * 链接键盘事件处理.
	 * @param cb
	 */
	public void setOnSoftKeyBoardListener(SoftKeyBoardListener cb);
	
	/**
	 * 设置键盘XML.
	 * @param layoutID : xml目录下的键盘ID.
	 */
	public void setSkbLayout(int layoutID);
	public int getSkbLayoutId();

	/**
	 * 不设置键盘XML，手动编写键盘值.
	 * @param softSkb
	 */
	public void setSoftKeyboard(SoftKeyboard softSkb);
	
	/**
	 * 设置选中的按键.
	 * @param softKey
	 * @return
	 */
	public boolean setKeySelected(SoftKey softKey);
	public void setDefualtSelectKey(int row, int index);
	public SoftKey getSelectKey();

	/**
	 * 外部处理按键事件.
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onSoftKeyDown(int keyCode, KeyEvent event);
	public boolean onSoftKeyUp(int keyCode, KeyEvent event);
}
