package com.example.guangdongnews.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 功能描述:  自定义屏蔽滑动事件的viewpager，
 * 这是ContentFragment的viewpager，负责的是底部 主页 新闻 智慧 政要等的页面切换
 * 因为页面滑动到倍儿逗之后，NewsMenuDetailPager是滑不下去了
 *      但是，ContentFragment的viewpager还可以继续响应事件
 *
 * 时　　间: 2018/7/6.1:21
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class NoScrollViewPager extends ViewPager {
    /**
     * 通常在代码中实例化的时候用该方法
     * @param context
     */
    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    /**
     * 在布局文件中使用该类的时候，实例化该类用该构造方法，这个方法不能少，少的话会崩溃。
     * @param context
     * @param attrs
     */
    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写触摸事件，消费掉，不让事件继续传递
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 返回 false 让事件传递下去，否则 ，true
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
