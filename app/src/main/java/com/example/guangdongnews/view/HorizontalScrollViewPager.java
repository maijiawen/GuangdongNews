package com.example.guangdongnews.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 功能描述:  新闻页签页面的viewpager，用于处理滑动事件冲突
 *          因为在第一页北京页面时，除非顶部图片所在的viewpager，在第0页，并且是从左到右滑动，才可以显示左侧菜单，
 *          否则，不响应
 * 时　　间: 2018/7/9.10:32
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class HorizontalScrollViewPager extends ViewPager {

    /**
     * 起始坐标
     */
    private float startX;
    private float startY;

    public HorizontalScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 需要让父类不处理滑动事件
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //请求父层视图不拦截，当前控件的事件
                getParent().requestDisallowInterceptTouchEvent(true);//都把事件传给当前控件（HorizontalScrollViewPager）
                //1.记录起始坐标
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2.来到新的坐标
                float endX = ev.getX();
                float endY = ev.getY();
                //3.计算偏移量
                float distanceX = endX - startX;
                float distanceY = endY - startY;
                //4.判断滑动方向
                if(Math.abs(distanceX) > Math.abs(distanceY)){
                    //水平方向滑动
//                    2.1，当滑动到ViewPager的第0个页面，并且是从左到右滑动
                    if(getCurrentItem()==0&&distanceX >0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }

//                    2.2，当滑动到ViewPager的最后一个页面，并且是从右到左滑动
                    else if((getCurrentItem()==(getAdapter().getCount()-1))&& distanceX <0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
//                    2.3，其他,中间部分
                    else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else{
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
