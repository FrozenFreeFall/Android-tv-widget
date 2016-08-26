package com.open.androidtvwidget.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.open.androidtvwidget.R;
import com.open.androidtvwidget.utils.GenerateViewId;
import com.open.androidtvwidget.utils.OPENLOG;

/**
 * Created by hailongqiu on 2016/8/23.
 */
public class OpenMenuItemView extends LinearLayout implements OpenMenuItem.ItemView {
    private OpenMenuItem mItemData;
    private Context mContext;

    private ImageView mIconView;
    private CompoundButton mCompoundButton;
    private TextView mTitleView;

    private LayoutInflater mInflater;

    public OpenMenuItemView(Context context) {
        this(context, null, 0);
    }

    public OpenMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OpenMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //
        setFocusableInTouchMode(true);
        setFocusable(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleView = (TextView) findViewById(R.id.title_tv);
    }

    @Override
    public void initialize(OpenMenuItem itemData) {
        this.mItemData = itemData;
        //
        setTitle(itemData.getTitle());
        setIcon(itemData.getIconRes());
        setTextSize(itemData.getTextSize());
        setChecked(itemData, itemData.isChecked());
        // 如果没有设置菜单ITEM ID，则默认设置ID.
        if (itemData.getId() == 0) {
            int id = GenerateViewId.getSingleton().generateViewId();
            itemData.setId(id);
        }
        setId(itemData.getId()); // item ID.
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

    public void setIcon(int icon) {
        if (mIconView == null && icon <= 0)
            return;

        if (mIconView == null) {
            insertIconView();
        }

        if (icon > 0) {
            Drawable d = getContext().getResources().getDrawable(icon);
            mIconView.setImageDrawable(d);
            if (mIconView.getVisibility() != VISIBLE) {
                mIconView.setVisibility(VISIBLE);
            }
        } else {
            mIconView.setVisibility(GONE);
        }
    }

    public void setChecked(OpenMenuItem itemData, boolean checked) {
//        if (itemData.getCheckedView() != null) {
//            if (mCompoundButton == null) {
//                insertCompoundButton(itemData);
//            }
//            mCompoundButton.setChecked(checked);
//        }
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

    /**
     * 插入勾选view.
     */
    private void insertCompoundButton(OpenMenuItem itemData) {
        mCompoundButton = new RadioButton(mContext);
        addView(mCompoundButton, 0);
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
