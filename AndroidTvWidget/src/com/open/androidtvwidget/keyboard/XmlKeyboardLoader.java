package com.open.androidtvwidget.keyboard;

import java.io.IOException;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

/**
 * XML 键盘 布局加载.
 * 
 * @author hailong.qiu 356752238@qq.com
 */
public class XmlKeyboardLoader {

	private static final String TAG = "XmlKeyboardLoader";

	private Context mContext;
	private Resources mResources;

	private int mXmlEventType;

	/**
	 * 首先是读取 键盘类型(InputType).
	 * 
	 * 类型分别为 全英文，T9，五笔 键盘.
	 */
	public XmlKeyboardLoader(final Context context) {
		this.mContext = context;
		if (this.mContext == null)
			throw new AssertionError("mContext not found.");
		this.mResources = context.getResources();
		if (this.mResources == null)
			throw new AssertionError("mContext not found.");
	}

	private static final String XMLATTR_START_POS_X = "start_pos_x";
	private static final String XMLATTR_START_POS_Y = "start_pos_y";

	/**
	 * 键值的间距.
	 */
	private static final String XMLATTR_LEFT_PADDING = "left_padding";
	private static final String XMLATTR_TOP_PADDING = "top_padding";
	private static final String XMLATTR_BOTTOM_PADDING = "bottom_padding";
	private static final String XMLATTR_RIGHT_PADDING = "right_padding";

	/**
	 * XML键值.
	 */
	private static final String XMLTAG_KEYBOARD = "keyboard";
	private static final String XMLTAG_ROW = "row";
	private static final String XMLTAG_KEYS = "keys";
	private static final String XMLTAG_KEY = "key";
	private static final String XMLTAG_INCKEY = "inckey";

	// Kyes.
	private static final String XMLATTR_KEY_LABELS = "labels";
	private static final String XMLATTR_KEY_CODES = "codes";
	private static final String XMLATTR_KEY_SPLITTER = "splitter";
	// key.
	private static final String XMLATTR_KEY_LABEL = "key_label";
	private static final String XMLATTR_KEY_ICON = "key_icon";
	private static final String XMLATTR_KEY_CODE = "key_code";

	private static final String XMLATTR_TEXT_SIZE = "text_size";
	private static final String XMLATTR_TEXT_COLOR = "text_color"; // 文本颜色.
	private static final String XMLATTR_KEY_WIDTH = "width";
	private static final String XMLATTR_KEY_HEIGHT = "height";

	private static final String XMLATTR_KEYBOARD_BG = "bg_res"; // 键盘界面.
	private static final String XMLATTR_KEY_BG = "key_bg_res"; // 键盘按键背景图片.
	private static final String XMLATTR_KEY_SELECT_RES = "key_select_res"; // 键盘按键选中状态图片.
	private static final String XMLATTR_KEY_PRESS_RES = "key_press_res"; // 键盘按键按下.

	float mSaveKeyXPos; // 保存键值的位置.
	float mSaveKeyYPos;
	boolean isStartPosY = true;

	Drawable d;

