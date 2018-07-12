package com.example.guangdongnews.page.tabdetailpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guangdongnews.R;
import com.example.guangdongnews.activity.MainActivity;
import com.example.guangdongnews.activity.NewsDetailActivity;
import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.domain.TabDetailPagerBean;
import com.example.guangdongnews.utils.CacheUtils;
import com.example.guangdongnews.utils.Constants;
import com.example.guangdongnews.utils.DensityUtil;
import com.example.guangdongnews.utils.LogUtil;
import com.example.guangdongnews.utils.M;
import com.example.guangdongnews.view.HorizontalScrollViewPager;
import com.example.guangdongnews.volley.VolleyManager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 功能描述:  页签详情页面
 * 时　　间: 2018/7/7.21:28
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class TabDetailPager extends MenuDetaiBasePager {
    public static final String READ_ARRAY_ID = "read_array_id";//存储点击过的item的组key
    private String id_Array=""; //存储点击过的item的组value
    private final NewsCenterPagerBean.DataBean.ChildrenData childrenData;
    /**
     * 顶部轮播图部分的数据
     */
    private List<TabDetailPagerBean.DataEntity.TopnewsData> topNews;

    /***
     * 新闻列表数据的结合
     */
    private List<TabDetailPagerBean.DataEntity.NewsData> newsData;
    /***
     * 下一页的新闻列表数据的结合
     */
    private List<TabDetailPagerBean.DataEntity.NewsData> moreNewsData;

    private String url;
    private RequestOptions options;//glide加载图片时默认显示的图片

    private HorizontalScrollViewPager viewPager;//自定义处理触摸事件
    private TextView tv_title;
    private LinearLayout ll_point_group;//红点视图组
    private ListView listView;
    private PullToRefreshListView pullToRefreshListView;
    private TopDetailPageListAdapter listAdapter;

    private int prePosition;//红点的上一次的位置
    private String moreUrl;//下一页的网址

    private InternalHandler internalHandler;//定时轮播图片的消息处理
    private  boolean isDragging = false;//是否拖拽中

    private OkHttpClient client;
    private String TAG=TabDetailPager.class.getSimpleName();
    private TabDetailPagerBean bean;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenData childrenData) {
        super(context);
        M.d(TAG,"TabDetailPager super");
        this.childrenData = childrenData;
        //Glide加载图片或者加载失败时默认显示的图片
        options = new RequestOptions()
                .placeholder(R.drawable.news_pic_default).error(R.drawable.news_pic_default);
    }

    @Override
    public View initView() {
        M.d(TAG,"initview 88");
        View view = View.inflate(context, R.layout.tabdetail_pager, null);

        pullToRefreshListView = view.findViewById(R.id.pull_refresh_list);


        View topNewsView = View.inflate(context, R.layout.topnews, null);
        viewPager = topNewsView.findViewById(R.id.viewpager);
        tv_title = topNewsView.findViewById(R.id.tv_title);
        ll_point_group = topNewsView.findViewById(R.id.ll_point_group);

        listView = pullToRefreshListView.getRefreshableView(); //获取listview
        listView.addHeaderView(topNewsView);//将viewpager融入listview的头部
        listView.setOnItemClickListener(new MyOnItemClickListener());
        //设置模式 支持下拉刷新 上拉加载
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new MyRefreshListener());//刷新监听器
//        pullToRefreshListView.setOnItemClickListener(new MyOnItemClickListener());
        /**
         * Add Sound Event Listener
         * 这个是上下拉动刷新的的声音特效
         */
