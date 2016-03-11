package com.open.androidtvwidget.keyboard;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 软键盘主容器.
 * 
 * @author hailong.qiu 356752238@qq.com
 *
 */
public class SkbContainer extends RelativeLayout {
	
	private static final String TAG = "SkbContainer";
	
	public SkbContainer(Context context) {
		super(context);
		init(context, null);
	}

	public SkbContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SkbContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private InputModeSwitcher mInputModeSwitcher;
	private SoftKeyboardView mSoftKeyboardView; // 主要的子软键盘.
	private SoftKeyboardView mPopupKeyboardView; // 弹出的软键盘.
	private int mSkbLayout;
	private Context mContext;

	/**
	 * 初始化.
	 */
	private void init(Context context, AttributeSet attrs) {
		this.mContext = context;
		View.inflate(context, R.layout.softkey_layout_view, this);
		mInputModeSwitcher = new InputModeSwitcher();
		updateInputMode();
	}

	/**
	 * 设置软键盘类型.
	 */
	public void setInputMode(int inputMode) {
		mInputModeSwitcher.setInputMode(inputMode);
		updateInputMode();
	}

	public void updateInputMode() {
		int skbLayout = mInputModeSwitcher.getSkbLayout(); // 输入类型转换出布局XML id.
		if (mSkbLayout != skbLayout) {
			mSkbLayout = skbLayout;
			updateSkbLayout(); // 更新软键盘布局.
		}
	}

	private void updateSkbLayout() {
		SkbPool skbPool = SkbPool.getInstance();
		SoftKeyboard softKeyboard = null; // XML中读取保存的键值.
		switch (mSkbLayout) {
		case R.xml.sbd_qwerty: // 全英文键盘.
			softKeyboard = skbPool.getSoftKeyboard(mContext, R.xml.sbd_qwerty);
			break;
		default:
			break;
		}
		Log.d(TAG, "softKeyboard:" + softKeyboard);
		mSoftKeyboardView = (SoftKeyboardView) findViewById(R.id.softKeyboardView);
		// 重新绘制 软键盘.
		mSoftKeyboardView.setSoftKeyboard(softKeyboard);
	}
	
	public boolean onKeyDown() {
		return false;
	}
	
	SoftKeyListener mSoftKeyListener;
	
	public void setOnSoftKeyListener(SoftKeyListener cb) {
		mSoftKeyListener = cb;
	}
	
	class SoftKeyListener {
		public void onCommitText(String text) {
		}
	}
	
}
