package com.example.guangdongnews.menudetailpage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.guangdongnews.R;
import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.page.tabdetailpager.TabDetailPager;
import com.example.guangdongnews.utils.LogUtil;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:   新闻页面
 * 时　　间: 2018/7/7.13:53
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class NewsMenuDetailBasePager extends MenuDetaiBasePager {

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator tabPageIndicator;

    /**
     * 页签页面数据集合
     */
    private List<NewsCenterPagerBean.DataBean.ChildrenData> children;

    private ArrayList<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailBasePager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        children=dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.newsmenu_detail_pager,null);
        x.view().inject(NewsMenuDetailBasePager.this,view);
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
        tabPageIndicator.setViewPager(viewPager);
        LogUtil.e("新闻页面初始化了");
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
            tabDetailPager.initData();
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
