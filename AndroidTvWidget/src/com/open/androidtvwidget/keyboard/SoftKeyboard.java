package com.open.androidtvwidget.keyboard;

import java.util.ArrayList;
import java.util.List;

public class SoftKeyboard {

	private List<KeyRow> keyRows = new ArrayList<KeyRow>();

	public KeyRow getKeyRowForDisplay(int row) {
		return keyRows.get(row);
	}

	public int getRowNum() {
		if (keyRows != null) {
			return keyRows.size();
		}
		return 0;
	}
	
}
