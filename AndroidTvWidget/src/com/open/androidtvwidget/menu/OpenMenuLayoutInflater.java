package com.open.androidtvwidget.menu;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;

public class OpenMenuLayoutInflater {

	private static OpenMenuLayoutInflater sInstance = null;
	private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<String, Constructor<? extends View>>();

	private Context mContext;
	final Object[] mConstructorArgs = new Object[2];

	public static void testLoadLayout(Context context) {
//		OpenMenuLayoutInflater.from(context).inflate(R.layout.item_listview);
	}

	public static OpenMenuLayoutInflater from(Context context) {
		if (sInstance == null)
			sInstance = new OpenMenuLayoutInflater(context);
		return sInstance;
	}

	public Context getContext() {
		return mContext;
	}

	protected OpenMenuLayoutInflater(Context context) {
		mContext = context;
	}

	public View inflate(int reSource) {
		XmlResourceParser parser = getContext().getResources().getLayout(reSource);
		try {
			return inflate(parser);
		} finally {
			parser.close();
		}
	}

	public View inflate(XmlPullParser parser) {
		synchronized (mConstructorArgs) {
			final AttributeSet attrs = Xml.asAttributeSet(parser);
			Context lastContext = (Context) mConstructorArgs[0];
			mConstructorArgs[0] = mContext;
			try {
				int type;
				while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
					 // Empty
				}
			} catch (XmlPullParserException e) {
			} catch (IOException e) {
			} finally {
			}
		}
		return null;
	}

	public final View createView(String name, String prefix, AttributeSet attrs) {
		Constructor<? extends View> constructor = sConstructorMap.get(name);
		return null;
	}

}
