package com.example.guangdongnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.guangdongnews.R;
import com.example.guangdongnews.SplashActivity;
import com.example.guangdongnews.utils.CacheUtils;
import com.example.guangdongnews.utils.DensityUtil;

import java.util.ArrayList;


/**
 *
 * 1.引导页面，有三张图片，轮播完后，点击“开始体验”进入主界面
 * 2.使用viewpager做图片轮播，自定义控件shape 做小圆点指示器（这是技术要点）
 *
 */
public class GuideActivity extends Activity {

    private static final String TAG = GuideActivity.class.getSimpleName();
    private ViewPager viewpager;
    private Button btn_start_main;  //”开始体验“按钮
    private LinearLayout ll_point_group; //放置原点的布局
    private ImageView iv_red_point;//小红圆点

    private ArrayList<ImageView> imageViews;
    private int widthdpi;

    private int distance;  //两个小原点的间距

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findView();
        initData();
        //根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高和宽，边距都有了
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //得到屏幕滑动的百分比
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewpager.setAdapter(new MyPagerAdapter());
    }

    /**
     * 获取控件实例
     */
    private void findView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);

        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.保存曾经进入过主页面
                CacheUtils.putBoolean(GuideActivity.this, SplashActivity.START_MAIN,true);

                //2.跳转到主页面
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);

                //3.关闭引导页面
                finish();
            }
        });
    }


    /**
     * 初始化数据
     */
    private void initData() {
        //准备图片
        int[] ids = new int[]{
                R.drawable.guide_1,
                R.drawable.guide_2,
                R.drawable.guide_3
        };
        imageViews = new ArrayList<>(); //图片集合
        widthdpi = DensityUtil.dip2px(this, 10);//将10dp转成对应的px
        Log.e(TAG, " widthdpi = " + widthdpi);
        //循环创造3个页面内容
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);//设置背景
            imageViews.add(imageView);//添加到集合中
            ImageView point = new ImageView(this);//创建灰色圆点
            point.setBackgroundResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi, widthdpi);
            if (i != 0) {
                //不包括第0个，所有的点距离左边有10个px
                params.leftMargin = widthdpi;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);//将每个小圆点添加到线性布局里面
        }
    }

    /**
     * 通过布局监听器获取原点之间的距离
     */
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            //执行不只一次
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(MyOnGlobalLayoutListener.this);
//            间距  = 第1个点距离左边的距离 - 第0个点距离左边的距离
            distance = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
            Log.e(TAG, "distance==" + distance);

        }
    }

    /**
     * 页面滑动监听器
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滑动了会回调这个方法
         *
         * @param position             当前滑动页面的位置
         * @param positionOffset       页面滑动的百分比
         * @param positionOffsetPixels 滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            /**
             * 两点间移动的距离 = 屏幕滑动百分比 * 间距
             */
            //两点间滑动距离对应的坐标 = 原来的起始位置 +  两点间移动的距离
            int leftmargin = (int) (position * distance + (positionOffset * distance));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(params);//动态设置红点的位置

        }

        /**
         * 当页面被选中的时候，回调这个方法,让“开始体验”这个按钮是否可见
         *
         * @param position 被选中页面的对应的位置
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                //最后一个页面
                btn_start_main.setVisibility(View.VISIBLE);
            } else {
                //其他页面
                btn_start_main.setVisibility(View.GONE);
            }

        }

        /**
         * 当ViewPager页面滑动状态发生变化的时候
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /**
     * 设置适配器,用于将三张图片适配到三个页面
     */
    class MyPagerAdapter extends PagerAdapter {

        /**
         * 返回数据的总个数
         *
         * @return
         */
        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * 作用，getView
         * @param container ViewPager
         * @param position  要创业页面的位置
         * @return 返回和创建当前页面右关系的值
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            //添加到容器中
            container.addView(imageView);
//            return  position;
            return imageView;
//            return super.instantiateItem(container, position);
        }

        /**
         * 判断
         *
         * @param view   当前创建的视图
         * @param object 上面instantiateItem返回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
//            return view ==imageViews.get(Integer.parseInt((String) object));
            return view == object;
        }


        /**
         * 销毁页面
         * @param container ViewPager
         * @param position  要销毁页面的位置
         * @param object    要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
