package com.example.guangdongnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.guangdongnews.R;

/**
 * 功能描述:
 * 时　　间: 2018/7/9.11:35
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class RefreshListview extends ListView {

    /**
     * 下拉刷新和顶部轮播图
     */
    LinearLayout headerView;

    public RefreshListview(Context context) {
        this(context,null);
    }

    public RefreshListview(Context context, AttributeSet attrs) {
        this(context, null,0);
    }

    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
    }

    private void initHeaderView(Context context) {
        headerView= (LinearLayout) View.inflate(context, R.layout.refresh_header,null);
    }
}
