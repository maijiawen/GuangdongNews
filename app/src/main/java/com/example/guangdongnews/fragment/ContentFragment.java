package com.example.guangdongnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.example.guangdongnews.R;
import com.example.guangdongnews.base.BaseFragment;
import com.example.guangdongnews.utils.LogUtil;

/**
 * 功能描述:  主界面内容碎片
 * 时　　间: 2018/7/4.20:46
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class ContentFragment extends BaseFragment {

    private ViewPager viewpager;
    private RadioGroup rg_main;

    @Override
    public View initView() {
        LogUtil.e("正文Fragemnt视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment,null);
        viewpager=(ViewPager)view.findViewById(R.id.viewpager);
        rg_main=(RadioGroup)view.findViewById(R.id.rg_main);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment数据被初始化了");
        rg_main.check(R.id.rb_home);
    }
}
