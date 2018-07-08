package com.example.guangdongnews.page.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guangdongnews.R;
import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.domain.TabDetailPagerBean;
import com.example.guangdongnews.utils.CacheUtils;
import com.example.guangdongnews.utils.Constants;
import com.example.guangdongnews.utils.DensityUtil;
import com.example.guangdongnews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * 功能描述:  页签详情页面
 * 时　　间: 2018/7/7.21:28
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class TabDetailPager extends MenuDetaiBasePager{
    private final NewsCenterPagerBean.DataBean.ChildrenData childrenData;
    /**
     * 顶部轮播图部分的数据
     */
    private  List<TabDetailPagerBean.DataEntity.TopnewsData> topNews;

    /***
     * 新闻列表数据的结合
     */
    private List<TabDetailPagerBean.DataEntity.NewsData> newsData;

    private String url;

    private ViewPager viewPager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private ListView listView;

    private TopDetailPageListAdapter listAdapter;

    private int prePosition;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenData childrenData) {
        super(context);
        this.childrenData=childrenData;
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.tabdetail_pager,null);
        viewPager=view.findViewById(R.id.viewpager);
        tv_title=view.findViewById(R.id.tv_title);
        ll_point_group=view.findViewById(R.id.ll_point_group);
        listView=view.findViewById(R.id.listview);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url= Constants.BASE_URL+childrenData.getUrl();
        String saveJson= CacheUtils.getString(context,url);//获取之前的json网址缓存
        if (!TextUtils.isEmpty(saveJson)){
            //如果之前有缓存的话，发起网络数据请求
            processData(saveJson);
        }
        getDateFromNet();
        LogUtil.e("childrenData title "+childrenData.getTitle()+ " 网址 "+url);
    }

    /**
     * 获取网络数据
     */
    public void getDateFromNet() {
        RequestParams params=new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context,url,result);//将网址写入缓存中
                processData(result);
//                LogUtil.e("onSuccess data= "+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 处理网络返回的json数据
     * @param json
     */
    private void processData(String json) {
        TabDetailPagerBean bean=parsedJson(json);
        topNews=bean.getData().getTopnews();
        viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());

        addPoint();
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        tv_title.setText(topNews.get(prePosition).getTitle());//设置图中标题

        //获取listview的数据
        newsData=bean.getData().getNews();

        //设置listview的适配器
        listAdapter=new TopDetailPageListAdapter();
        listView.setAdapter(listAdapter);
//        LogUtil.e("title "+bean.getData().getNews().get(0).getTitle());
    }

    class TopDetailPageListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return newsData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(view==null){
                view=View.inflate(context,R.layout.item_tabdetail_pager,null);
                viewHolder=new ViewHolder();
                viewHolder.iv_icon=view.findViewById(R.id.iv_icon);
                viewHolder.tv_title=view.findViewById(R.id.tv_title);
                viewHolder.tv_time=view.findViewById(R.id.tv_time);
                view.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }

            TabDetailPagerBean.DataEntity.NewsData news=newsData.get(position);
            String imgUrl=Constants.BASE_URL+news.getListimage();
            //请求网络图片
            x.image().bind(viewHolder.iv_icon,imgUrl);
            //标题
            viewHolder.tv_title.setText(news.getTitle());
            //时间
            viewHolder.tv_time.setText(news.getPubdate());
            return view;
        }


    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }
    /**
     * 添加红点
     */
    private void addPoint() {
        ll_point_group.removeAllViews();//移除之前的所有视图
        for (int i = 0; i < topNews.size(); i++) {
            ImageView imageView=new ImageView(context);
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DensityUtil.dip2px(context,8),DensityUtil.dip2px(context,8));

            if(i==0){
                imageView.setEnabled(true);//图片为红点

            }else{
                imageView.setEnabled(false);//图片为灰点
                params.leftMargin=DensityUtil.dip2px(context,8);
            }
            imageView.setLayoutParams(params);
            ll_point_group.addView(imageView);
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tv_title.setText(topNews.get(position).getTitle());//设置图中标题
            ll_point_group.getChildAt(prePosition).setEnabled(false);//设置上一个点为灰色
            ll_point_group.getChildAt(position).setEnabled(true);//设置当前的点为红色
            prePosition=position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class TabDetailPagerTopNewsAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topNews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView=new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);//设置默认图片
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//拉伸图片来填充背景
            container.addView(imageView);

            TabDetailPagerBean.DataEntity.TopnewsData topnewsData=topNews.get(position);

            String url= Constants.BASE_URL+topnewsData.getTopimage();//图片地址
            //网络请求图片
            x.image().bind(imageView,url);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * //解析json数据，对象映射解析
     * @param json
     * @return
     */
    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json,TabDetailPagerBean.class);
    }
}
