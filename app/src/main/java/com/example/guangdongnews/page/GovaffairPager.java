package com.example.guangdongnews.page;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.guangdongnews.base.BasePager;
import com.example.guangdongnews.utils.LogUtil;

/**
 * 功能描述:  政要指南
 * 时　　间: 2018/7/6.0:29
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class GovaffairPager extends BasePager {
    public GovaffairPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("政要指南数据被初始化了..");
        //1.设置标题
        tv_title.setText("政要指南页面");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
        textView.setText("政要指南页面内容");

    }
}
