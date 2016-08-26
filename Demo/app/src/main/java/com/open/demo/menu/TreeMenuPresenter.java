package com.open.demo.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.menu.OpenMenu;
import com.open.androidtvwidget.menu.OpenMenuItem;
import com.open.androidtvwidget.menu.OpenMenuItemView;
import com.open.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 树级菜单的 Presenter. (mvp)
 * Created by hailongqiu on 2016/8/23.
 */
public class TreeMenuPresenter extends OpenPresenter {

    private RecyclerViewTV mRecyclerViewTV;
    private OpenMenu mOpenMenu;

    public TreeMenuPresenter(RecyclerViewTV recyclerViewTV, OpenMenu openMenu) {
        this.mRecyclerViewTV = recyclerViewTV;
        this.mOpenMenu = openMenu;
    }

    private OpenMenuItem getItemPosition(int position) {
        return mOpenMenu.getMenuDatas().get(position);
    }

    private void addAll(List<OpenMenuItem> list, int pos) {
        mOpenMenu.getMenuDatas().addAll(pos, list);
        getAdapter().notifyItemRangeInserted(pos, list.size());
    }

    private void removeAll(List<OpenMenuItem> list, int pos) {
        int sizeNum = removeAllSubMenu(list);
        getAdapter().notifyItemRangeRemoved(pos, sizeNum);
    }

    private int removeAllSubMenu(List<OpenMenuItem> list) {
        int sizeNum = list.size();
        for (OpenMenuItem menuItem : list) {
            if (menuItem.hasSubMenu() && menuItem.isShowSubMenu()) {
                menuItem.setShowSubMenu(false);
                List<OpenMenuItem> delSubItems = menuItem.getSubMenu().getMenuDatas();
                sizeNum += removeAllSubMenu(delSubItems);
            }
        }
        mOpenMenu.getMenuDatas().removeAll(list);
        return sizeNum;
    }

    @Override
    public int getItemCount() {
        return mOpenMenu != null ? mOpenMenu.getMenuDatas().size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_item_layout, parent, false);
        //
        OpenPresenter.ViewHolder result = new ContainerViewHolder(rootView);
        return result;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ArrayList<OpenMenuItem> items = mOpenMenu.getMenuDatas();
        OpenMenuItem menuItem = items.get(position);
        ContainerViewHolder holder = (ContainerViewHolder) viewHolder;
        OpenMenuItemView openMenuItemView = (OpenMenuItemView) holder.view;
        openMenuItemView.initialize(menuItem);
        // 子控件.
        if (menuItem.getMenu().getParentMenu() != null) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) openMenuItemView.getLayoutParams();
            lp.leftMargin = menuItem.getMenu().getTreeDepth() * 45;
        }
        // item 单击处理.
        mRecyclerViewTV.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                GeneralAdapter menuAdapter = getAdapter();
                OpenMenuItem menuItem = getItemPosition(position);
                // 判断是否存在子菜单.
                if (menuItem.hasSubMenu()) {
                    if (!menuItem.isShowSubMenu()) {
                        // 显示菜单.
                        addAll(menuItem.getSubMenu().getMenuDatas(), position + 1);
                        mRecyclerViewTV.scrollToPosition(position + 1);
                    } else {
                        // 隐藏菜单.
                        removeAll(menuItem.getSubMenu().getMenuDatas(), position + 1);
                        mRecyclerViewTV.scrollToPosition(position + 1);
                    }
                    menuItem.setShowSubMenu(!menuItem.isShowSubMenu());
                }
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }

    public GeneralAdapter getAdapter() {
        return (GeneralAdapter) mRecyclerViewTV.getAdapter();
    }

    static class ContainerViewHolder extends OpenPresenter.ViewHolder {
        public ContainerViewHolder(View view) {
            super(view);
        }
    }

}
