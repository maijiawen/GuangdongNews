package com.example.guangdongnews.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.guangdongnews.R;
import com.example.guangdongnews.base.BaseFragment;
import com.example.guangdongnews.base.BasePager;
import com.example.guangdongnews.page.GovaffairPager;
import com.example.guangdongnews.page.HomePager;
import com.example.guangdongnews.page.NewsCenterPager;
import com.example.guangdongnews.page.SettingPager;
import com.example.guangdongnews.page.SmartServicePager;
import com.example.guangdongnews.utils.LogUtil;

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
    private ViewPager viewpager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    private ArrayList<BasePager> basePagers;//页面集合

    @Override
    public View initView() {
        LogUtil.e("正文Fragemnt视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment, null);

        //1.把视图注入到框架中，让ContentFragment.this和View即布局文件关联起来
        x.view().inject(ContentFragment.this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment数据被初始化了");

        basePagers = new ArrayList<>();//实例化
        basePagers.add(new HomePager(context));//添加主页面
        basePagers.add(new NewsCenterPager(context));//添加新闻中心页面
        basePagers.add(new SmartServicePager(context));//添加智慧服务指南页面
        basePagers.add(new GovaffairPager(context));//添加政要中心页面
        basePagers.add(new SettingPager(context));//添加设置中心页面
        viewpager.setAdapter(new ContentFragmentAdapter());
        rg_main.check(R.id.rb_home);//设置默认选中主页


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
            View view=basePager.rootView;  //获取当前页面视图
            basePager.initData();  //初始化数据
            container.addView(view);  //添加当前页面的视图到容器中
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
