package com.open.androidtvwidget.keyboard;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
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

	/**
	 * 解析XML键盘文件.
	 * 
	 * 保存数据到 SoftKeyboard 中去.
	 */
	public SoftKeyboard loadKeyboard(int resourceId) {
		SoftKeyboard softKeyboard = new SoftKeyboard();
		XmlResourceParser xrp = mResources.getXml(resourceId);
		try {
			mXmlEventType = xrp.next();
			while (mXmlEventType != XmlResourceParser.END_DOCUMENT) {
				if (mXmlEventType == XmlResourceParser.START_TAG) {
					//
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
