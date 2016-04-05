package com.open.androidtvwidget.keyboard;

import android.content.Context;

/**
 * 加载XML键盘布局文件.
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class SkbPool {
	
	private static final String TAG = "SkbPool";
	
	private static SkbPool mInstance = new SkbPool(); // 单例模式.

	private SkbPool() {
	}

	public static final SkbPool getInstance() {
		return SkbPool.mInstance;
	}

	public SoftKeyboard getSoftKeyboard(Context context, int skbXmlId) {
		if (null != context) {
			XmlKeyboardLoader xkbl = new XmlKeyboardLoader(context);
			SoftKeyboard skb = xkbl.loadKeyboard(skbXmlId);
			return skb;
		}
		return null;
	}

}
