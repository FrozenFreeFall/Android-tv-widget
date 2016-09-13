package com.open.demo.network.mode;

import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Json数据.
 * Created by hailongqiu on 2016/9/5.
 */
public class BaseJsonData {

    private String bgRes;

    private List<Item> items = new ArrayList<Item>();
    private UpRectItem upRectView;

    public String getBgRes() {
        return bgRes;
    }

    public void setBgRes(String bgRes) {
        this.bgRes = bgRes;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public UpRectItem getUpRectItem() {
        return upRectView;
    }

    public void setUpRectItem(UpRectItem upRectView) {
        this.upRectView = upRectView;
    }

    public class Item {
        String width;
        String height;
        String x;
        String y;
        String imgUrl;
        String text;
        boolean isFocus; // 是否有焦点移动边框.
        boolean isMouse;
        boolean isReflect; // 倒影.
        float radius; // 圆角角度.

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public boolean isReflect() {
            return isReflect;
        }

        public void setReflect(boolean reflect) {
            isReflect = reflect;
        }

        public boolean isMouse() {
            return isMouse;
        }

        public void setMouse(boolean mouse) {
            isMouse = mouse;
        }

        public boolean isFocus() {
            return isFocus;
        }

        public void setFocus(boolean focus) {
            isFocus = focus;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }

    /* *
     *  移动的边框属性.
     */
    public class UpRectItem {
        int type; /* noDraw | open |  RecyclerView  类型 */
        String upRes; /* *上层图片 /
        String shadowRes; /* 阴影图片 */
        int tranAnimTime; /* 移动时间 */
        float scaleX;
        float scaleY;
        String rectLeftPadding;
        String rectRightPadding;
        String rectTopPadding;
        String rectBottomPadding;

        public String getRectLeftPadding() {
            return rectLeftPadding;
        }

        public void setRectLeftPadding(String rectLeftPadding) {
            this.rectLeftPadding = rectLeftPadding;
        }

        public String getRectRightPadding() {
            return rectRightPadding;
        }

        public void setRectRightPadding(String rectRightPadding) {
            this.rectRightPadding = rectRightPadding;
        }

        public String getRectTopPadding() {
            return rectTopPadding;
        }

        public void setRectTopPadding(String rectTopPadding) {
            this.rectTopPadding = rectTopPadding;
        }

        public String getRectBottomPadding() {
            return rectBottomPadding;
        }

        public void setRectBottomPadding(String rectBottomPadding) {
            this.rectBottomPadding = rectBottomPadding;
        }

        public float getScaleX() {
            return scaleX;
        }

        public void setScaleX(float scaleX) {
            this.scaleX = scaleX;
        }

        public float getScaleY() {
            return scaleY;
        }

        public void setScaleY(float scaleY) {
            this.scaleY = scaleY;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUpRes() {
            return upRes;
        }

        public void setUpRes(String upRes) {
            this.upRes = upRes;
        }

        public int getTranAnimTime() {
            return tranAnimTime;
        }

        public void setTranAnimTime(int tranAnimTime) {
            this.tranAnimTime = tranAnimTime;
        }
    }

}