//        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
//        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
//        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
//        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
//        pullToRefreshListView.setOnPullEventListener(soundListener);
        return view;
    }

    /**
     * 列表点击处理
     */
    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            int realPosition=position-2;//因为listview顶部有两个view分别是 刷新控件和viewpager
            TabDetailPagerBean.DataEntity.NewsData  newsBean=newsData.get(realPosition);
            id_Array=CacheUtils.getString(context,READ_ARRAY_ID);
            if(!id_Array.contains(newsBean.getId()+"")){
                //若没有存在该id，就添加进去
                CacheUtils.putString(context,READ_ARRAY_ID,id_Array+newsBean.getId()+",");
                listAdapter.notifyDataSetChanged(); //getcount  --->  getView
            }
            //跳新闻详情activity
            //即跳转到新闻浏览页面
            Intent intent = new Intent(context,NewsDetailActivity.class);
            intent.putExtra("url",Constants.BASE_URL+newsBean.getUrl());
            context.startActivity(intent);
        }
    }


    @Override
    public void initData() {
        super.initData();
        M.d(TAG,"mjw initData");
        url = Constants.BASE_URL + childrenData.getUrl();
        String saveJson = CacheUtils.getString(context, url);//获取之前的json网址缓存
        if (!TextUtils.isEmpty(saveJson)) {
            //如果之前有缓存的话，发起网络数据请求
            processData(saveJson);
        }
//        getDateFromNet();
//         getDateFromNetByOkHttp();
        getDataFromNetByVolley();
        M.d(TAG,"mjw childrenData title " + childrenData.getTitle() + " 网址 " + url);
    }



    /**
     * 使用volley联网请求数据
     */
    private void getDataFromNetByVolley() {
        //String请求
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("使用Volley联网请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, url, result);

                processData(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用Volley联网请求失败==" + volleyError.getMessage());
            }
        }) {
            @Override
            protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return com.android.volley.Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };

        //添加到队列
        VolleyManager.getRequestQueue().add(request);
    }

    /**
     * 使用okhttp请求网路数据
     */
    private void getDateFromNetByOkHttp(){
        M.d(TAG,"mjwgetDateFromNetByOkHttp");
        client=new OkHttpClient();
        Request request=new Request.Builder().get().url(url).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pullToRefreshListView.onRefreshComplete();
                M.d(TAG,e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pullToRefreshListView.onRefreshComplete();

//                CacheUtils.putString(context, url, response.body().string());//将网址写入缓存中
                processData(response.body().string());
                    M.d(TAG,"mjw555");

            }
        });

    }

    /**
     * 获取网络数据
     */
    public void getDateFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                pullToRefreshListView.onRefreshComplete();
                CacheUtils.putString(context, url, result);//将网址写入缓存中
                processData(result);
