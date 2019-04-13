package com.xforg.g2020.base.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created By apple on 2019/4/13
 * github: https://github.com/xianfeng92
 */
public interface AppLifecycles {

    void attachBaseContext(@NonNull Context base);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);
}
