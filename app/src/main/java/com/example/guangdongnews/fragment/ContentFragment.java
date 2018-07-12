package com.example.guangdongnews.fragment;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.guangdongnews.R;
import com.example.guangdongnews.activity.MainActivity;
import com.example.guangdongnews.base.BaseFragment;
import com.example.guangdongnews.base.BasePager;
import com.example.guangdongnews.page.GovaffairPager;
import com.example.guangdongnews.page.HomePager;
import com.example.guangdongnews.page.NewsCenterPager;
import com.example.guangdongnews.page.SettingPager;
import com.example.guangdongnews.page.SmartServicePager;
import com.example.guangdongnews.utils.LogUtil;
import com.example.guangdongnews.utils.M;
import com.example.guangdongnews.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 功能描述:  主界面内容碎片
 * 时　　间: 2018/7/4.20:46
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.viewpager)   //注解关联控件
    private NoScrollViewPager viewpager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    private ArrayList<BasePager> basePagers;//页面集合

    private String TAG=ContentFragment.class.getSimpleName();

    @Override
    public View initView() {
        M.d(TAG, "initView: ");
        View view = View.inflate(context, R.layout.content_fragment, null);

        //1.把视图注入到框架中，让ContentFragment.this和View即布局文件关联起来
        x.view().inject(ContentFragment.this, view);
        return view;
    }

    @Override
    public void initData() {

        super.initData();
        M.d(TAG, "initData: ");
        basePagers = new ArrayList<>();//实例化
        basePagers.add(new HomePager(context));//添加主页面
        basePagers.add(new NewsCenterPager(context));//添加新闻中心页面
        basePagers.add(new SmartServicePager(context));//添加智慧服务指南页面
        basePagers.add(new GovaffairPager(context));//添加政要中心页面
        basePagers.add(new SettingPager(context));//添加设置中心页面
        viewpager.setAdapter(new ContentFragmentAdapter());
        viewpager.addOnPageChangeListener(new PageChangeListener());
        rg_main.setOnCheckedChangeListener(new GroupCheckedChangeListener());
        rg_main.check(R.id.rb_home);//设置默认选中主页
        basePagers.get(0).initData();
        M.d(TAG, "initData: basePagers.get(0).initData()");
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);//设置默认不可滑动左侧菜单

    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * @param position 当前选中的页面位置
         */
        @Override
        public void onPageSelected(int position) {
            M.d(TAG,"basePagers.get(position).initData() 初始化选中的页面的数据");
            basePagers.get(position).initData();  //初始化选中的页面的数据，避免预解析数据
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class GroupCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        /**
         * @param radioGroup
         * @param checkedId  选中的RadioButton ID
         */
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home: //主界面
                    viewpager.setCurrentItem(0, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter: //新闻界面
                    viewpager.setCurrentItem(1, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_smartservice: //智慧服务界面
                    viewpager.setCurrentItem(2, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_govaffair: //政要指南界面
                    viewpager.setCurrentItem(3, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting: //设置中心界面
                    viewpager.setCurrentItem(4, false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
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

    class ContentFragmentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position); //获取当前位置的页面
            View view = basePager.rootView;  //获取当前页面视图
            container.addView(view);  //添加当前页面的视图到容器中
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);//从容器中移除
        }
    }

    /**
     * 得到新闻中心页面
     * @return
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }
}
