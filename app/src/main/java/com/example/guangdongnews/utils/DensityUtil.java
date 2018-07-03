package com.example.guangdongnews.utils;

import android.content.Context;

/**
 * 功能描述:  dp和px相互转换的工具
 * 时　　间: 2018/7/3.15:45
 * 作    者: maijiawen
 * 版本信息: V1.0.0
 **/
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
