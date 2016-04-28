package com.open.demo;

import com.open.androidtvwidget.keyboard.SkbContainer;
import com.open.androidtvwidget.keyboard.SoftKey;
import com.open.androidtvwidget.keyboard.SoftKeyBoardListener;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * https://git.oschina.net/hailongqiu/AndroidTVWidget/wikis/Android-TV-如何使用键盘控件%28SkbContainer%29
 * 暂时的demo.
 * top_bottom_move 是设置上下不循环.
 * left_right_move 默认为true,就像DEMO里一样，是左右循环的.
 * 具体看DEMO.
 * 千万不要将 keyCode设置为 0.
 */
public class DemoKeyBoardActivity extends Activity {

	TextView input_tv;
	SkbContainer skbContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_keyboard_activity);
		input_tv = (TextView) findViewById(R.id.input_tv);
		skbContainer = (SkbContainer) findViewById(R.id.skbContainer);
		skbContainer.setSkbLayout(R.xml.sbd_qwerty);
		skbContainer.setFocusable(true);
		skbContainer.setFocusableInTouchMode(true);
		// 监听键盘事件.
		skbContainer.setOnSoftKeyBoardListener(new SoftKeyBoardListener() {
			@Override
			public void onCommitText(SoftKey softKey) {
				int keyCode = softKey.getKeyCode();
				String keyLabel = softKey.getKeyLabel();
				if (!TextUtils.isEmpty(keyLabel)) { // 输入文字.
					input_tv.setText(input_tv.getText() + softKey.getKeyLabel());
				} else { // 自定义按键，这些都是你自己在XML中设置的keycode.
					keyCode = softKey.getKeyCode();
					if (keyCode == KeyEvent.KEYCODE_DEL) {
						String text = input_tv.getText().toString();
						if (TextUtils.isEmpty(text)) {
							Toast.makeText(getApplicationContext(), "文本已空", Toast.LENGTH_LONG).show();
						} else {
							input_tv.setText(text.substring(0, text.length() - 1));
						}
					} else if (keyCode == KeyEvent.KEYCODE_BACK) {
						finish();
					} else if (keyCode == 66) {
						Toast.makeText(getApplicationContext(), "回车", Toast.LENGTH_LONG).show();
					} else if (keyCode == 250) { //切换键盘
						// 这里只是测试，你可以写自己其它的数字键盘或者其它键盘
						skbContainer.setSkbLayout(R.xml.sbd_number);
					}
				}
			}
			
			@Override
			public void onBack(SoftKey key) {
				finish();
			}
			
			@Override
			public void onDelete(SoftKey key) {
				String text = input_tv.getText().toString();
				input_tv.setText(text.substring(0, text.length() - 1));
			}
			
		});
		// 英文键盘切换测试.
		findViewById(R.id.en_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				skbContainer.setSkbLayout(R.xml.sbd_qwerty);
			}
		});
		// 数字键盘切换测试.
		findViewById(R.id.num_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				skbContainer.setSkbLayout(R.xml.sbd_number);
			}
		});
		// 全键盘切换测试.
		findViewById(R.id.all_key_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				skbContainer.setSkbLayout(R.xml.skb_all_key);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (skbContainer.onSoftKeyDown(keyCode, event))
			return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (skbContainer.onSoftKeyUp(keyCode, event))
			return true;
		return super.onKeyDown(keyCode, event);
	}

}
