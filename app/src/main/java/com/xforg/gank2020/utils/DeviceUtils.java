package com.xforg.gank2020.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created By apple on 2019/4/14
 * github: https://github.com/xianfeng92
 */
public class DeviceUtils {
    public DeviceUtils() {
    }

    @TargetApi(13)
    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT < 13) {
            return new Point(display.getWidth(), display.getHeight());
        } else {
            Point point = new Point();
            display.getSize(point);
            return point;
        }
    }

    public static String getId(Context context) {
        return ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
    }

    public static String getMacAddress(Context context) {
        return ((WifiManager)context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }
}
