package com.example.guangdongnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.example.guangdongnews.R;
import com.example.guangdongnews.base.BaseFragment;
import com.example.guangdongnews.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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

    @Override
    public View initView() {
        LogUtil.e("正文Fragemnt视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment,null);

        //1.把视图注入到框架中，让ContentFragment.this和View即布局文件关联起来
        x.view().inject(ContentFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment数据被初始化了");
        rg_main.check(R.id.rb_home);
    }
}
