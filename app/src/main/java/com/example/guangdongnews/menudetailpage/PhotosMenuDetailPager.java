package com.example.guangdongnews.menudetailpage;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.guangdongnews.R;
import com.example.guangdongnews.activity.ShowImageActivity;
import com.example.guangdongnews.base.MenuDetaiBasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.domain.PhotosMenuDetailPagerBean;
import com.example.guangdongnews.utils.CacheUtils;
import com.example.guangdongnews.utils.Constants;
import com.example.guangdongnews.utils.LogUtil;
import com.example.guangdongnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * 功能描述:   组图页面
 * 时　　间: 2018/7/7.13:53
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class PhotosMenuDetailPager extends MenuDetaiBasePager {



    @ViewInject(R.id.listview)
    private ListView listview;

    @ViewInject(R.id.gridview)
    private GridView gridview;



    private NewsCenterPagerBean.DataBean dataBean;
    private List<PhotosMenuDetailPagerBean.DataEntity.NewsEntity> news;
    private String url;
    /**
     * true,显示ListView，隐藏GridView
     * false,显示GridView,隐藏ListView
     */
    private boolean isShowListView;
    private PhotosMenuDetailPagerAdapter adapter;

    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        this.dataBean=dataBean;

    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.photos_menudetail_pager, null);
        x.view().inject(PhotosMenuDetailPager.this,view);
        listview.setOnItemClickListener(new MyOnItemClickListener());
        gridview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position);
            String imageUrl = Constants.BASE_URL+newsEntity.getLargeimage();
            Intent intent = new Intent(context, ShowImageActivity.class);
            intent.putExtra("url",imageUrl);
            context.startActivity(intent);
        }
    }


    /**
     * 切换图片视图列表
     * @param ib_swich_list_grid
     */
    public void swichListAndGrid(ImageButton ib_swich_list_grid) {
        if(isShowListView){
            isShowListView = false;
            //显示GridView,隐藏ListView
            gridview.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            gridview.setAdapter(adapter);
            listview.setVisibility(View.GONE);
            //按钮显示--ListView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);


        }else{
            isShowListView = true;
            //显示ListView，隐藏GridView
            listview.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            listview.setAdapter(adapter);
            gridview.setVisibility(View.GONE);
            //按钮显示--GridView
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + dataBean.getUrl();
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
        LogUtil.e("组图页面初始化了"+url);
    }

    private void processData(String json) {
        PhotosMenuDetailPagerBean bean=parsedJson(json);
        isShowListView = true;
        //设置适配器
        news = bean.getData().getNews();
        adapter = new PhotosMenuDetailPagerAdapter();
        listview.setAdapter(adapter);
    }

    /**
     * 解析json数据
     * @param json
     * @return
     */
    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json,PhotosMenuDetailPagerBean.class);
    }

    /**
     * 联网请求数据
     */
    private void getDataFromNet() {
        //String请求
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.e("使用Volley联网请求成功==" + result);
                //缓存数据
                CacheUtils.putString(context, url, result);

                processData(result);
                //设置适配器
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("使用Volley联网请求失败==" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };

        //添加到队列
        VolleyManager.getRequestQueue().add(request);
    }

    class PhotosMenuDetailPagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(context,R.layout.item_photos_menudetail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到对应的数据
            PhotosMenuDetailPagerBean.DataEntity.NewsEntity newsEntity = news.get(position);
            viewHolder.tv_title.setText(newsEntity.getTitle());
            String imageUrl = Constants.BASE_URL+newsEntity.getSmallimage();
            //使用Volley请求图片-设置图片了
            loaderImager(viewHolder, imageUrl );
            return convertView;
        }
    }


    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }


    /**
     *  加载图片
     * @param viewHolder
     * @param imageurl
     */
    private void loaderImager(final ViewHolder viewHolder, String imageurl) {

        //设置tag
        viewHolder.iv_icon.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            //设置图片
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            //设置默认图片
                            viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
            }
        };
        VolleyManager.getImageLoader().get(imageurl, listener);
    }
}
