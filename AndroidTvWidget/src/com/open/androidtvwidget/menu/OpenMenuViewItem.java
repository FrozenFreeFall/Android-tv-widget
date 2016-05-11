package com.open.androidtvwidget.menu;

import com.open.androidtvwidget.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 菜单Listview中的Item View.
 * 
 * @author hailongqiu 356752238@qq.com
 */
public class OpenMenuViewItem extends LinearLayout implements IOpenMenuView.ItemView {

	private OpenMenuItemImpl mItemData;
	private int mMenuType;

	private ImageView mIconView;
	private RadioButton mRadioButton;
	private TextView mTitleView;
	private CheckBox mCheckBox;

	private LayoutInflater mInflater;

	public OpenMenuViewItem(Context context) {
		super(context);
	}

	public OpenMenuViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTitleView = (TextView) findViewById(R.id.title_tv);
	}

	@Override
	public void initialize(OpenMenuItemImpl itemData, int menuType) {
		this.mItemData = itemData;
		//
		setTitle(itemData.getTitle());
		setIcon(itemData.getIcon());
		setTextSize(itemData.getTextSize());
	}

	public void setTitle(CharSequence title) {
		if (title != null) {
			mTitleView.setText(title);
			if (mTitleView.getVisibility() != VISIBLE)
				mTitleView.setVisibility(VISIBLE);
		} else {
			if (mTitleView.getVisibility() != GONE)
				mTitleView.setVisibility(GONE);
		}
	}

	public void setIcon(Drawable icon) {
		if (mIconView == null && icon == null)
			return;

		if (mIconView == null) {
			insertIconView();
		}

		if (icon != null) {
			mIconView.setImageDrawable(icon);
			if (mIconView.getVisibility() != VISIBLE) {
				mIconView.setVisibility(VISIBLE);
			}
		} else {
			mIconView.setVisibility(GONE);
		}
	}
	
	/**
	 * 设置字体.
	 */
	public void setTextSize(int size) {
		if (size > 0) {
			mTitleView.setTextSize(size);
		}
	}
	
	private void insertIconView() {
		LayoutInflater inflater = getInflater();
		mIconView = (ImageView) inflater.inflate(R.layout.list_menu_item_icon, this, false);
		addView(mIconView, 0);
	}

	private LayoutInflater getInflater() {
		if (mInflater == null) {
			mInflater = LayoutInflater.from(getContext());
		}
		return mInflater;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mIconView != null) {
			ViewGroup.LayoutParams lp = getLayoutParams();
			LayoutParams iconLp = (LayoutParams) mIconView.getLayoutParams();
			if (lp.height > 0 && iconLp.width <= 0) {
				iconLp.width = lp.height;
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
