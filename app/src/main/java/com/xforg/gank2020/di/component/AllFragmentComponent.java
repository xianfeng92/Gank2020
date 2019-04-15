package com.xforg.gank2020.di.component;

import com.xforg.g2020.di.component.AppComponent;
import com.xforg.g2020.di.scope.FragmentScope;
import com.xforg.gank2020.fragment.GAllFrament;
import com.xforg.gank2020.fragment.GCommonFragment;
import com.xforg.gank2020.fragment.GFuLiFragment;
import dagger.Component;

/**
 * Created By zhongxianfeng on 19-4-15
 * github: https://github.com/xianfeng92
 */
@FragmentScope
@Component(dependencies = AppComponent.class)
public interface AllFragmentComponent {
    void inject(GAllFrament gAllFrament);
    void inject(GFuLiFragment gFuLiFragment);
    void inject(GCommonFragment gCommonFragment);
}
