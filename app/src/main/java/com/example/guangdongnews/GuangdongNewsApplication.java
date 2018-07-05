package com.example.guangdongnews;

import android.app.Application;

import org.xutils.x;

/**
 * 功能描述: 应用全局的配置
 * 时　　间: 2018/7/5.21:10
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class GuangdongNewsApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);  //初始化xutils
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
