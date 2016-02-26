# AndroidTVWidget

 欢迎进入 TV开发，希望大家不断的分享代码，一起进步，谢谢.
  
![输入图片说明](http://git.oschina.net/uploads/images/2016/0223/094451_e49419a7_111902.png "在这里输入图片标题")
 
 ---- hailongqiu 356752238@qq.com

##Tab 测试DEMO图片.

![输入图片说明](http://git.oschina.net/uploads/images/2015/0905/050539_109ee7a3_111902.png "test1")

![输入图片说明](http://git.oschina.net/uploads/images/2015/0905/050611_b341a277_111902.png "test2")

![输入图片说明](http://git.oschina.net/uploads/images/2015/0905/163056_5d6b6a7a_111902.png "test123")

![输入图片说明](http://git.oschina.net/uploads/images/2015/0905/163423_be647737_111902.png "正在移动的边框")

![输入图片说明](http://git.oschina.net/uploads/images/2016/0223/190022_02e85c54_111902.png "ListVie的支持")

当ListView中有一些抢焦点的控件的时候，请使用 setDescendantFocusability ... ... 比如 Button，EditText

![输入图片说明](http://git.oschina.net/uploads/images/2016/0223/190048_e9e0be4c_111902.png "GridView的支持")

![标题栏](http://git.oschina.net/uploads/images/2016/0224/150241_7657d29b_111902.png "标题栏")

【MainUpView 部分API】
   
   private static int TRAN_DUR_ANIM = 300; // 控件动画的时间.(默认时间，建议不更改)
   
   public void setTranDurAnimTime(int time) { // 控件动画的时间.
   
   public void runTranslateAnimation(View toView, float scaleX, float scaleY) { // 边框移动到那个焦点控件.
   
   public void setFocusView(View view, float scale) { 设置焦点子控件的移动和放大.
   
   public void setUnFocusView(View view) { 设置无焦点子控件还原.
   
   public void setDrawUpRectPadding(int size) { 根据图片边框 自行 填写 相差的边距.

   public void setDrawUpRectPadding(Rect rect) { 根据图片边框 自行 填写 相差的边距.
   
   public void setShadowDrawable(Drawable shadowDrawable) {  当图片边框不自带阴影的话，可以自行设置阴影图片.
   
   public void setShadowResource(int resId) {
   
   public void setUpRectDrawable(Drawable upRectDrawable) { 设置移动边框，也是最上层的边框
   
   public void setUpRectResource(int resId) {
   
   // 设置 setDrawUpRectEnabled 类似图片中的小人，如果想让小人在最上面，需要设置这个属性.
   
   public void setDrawUpRectEnabled(boolean isDrawUpRect) { // 设置是否移动边框在最下层. true : 移动边框在最上层. 反之否.
   
   public void setTvScreenEnabled(boolean isTvScreen) { // 是否是TV的设备
   
   public boolean isTvScreenEnabled() {
   
   public void setInDraw(boolean isInDraw) { // 屏蔽 阴影，倒影，子控件的绘制.
   
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