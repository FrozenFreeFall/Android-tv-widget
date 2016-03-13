package com.open.androidtvwidget;

import com.open.androidtvwidget.keyboard.SkbContainer;
import com.open.androidtvwidget.keyboard.SoftKeyBoardListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

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
			public void onCommitText(String text) {
				input_tv.setText(input_tv.getText() + text);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
}
