package com.open.demo.mode;

import com.open.androidtvwidget.leanback.mode.DefualtListPresenter;
import com.open.androidtvwidget.leanback.mode.ItemListPresenter;

/**
 * Created by hailongqiu on 2016/8/26.
 */
public class NewItemListPresenter extends ItemListPresenter {

    @Override
    public DefualtListPresenter getDefualtListPresenter() {
        return new TestMoviceListPresenter();
    }
}
