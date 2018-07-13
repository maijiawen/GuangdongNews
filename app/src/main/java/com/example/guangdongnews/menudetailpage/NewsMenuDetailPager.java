package com.example.guangdongnews.menudetailpage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.guangdongnews.R;
import com.example.guangdongnews.activity.MainActivity;
import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.page.tabdetailpager.TabDetailPager;
import com.example.guangdongnews.utils.LogUtil;
import com.example.guangdongnews.utils.M;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:   接收 NewsCenterPager 丢过来的数据，新闻页面
 * 时　　间: 2018/7/7.13:53
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class NewsMenuDetailPager extends MenuDetaiBasePager {

    private static final String TAG = NewsMenuDetailPager.class.getSimpleName();
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager; //顶部图片的容器

    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator tabPageIndicator; //容器上面的标签指示器（北京/中国/国际/文娱...）

    @ViewInject(R.id.ib_tab_next)
    private ImageButton imageButton; //标签指示器尾部的下一页功能的按钮

    /**
     * 北京/中国/国际/文娱各个页面数据的集合
     */
    private List<NewsCenterPagerBean.DataBean.ChildrenData> children;

    private ArrayList<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        children=dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.newsmenu_detail_pager,null);
        x.view().inject(NewsMenuDetailPager.this,view);
        imageButton.setOnClickListener(new View.OnClickListener() {
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
            //获取 北京/中国/国际/文娱 **  等 10几个的页面数据
            tabDetailPagers.add(new TabDetailPager(context,children.get(i)));
        }
        viewPager.setAdapter(new MyNewsMenuDetailBasePagerAdapter());
        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeLisener());
        tabPageIndicator.setViewPager(viewPager);
        M.d(TAG,"新闻页面初始化了");
    }

    class MyOnPageChangeLisener implements ViewPager.OnPageChangeListener{

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
