# AndroidTVWidget

 欢迎进入 TV开发，希望大家不断的分享代码，一起进步，谢谢.
 
 ---- hailongqiu 356752238@qq.com

##Tab 测试DEMO图片.

![输入图片说明](http://git.oschina.net/uploads/images/2015/0905/050539_109ee7a3_111902.png "test1")

![输入图片说明](http://git.oschina.net/uploads/images/2015/0905/050611_b341a277_111902.png "test2")

![输入图片说明](http://git.oschina.net/uploads/images/2015/0905/163056_5d6b6a7a_111902.png "test123")

正在移动的边框.

![输入图片说明](http://git.oschina.net/uploads/images/2015/0905/163423_be647737_111902.png "在这里输入图片标题")

【MainUpView 部分API】
    
     public void setShadow(int resId) { // 设置阴影图片

     public void setShadow(Drawable shadowDrawable) { // 设置阴影图片

     mainUpView1.setUpRect(R.drawable.white_light_10); // 设置移动边框图片.

     mainUpView1.setDrawUpRectPadding(new Rect(22, 22, 22, 22)); // 根据图片边框 自行 填写 相差的边距.

     public void setDrawUpRectPadding(int size) { // 根据图片边框 自行 填写 相差的边距.

    public void setDrawUpRect(boolean isDrawUpRect) { // 设置是否移动边框在最下层. true : 移动边框在最上层. 反之否.


【需要倒影功能 XML布局就可以设置 app:isReflect="false" 默认为 true ，有倒影，如果无法满足，请查看代码，自行修改】

 <com.open.androidtvwidget.view.ReflectItemView

                ... ...

                app:isReflect="false"

                ... ...



## 后期加入
   
   完整DEMO
   
   GridView demo
   
   ListView demo
   
   网络加载布局
   
## 感谢开源[参考代码]
<p>
<a href="https://github.com/XiaoMi/android_tv_metro">XiaoMi android_tv_metro </a>
</p>
<p>
<a href="https://github.com/lf8289/BorderViewDemo">BorderViewDemo</a>
</p>