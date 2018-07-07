package com.example.guangdongnews.menudetailpage;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.utils.LogUtil;

/**
 * 功能描述:   组图页面
 * 时　　间: 2018/7/7.13:53
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class PhotosMenuDetaiBasePager extends MenuDetaiBasePager {

    private TextView textView;

    public PhotosMenuDetaiBasePager(Context context) {
        super(context);
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
        textView.setText("组图页面");
        LogUtil.e("组图页面初始化了");
    }
}