	/**
	 * 解析XML键盘文件.
	 * 
	 * 保存数据到 SoftKeyboard 中去.
	 */
	public SoftKeyboard loadKeyboard(int resourceId) {
		SoftKeyboard softKeyboard = null;
		SoftKey softKey = null;
		//
		XmlResourceParser xrp = mResources.getXml(resourceId);
		//
		KeyCommonAttributes attrDef = new KeyCommonAttributes(xrp);
		KeyCommonAttributes attrSkb = new KeyCommonAttributes(xrp);
		KeyCommonAttributes attrRow = new KeyCommonAttributes(xrp);
		KeyCommonAttributes attrKeys = new KeyCommonAttributes(xrp);
		KeyCommonAttributes attrKey = new KeyCommonAttributes(xrp);
		//
		try {
			mXmlEventType = xrp.next();
			while (mXmlEventType != XmlResourceParser.END_DOCUMENT) {
				if (mXmlEventType == XmlResourceParser.START_TAG) {
					String attr = xrp.getName();
					// 转换XML值为小写.
					if (!TextUtils.isEmpty(attr)) {
						attr = attr.toLowerCase();
					}
					if (XMLTAG_KEYBOARD.compareTo(attr) == 0) { // keyboard
						if (!attrSkb.getAttributes(attrDef))
							return null;
						Drawable bg = getDrawable(xrp, XMLATTR_KEYBOARD_BG, null); // 获取键盘背景.
						softKeyboard = new SoftKeyboard();
						softKeyboard.setKeyboardBg(bg);
						mSaveKeyYPos = attrSkb.mKeyYPos;
					} else if (XMLTAG_ROW.compareTo(attr) == 0) { // row 列.
						if (!attrRow.getAttributes(attrSkb)) {
							return null;
						}
						if (softKeyboard != null) {
							KeyRow keyRow = new KeyRow();
							softKeyboard.addKeyRow(keyRow);
						}
						isStartPosY = getFloat(xrp, XMLATTR_START_POS_Y, -1.0f) != -1.0f ? true : false;
						mSaveKeyXPos = 0;
					} else if (XMLTAG_KEYS.compareTo(attr) == 0) { // keys.
						if (!attrKeys.getAttributes(attrRow)) {
							return null;
						}
						//
						String splitter = xrp.getAttributeValue(null, XMLATTR_KEY_SPLITTER);
						splitter = Pattern.quote(splitter);
						String labels = xrp.getAttributeValue(null, XMLATTR_KEY_LABELS);
						String codes = xrp.getAttributeValue(null, XMLATTR_KEY_CODES); // 后续加入.
						if (null == splitter || null == labels) {
							return null;
						}
						String labelArr[] = labels.split(splitter);
						// 添加KEYS中的键值.
						for (int i = 0; i < labelArr.length; i++) {
							softKey = getSoftKey(xrp, attrKeys);
							softKey.setKeyLabel(labelArr[i]); // 设置 label.
							softKeyboard.addSoftKey(softKey);
							mSaveKeyXPos += softKey.getWidth() + attrKeys.mKeyLeftPadding;
						}
					} else if (XMLTAG_KEY.compareTo(attr) == 0) { // key
						if (null == softKeyboard) {
							return null;
						}
						if (!attrKey.getAttributes(attrRow)) {
							return null;
						}
						softKey = getSoftKey(xrp, attrKey);
						softKeyboard.addSoftKey(softKey);
						mSaveKeyXPos += softKey.getWidth() + attrKey.mKeyLeftPadding;
					}
				} else if (mXmlEventType == XmlResourceParser.END_TAG) {
					// 判断是否为 </row>
					String attr = xrp.getName();
					if (XMLTAG_ROW.compareTo(attr) == 0) {
						mSaveKeyYPos += attrRow.keyHeight + attrRow.mKeyTopPadding;
					}
				}
				mXmlEventType = xrp.next();
			}
			xrp.close();
			return softKeyboard;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			Log.e(TAG, "loadKeyboard XmlPullParserException " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "loadKeyboard IOException " + e.getMessage());
		} finally {
			if (xrp != null)
				xrp.close();
		}
		return null;
	}

	/**
	 * 获取按键.
	 */
	private SoftKey getSoftKey(XmlResourceParser xrp, KeyCommonAttributes attrKey) {
		int keyCode = getInteger(xrp, XMLATTR_KEY_CODE, 0); // key_code
		Drawable keyIcon = getDrawable(xrp, XMLATTR_KEY_ICON, null); // key_icon
		String keyLabel = getString(xrp, XMLATTR_KEY_LABEL, null); // key_label
		float textSize = getFloat(xrp, XMLATTR_TEXT_SIZE, attrKey.mTextSize); // 按键文本字体大小
		int textColor = getColor(xrp, XMLATTR_TEXT_COLOR, attrKey.mTextColor); // 按键文本颜色.
		//
		float left, right, top = 0, bottom;
		left = mSaveKeyXPos + attrKey.mKeyXPos + attrKey.mKeyLeftPadding;
		right = left + attrKey.keyWidth;
		// 判断是否<key 没有设置 start_pos_y
		if (isStartPosY) {
			top = attrKey.mKeyYPos + attrKey.mKeyTopPadding;
			mSaveKeyYPos = attrKey.mKeyYPos ; // 保存 top属性.
		} else {
			top = mSaveKeyYPos + attrKey.mKeyTopPadding;
		}
		bottom = top + attrKey.keyHeight;
		// 按键.
		SoftKey softKey = new SoftKey();
		softKey.setTextSize(textSize);
		softKey.setKeyLabel(keyLabel);
		softKey.setKeyIcon(keyIcon);
		softKey.setTextColor(textColor);
		softKey.setKeyCode(keyCode); // 自定义的一些值,比如删除,回车.
		softKey.setKeySelectDrawable(attrKey.mKeySelectDrawable); // 设置选中的图片.
		softKey.setKeyPressDrawable(attrKey.mKeyPressDrawable); // 按下的图片.
		softKey.setKeyBgDrawable(attrKey.mKeyBgDrawable); // 按键背景.
		softKey.setKeyDimensions(left, top, right, bottom);
		return softKey;
	}

	/**
	 * 用于读取键值中的属性.
	 */
	class KeyCommonAttributes {
		private static final float KEY_TEXT_SIZE = 30;

