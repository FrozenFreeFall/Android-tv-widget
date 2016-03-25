package com.open.androidtvwidget;

import com.open.androidtvwidget.keyboard.SkbContainer;
import com.open.androidtvwidget.keyboard.SoftKey;
import com.open.androidtvwidget.keyboard.SoftKeyBoardListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 暂时的demo.
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
		skbContainer.setOnSoftKeyBoardListener(new SoftKeyBoardListener() {
			@Override
			public void onCommitText(SoftKey softKey) {
				input_tv.setText(input_tv.getText() + softKey.getKeyLabel());
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

			@Override
			public void onCenter(SoftKey key) {
				// 回车.
				Toast.makeText(getApplicationContext(), "回车", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCursorLeftMove(SoftKey key) {
				// 光标移动.
				Toast.makeText(getApplicationContext(), "光标移动Left", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCursorRightMove(SoftKey key) {
				// 光标移动.
				Toast.makeText(getApplicationContext(), "光标移动Right", Toast.LENGTH_LONG).show();
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
