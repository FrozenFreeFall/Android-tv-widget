package com.open.androidtvwidget.keyboard;

import java.io.IOException;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
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

	private static final String XMLATTR_KEY_LABELS = "labels";
	private static final String XMLATTR_KEY_CODES = "codes";
	private static final String XMLATTR_KEY_LABEL = "label";
	private static final String XMLATTR_KEY_ICON = "icon";

	private static final String XMLATTR_KEY_SPLITTER = "splitter";
	private static final String XMLATTR_TEXT_SIZE = "text_size";
	private static final String XMLATTR_KEY_WIDTH = "width";
	private static final String XMLATTR_KEY_HEIGHT = "height";

	int mTextSize = 0;
	float mSaveKeyXPos;
	float mSaveKeyYPos;

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
						softKeyboard = new SoftKeyboard();
					} else if (XMLTAG_ROW.compareTo(attr) == 0) { // row 列.
						if (!attrRow.getAttributes(attrSkb)) {
							return null;
						}
						if (softKeyboard != null) {
							KeyRow keyRow = new KeyRow();
							softKeyboard.addKeyRow(keyRow);
						}
						mSaveKeyXPos = 0;
					} else if (XMLTAG_KEYS.compareTo(attr) == 0) { // keys.
						if (!attrKeys.getAttributes(attrRow)) {
							return null;
						}
						//
						String splitter = xrp.getAttributeValue(null, XMLATTR_KEY_SPLITTER);
						splitter = Pattern.quote(splitter);
						String labels = xrp.getAttributeValue(null, XMLATTR_KEY_LABELS);
						String codes = xrp.getAttributeValue(null, XMLATTR_KEY_CODES);
						int textSize = getInteger(xrp, XMLATTR_TEXT_SIZE, mTextSize);
						if (null == splitter || null == labels) {
							return null;
						}
						String labelArr[] = labels.split(splitter);
						// 添加KEYS中的键值.
						float saveKeyX = 0;
						for (int i = 0; i < labelArr.length; i++) {
							softKey = new SoftKey();
							softKey.setKeyLabel(labelArr[i]);
							// key 位置.
							float left, right, top, bottom;
							left = saveKeyX + attrKeys.mKeyXPos + attrKeys.mKeyLeftPadding;
							right = left + attrKeys.keyWidth;
							top = attrKeys.mKeyYPos;
							bottom = top + attrKeys.keyHeight;
							softKey.setKeyDimensions(left, top, right, bottom);
							softKeyboard.addSoftKey(softKey);
							saveKeyX += softKey.getWidth() + attrKeys.mKeyLeftPadding;
						}
					} else if (XMLTAG_KEY.compareTo(attr) == 0) { // key
						if (null == softKeyboard) {
							return null;
						}
						if (!attrKey.getAttributes(attrRow)) {
							return null;
						}
					}
				} else if (mXmlEventType == XmlResourceParser.END_TAG) {
					//
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
	 * 用于读取键值中的属性.
	 */
	class KeyCommonAttributes {
		XmlResourceParser mXrp;
		float keyWidth;
		float keyHeight;
		float mKeyXPos;
		float mKeyYPos;
		float mKeyLeftPadding;

		public KeyCommonAttributes(XmlResourceParser xrp) {
			this.mXrp = xrp;
		}

		boolean getAttributes(KeyCommonAttributes defAttr) {
			mKeyLeftPadding = getFloat(mXrp, XMLATTR_LEFT_PADDING, defAttr.mKeyLeftPadding);
			mKeyXPos = getFloat(mXrp, XMLATTR_START_POS_X, defAttr.mKeyXPos);
			mKeyYPos = getFloat(mXrp, XMLATTR_START_POS_Y, defAttr.mKeyYPos);
			// 宽度.
			keyWidth = getFloat(mXrp, XMLATTR_KEY_WIDTH, defAttr.keyWidth);
			// 高度.
			keyHeight = getFloat(mXrp, XMLATTR_KEY_HEIGHT, defAttr.keyHeight);
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
