package com.xforg.gank2020.utils;

import android.content.Context;

/**
 * Created By apple on 2019/4/14
 * github: https://github.com/xianfeng92
 */
public class DimenUtils {
    public DimenUtils() {
    }

    public static int dp2px(Context context, float dpValue) {
        return (int)(dpValue * context.getResources().getDisplayMetrics().density + 0.5F);
    }

    public static int px2dp(Context context, float pxValue) {
        return (int)(pxValue / context.getResources().getDisplayMetrics().density + 0.5F);
    }
}
