package com.example.guangdongnews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.guangdongnews.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


/**
 * 主界面
 */
public class MainActivity extends SlidingFragmentActivity {

    private int screeWidth;

//    public static final String MAIN_CONTENT_TAG = "main_content_tag";
//    public static final String LEFTMENU_TAG = "leftmenu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.设置主页面
        setContentView(R.layout.activity_main);
        initSlidingMenu();
    }


    /**
     * 初始化侧滑菜单界面
     */
    private void initSlidingMenu() {
        //2.设置左侧菜单界面
        setBehindContentView(R.layout.activity_leftmenu);
        //3.获取菜单实例，用于配置菜单
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);//用于设置右侧菜单
        //4.设置显示的模式：左侧菜单+主页，左侧菜单+主页面+右侧菜单；主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);
        //5.设置滑动模式：滑动边缘，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //获取屏幕参数
        DisplayMetrics outmetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outmetrics);
        screeWidth = outmetrics.widthPixels;
        //6.设置主页占据的宽度
        slidingMenu.setBehindOffset((int) (screeWidth*0.625));
    }
}
