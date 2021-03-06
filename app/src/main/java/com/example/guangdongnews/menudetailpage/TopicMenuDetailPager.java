package com.example.guangdongnews.menudetailpage;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.guangdongnews.R;
import com.example.guangdongnews.activity.MainActivity;
import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.page.tabdetailpager.TabDetailPager;
import com.example.guangdongnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:   专题页面
 * 时　　间: 2018/7/7.13:53
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class TopicMenuDetailPager extends MenuDetaiBasePager {
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.tablayout)
    private TabLayout tabLayout;


    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;
    /**
     * 页签页面数据集合
     */
    private List<NewsCenterPagerBean.DataBean.ChildrenData> children;

    private ArrayList<TabDetailPager> tabDetailPagers;

    public TopicMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        children=dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.topicmenu_detail_pager,null);
        x.view().inject(TopicMenuDetailPager.this,view);
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        tabDetailPagers=new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(context,children.get(i)));
        }
        viewPager.setAdapter(new MyNewsMenuDetailBasePagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeLisener());
//        tabPageIndicator.setViewPager(viewPager);
        LogUtil.e("新闻页面初始化了");
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position==0){
                //可以左侧滑出菜单
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else{
                //不可以
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /***
     * 根据传入参数设置是否可以滑动左侧菜单
     * @param para  SlidingMenu.TOUCHMODE_NONE 不可滑
     *              SlidingMenu.TOUCHMODE_FULLSCREEN 可滑
     */
    private void isEnableSlidingMenu(int para) {
        MainActivity mainActivity= (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(para);
    }

    class MyNewsMenuDetailBasePagerAdapter extends PagerAdapter{

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager tabDetailPager=tabDetailPagers.get(position);
            View view=tabDetailPager.rootView;
            tabDetailPager.initData();//切换页面，初始化请求不同的页面数据
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }


    }
}
