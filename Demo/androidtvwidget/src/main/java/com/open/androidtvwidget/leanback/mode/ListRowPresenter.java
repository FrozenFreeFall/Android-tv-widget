package com.open.androidtvwidget.leanback.mode;

import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.leanback.widget.ItemContainerView;

import java.util.List;

/**
 * Leanback ListRowPresenter 控制层.
 * Created by hailongqiu on 2016/8/24.
 */
public class ListRowPresenter extends OpenPresenter {

    OpenPresenter mItemHeaderPresenter;//  item 标题头的 Presenter.
    OpenPresenter mItemListPresenter;// item 标题头下面的 横向 items.

    List<ListRow> mItems;

    /**
     * 你可以设置自己的 头 presenter, 还有横向 presenter.
     *
     * @param items
     * @param headPresenter
     * @param listPresenter
     */
    public ListRowPresenter(List<ListRow> items, OpenPresenter headPresenter, OpenPresenter listPresenter) {
        this.mItems = items;
        if (headPresenter != null) {
            this.mItemHeaderPresenter = headPresenter;
        }
        if (listPresenter != null) {
            this.mItemListPresenter = listPresenter;
        }
    }

    public ListRowPresenter(List<ListRow> items) {
        this(items, new ItemHeaderPresenter(), new ItemListPresenter(new DefualtListPresenter()));
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * 设置标题头的Presenter.
     */
    public void setHeadPresenter(OpenPresenter presenter) {
        this.mItemHeaderPresenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemContainerView itemContainerView = new ItemContainerView(parent.getContext());
        // 添加标题头.
        ViewHolder headVH = mItemHeaderPresenter.onCreateViewHolder(parent, viewType);
        itemContainerView.addHeaderView(headVH.view);
        // 添加横向控件.
        ViewHolder listVH = mItemListPresenter.onCreateViewHolder(parent, viewType);
        itemContainerView.addRowView(listVH.view);
        //
        return new ListRowViewHolder(itemContainerView, headVH, listVH);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ListRowViewHolder listRowViewHolder = (ListRowViewHolder) viewHolder;
        ListRow listRow = mItems.get(position);

        if (listRowViewHolder.mHeadViewHolder != null) {
            mItemHeaderPresenter.onBindViewHolder(listRowViewHolder.mHeadViewHolder, listRow.getHeaderItem());
        }

        if (listRowViewHolder.mListViewHolder != null) {
            mItemListPresenter.onBindViewHolder(listRowViewHolder.mListViewHolder, listRow.getItems());
        }

    }

    static class ListRowViewHolder extends OpenPresenter.ViewHolder {

        ViewHolder mHeadViewHolder;
        ViewHolder mListViewHolder;

        public ListRowViewHolder(View view, ViewHolder headVH, ViewHolder lilstVH) {
            super(view);
            this.mHeadViewHolder = headVH;
            this.mListViewHolder = lilstVH;
        }

    }

}
