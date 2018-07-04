package com.example.guangdongnews.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 功能描述: 碎片基类，方便统筹子类，减少代码量
 *          LeftMenuFragment和ContentFragment将继承它
 * 时　　间: 2018/7/4.20:07
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public abstract class BaseFragment extends Fragment {

    public Activity context;//缓存MainActivity实例

    /**
     * 当Fragment被创建的时候回调这个方法
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }


    /**
     * 当视图被创建的时候回调
     * @param inflater
     * @param container
     * @param savedInstanceState
     * 创建了视图
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }


    /**
     * 让子类实现自己的视图，达到自己特有的效果
     * @return
     */
    public abstract View initView();

    /**
     * 当Activity被创建之后被回调
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 由子类自己选择重写
     * 1.如果自页面没有数据，联网请求数据，并且绑定到initView初始化的视图上
     * 2.绑定到initView初始化的视图上
     */
    public void initData(){
    }


}
