package com.example.guangdongnews.page.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.utils.LogUtil;

/**
 * 功能描述:  页签详情页面
 * 时　　间: 2018/7/7.21:28
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class TabDetailPager extends MenuDetaiBasePager{
    private final NewsCenterPagerBean.DataBean.ChildrenData childrenData;
    private TextView textView;
    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenData childrenData) {
        super(context);
        this.childrenData=childrenData;
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText(childrenData.getTitle());
        LogUtil.e("childrenData title "+childrenData.getTitle());
    }
}
