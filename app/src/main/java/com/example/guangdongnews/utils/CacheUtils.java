package com.example.guangdongnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.guangdongnews.activity.GuideActivity;

/**
 * 功能描述:缓存软件的一些参数和数据
 * 时　　间: 2018/7/3.12:05
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class CacheUtils {

    /**
     *  获取缓存值
     * @param context 上下文
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    /**
     * 保存软件参数
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 缓存文本数据
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 获取缓存的文本数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }
}
