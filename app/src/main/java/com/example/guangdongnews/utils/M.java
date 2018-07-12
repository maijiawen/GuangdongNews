package com.example.guangdongnews.utils;

import android.util.Log;

/**
 * 功能描述:  日志打印
 * 时　　间: 2018/7/12.22:27
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class M {
    static String TAG="gdn";
    public static void d(String tag,String log){
        Log.d(TAG,tag+" "+log);
    }
}
