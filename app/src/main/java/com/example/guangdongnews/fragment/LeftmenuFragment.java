package com.example.guangdongnews.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guangdongnews.R;
import com.example.guangdongnews.activity.MainActivity;
import com.example.guangdongnews.base.BaseFragment;
import com.example.guangdongnews.base.BasePager;
import com.example.guangdongnews.domain.NewsCenterPagerBean;
import com.example.guangdongnews.page.NewsCenterPager;
import com.example.guangdongnews.utils.DensityUtil;
import com.example.guangdongnews.utils.LogUtil;

import java.util.List;

/**
 * 功能描述:  左侧滑菜单
 * 时　　间: 2018/7/4.20:42
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class LeftmenuFragment extends BaseFragment {

    private List<NewsCenterPagerBean.DataBean> data;
    private ListView listView;
    private LeftAdapter leftAdapter;//左侧菜单适配器
    /**
     * 点击的位置
     */
    private int prePosition;

    @Override
    public View initView() {
        listView=new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context,40),0,0);
        listView.setDividerHeight(0);//设置分割线高度为0
//        listView.setCacheColorHint(Color.TRANSPARENT);

        //设置按下listView的item不变色
        listView.setSelector(android.R.color.transparent);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //1.记录点击的位置，变成红色
                prePosition = position;
                leftAdapter.notifyDataSetChanged();//getCount()-->getView

                //2.把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//关<->开
                LogUtil.e("prePosition "+prePosition);
                swichPager(prePosition);
            }
        });
        return listView;
    }

    /**
     * 处理传递过来的数据
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for(int i=0;i<data.size();i++){
            LogUtil.e("title=="+data.get(i).getTitle());
        }

        leftAdapter=new LeftAdapter();
        listView.setAdapter(leftAdapter);
        swichPager(prePosition);
    }


    class LeftAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
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
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            textView.setText(data.get(position).getTitle());
            textView.setEnabled(position==prePosition);//通过设置使能来设置按钮状态
            return textView;
        }
    }

    /**
     * 根据位置切换不同详情页面
     * @param position
     */
    private void swichPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.swichPager(position);
    }
}
