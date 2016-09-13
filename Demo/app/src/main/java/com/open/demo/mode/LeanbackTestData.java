package com.open.demo.mode;

import java.util.ArrayList;
import java.util.List;

/**
 * Leanaback的测试数据.
 * Created by hailongqiu on 2016/9/6.
 */
public class LeanbackTestData {

    /**
     * Leanback 标题头.
     */
    public static final String MOVIE_CATEGORY[] = {
            "全部软件",
            "聊天工具",
            "浏览器",
            "游戏娱乐",
            "网络游戏",
            "杀毒安全",
    };

    /**
     * Leanback 横向 数据测试.
     */
    public static final List<Movie> MOVIE_ITEMS = new ArrayList<Movie>() {
        {
            add(new Movie(0, "天天2模拟器"));
            add(new Movie(0, "陌陌222"));
            add(new Movie(0, "爱奇艺222"));
            add(new Movie(0, "英雄2联盟2"));
            add(new Movie(0, "腾讯22视频"));
            add(new Movie(0, "QQ22音乐"));
            add(new Movie(0, "无敌22讯飞"));
            add(new Movie(0, "360浏览器"));
            add(new Movie(0, "美图秀秀"));
            add(new Movie(0, "YY语音"));
            add(new Movie(0, "迅雷"));
            add(new Movie(0, "腾讯视频"));
            add(new Movie(0, "酷狗阴影"));
            add(new Movie(0, "优酷"));
            add(new Movie(0, "篮球"));
            add(new Movie(0, "足球"));
        }
    };

    public static final List<Movie> MOVIE_ITEMS2 = new ArrayList<Movie>() {
        {
            add(new Movie(0, "天天模拟器AAA"));
            add(new Movie(0, "陌陌AAA"));
            add(new Movie(0, "爱奇艺222AAA"));
            add(new Movie(0, "英雄2联盟2AA"));
            add(new Movie(0, "腾讯视频AA"));
            add(new Movie(0, "酷狗阴影AA"));
            add(new Movie(0, "优酷AA"));
            add(new Movie(0, "篮球AA"));
            add(new Movie(0, "足球AAA1"));
            add(new Movie(0, "足球AAA15"));
            add(new Movie(0, "足球AAA16"));
        }
    };

}
