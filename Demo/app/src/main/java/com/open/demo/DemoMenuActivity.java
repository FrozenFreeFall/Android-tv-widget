package com.open.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.menu.OpenMenu;
import com.open.androidtvwidget.menu.OpenMenuImpl;
import com.open.androidtvwidget.menu.OpenMenuItem;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.demo.menu.TreeMenuPresenter;

/**
 * 菜单DEMO测试.
 *
 * @author hailongqiu
 */
public class DemoMenuActivity extends Activity implements OnClickListener {

    private Context mContext;
    OpenMenu mOpenMenu;
    RecyclerViewTV mRecyclerView;

    public DemoMenuActivity() {
        OPENLOG.initTag("hailongqiu", true); // 测试LOG输出.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_menu_activity);
//		findViewById(R.id.content11).setBackgroundResource(R.drawable.main_bg);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        mContext = DemoMenuActivity.this;
        initAllMenu();
    }

    private Drawable getResources(int id) {
        return getResources().getDrawable(id);
    }

    private void initAllMenu() {
        // 主菜单.
        mOpenMenu = new OpenMenuImpl();
        final OpenMenuItem menuItem1 = mOpenMenu.add("菜单1");
        menuItem1.setIconRes(R.drawable.ic_launcher).setId(R.id.menu_1_1).setChecked(true);
        mOpenMenu.add("菜单2").setIconRes(R.drawable.ic_launcher).setId(R.id.menu_1_2);
        mOpenMenu.add("菜单3").setIconRes(R.drawable.ic_launcher).setId(R.id.menu_1_3);
        mOpenMenu.add("菜单4").setIconRes(R.drawable.ic_launcher).setId(R.id.menu_1_4);
        mOpenMenu.add("菜单5").setIconRes(R.drawable.ic_launcher).setId(R.id.menu_1_5);
        mOpenMenu.add("菜单6").setIconRes(R.drawable.ic_launcher);
        mOpenMenu.add("菜单7").setIconRes(R.drawable.ic_launcher);
        // 菜单1的子菜单.
        OpenMenu subMenu1 = new OpenMenuImpl();
        subMenu1.add("菜单1-1");
        subMenu1.add("菜单1-2").setIconRes(R.drawable.ic_launcher);
        subMenu1.add("菜单1-3");
        // 菜单2的子菜单.
        OpenMenu subMenu2 = new OpenMenuImpl();
        subMenu2.add("菜单5-1");
        subMenu2.add("菜单5-2");
        subMenu2.add("菜单5-3");
        // 菜单1-2 添加子菜单(第三级菜单).
        OpenMenu subMenu1_2 = new OpenMenuImpl();
        subMenu1_2.add("菜单1-2-1");
        subMenu1_2.add("菜单1-2-2");
        subMenu1_2.add("菜单1-2-3");
        //
        OpenMenu subMenu1_2_1 = new OpenMenuImpl();
        subMenu1_2_1.add("菜单1-2-1-1");
        subMenu1_2_1.add("菜单1-2-1-2");
        subMenu1_2.addSubMenu(0, subMenu1_2_1); // 三级菜单添加四级菜单.
        // 添加子菜单.
        menuItem1.addSubMenu(subMenu1); // 一级菜单的menuItem1 添加二级菜单.
        mOpenMenu.addSubMenu(4, subMenu2); // 一级菜单添加二级菜单.
        subMenu1.addSubMenu(1, subMenu1_2); // 二级菜单添加三级菜单.
        // 输出菜单数据.
        mOpenMenu.toString();
        // test menu.
        final MainUpView mainUpView = new MainUpView(mContext);
        mainUpView.setEffectBridge(new OpenEffectBridge());
        mainUpView.setUpRectResource(R.drawable.white_light_10);
        mRecyclerView = (RecyclerViewTV) findViewById(R.id.recyclerView_menu);
        GeneralAdapter menuAdapter = new GeneralAdapter(new TreeMenuPresenter(mRecyclerView, mOpenMenu));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(menuAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                mainUpView.setUnFocusView(itemView);
            }
            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mainUpView.setFocusView(itemView, 1.0f);
            }
            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button1:
                mOpenMenu.showMenu();
                break;
            case R.id.button2:
                mOpenMenu.hideMenu();
                break;
        }
    }
}
