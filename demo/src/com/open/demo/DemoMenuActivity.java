package com.open.demo;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.menu.OpenMenu;
import com.open.androidtvwidget.menu.OpenSubMenu;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 菜单DEMO测试.
 * 
 * @author hailongqiu
 *
 */
public class DemoMenuActivity extends Activity {

	private Context mContext;
	OpenMenu openMenu;
	View oldView;
	// private SmoothHorizontalScrollView test_hscroll;

	public DemoMenuActivity() {
		OPENLOG.initTag("hailongqiu", true); // 测试LOG输出.
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_menu_activity);
		// test_hscroll = (SmoothHorizontalScrollView)
		// findViewById(R.id.test_hscroll);
		findViewById(R.id.content11).setBackgroundResource(R.drawable.mainbackground);
		mContext = DemoMenuActivity.this;
		initAllMenu();
	}

	private void initAllMenu() {
		// 主菜单.
		int width = getResources().getDimensionPixelSize(R.dimen.px380);
		openMenu = new OpenMenu(mContext, width).setGravity(RelativeLayout.CENTER_VERTICAL);
		openMenu.setBackgroundResource(R.drawable.ic_bg_setting); // 设置背景.
		openMenu.add("菜单1").setIcon(R.drawable.ic_launcher);
		openMenu.add("菜单2").setIcon(R.drawable.ic_launcher);
		openMenu.add("菜单3").setIcon(R.drawable.ic_launcher);
		openMenu.add("菜单4").setIcon(R.drawable.ic_launcher);
		openMenu.add("菜单5").setIcon(R.drawable.ic_launcher);
		openMenu.add("菜单6").setIcon(R.drawable.ic_launcher);
		openMenu.add("菜单7").setIcon(R.drawable.ic_launcher);
		// 菜单1的子菜单.
		OpenSubMenu subMenu1 = new OpenSubMenu(mContext);
		subMenu1.add("菜单1-1");
		subMenu1.add("菜单1-2").setIcon(R.drawable.ic_launcher);
		subMenu1.add("菜单1-3");
		// 菜单2的子菜单.
		OpenSubMenu subMenu2 = new OpenSubMenu(mContext);
		subMenu2.add("菜单2-1");
		subMenu2.add("菜单2-2");
		subMenu2.add("菜单2-3");
		// 添加子菜单.
		openMenu.addSubMenu(0, subMenu1);
		openMenu.addSubMenu(1, subMenu2);
		// 菜单1添加子菜单.
		OpenSubMenu subMenu1_1 = new OpenSubMenu(mContext);
		subMenu1_1.add("菜单1-2-1");
		subMenu1_1.add("菜单1-2-2");
		subMenu1_1.add("菜单1-2-3");
		subMenu1.addSubMenu(1, subMenu1_1);
		//
		openMenu.toString();
		// 添加菜单动画.
		// Animation animation = new AlphaAnimation(0f, 1f);
//		Animation animation = new RotateAnimation(0f, 360f);
		 Animation animation = new TranslateAnimation(-width, 0f, 0f, 0f);
		animation.setDuration(500);
		// 1f为延时
		LayoutAnimationController controller = new LayoutAnimationController(animation, 0.5f);
		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
		// 设置菜单item显示出来的动画.(test)
		openMenu.setLayoutAnimation(controller);
		subMenu1.setLayoutAnimation(controller);
		subMenu1_1.setLayoutAnimation(controller);
		subMenu2.setLayoutAnimation(controller);
		//
		openMenu.showMenu();
		//
		final MainUpView mainUpView = new MainUpView(mContext);
		mainUpView.setEffectBridge(new EffectNoDrawBridge());
		mainUpView.setUpRectResource(R.drawable.white_light_10);
		((ListView)openMenu.getMenuView().getMenuView()).setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mainUpView.setFocusView(view, oldView, 1.2f);
				oldView = view;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// openMenu.getMenuView().getViewTreeObserver().addOnGlobalFocusChangeListener(new
		// OnGlobalFocusChangeListener() {
		// @Override
		// public void onGlobalFocusChanged(View oldFocus, View newFocus) {
		// mainUpView.setFocusView(newFocus, oldView, 1.2f);
		// oldView = newFocus;
		// }
		// });
	}

}