//                M.d(TAG,"onSuccess data= "+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                pullToRefreshListView.onRefreshComplete();
            }
        });
    }


    /**
     * 处理网络返回的json数据
     *
     * @param json
     */
    private void processData(String json) {
        M.d(TAG,"mjw processData");
        final TabDetailPagerBean bean = parsedJson(json);
        topNews = bean.getData().getTopnews();
        viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());

        addPoint();
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        tv_title.setText(topNews.get(prePosition).getTitle());//设置图中标题
        MainActivity mainActivity= (MainActivity) context;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //获取listview的数据
                newsData = bean.getData().getNews();
                //设置listview的适配器
                listAdapter = new TopDetailPageListAdapter();
                listView.setAdapter(listAdapter);
             //  listView.setOnScrollListener(new OnMyListviewScrollListener());
                moreUrl = "";//清空内容
                if (!TextUtils.isEmpty(bean.getData().getMore())) {
                    //下一页内容不为空的话，获取下一页的网址
                    moreUrl = Constants.BASE_URL + bean.getData().getMore();
                }
                viewPagerTimer();
                M.d(TAG,"title "+bean.getData().getNews().get(0).getTitle());
                M.d(TAG,"mjw processData end");
            }
        });

    }

    /**
     * 定时轮播图片处理的方法
     */
    private void viewPagerTimer(){
        //发消息每隔4000切换一次ViewPager页面
        if(internalHandler == null){
            internalHandler = new InternalHandler();
        }

        //是把消息队列所有的消息和回调移除
        internalHandler.removeCallbacksAndMessages(null);
        internalHandler.postDelayed(new MyRunnable(),4000);
    }

    class InternalHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            M.d(TAG,"mjw444");
            switch (msg.what){
                case 0:
                    //切换ViewPager的下一个页面
                    int item = (viewPager.getCurrentItem()+1)%topNews.size();
                    viewPager.setCurrentItem(item);
                    internalHandler.postDelayed(new MyRunnable(), 4000);
                    break;
                case 1:
                    M.d(TAG,"mjw11111111");

                    break;
            }

        }
    }

    class MyRunnable implements  Runnable{

        @Override
        public void run() {
            internalHandler.sendEmptyMessage(0);
        }
    }

    class MyRefreshListener implements PullToRefreshBase.OnRefreshListener2<ListView> {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//            Toast.makeText(context, "我在这里下拉刷新了", Toast.LENGTH_SHORT).show();
            String label = DateUtils.formatDateTime(context, System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

            // Update the LastUpdatedLabel
            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            getDateFromNet();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//            Toast.makeText(context, "我在这里上拉刷新了", Toast.LENGTH_SHORT).show();
            if (listView.getLastVisiblePosition() >= listAdapter.getCount() - 1) {
                getMoreDataFromNet();
//                    Toast.makeText(context,"加载更多",Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 加载下一页网络数据
     */
    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                pullToRefreshListView.onRefreshComplete();
                CacheUtils.putString(context, moreUrl, result);//将网址写入缓存中
                processMoreData(result);
                M.d(TAG,"getMoreDataFromNet onSuccess data= " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                pullToRefreshListView.onRefreshComplete();
            }
        });
    }

    /**
     * 处理下一页的网络请求返回的数据
     *
     * @param json
     */
    private void processMoreData(String json) {
        TabDetailPagerBean bean = parsedJson(json);
        //获取listview的数据
        moreNewsData = bean.getData().getNews();
        newsData.addAll(moreNewsData);
        listAdapter.notifyDataSetChanged();
        moreUrl = "";//清空内容
        if (!TextUtils.isEmpty(bean.getData().getMore())) {
            //下一页内容不为空的话，获取下一页的网址
            moreUrl = Constants.BASE_URL + bean.getData().getMore();
        } else {
            Toast.makeText(context, "没有更多的了...", Toast.LENGTH_SHORT).show();
        }
        M.d(TAG,"getMoreDataFromNet processMoreData success!! ");
    }

    /**
     * listview 的数据适配器
     */
    class TopDetailPageListAdapter extends BaseAdapter {

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
            M.d(TAG,"getView!!!");
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(context, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = view.findViewById(R.id.iv_icon);
                viewHolder.tv_title = view.findViewById(R.id.tv_title);
                viewHolder.tv_time = view.findViewById(R.id.tv_time);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            TabDetailPagerBean.DataEntity.NewsData news = newsData.get(position);
            String imgUrl = Constants.BASE_URL + news.getListimage();
            //请求网络图片
//            x.image().bind(viewHolder.iv_icon,imgUrl,imageOptions);
            Glide.with(context).load(imgUrl).apply(options).into(viewHolder.iv_icon);
            //标题
            viewHolder.tv_title.setText(news.getTitle());
            //时间
            viewHolder.tv_time.setText(news.getPubdate());
            id_Array=CacheUtils.getString(context,READ_ARRAY_ID);
            if(id_Array.contains(news.getId()+"")){
                M.d(TAG,"getView GRAY!!!");
                //如果是之前点过的item，就将其设置为灰色
                viewHolder.tv_title.setTextColor(Color.GRAY);
            }else{
                //黑色
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }
            return view;
        }


    }

    static class ViewHolder {
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
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 8), DensityUtil.dip2px(context, 8));

            if (i == 0) {
                imageView.setEnabled(true);//图片为红点

            } else {
                imageView.setEnabled(false);//图片为灰点
                params.leftMargin = DensityUtil.dip2px(context, 8);
            }
            imageView.setLayoutParams(params);
            ll_point_group.addView(imageView);
        }
    }

    /**
     * 顶部轮播图viewpager 的页面切换监听
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tv_title.setText(topNews.get(position).getTitle());//设置图中标题
            ll_point_group.getChildAt(prePosition).setEnabled(false);//设置上一个点为灰色
            ll_point_group.getChildAt(position).setEnabled(true);//设置当前的点为红色
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state ==ViewPager.SCROLL_STATE_DRAGGING){//拖拽
                isDragging = true;
                M.d(TAG,"拖拽");
                //拖拽要移除消息
                internalHandler.removeCallbacksAndMessages(null);
            }else if(state ==ViewPager.SCROLL_STATE_SETTLING&&isDragging){//惯性
                //发消息
                M.d(TAG,"惯性");
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyRunnable(),4000);

            }else if(state ==ViewPager.SCROLL_STATE_IDLE&&isDragging){//静止状态
                //发消息
                M.d(TAG,"静止状态");
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyRunnable(),4000);
            }
        }
    }

    /**
     * 顶部viewpager的适配器
     */
    class TabDetailPagerTopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topNews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);//设置默认图片
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//拉伸图片来填充背景
            container.addView(imageView);

            TabDetailPagerBean.DataEntity.TopnewsData topnewsData = topNews.get(position);

            String url = Constants.BASE_URL + topnewsData.getTopimage();//图片地址
            //网络请求图片
//            x.image().bind(imageView,url,imageOptions);

            Glide.with(context).load(url).apply(options).into(imageView);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN://按下
                            M.d(TAG,"按下");
                            //是把消息队列所有的消息和回调移除
                            internalHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP://离开
                            M.d(TAG,"离开");
                            //是把消息队列所有的消息和回调移除
                            internalHandler.removeCallbacksAndMessages(null);
                            internalHandler.postDelayed(new MyRunnable(), 4000);
                    }
                    return true;
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * //解析json数据，对象映射解析
     *
     * @param json
     * @return
     */
    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, TabDetailPagerBean.class);
    }
}
