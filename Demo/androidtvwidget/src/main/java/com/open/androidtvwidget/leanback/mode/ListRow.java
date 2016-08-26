package com.open.androidtvwidget.leanback.mode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Leanback 数据结构.
 * Created by hailongqiu on 2016/8/24.
 */
public class ListRow {
    private Object mHeaderItem;
    private List<Object> mItems = new ArrayList<Object>();

    public ListRow(Object header) {
        this.mHeaderItem = header;
    }

    public final Object getHeaderItem() {
        return this.mHeaderItem;
    }

    public final void setHeaderItem(Object headerItem) {
        mHeaderItem = headerItem;
    }

    public void add(Object item) {
        mItems.add(item);
    }

    public void addAll(Object items) {
        this.mItems.addAll((Collection<?>) items);
    }

    public List<Object> getItems() {
        return this.mItems;
    }

}
