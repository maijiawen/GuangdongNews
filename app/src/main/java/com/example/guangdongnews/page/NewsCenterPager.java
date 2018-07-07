package com.example.guangdongnews.page;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.guangdongnews.activity.MainActivity;
import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.base.BasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.fragment.LeftmenuFragment;
import com.example.guangdongnews.menudetailpage.InteracMenuDetaiBasePager;
import com.example.guangdongnews.menudetailpage.NewsMenuDetaiBasePager;
import com.example.guangdongnews.menudetailpage.PhotosMenuDetaiBasePager;
import com.example.guangdongnews.menudetailpage.TopicMenuDetaiBasePager;
import com.example.guangdongnews.utils.CacheUtils;
import com.example.guangdongnews.utils.Constants;
import com.example.guangdongnews.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:  新闻中心页面
 * 时　　间: 2018/7/6.0:34
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class NewsCenterPager extends BasePager {

    private List<NewsCenterPagerBean.DataBean> data;

    /**
     * 详情页面的集合
     */
    private ArrayList<MenuDetaiBasePager> detaiBasePagers;


    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了..");
        ib_menu.setVisibility(View.VISIBLE);
        //1.设置标题
//        tv_title.setText("新闻中心");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);
        //4.绑定数据
//        textView.setText("新闻中心内容");
        String saveJson=CacheUtils.getString(context,Constants.NEWSCENTER_PAGER_URL);
        if(!TextUtils.isEmpty(saveJson)){
            //试图获取缓存
            processData(saveJson);
        }
        getDataFromNet();//请求网络
    }

    /**
     * 使用xutils3 请求网络数据
     */
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context,Constants.NEWSCENTER_PAGER_URL,result);//缓存文本
                processData(result);
                LogUtil.e("使用xUtils3联网请求成功==");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请求错误==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("取消使用xUtils3联网请求==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3-onFinished");
            }
        });
    }

    /**
     * 处理json数据
     * @param json
     */
    private void processData(String json){
        NewsCenterPagerBean bean=parseJson(json);
        String title=bean.getData().get(0).getChildren().get(0).getTitle();
        data=bean.getData();
        MainActivity mainActivity= (MainActivity) context;
        LeftmenuFragment leftmenuFragment=mainActivity.getLeftmenuFragment();




        //添加详情页面
        detaiBasePagers = new ArrayList<>();
        detaiBasePagers.add(new NewsMenuDetaiBasePager(context));//新闻详情页面
        detaiBasePagers.add(new TopicMenuDetaiBasePager(context));//专题详情页面
        detaiBasePagers.add(new PhotosMenuDetaiBasePager(context));//图组详情页面
        detaiBasePagers.add(new InteracMenuDetaiBasePager(context));//互动详情页面

        leftmenuFragment.setData(data);//传递数据给左侧菜单

        LogUtil.e("json title = "+title);
    }

    /**
     * 解析json数据,并返回对象
     * @param json
     * @return
     */
    private NewsCenterPagerBean parseJson(String json) {
        Gson gson=new Gson();
        NewsCenterPagerBean bean= gson.fromJson(json,NewsCenterPagerBean.class);
        return bean;
    }

    /**
     * 根据位置切换详情页面
     *
     * @param position
     */
    public void swichPager(int position) {
        if (position < detaiBasePagers.size()) {
            //1.设置标题
            tv_title.setText(data.get(position).getTitle());
            //2.移除之前内容
            fl_content.removeAllViews();//移除之前的视图

            //3.添加新内容
            LogUtil.e("swichPager position = "+position);
            MenuDetaiBasePager detailBasePager = detaiBasePagers.get(position);//
            View rootView = detailBasePager.rootView;
            detailBasePager.initData();//初始化数据
            fl_content.addView(rootView);

        }
    }
}
