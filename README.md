
Android 组件，主要用于 投影仪，TV电视，手机等等Android设备.
需要定制luncher桌面的请不要联系我.

移动边框，焦点控件动画.
高斯模糊(毛玻璃效果)
图片主色
主题  http://www.oschina.net/p/colortheme

=============================================================

目录结构：
=============================
--com/androidtv/activity
-----------------------------
###----activity.java : 测试demo
--com/androidtv/color (获取图片颜色主色和其它颜色)
-----------------------------
###----ColorArt.java ：
###----HashBag.java
--com/androidtv/utils (一些常用函数)
-----------------------------
###----AnimUtils.java
###----DensityUtil.java
###----FastBlur.java  ：高斯模糊
###----ImageUtils.java
--com/androidtv/view
-----------------------------
###----BorderView.java ：移动边框
###----CopyOfCopyOfFocusBorderView.java
###----FocusBorderView.java
###----FocusRelativeLayout.java
###----ReflectionRelativeLayout.java
###----VerticalSmoothGridView.java
--net.qiujuer.imageblurring.jni (高斯模糊JNI)
-----------------------------

##例子图片查看
=============================================================
[image]https://github.com/FrozenFreeFall/Android-tv-widget/blob/master/demo/QQ%E5%9B%BE%E7%89%8720150123025437.png

如果想在GridView上加上BorderView，只需要调用 runTranslateAnimation