
package com.xforg.gank2020.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.xforg.g2020.BuildConfig;
import com.xforg.g2020.base.delegate.AppLifecycles;
import com.xforg.g2020.integration.cache.IntelligentCache;
import com.xforg.g2020.utils.ArmsUtils;
import butterknife.ButterKnife;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {
//          MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        ButterKnife.setDebug(true);
        //LeakCanary 内存泄露检查
        //使用 IntelligentCache.KEY_KEEP 作为 key 的前缀, 可以使储存的数据永久存储在内存中
        //否则存储在 LRU 算法的存储空间中, 前提是 extras 使用的是 IntelligentCache (框架默认使用)
        ArmsUtils.obtainAppComponentFromContext(application).extras()
                .put(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())
                        , true ? LeakCanary.install(application) : RefWatcher.DISABLED);
        Logger.init("hhh")
                .methodOffset(2)
                .methodCount(2)
                .hideThreadInfo()
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