		XmlResourceParser mXrp;
		float keyWidth;
		float keyHeight;
		float mKeyXPos;
		float mKeyYPos;
		float mKeyLeftPadding;
		float mKeyTopPadding;
		float mTextSize = KEY_TEXT_SIZE;
		int mTextColor = Color.RED; // 按键文本颜色.

		Drawable mKeySelectDrawable;
		Drawable mKeyPressDrawable;
		Drawable mKeyBgDrawable;

		public KeyCommonAttributes(XmlResourceParser xrp) {
			this.mXrp = xrp;
		}

		boolean getAttributes(KeyCommonAttributes defAttr) {
			this.mKeyBgDrawable = getDrawable(mXrp, XMLATTR_KEY_BG, defAttr.mKeyBgDrawable); // 按键背景.
			this.mKeySelectDrawable = getDrawable(mXrp, XMLATTR_KEY_SELECT_RES, defAttr.mKeySelectDrawable); // 按键选中.
			this.mKeyPressDrawable = getDrawable(mXrp, XMLATTR_KEY_PRESS_RES, defAttr.mKeyPressDrawable); // 按键按下.
			this.mKeyLeftPadding = getFloat(mXrp, XMLATTR_LEFT_PADDING, defAttr.mKeyLeftPadding);
			this.mKeyTopPadding = getFloat(mXrp, XMLATTR_TOP_PADDING, defAttr.mKeyTopPadding);
			this.mKeyXPos = getFloat(mXrp, XMLATTR_START_POS_X, defAttr.mKeyXPos);
			this.mKeyYPos = getFloat(mXrp, XMLATTR_START_POS_Y, defAttr.mKeyYPos);
			this.keyWidth = getFloat(mXrp, XMLATTR_KEY_WIDTH, defAttr.keyWidth);
			this.keyHeight = getFloat(mXrp, XMLATTR_KEY_HEIGHT, defAttr.keyHeight);
			this.mTextSize = getFloat(mXrp, XMLATTR_TEXT_SIZE, defAttr.mTextSize); // 按键文本大小.
			this.mTextColor = getColor(mXrp, XMLATTR_TEXT_COLOR, defAttr.mTextColor); // 按键文本颜色.
			return true;
		}
	}

	private int getInteger(XmlResourceParser xrp, String name, int defValue) {
		int resId = xrp.getAttributeResourceValue(null, name, 0);
		String s;
		if (resId == 0) {
			s = xrp.getAttributeValue(null, name);
			if (null == s)
				return defValue;
			try {
				int ret = Integer.valueOf(s);
				return ret;
			} catch (NumberFormatException e) {
				return defValue;
			}
		} else {
			return Integer.parseInt(mContext.getResources().getString(resId));
		}
	}

	private int getColor(XmlResourceParser xrp, String name, int defValue) {
		int resId = xrp.getAttributeResourceValue(null, name, 0);
		String s;
		if (resId == 0) {
			s = xrp.getAttributeValue(null, name);
			if (null == s)
				return defValue;
			// 判断是否为 "#FF0000"
			if (s.startsWith("#")) {
				try {
					return Color.parseColor(s);
				} catch (Exception e) {
					e.printStackTrace();
					return defValue;
				}
			}
			try {
				int ret = Integer.valueOf(s);
				return ret;
			} catch (NumberFormatException e) {
				return defValue;
			}
		} else {
			return mContext.getResources().getColor(resId);
		}
	}

	private String getString(XmlResourceParser xrp, String name, String defValue) {
		int resId = xrp.getAttributeResourceValue(null, name, 0);
		if (resId == 0) {
			return xrp.getAttributeValue(null, name);
		} else {
			return mContext.getResources().getString(resId);
		}
	}

	private float getFloat(XmlResourceParser xrp, String name, float defValue) {
		int resId = xrp.getAttributeResourceValue(null, name, 0);
		if (resId == 0) {
			String s = xrp.getAttributeValue(null, name);
			if (null == s)
				return defValue;
			try {
				float ret;
				if (s.endsWith("%p")) {
					ret = Float.parseFloat(s.substring(0, s.length() - 2)) / 100;
				} else {
					ret = Float.parseFloat(s);
				}
				return ret;
			} catch (NumberFormatException e) {
				return defValue;
			}
		} else {
			return mContext.getResources().getDimension(resId);
		}
	}

	private boolean getBoolean(XmlResourceParser xrp, String name, boolean defValue) {
		String s = xrp.getAttributeValue(null, name);
		if (null == s)
			return defValue;
		try {
			boolean ret = Boolean.parseBoolean(s);
			return ret;
		} catch (NumberFormatException e) {
			return defValue;
		}
	}

	private Drawable getDrawable(XmlResourceParser xrp, String name, Drawable defValue) {
		int resId = xrp.getAttributeResourceValue(null, name, 0);
		if (0 == resId)
			return defValue;
		return mResources.getDrawable(resId);
	}

}
