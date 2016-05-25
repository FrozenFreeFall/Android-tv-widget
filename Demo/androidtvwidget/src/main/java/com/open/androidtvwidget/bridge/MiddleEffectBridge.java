package com.open.androidtvwidget.bridge;

/**
 * 一般这种效果只使用在横向或者纵向的listveiw或者Recycleview <br>
 * 假设有20个item,边框开始的时候移动，达到中间就不再移动，<br>
 * item懂滚动，如果后面已经快没有了，就开始移动. <br>
 * 使用方法 MainUpView.setAnimBridge(new MiddleEffectBridge()); <br>
 * 
 * @author hailongqiu
 *
 */
public class MiddleEffectBridge extends EffectNoDrawBridge {
	
}
