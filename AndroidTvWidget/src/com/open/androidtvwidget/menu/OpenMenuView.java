package com.open.androidtvwidget.menu;

public interface OpenMenuView {
	
	public void initialize(OpenMenuBuilder menu);
	
	public interface ItemView {
		public void initialize(OpenMenuItemImpl itemData, int menuType);
	}
}
