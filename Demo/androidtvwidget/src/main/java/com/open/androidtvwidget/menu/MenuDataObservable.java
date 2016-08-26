package com.open.androidtvwidget.menu;

import android.database.Observable;

/**
 * 观察者模式.
 * Created by hailongqiu on 2016/6/2.
 */
public class MenuDataObservable extends Observable<MenuSetObserver> {

    /**
     * 显示菜单改变.
     */
    public void nofityShow(OpenMenu openMenu) {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onShow(openMenu);
            }
        }
    }

    /**
     * 隐藏菜单的改变.
     */
    public void notifyHide(OpenMenu openMenu) {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onHide(openMenu);
            }
        }
    }

    /**
     * 数据改变.
     */
    public void notifyChanged(OpenMenu openMenu) {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged(openMenu);
            }
        }
    }

    /**
     * 数据改变.
     */
    public void notifyChanged() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

    public void notifyInvalidated() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onInvalidated();
            }
        }
    }
}
